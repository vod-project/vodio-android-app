package app.vodio.com.vodio.database.retrofit.services

import app.vodio.com.vodio.beans.Vod
import retrofit2.Call
import retrofit2.http.GET

interface VodService{

    @GET("vod/get.php")
    fun getVods() : Call<Array<Vod>>
}