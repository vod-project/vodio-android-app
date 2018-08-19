package app.vodio.com.vodio.services.utils

import app.vodio.com.vodio.beans.User
import app.vodio.com.vodio.database.DatabaseResponse
import app.vodio.com.vodio.services.LoginService
import app.vodio.com.vodio.utils.OnCompleteAsyncTask
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnCompleteRegister(private val complete: OnCompleteAsyncTask, private val usr : User) : SingleObserver<DatabaseResponse> {
    override fun onSuccess(t: DatabaseResponse) {
        val databaseResponse = t
        if (databaseResponse!!.resultCode == 0) {
            complete.onSuccess(databaseResponse)
            LoginService.getInstance()?.loggedIn = usr
        } else {
            complete.onFail()
        }
    }
    override fun onSubscribe(d: Disposable) {}
    override fun onError(e: Throwable) {
        complete.onFail()
    }
}