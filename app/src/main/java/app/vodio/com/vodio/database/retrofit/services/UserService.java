package app.vodio.com.vodio.database.retrofit.services;

import app.vodio.com.vodio.beans.User;
import app.vodio.com.vodio.database.DatabaseResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

    @GET("user/get.php")
    Call<User> getUser(@Query("login") String login, @Query("password") String password);

    @GET("user/create.php")
    Call<DatabaseResponse> createUser(@Query("login") String login, @Query("password") String password,
                                      @Query("name") String name, @Query("surname") String surname,@Query("email") String email);
}
