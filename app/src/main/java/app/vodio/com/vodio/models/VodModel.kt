package app.vodio.com.vodio.models

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.vodio.com.vodio.database.retrofit.repositories.VodRepository

class VodModel : ViewModel() {
    private var vodRepository : VodRepository? = null
    private var fragment : Fragment? = null

    fun initRepositories(vodRepo : VodRepository, fragment: Fragment){
        vodRepository = vodRepo
        this.fragment = fragment
    }

    fun like(vodId : Int) : MutableLiveData<Boolean>{
        return vodRepository!!.like(vodId)
    }

    fun unlike(vodId : Int) : MutableLiveData<Boolean>{
        return vodRepository!!.unlike(vodId)
    }

    fun isLiking(vodId : Int) : MutableLiveData<Boolean>{
        return vodRepository!!.isLiking(vodId)
    }

    fun numberOfLike(vodId : Int) : MutableLiveData<Int>{
        return vodRepository!!.numberOfLike(vodId)
    }

    fun deleteVod(vodId : Int) : MutableLiveData<Boolean>{
        return vodRepository!!.deleteVod(vodId)
    }


}