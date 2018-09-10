package app.vodio.com.vodio.models

import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.database.retrofit.services.VodDatabaseInterface
import app.vodio.com.vodio.fragments.utils.CollectionView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class VodsPresenter(private val retrofit: Retrofit) : BasePresenter<CollectionView<Vod>>(){

    fun loadFeedVods(loginProvider : String){
        retrofit
                .create(VodDatabaseInterface::class.java)
                .getVods(loginProvider).delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view()?.showCollectionLoading()
                }
                .subscribe({
                    if((it == null) || (it.isEmpty())){
                        view()?.showCollectionNoData()
                    }else {
                        view()?.showCollection(it.toList())
                    }
                },{
                    view()?.showCollectionError(it.message!!)
        })
    }

    fun loadVodsByAuthor(authorLogin : String){
        retrofit
                .create(VodDatabaseInterface::class.java)
                .getVodsByAuthor(authorLogin).delay(1,TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view()?.showCollectionLoading()
                }
                .subscribe({
                    if(it == null || (it.isEmpty())){
                        view()?.showCollectionNoData()
                    }else{
                        view()?.showCollection(it.toList())
                    }
                },{
                    view()?.showCollectionError(it.message!!)
                })
    }
}