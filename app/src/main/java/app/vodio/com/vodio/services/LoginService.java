package app.vodio.com.vodio.services;

import android.content.Context;

import java.util.List;

import app.vodio.com.vodio.beans.User;
import app.vodio.com.vodio.database.DatabaseResponse;
import app.vodio.com.vodio.database.retrofit.RetrofitInstance;
import app.vodio.com.vodio.database.retrofit.services.UserService;
import app.vodio.com.vodio.utils.AuthentificationChecker;
import app.vodio.com.vodio.utils.OnCompleteAsyncTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginService {
    private static LoginService INSTANCE;
    public static LoginService getInstance(){
        if(INSTANCE == null){INSTANCE = new LoginService();}
        return INSTANCE;
    }
    private User loggedIn;
    public void setLoggedIn(User usr){loggedIn = usr;}
    public User getLoggedIn(){return loggedIn;}

    public void signIn(String login, String password, OnCompleteAsyncTask onComplete, Context c){
        //AsyncTask getTask = new UserMapper.GetUserTask(login,password, onComplete, c);
        //getTask.execute();
        UserService service = RetrofitInstance.Companion.getRetrofitInstance().create(UserService.class);

        Call<User> call = service.getUser(login,password);

        call.enqueue(new OnCompleteLogin(onComplete));
    }

    public List<AuthentificationChecker.AuthCheckResult> signUp(String login, String password, String name,
                                                                       OnCompleteAsyncTask onComplete, Context c) {
        List<AuthentificationChecker.AuthCheckResult> checkResult = AuthentificationChecker.check(login, password,name);
        if(checkResult.size() == 0) {
            UserService service = RetrofitInstance.Companion.getRetrofitInstance().create(UserService.class);
            Call<DatabaseResponse> call = service.createUser(login,password,name,"","");
            call.enqueue(new OnCompleteRegister(onComplete));
        }
        return checkResult;
    }

    public class OnCompleteLogin implements Callback<User>{
        private OnCompleteAsyncTask complete;
        public OnCompleteLogin(OnCompleteAsyncTask onCompleteAsyncTask){
              complete = onCompleteAsyncTask;
        }
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            User usr = response.body();
            if(usr.isProvided()) {
                complete.onSuccess(usr);
            }else{
                complete.onFail();
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {
            complete.onFail();
        }
    }

    public class OnCompleteRegister implements Callback<DatabaseResponse>{
        private OnCompleteAsyncTask complete;
        public OnCompleteRegister(OnCompleteAsyncTask onCompleteAsyncTask){
            complete = onCompleteAsyncTask;
        }
        @Override
        public void onResponse(Call<DatabaseResponse> call, Response<DatabaseResponse> response) {
            DatabaseResponse databaseResponse = response.body();
            if(databaseResponse.getResultCode() == 0){
                complete.onSuccess(databaseResponse);
            }
            else{
                complete.onFail();
            }

        }

        @Override
        public void onFailure(Call<DatabaseResponse> call, Throwable t) {
            complete.onFail();
        }
    }
}
