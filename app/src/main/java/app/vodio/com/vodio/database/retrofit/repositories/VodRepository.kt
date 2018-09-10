package app.vodio.com.vodio.database.retrofit.repositories

import android.content.Context
import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.database.retrofit.utils.DatabaseResponse
import app.vodio.com.vodio.database.retrofit.utils.RetrofitInstance
import app.vodio.com.vodio.database.retrofit.services.VodDatabaseInterface
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import android.webkit.MimeTypeMap
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class VodRepository private constructor(var ctx: Context) {
    val providedVodById = HashMap<Int, Vod>()

    val createdVodMap = HashMap<Int, Vod>()

    val likedVods = HashMap<Int, MutableLiveData<Boolean>>()
    val numberOfLike = HashMap<Int, MutableLiveData<Int>>()

    val authorVod = HashMap<String, MutableLiveData<List<Vod>>>()

    private val feedVods = MutableLiveData<List<Vod>>()
    var latestActuVodsTimestamp = 0L
    private var retrofitInstance : Retrofit? = null


    companion object {

        private var INSTANCE : VodRepository? = null
        fun getInstance(ctx : Context) : VodRepository {
            if(INSTANCE == null) {
                INSTANCE = VodRepository(ctx)
                INSTANCE!!.retrofitInstance = RetrofitInstance.getRetrofitInstance(ctx)
            }
            return INSTANCE!!
        }
        fun getVodFilePath(audioName : String) : String{
            val base = INSTANCE!!.retrofitInstance!!.baseUrl()
            return "${base}vod/get.php?mode=file&file_name=$audioName"
        }
    }
    fun clear(){
        latestActuVodsTimestamp = 0L
        createdVodMap.clear()
        likedVods.clear()
        authorVod.clear()
        feedVods.value = null
    }

    fun getFeedVods(loginProvider : String) : MutableLiveData<List<Vod>>{
        retrofitInstance!!
                .create(VodDatabaseInterface::class.java)
                .getVods(loginProvider).delay(1,TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(OnVodProvided(feedVods))
        return feedVods
    }

    fun getVodsByAuthor(authorLogin : String) : MutableLiveData<List<Vod>>{
        val vodsLive : MutableLiveData<List<Vod>>
        if(authorVod.containsKey(authorLogin)) {
            vodsLive = authorVod.get(authorLogin)!!
        }else{
            vodsLive = MutableLiveData()
            authorVod.put(authorLogin, vodsLive)
        }
        retrofitInstance!!
                .create(VodDatabaseInterface::class.java)
                .getVodsByAuthor(authorLogin).delay(1,TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(OnVodProvided(vodsLive))
        return vodsLive
    }

    fun like(vodId : Int, login : String  = LoginRepository.getInstance(ctx).getLoggedUser().value!!.login!!) : MutableLiveData<Boolean>{
        val booleanLive = MutableLiveData<Boolean>()
        retrofitInstance!!
                .create(VodDatabaseInterface::class.java)
                .like(vodId, login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(OnBooleanProvided(booleanLive, Runnable {
                    val isLiking : MutableLiveData<Boolean>
                    if(likedVods.containsKey(vodId))
                        isLiking = likedVods.get(vodId)!!
                    else {
                        isLiking = MutableLiveData()
                        likedVods.put(vodId, isLiking)
                    }
                    isLiking.value = true
                }))
        return booleanLive
    }

    fun unlike(vodId : Int, login : String  = LoginRepository.getInstance(ctx).getLoggedUser().value!!.login!!) : MutableLiveData<Boolean>{
        val booleanLive = MutableLiveData<Boolean>()
        retrofitInstance!!
                .create(VodDatabaseInterface::class.java)
                .unlike(vodId, login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(OnBooleanProvided(booleanLive, Runnable {
                    val isLiking : MutableLiveData<Boolean>
                    if(likedVods.containsKey(vodId))
                        isLiking = likedVods.get(vodId)!!
                    else {
                        isLiking = MutableLiveData()
                        likedVods.put(vodId, isLiking)
                    }
                    isLiking.value = false
                }))
        return booleanLive
    }

    fun isLiking(vodId : Int, login : String  = LoginRepository.getInstance(ctx).getLoggedUser().value!!.login!!) : MutableLiveData<Boolean>{
        val booleanLive : MutableLiveData<Boolean>
        if(likedVods.containsKey(vodId))
            booleanLive = likedVods.get(vodId)!!
        else {
            booleanLive = MutableLiveData()
            likedVods.put(vodId, booleanLive)
        }
        retrofitInstance!!
                .create(VodDatabaseInterface::class.java)
                .isLiking(vodId, login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(OnBooleanProvided(booleanLive))
        return booleanLive
    }

    fun numberOfLike(vodId : Int) : MutableLiveData<Int>{
        val numberLive :  MutableLiveData<Int>
        if(numberOfLike.containsKey(vodId)){
            numberLive = numberOfLike.get(vodId)!!
        }else{
            numberLive = MutableLiveData()
            numberOfLike.put(vodId, numberLive)
        }
        retrofitInstance!!
                .create(VodDatabaseInterface::class.java)
                .numberOfLike(vodId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(OnNumberProvided(numberLive))
        return numberLive
    }

    fun deleteVod(idVod : Int) : MutableLiveData<Boolean>{
        val booleanLive = MutableLiveData<Boolean>()
        retrofitInstance!!
                .create(VodDatabaseInterface::class.java)
                .delete(idVod)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(OnBooleanProvided(booleanLive,Runnable {
                    val currentLogin = LoginRepository.getInstance(ctx).getLoggedUser().value!!.login!!
                    getVodsByAuthor(currentLogin)
                }))
        return booleanLive
    }

    fun createVod(vod : Vod) : MutableLiveData<Boolean>{
        val booleanLive = MutableLiveData<Boolean>()
        val idWork = Random().nextInt()
        createdVodMap.put(idWork, vod)
        val file = vod.getFile()

        val timeInSecond = vod.timeInSecond!!
        val v = getMimeType(file!!.path)
        val requestFile = RequestBody.create(
                MediaType.parse(v),
                file
        )

        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        retrofitInstance!!.create(VodDatabaseInterface::class.java)
                ?.createVod(timeInSecond,vod.authorLogin, vod.title, body)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(OnBooleanProvided(booleanLive, Runnable {
                    getFeedVods(vod.authorLogin)
                }))
        return booleanLive
    }

    private fun getMimeType(url: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    private inner class OnBooleanProvided(private var booleanLive : MutableLiveData<Boolean>, private var runnable : Runnable? = null) : SingleObserver<DatabaseResponse> {
        override fun onSuccess(t: DatabaseResponse) {
            booleanLive.value = t.resultCode == 0
            runnable?.run()
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            booleanLive.value = false
        }
    }

    private inner class OnNumberProvided(private var nbLive : MutableLiveData<Int>) : SingleObserver<DatabaseResponse>{
        override fun onSuccess(t: DatabaseResponse) {
            nbLive.value = t.resultCode
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            nbLive.value = -1
        }
    }

    private inner class OnVodProvided(private var vodsLive: MutableLiveData<List<Vod>>) : SingleObserver<Array<Vod>>{

        override fun onSuccess(t: Array<Vod>) {
            if(t.size > 0 ) {
                val vodsObservable = Observable.fromIterable(t.toList())
                        .filter { t ->
                            ((t.audioFilePath != null)
                                    && t.audioFilePath!!.isNotEmpty() && t.idVod != -1
                                    && t.timeInSecond != 0)
                        }
                        .blockingIterable()
                val providedList = vodsObservable.toList()
                val listToSend = ArrayList<Vod>()
                for(vod in providedList){
                    if(providedVodById.containsKey(vod.idVod)){
                        listToSend.add(providedVodById.get(vod.idVod)!!)
                    }else{
                        listToSend.add(vod)
                        providedVodById.put(vod.idVod, vod)
                    }
                }
                vodsLive.value = listToSend
            }else{
                vodsLive.value = listOf()
            }
        }
        override fun onSubscribe(d: Disposable) {}
        override fun onError(e: Throwable) {
            vodsLive.value = listOf()
        }

    }

}