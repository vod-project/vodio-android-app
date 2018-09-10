package app.vodio.com.vodio.database.retrofit.repositories

import android.content.Context
import androidx.lifecycle.MutableLiveData
import app.vodio.com.vodio.beans.User
import app.vodio.com.vodio.database.retrofit.utils.DatabaseResponse
import app.vodio.com.vodio.database.retrofit.utils.RetrofitInstance
import app.vodio.com.vodio.database.retrofit.services.UserDatabaseInterface
import app.vodio.com.vodio.services.UserLocalDatabase

import app.vodio.com.vodio.utils.tasking.OnCompleteAsyncTask
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginRepository private constructor(var context : Context){
    companion object {
        private var INSTANCE : LoginRepository? = null
        fun getInstance(c : Context) : LoginRepository {
            if(INSTANCE == null) INSTANCE = LoginRepository(c)
            return INSTANCE!!
        }
    }

    fun signIn(login: String?, password: String, onCompleteAsyncTask: OnCompleteAsyncTask) {
        RetrofitInstance.getRetrofitInstance(context).create(UserDatabaseInterface::class.java)
                ?.getUser(login!!, password)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(OnCompleteLogin(password, onCompleteAsyncTask, userLive ))

    }

    fun signUp(login : String, password : String, name : String, surname : String, email : String
               , onCompleteAsyncTask: OnCompleteAsyncTask){
        userLive.value = User(name,surname,login,email,password)
        RetrofitInstance.getRetrofitInstance(context).create(UserDatabaseInterface::class.java)
                ?.createUser(login,password,name,surname,email)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(OnCompleteRegister(password,onCompleteAsyncTask, userLive))
    }


    private var userLive = MutableLiveData<User>()

    fun getLoggedUser() : MutableLiveData<User> {
        if(userLive.value == null) {
            userLive.value  = getUserFromLocalStorage()
        }
        return userLive
    }

    private fun removeUserFromLocalStorage(){
        val login = userLive.value?.login
        if(login != null)
            UserLocalDatabase(context).disconnectUser(login)//.deleteUser(userLive.value!!.login!!)
    }

    private fun setUserInLocalStorage(){
        if(userLive.value != null){
            val login = userLive.value!!.login!!
            val password = userLive.value!!.password!!
            UserLocalDatabase(context).storeCurrentUser(login, password)
        }
    }

    private fun getUserFromLocalStorage() : User?{
        val usr = UserLocalDatabase(context).getLocalUser()
        if(usr != null) {
            userLive.value = usr
            fillUserData(userLive.value!!)
            return userLive.value
        }
        return null
    }

    fun signOut(){
        removeUserFromLocalStorage()
        userLive.value = null
    }

    private fun fillUserData(user : User){
        signIn(user.login, user.password!!, OnCompleteFillData())
    }

    inner class OnCompleteFillData : OnCompleteAsyncTask {
        override fun onSuccess(obj: Any) {
            val usr = obj as User
            userLive.value!!.email = usr.email
            userLive.value!!.name = usr.name
            userLive.value!!.surname = usr.surname
        }

        override fun onFail(t: Throwable) {}
    }


    inner class OnCompleteLogin(private val passwordUser: String, private val complete: OnCompleteAsyncTask, private val userLive: MutableLiveData<User>) : SingleObserver<User> {
        override fun onSuccess(t: User) {
            if (t.isProvided()) {
                complete.onSuccess(t)
                t.password = passwordUser
                userLive.value = t
                setUserInLocalStorage()
            } else {
                userLive.value = null
                complete.onFail(Throwable())
            }
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            complete.onFail(e)
            userLive.value = null
        }
    }

    inner class OnCompleteRegister(private val passwordUser: String, private val complete: OnCompleteAsyncTask, private val usrLive : MutableLiveData<User>) : SingleObserver<DatabaseResponse> {
        override fun onSuccess(t: DatabaseResponse) {
            if (t.resultCode == 0) {
                complete.onSuccess(t)
                usrLive.value!!.password = passwordUser
                setUserInLocalStorage()
            } else {
                complete.onFail(Throwable())
            }
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            complete.onFail(e)
        }
    }


}