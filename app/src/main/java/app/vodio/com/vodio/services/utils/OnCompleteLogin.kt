package app.vodio.com.vodio.services.utils

import app.vodio.com.vodio.beans.User
import app.vodio.com.vodio.services.LoginService
import app.vodio.com.vodio.utils.OnCompleteAsyncTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnCompleteLogin(private val complete: OnCompleteAsyncTask) : Callback<User> {
    override fun onResponse(call: Call<User>, response: Response<User>) {
        val usr = response.body()
        if (usr!!.isProvided()) {
            complete.onSuccess(usr)
            LoginService.getInstance()?.loggedIn = usr
        } else {
            complete.onFail()
        }
    }

    override fun onFailure(call: Call<User>, t: Throwable) {
        complete.onFail()
    }
}