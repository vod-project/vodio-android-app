package app.vodio.com.vodio.services

import app.vodio.com.vodio.beans.User
import app.vodio.com.vodio.database.DatabaseResponse
import app.vodio.com.vodio.database.retrofit.RetrofitInstance
import app.vodio.com.vodio.database.retrofit.services.UserService
import app.vodio.com.vodio.services.utils.OnCompleteLogin
import app.vodio.com.vodio.services.utils.OnCompleteRegister
import app.vodio.com.vodio.utils.OnCompleteAsyncTask
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
        val service : UserService? = RetrofitInstance.getRetrofitInstance()?.create(UserService::class.java)
        val call : Call<User>? = service?.getUser(login,password)
        call?.enqueue(OnCompleteLogin(onComplete))
    }

    fun signUp(login : String, password : String, name : String, onComplete: OnCompleteAsyncTask){
        val usr : User = User(name,"",login,"",password)
        val service : UserService? = RetrofitInstance.getRetrofitInstance()?.create(UserService::class.java)
        val call : Call<DatabaseResponse>? = service?.createUser(login,password,"","","")
        call?.enqueue(OnCompleteRegister(onComplete, usr))
    }
}