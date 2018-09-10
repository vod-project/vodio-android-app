package app.vodio.com.vodio.utils.tasking

interface OnCompleteAsyncTask {
    fun onSuccess(obj : Any)
    fun onFail(t : Throwable)
}
