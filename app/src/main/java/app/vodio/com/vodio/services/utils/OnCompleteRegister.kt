package app.vodio.com.vodio.services.utils

import app.vodio.com.vodio.beans.User
import app.vodio.com.vodio.database.DatabaseResponse
import app.vodio.com.vodio.services.LoginService
import app.vodio.com.vodio.utils.OnCompleteAsyncTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnCompleteRegister(private val complete: OnCompleteAsyncTask, private val usr : User) : Callback<DatabaseResponse> {
    override fun onResponse(call: Call<DatabaseResponse>, response: Response<DatabaseResponse>) {
        val databaseResponse = response.body()
        if (databaseResponse!!.resultCode == 0) {
            complete.onSuccess(databaseResponse)
            LoginService.getInstance()?.loggedIn = usr
        } else {
            complete.onFail()
        }

    }

    override fun onFailure(call: Call<DatabaseResponse>, t: Throwable) {
        complete.onFail()
    }
}