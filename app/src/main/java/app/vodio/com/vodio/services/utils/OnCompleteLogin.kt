package app.vodio.com.vodio.services.utils

import app.vodio.com.vodio.beans.User
import app.vodio.com.vodio.services.LoginService
import app.vodio.com.vodio.utils.OnCompleteAsyncTask
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnCompleteLogin(private val complete: OnCompleteAsyncTask) : SingleObserver<User> {
    override fun onSuccess(t: User) {
        val usr = t
        if (usr!!.isProvided()) {
            complete.onSuccess(usr)
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