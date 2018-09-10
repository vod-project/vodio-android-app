package app.vodio.com.vodio.database.retrofit.services


import app.vodio.com.vodio.beans.Profile
import app.vodio.com.vodio.beans.User
import app.vodio.com.vodio.database.retrofit.utils.DatabaseResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface UserDatabaseInterface {


    @GET("user/get.php")
    fun getUser(@Query("login") login: String,
                @Query("password") password: String): Single<User>

    @Headers("Cache-Control: no-cache")
    @GET("user/create.php")
    fun createUser(@Query("login") login: String, @Query("password") password: String,
                   @Query("name") name: String, @Query("surname") surname: String,
                   @Query("email") email: String): Single<DatabaseResponse>

    @Headers("Cache-Control: no-cache")
    @Multipart
    @GET("user/update.php")
    fun changeUserProfileImg(
            @Query("login") login : String,
            @Part file : MultipartBody.Part,
            @Query("mode") mode : String = "profile_img"
    ) : Single<DatabaseResponse>


    @GET("user/update.php")
    fun follow(@Query("follower") follower : String,
               @Query("followed") followed : String,
               @Query("mode") mode : String = "follow") : Single<DatabaseResponse>


    @GET("user/update.php")
    fun unfollow(@Query("unfollower") follower : String,
               @Query("unfollowed") followed : String,
               @Query("mode") mode : String = "unfollow") : Single<DatabaseResponse>


    @GET("user/get.php")
    fun isFollowing(
            @Query("login") login : String,
            @Query("withLogin") withLogin : String,
            @Query("mode") mode : String = "is_following") : Single<DatabaseResponse>


    @GET("user/get.php")
    fun numberOfFollower(@Query("login") login : String,
                         @Query("mode") mode : String = "number_of_follower") : Single<DatabaseResponse>


    @GET("user/get.php")
    fun numberOfFollowed(@Query("login") login : String,
                         @Query("mode") mode : String = "number_of_followed") : Single<DatabaseResponse>


    @GET("user/get.php")
    fun getCompleteName(@Query("login") login : String,
                        @Query("mode") mode : String = "complete_name") : Single<DatabaseResponse>


    @GET("user/get.php")
    fun getProfile(
            @Query("login") login : String,
            @Query("mode") mode : String = "profile"

    ) : Single<Profile>

    @GET("user/get.php")
    fun searchAll(
            @Query("by_name") name : String = "",
            @Query("mode") mode : String = "search_all") : Single<Array<Profile>>
}