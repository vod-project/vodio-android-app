package app.vodio.com.vodio.fragments

import android.os.Bundle


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import app.vodio.com.vodio.R
import app.vodio.com.vodio.beans.Profile
import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.database.retrofit.repositories.LoginRepository
import app.vodio.com.vodio.database.retrofit.repositories.ProfileRepository
import app.vodio.com.vodio.database.retrofit.repositories.VodRepository
import app.vodio.com.vodio.fragments.utils.CollectionView
import app.vodio.com.vodio.fragments.utils.ProfileView
import app.vodio.com.vodio.models.*
import app.vodio.com.vodio.utils.tasking.OnCompleteAsyncTask
import app.vodio.com.vodio.views.VodAdapter
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*

class ProfileFragment : AbstractFragment() , CollectionView<Vod>, ProfileView{
    override fun showProfile(profile: Profile) {
        val nbVodeur = profile.nVodeurs
        numberOfVodeursTv.text = resources.getQuantityString(R.plurals.nb_vode, nbVodeur, nbVodeur)
        loadingVodeur.visibility = View.GONE
        numberOfVodeursTv.visibility = View.VISIBLE

        val nbVoditeur = profile.nVoditeurs
        numberOfVoditeursTv.text = resources.getQuantityString(R.plurals.nb_vodi, nbVoditeur, nbVoditeur)
        loadingVoditeur.visibility = View.GONE
        numberOfVoditeursTv.visibility = View.VISIBLE

        val cName = "${profile.name} ${profile.surname}"
        userCompleteNameTv.text = cName
        loadingUsername.visibility = View.GONE
        userCompleteNameTv.visibility = View.VISIBLE
    }

    override fun showNoProfileData() {
        loadingUsername.visibility = View.GONE
        loadingVoditeur.visibility = View.GONE
        loadingVodeur.visibility = View.GONE
    }

    override fun showProfileLoading() {
        loadingUsername.visibility = View.VISIBLE
        loadingVoditeur.visibility = View.VISIBLE
        loadingVodeur.visibility = View.VISIBLE
    }

    override fun showProfileError(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    private var vods = ArrayList<Vod>()
    private var adapter : VodAdapter? = null
    private var idProvided = ArrayList<Int>()

    private var usrLogin : String? = null
    private var usrModel : UserModel? = null

    private var isFollowingObserver = IsFollowingObserver()
    private var isFollowedObserver  = IsFollowedObserver()
    private var isUserProfile = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataModels()
        provideProfileUser()
    }

    override fun onStart() {
        super.onStart()

        refreshWhenEmptyProfile.setOnClickListener {
            refreshProfileVodsLayout.isRefreshing = true
            vodsPresenter!!.loadVodsByAuthor(usrLogin!!)
        }

        if(!usrLogin.equals(usrModel!!.getLoggedUser().value!!.login)){
            editProfile.visibility = View.GONE
            followProfile.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        if(adapter == null) {
            if(!usrModel!!.getLoggedUser().value!!.login.equals(usrLogin)){
                adapter = VodAdapter(vods, context!!, this, false)
            }else{
                adapter = VodAdapter(vods, context!!, this)
            }
        }
        listViewVodsProfile.adapter = adapter
        listViewVodsProfile.layoutManager = LinearLayoutManager(context)

        attachObservers()

        refreshProfileVodsLayout.setOnRefreshListener {
            vodsPresenter!!.loadVodsByAuthor(usrLogin!!)
        }

    }

    override fun onPause() {
        super.onPause()
        detachObservers()
    }

    override fun showCollectionError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCollectionLoading(){
        noVodProvidedProfile.visibility = View.GONE
        loadingVodsProfile.visibility = View.VISIBLE
    }

    override fun showCollectionNoData(){
        refreshProfileVodsLayout.isRefreshing = false
        noVodProvidedProfile.visibility = View.VISIBLE
        if(isUserProfile) {
            NoVodProvidedTv.text = context!!.resources.getString(R.string.you_have_no_vod)
        }else {
            NoVodProvidedTv.text = context!!.resources.getString(R.string.profile_have_no_vod)
        }
        loadingVodsProfile.visibility = View.GONE
    }

    override fun showCollection(t: List<Vod>) {
        noVodProvidedProfile.visibility = View.GONE
        loadingVodsProfile.visibility = View.GONE
        refreshProfileVodsLayout.visibility = View.VISIBLE
        refreshProfileVodsLayout.isRefreshing = false
        vods.clear()
        for (vod in t) {
            vods.add(vod)
            idProvided.add(vod.idVod)
            adapter!!.notifyItemInserted(vods.size)
        }
        adapter!!.notifyDataSetChanged()
    }

    fun attachObservers(){
        if(!usrLogin.equals(usrModel!!.getLoggedUser().value!!.login)){
            usrModel!!.isFollowing(usrLogin!!, usrModel!!.getLoggedUser().value!!.login!!).observe(this, isFollowingObserver)
            usrModel!!.isFollowing(usrModel!!.getLoggedUser().value!!.login!!, usrLogin!!)
                    .observe(this, isFollowedObserver)
        }
        vodsPresenter!!.loadVodsByAuthor(usrLogin!!)

        profilePresenter!!.loadProfileByLogin(usrLogin!!)
    }

    fun detachObservers(){
        if(!usrLogin.equals(usrModel!!.getLoggedUser().value!!.login)){
            usrModel!!.isFollowing(usrLogin!!, usrModel!!.getLoggedUser().value!!.login!!).removeObserver(isFollowingObserver)
            usrModel!!.isFollowing(usrModel!!.getLoggedUser().value!!.login!!, usrLogin!!)
                    .removeObserver(isFollowedObserver)
        }
    }

    private var vodsPresenter : VodsPresenter? = null
    private var profilePresenter : ProfilePresenter? = null
    private fun initDataModels(){
        usrModel = ViewModelProviders.of(requireActivity()).get(UserModel::class.java)
        usrModel!!.initRepositories(
                LoginRepository.getInstance(context!!),
                ProfileRepository.getInstance(context!!),
                VodRepository.getInstance(context!!),
                this
        )
        vodsPresenter = VodsPresenterFactory(context!!).create()
        vodsPresenter!!.attachView(this, lifecycle)

        profilePresenter = ProfilePresenterFactory(context!!).create()
        profilePresenter!!.attachView(this, lifecycle)
    }

    private fun provideProfileUser(){
        if(arguments != null && arguments!!.containsKey("USER")){
            usrLogin = (arguments!!.get("USER") as String)
        }else{
            usrLogin = usrModel!!.getLoggedUser().value!!.login
            isUserProfile = true
        }
    }

    private inner class IsFollowingObserver : Observer<Boolean>{
        override fun onChanged(t: Boolean?) {
            if(t!!){
                isFollowingTv.visibility = View.VISIBLE
            }else{
                isFollowingTv.visibility = View.GONE
            }
        }
    }

    private inner class IsFollowedObserver : Observer<Boolean>{
        override fun onChanged(t: Boolean?) {
            if(t!!){
                isFollowedTv.visibility = View.VISIBLE
                followProfile.text = resources.getString(R.string.unfollow)
                followProfile.setOnClickListener { usrModel!!.unfollow(usrLogin!!) }
            }
            else{
                isFollowedTv.visibility = View.GONE
                followProfile.text = resources.getString(R.string.follow)
                followProfile.setOnClickListener { usrModel!!.follow(usrLogin!!) }
            }
        }
    }

    override fun onClick(view: View) {
        parent?.onItemSelected(view.id)
    }

    override fun getIconId(): Int {
        return iconMapping.idIcon
    }

    object iconMapping{
        const val idIcon = R.id.profile_navbar
    }
}
