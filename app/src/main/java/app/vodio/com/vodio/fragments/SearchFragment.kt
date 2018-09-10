package app.vodio.com.vodio.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.vodio.com.vodio.R
import app.vodio.com.vodio.beans.Profile
import app.vodio.com.vodio.database.retrofit.repositories.LoginRepository
import app.vodio.com.vodio.database.retrofit.repositories.ProfileRepository
import app.vodio.com.vodio.database.retrofit.repositories.VodRepository
import app.vodio.com.vodio.fragments.utils.CollectionView
import app.vodio.com.vodio.models.UserModel
import app.vodio.com.vodio.views.ProfileAdapter
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : AbstractFragment() , CollectionView<Profile>{
    override fun showCollectionLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCollection(t: List<Profile>) {
        profiles.clear()
        adapter!!.notifyDataSetChanged()
        for(profile in t){
            profiles.add(profile)
            loginProvided.add(profile.login!!)
        }
        adapter!!.notifyDataSetChanged()
    }

    override fun showCollectionError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCollectionNoData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val profiles = ArrayList<Profile>()
    private val loginProvided = HashSet<String>()
    private var usrModel : UserModel? = null
    private var adapter : ProfileAdapter? = null

    var profileObserver = SearchProfilesObserver()
    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataModels()
        adapter = ProfileAdapter(profiles, context!!, this)
    }

    override fun onPause() {
        super.onPause()
        profiles.clear()
        adapter!!.notifyDataSetChanged()
        usrModel!!.searchAllProfile().removeObserver(profileObserver)

    }
    override fun onResume() {
        super.onResume()
        if(listProfile.adapter == null) {
            listProfile.adapter = adapter
        }
        listProfile.layoutManager = LinearLayoutManager(context)

        searchEditText.addTextChangedListener(SearchTextWatcher())
        usrModel!!.searchAllProfile("").observe(this, profileObserver)
    }

    private fun initDataModels(){
        usrModel = UserModel()
        usrModel!!.initRepositories(
                LoginRepository.getInstance(context!!),
                ProfileRepository.getInstance(context!!),
                VodRepository.getInstance(context!!),
                this
        )
    }

    inner class SearchTextWatcher : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            val word = s!!.toString()

            usrModel!!.searchAllProfile(word)//.observe(this@SearchFragment, profileObserver)
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            Toast.makeText(context, "before", Toast.LENGTH_SHORT).show()
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    }
    inner class SearchProfilesObserver : Observer<List<Profile>> {
        override fun onChanged(t: List<Profile>?) {
            if(t != null)
                showCollection(t)
            else{
                showCollectionNoData()
            }
        }
    }

    override fun getIconId(): Int {
        return iconMapping.idIcon
    }

    object iconMapping{
        const val idIcon = R.id.search_navbar
    }
}
