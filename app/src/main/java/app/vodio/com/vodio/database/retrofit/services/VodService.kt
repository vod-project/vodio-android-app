package app.vodio.com.vodio.database.retrofit.services

import app.vodio.com.vodio.beans.Vod
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET

interface VodService{

    @GET("vod/get.php")
    fun getVods_old() : Call<Array<Vod>>

    @GET("vod/get.php")
    fun getVods() : Single<Array<Vod>>
}