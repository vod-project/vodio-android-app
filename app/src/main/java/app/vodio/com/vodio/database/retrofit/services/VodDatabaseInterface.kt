package app.vodio.com.vodio.database.retrofit.services

import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.database.retrofit.utils.DatabaseResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface VodDatabaseInterface{


    @GET("vod/get.php?mode=feed")
    fun getVods(@Query("login_provider") providerLogin : String) : Single<Array<Vod>>


    @GET("vod/get.php")
    fun getVodsByAuthor(@Query("authorLogin") login : String,
                        @Query("timestamp_since") timestamp : Int = 0,
                        @Query("mode") mode : String = "simple") : Single<Array<Vod>>


    @GET("vod/delete.php")
    fun delete(@Query("idVod") idVod : Int) : Single<DatabaseResponse>


    @GET("vod/update.php")
    fun like(@Query("vod_id") idVod : Int,
             @Query("by_login") login : String,
             @Query("mode") mode : String = "like") : Single<DatabaseResponse>


    @GET("vod/update.php")
    fun unlike(@Query("vod_id") idVod : Int,
             @Query("by_login") login : String,
             @Query("mode") mode : String = "unlike") : Single<DatabaseResponse>


    @GET("vod/get.php")
    fun isLiking(@Query("vod_id") idVod : Int,
               @Query("by_login") login : String,
               @Query("mode") mode : String = "isLiking") : Single<DatabaseResponse>


    @GET("vod/get.php")
    fun numberOfLike(@Query("vod_id") idVod : Int,
                 @Query("mode") mode : String = "number_of_like") : Single<DatabaseResponse>


    @Multipart
    @POST("vod/create.php")
    fun createVod(
            @Query("timeInSecond") timeInSecond : Int,
            @Query("authorLogin") authorLogin : String,
            @Query("title") title : String,
            @Part file : MultipartBody.Part
    ) : Single<DatabaseResponse>
}