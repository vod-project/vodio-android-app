package app.vodio.com.vodio.database.retrofit.services;


import app.vodio.com.vodio.beans.User;
import app.vodio.com.vodio.database.DatabaseResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface UserService {

    @GET("user/get.php")
    fun getUser(@Query("login") login: String, @Query("password") password: String): Call<User>

    @GET("user/create.php")
    fun createUser(@Query("login") login: String, @Query("password") password: String,
                   @Query("name") name: String, @Query("surname") surname: String, @Query("email") email: String): Call<DatabaseResponse>
}