package app.vodio.com.vodio.database.retrofit.services;


import app.vodio.com.vodio.beans.User;
import app.vodio.com.vodio.database.DatabaseResponse;
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import java.util.*

interface UserService {

    @GET("user/get.php")
    fun getUser_old(@Query("login") login: String, @Query("password") password: String): Call<User>

    @GET("user/get.php")
    fun getUser(@Query("login") login: String, @Query("password") password: String): Single<User>

    @GET("user/create.php")
    fun createUser_old(@Query("login") login: String, @Query("password") password: String,
                   @Query("name") name: String, @Query("surname") surname: String, @Query("email") email: String): Call<DatabaseResponse>

    @GET("user/create.php")
    fun createUser(@Query("login") login: String, @Query("password") password: String,
                   @Query("name") name: String, @Query("surname") surname: String, @Query("email") email: String): Single<DatabaseResponse>
}