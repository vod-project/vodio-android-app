package app.vodio.com.vodio.services

import app.vodio.com.vodio.beans.User
import app.vodio.com.vodio.database.DatabaseResponse
import app.vodio.com.vodio.database.retrofit.RetrofitInstance
import app.vodio.com.vodio.database.retrofit.services.UserService
import app.vodio.com.vodio.services.utils.OnCompleteLogin
import app.vodio.com.vodio.services.utils.OnCompleteRegister
import app.vodio.com.vodio.utils.OnCompleteAsyncTask
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call

class LoginService{
    companion object {
        private var INSTANCE : LoginService? = null
        public fun getInstance() : LoginService?{
            if(INSTANCE == null) INSTANCE = LoginService()
            return INSTANCE
        }
    }

    var loggedIn : User? = null

    fun signIn(login : String, password : String, onComplete : OnCompleteAsyncTask){
        RetrofitInstance.getRetrofitInstance()
                ?.create(UserService::class.java)
                ?.getUser(login, password)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(OnCompleteLogin(onComplete))
    }

    fun signUp(login : String, password : String, name : String, onComplete: OnCompleteAsyncTask){
        val usr : User = User(name,"",login,"",password)
        RetrofitInstance.getRetrofitInstance()
                ?.create(UserService::class.java)
                ?.createUser(login,password,"","","")
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(OnCompleteRegister(onComplete,usr))
    }
}