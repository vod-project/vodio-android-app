package app.vodio.com.vodio.services

import android.content.Context
import android.net.Uri
import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.database.DatabaseResponse
import app.vodio.com.vodio.database.retrofit.RetrofitInstance
import app.vodio.com.vodio.database.retrofit.services.VodService
import app.vodio.com.vodio.utils.OnCompleteAsyncTask
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import android.media.MediaMetadataRetriever
import android.webkit.MimeTypeMap


class VodService(){

    companion object {
        private var c : Context? = null
        private var INSTANCE : app.vodio.com.vodio.services.VodService? = null
        public fun getInstance(ctx : Context) : app.vodio.com.vodio.services.VodService?{
            if(INSTANCE == null) {
                INSTANCE = VodService()
                c = ctx
            }
            return INSTANCE
        }
    }
    fun getVods(onComplete : OnCompleteAsyncTask){
        RetrofitInstance.getRetrofitInstance()
                ?.create(VodService::class.java)
                ?.getVods()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(OnVodProvided(onComplete))
    }

    fun createVod(file : File, authorLogin : String, title : String, onComplete: OnCompleteAsyncTask){
        //
        val uri = Uri.fromFile(file)
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(c, uri)
        val durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val timeInSecond = Integer.parseInt(durationStr) / 1000
        //
        //val v = c!!.getContentResolver().getType(uri)
        val v = getMimeType(file.path)
        val requestFile = RequestBody.create(
                MediaType.parse(v ),
                file
        )

        val body = MultipartBody.Part.createFormData("audio", file.name, requestFile)
        val description = RequestBody.create(okhttp3.MultipartBody.FORM, "desc")

        RetrofitInstance.getRetrofitInstance()
                ?.create(VodService::class.java)
                ?.createVod(timeInSecond, title, authorLogin, description, requestFile)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(OnVodCreated(onComplete))
    }

    fun getMimeType(url: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    class   OnVodProvided(private var onComplete: OnCompleteAsyncTask) : SingleObserver<Array<Vod>>{
        override fun onSuccess(t: Array<Vod>) {
            if(t != null)
                onComplete.onSuccess(t)
            else{
                onComplete.onFail()
            }
        }

        override fun onSubscribe(d: Disposable) {}

        override fun onError(e: Throwable) {
            onComplete.onFail()
        }
    }

    class OnVodCreated(private var onComplete: OnCompleteAsyncTask) : SingleObserver<DatabaseResponse>{
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            onComplete.onFail()
        }
        override fun onSuccess(t: DatabaseResponse) {
            onComplete.onSuccess(t)
        }
    }
}