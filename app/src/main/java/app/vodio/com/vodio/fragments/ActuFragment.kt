package app.vodio.com.vodio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.vodio.com.vodio.R
import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.database.retrofit.repositories.LoginRepository
import app.vodio.com.vodio.fragments.utils.CollectionView
import app.vodio.com.vodio.models.VodsPresenter
import app.vodio.com.vodio.models.VodsPresenterFactory
import app.vodio.com.vodio.views.VodAdapter
import kotlinx.android.synthetic.main.fragment_actu.*
import java.util.*


class ActuFragment : AbstractFragment(), CollectionView<Vod> {

    private val vods = ArrayList<Vod>()
    private var adapter : VodAdapter? = null
    private var idVodProvided = ArrayList<Int>()

    private var vodPresenter : VodsPresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_actu, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataModels()
    }

    override fun onResume() {
        super.onResume()
        attachObservers()
        configureAdapter()
        configureOnRefresh()
    }

    override fun onPause() {
        super.onPause()
        detachObservers()
    }

    override fun showCollectionError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCollection(t : List<Vod>){
        loadingVods.visibility = View.GONE
        vods.clear()
        for (vod in t) {
            vods.add(vod)
            idVodProvided.add(vod.idVod)
            adapter!!.notifyItemInserted(vods.size )
        }
        refreshlayout.isRefreshing = false
    }

    override fun showCollectionNoData(){
        loadingVods.visibility = View.GONE
        noVodProvided.visibility = View.VISIBLE
        refreshlayout.isRefreshing = false
    }

    override fun showCollectionLoading(){
        loadingVods.visibility = View.VISIBLE
        noVodProvided.visibility = View.GONE
    }

    private fun attachObservers(){
        vodPresenter!!.loadFeedVods(LoginRepository.getInstance(context!!).getLoggedUser().value!!.login!!)
    }

    private fun detachObservers(){
        //vodModel!!.getFeedVods().removeObserver(vodObserver)
    }

    private fun configureOnRefresh(){
        refreshlayout.setOnRefreshListener {
            refreshlayout.isRefreshing = true
            vodPresenter!!.loadFeedVods(LoginRepository.getInstance(context!!).getLoggedUser().value!!.login!!)
        }
        refreshWhenEmptyActu.setOnClickListener {
            refreshlayout.isRefreshing = true
            vodPresenter!!.loadFeedVods(LoginRepository.getInstance(context!!).getLoggedUser().value!!.login!!)
        }
    }

    private fun configureAdapter(){
        if(adapter == null) {
            adapter = VodAdapter(vods, context!!, this)
        }
        listOfPost.adapter = adapter
        listOfPost.layoutManager = LinearLayoutManager(context)
    }



    private fun nonEmptyVodsUiMode(){
        loadingVods.visibility = View.GONE
        noVodProvided.visibility = View.GONE
        refreshlayout.visibility =  View.VISIBLE
    }

    private fun initDataModels(){
        vodPresenter = VodsPresenterFactory(context!!).create()
        vodPresenter!!.attachView(this, lifecycle)
    }

    override fun onClick(view: View) {}

    override fun getIconId(): Int {
        return iconMapping.idIcon
    }

    object iconMapping{
        const val idIcon = R.id.actu_navbar
    }

}
