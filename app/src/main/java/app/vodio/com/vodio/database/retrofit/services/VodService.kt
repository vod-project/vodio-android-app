package app.vodio.com.vodio.database.retrofit.services

import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.database.DatabaseResponse
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface VodService{

    @GET("vod/get.php")
    fun getVods_old() : Call<Array<Vod>>

    @GET("vod/get.php")
    fun getVods() : Single<Array<Vod>>

    @Multipart
    @POST("vod/create.php")
    fun createVod(
            @Query("timeInSecond") timeInSecond : Int,
            @Query("authorLogin") authorLogin : String,
            @Query("title") title : String,
            @Part("description") reqBody : RequestBody,
            @Part("file") file : RequestBody
    ) : Single<DatabaseResponse>
}