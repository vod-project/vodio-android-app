package app.vodio.com.vodio.database.retrofit.repositories

import android.content.Context
import android.os.Build
import android.webkit.MimeTypeMap
import androidx.lifecycle.MutableLiveData
import app.vodio.com.vodio.beans.Profile
import app.vodio.com.vodio.database.retrofit.services.UserDatabaseInterface
import app.vodio.com.vodio.database.retrofit.utils.DatabaseResponse
import app.vodio.com.vodio.database.retrofit.utils.RetrofitInstance
import app.vodio.com.vodio.utils.tasking.OnCompleteAsyncTask
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ProfileRepository private constructor(private var c : Context){
    companion object {
        private var INSTANCE : ProfileRepository? = null
        fun getInstance(c : Context) : ProfileRepository {
            if(INSTANCE == null){
                INSTANCE = ProfileRepository(c)
                INSTANCE!!.retrofitInstance = RetrofitInstance.getRetrofitInstance(c)
            }
            return INSTANCE!!
        }
    }
    private var retrofitInstance : Retrofit? = null

    private var followed = HashMap<String, MutableLiveData<Boolean>>()
    private var mapFollowedByLogin = HashMap<String,HashMap<String, MutableLiveData<Boolean>>>()
    private var mapFollowerByLogin = HashMap<String,HashMap<String, MutableLiveData<Boolean>>>()
    private var nbFollower = HashMap<String, MutableLiveData<Int>>()
    private var nbFollowed = HashMap<String, MutableLiveData<Int>>()
    private var searchAllProfiles = MutableLiveData<List<Profile>>()
    private var searchProfileByName = MutableLiveData<List<Profile>>()
    private var completeNameLogin = HashMap<String, MutableLiveData<String>>()
    fun changeProfileImg(login : String, fileImg : File, onCompleteAsyncTask: OnCompleteAsyncTask){
        val v = getMimeType(fileImg.path)
        if(v != null) {
            val requestFile = RequestBody.create(
                    MediaType.parse(v),
                    fileImg
            )
            val body = MultipartBody.Part.createFormData("file", fileImg.name, requestFile)

            retrofitInstance!!
                    .create(UserDatabaseInterface::class.java)
                    .changeUserProfileImg(login, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(OnImageProfileUpdated(onCompleteAsyncTask))
        }else{
            onCompleteAsyncTask.onFail(Throwable())
        }
    }

    fun follow(followedLogin : String) : MutableLiveData<Boolean>{
        val booleanLive : MutableLiveData<Boolean>
        if(followed.contains(followedLogin)){
            booleanLive = followed.get(followedLogin)!!
        }else{
            booleanLive = MutableLiveData()
            followed.put(followedLogin, booleanLive)
        }
        val usr = LoginRepository.getInstance(c).getLoggedUser().value



        retrofitInstance!!
                .create(UserDatabaseInterface::class.java)
                .follow(usr!!.login!!, followedLogin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(OnBooleanProvided(booleanLive, Runnable {
                   numberOfFollowed(usr.login!!)
                    numberOfFollower(followedLogin)
                    isFollowing(usr.login!!, followedLogin)
                    VodRepository.getInstance(c).getFeedVods(usr.login!!)
                }))
        return booleanLive
    }

    fun unfollow(unfollowedLogin : String) : MutableLiveData<Boolean>{
        val booleanLive : MutableLiveData<Boolean>
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            followed.putIfAbsent(unfollowedLogin, MutableLiveData())
            booleanLive = followed.get(unfollowedLogin)!!
        }else {
            if (followed.contains(unfollowedLogin)) {
                booleanLive = followed.get(unfollowedLogin)!!
            } else {
                booleanLive = MutableLiveData()
                followed.put(unfollowedLogin, booleanLive)
            }
        }
        val usr = LoginRepository.getInstance(c).getLoggedUser().value
        retrofitInstance!!
                .create(UserDatabaseInterface::class.java)
                .unfollow(usr!!.login!!, unfollowedLogin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(OnBooleanProvided(booleanLive, Runnable {
                    numberOfFollower(unfollowedLogin)
                    numberOfFollowed(usr.login!!)
                    isFollowing(usr.login!!, unfollowedLogin)
                    VodRepository.getInstance(c).getFeedVods(usr.login!!)
                }))
        return booleanLive
    }

    fun isFollowing(loginFollower : String, loginFollowed : String) : MutableLiveData<Boolean>{
        val booleanLive : MutableLiveData<Boolean>

        // loginFollower in follower of loginFollowed
        if(mapFollowerByLogin.containsKey(loginFollowed)){
            val follower = mapFollowerByLogin.get(loginFollowed)
            if(follower!!.containsKey(loginFollower)){
                booleanLive = follower.get(loginFollower)!!
            }else{
                booleanLive = MutableLiveData()
                follower.put(loginFollower, booleanLive)
            }
        }else{
            booleanLive = MutableLiveData()
            val follower = HashMap<String, MutableLiveData<Boolean>>()
            follower.put(loginFollower, booleanLive)
            mapFollowerByLogin.put(loginFollowed, follower)
        }
        //loginFollowed in followed of loginFollower
        if(mapFollowedByLogin.containsKey(loginFollower)){
            val followed = mapFollowedByLogin.get(loginFollower)
            if(followed!!.containsKey(loginFollowed)){
                followed.get(loginFollowed)!!.value = true
            }else{
                val bool = MutableLiveData<Boolean>()
                bool.value = true
                followed.put(loginFollowed, bool)
            }
        }else{
            val bool = MutableLiveData<Boolean>()
            val followed = HashMap<String, MutableLiveData<Boolean>>()
            followed.put(loginFollowed, bool)
            mapFollowedByLogin.put(loginFollower, followed)
        }

        retrofitInstance!!
                .create(UserDatabaseInterface::class.java)
                .isFollowing(loginFollower, loginFollowed)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(OnBooleanProvided(booleanLive))
        return booleanLive
    }

    fun numberOfFollower(login : String = LoginRepository.getInstance(c).getLoggedUser().value!!.login!!) : MutableLiveData<Int>{
        val followerLive : MutableLiveData<Int>
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            nbFollower.putIfAbsent(login, MutableLiveData())
            followerLive = nbFollower.get(login)!!
        }else {
            if (nbFollower.containsKey(login)) {
                followerLive = nbFollower.get(login)!!
            } else {
                followerLive = MutableLiveData()
                nbFollower.put(login, followerLive)
            }
        }
        retrofitInstance!!
                .create(UserDatabaseInterface::class.java)
                .numberOfFollower(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(OnNumberProvided(followerLive))
        return followerLive
    }

    fun numberOfFollowed(login : String = LoginRepository.getInstance(c).getLoggedUser().value!!.login!!) : MutableLiveData<Int>{
        val followedLive : MutableLiveData<Int>
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            nbFollowed.putIfAbsent(login, MutableLiveData())
            followedLive = nbFollowed.get(login)!!
        }else {
            if (nbFollowed.containsKey(login)) {
                followedLive = nbFollower.get(login)!!
            } else {
                followedLive = MutableLiveData()
                nbFollower.put(login, followedLive)
            }
        }
        retrofitInstance!!
                .create(UserDatabaseInterface::class.java)
                .numberOfFollowed(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(OnNumberProvided(followedLive))
        return followedLive
    }

    fun getCompleteName(login : String) : MutableLiveData<String>{
        val completeNameLive : MutableLiveData<String>
        if(completeNameLogin.containsKey(login)){
            completeNameLive = completeNameLogin.get(login)!!
        }else{
            completeNameLive = MutableLiveData()
            completeNameLogin.put(login, completeNameLive)
        }
        retrofitInstance!!
                .create(UserDatabaseInterface::class.java)
                .getCompleteName(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(OnStringProvided(completeNameLive))
        return completeNameLive
    }

    fun searchAllProfile() : MutableLiveData<List<Profile>>{
        retrofitInstance!!
                .create(UserDatabaseInterface::class.java)
                .searchAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(OnListProvided(searchAllProfiles))
        return searchAllProfiles
    }

    fun searchProfilesByName(name : String) : MutableLiveData<List<Profile>>{
        retrofitInstance!!
                .create(UserDatabaseInterface::class.java)
                .searchAll(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(OnListProvided(searchProfileByName))
        return searchProfileByName
    }

    private val executor = ThreadPoolExecutor(5,10,60, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())
    private inner class OnNumberProvided(private var nbLive : MutableLiveData<Int>, private var runOnSuccess : Runnable? = null) : SingleObserver<DatabaseResponse>{
        override fun onSuccess(t: DatabaseResponse) {
            nbLive.value = t.resultCode
            if(runOnSuccess != null) runOnSuccess!!.run()
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            nbLive.value = -1
        }
    }

    private inner class OnStringProvided(private var stringLive : MutableLiveData<String>) : SingleObserver<DatabaseResponse>{
        override fun onSuccess(t: DatabaseResponse) {
            stringLive.value = t.message
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            stringLive.value = ""
        }
    }

    private inner class OnBooleanProvided(private var booleanLive : MutableLiveData<Boolean>, private var runOnSuccess : Runnable? = null) : SingleObserver<DatabaseResponse> {
        override fun onSuccess(t: DatabaseResponse) {
            booleanLive.value = t.resultCode == 0
            if(t.resultCode == 0 && runOnSuccess != null) runOnSuccess!!.run()
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            booleanLive.value = false
        }
    }

    private inner class OnListProvided<T>(private var profileListLive: MutableLiveData<List<T>>) : SingleObserver<Array<T>> {
        override fun onSuccess(t: Array<T>) {
            profileListLive.value = t.toList()
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            profileListLive.value = ArrayList()
        }
    }

    private inner class OnImageProfileUpdated(var onCompleteAsyncTask: OnCompleteAsyncTask) : SingleObserver<DatabaseResponse> {
        override fun onSuccess(t: DatabaseResponse) {
            if(t.resultCode == 0){
                onCompleteAsyncTask.onSuccess(t)
            }else{
                onCompleteAsyncTask.onFail(Throwable())
            }
        }
        override fun onSubscribe(d: Disposable) {}

        override fun onError(e: Throwable) {
            onCompleteAsyncTask.onFail(e)
        }
    }

    private fun getMimeType(url: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }
}