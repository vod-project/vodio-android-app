package app.vodio.com.vodio.models

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.database.retrofit.repositories.LoginRepository
import app.vodio.com.vodio.database.retrofit.repositories.VodRepository

class ListVodModel : ViewModel(){
    private var vods : MutableLiveData<List<Vod>>? = null

    private var vodRepository : VodRepository? = null
    private var loginRepository : LoginRepository? = null
    private var fragment : Fragment? = null


    fun init(vodRepository : VodRepository, loginRepository: LoginRepository, fragment: Fragment){
        this.vodRepository = vodRepository
        this.loginRepository = loginRepository
        this.fragment = fragment
    }

    fun getFeedVods() : MutableLiveData<List<Vod>>{
        val login = loginRepository!!.getLoggedUser().value!!.login!!
        vods = vodRepository!!.getFeedVods(login)
        return vods!!
    }

    fun getVodsByAuthor(login : String) : MutableLiveData<List<Vod>>{
        vods = vodRepository!!.getVodsByAuthor(login)
        return vods!!
    }
}