package app.vodio.com.vodio.utils;

import android.content.Context;
import android.os.AsyncTask;

abstract class MyAsyncTask<T>(private val onComplete : OnCompleteAsyncTask) : AsyncTask<Object, Object, Object>(){
   // private var onComplete : OnCompleteAsyncTask? = null;
    private var isSuccess : Boolean = false

    override abstract fun doInBackground(vararg p0: Object?): Object

    fun setSuccess(b : Boolean){isSuccess = b}

    override fun onPostExecute(result: Object?) {
        super.onPostExecute(result)
        if(onComplete != null)
            when(isSuccess){
                true -> onComplete?.onSuccess(getObject())
                false -> onComplete?.onFail()
            }
    }

    abstract  fun getObject(): T
}