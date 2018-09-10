package app.vodio.com.vodio.models

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.vodio.com.vodio.beans.Profile
import app.vodio.com.vodio.beans.User
import app.vodio.com.vodio.database.retrofit.repositories.LoginRepository
import app.vodio.com.vodio.database.retrofit.repositories.ProfileRepository
import app.vodio.com.vodio.database.retrofit.repositories.VodRepository

class UserModel : ViewModel(){
    private var liveUser : MutableLiveData<User> = MutableLiveData()

    private var loginRepository : LoginRepository? = null
    private var profileRepository : ProfileRepository? = null
    private var vodRepository : VodRepository? = null
    private var fragment : Fragment? = null


    fun initRepositories(loginRepository: LoginRepository, profileRepository: ProfileRepository, vodRepository: VodRepository, frag : Fragment){
        this.loginRepository = loginRepository
        this.profileRepository = profileRepository
        this.vodRepository = vodRepository
        this.fragment = frag
    }

    fun getLoggedUser() : MutableLiveData<User>{
        if(liveUser.value == null){
            liveUser.value = loginRepository!!.getLoggedUser().value
        }
        return liveUser
    }

    fun getCompleteName(login : String) : MutableLiveData<String>{
        return profileRepository!!.getCompleteName(login)
    }

    fun getNbVodeur(login : String) : MutableLiveData<Int>{
        return profileRepository!!.numberOfFollowed(login)
    }

    fun getNbVoditeur(login : String) : MutableLiveData<Int>{
        return profileRepository!!.numberOfFollower(login)
    }

    fun isFollowing(loginFollower : String, loginFollowed : String) : MutableLiveData<Boolean>{
        return profileRepository!!.isFollowing(loginFollower, loginFollowed)
    }

    fun follow(login : String) : MutableLiveData<Boolean>{
        return profileRepository!!.follow(login)
    }

    fun unfollow(login : String) : MutableLiveData<Boolean>{
        return profileRepository!!.unfollow(login)
    }

    fun searchAllProfile(byName : String? = null) : MutableLiveData<List<Profile>>{
        if(byName == null)
            return profileRepository!!.searchAllProfile()
        else {
            return profileRepository!!.searchProfilesByName(byName)
        }
    }
}