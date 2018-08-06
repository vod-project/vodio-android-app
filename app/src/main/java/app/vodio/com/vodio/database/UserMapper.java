package app.vodio.com.vodio.database;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import app.vodio.com.vodio.R;
import app.vodio.com.vodio.beans.User;
import app.vodio.com.vodio.services.LoginService;
import app.vodio.com.vodio.utils.MyAsyncTask;
import app.vodio.com.vodio.utils.OnCompleteAsyncTask;


public  class UserMapper {
    private static User currentUser;

    public static class GetUserTask extends MyAsyncTask<User> {
        private String login;private String password;
        private String path = "/user/get.php";
        private String host;
        private User usr;
        public GetUserTask(String login, String password, OnCompleteAsyncTask onComplete, Context c){
            super(onComplete,c);
            host = c.getString(R.string.host_database);
            this.login = login;this.password = password;
        }

        @Override
        public Object doInBackground(Object[] objects) {
            Map<String, String> map = new HashMap<>();
            map.put("login",login);
            if(password != null){map.put("password",password);}
            JSONObject obj = HTTPUtils.getObject(host,path,map);
            if(obj != null){
                usr = new User(obj);
                setSuccess(usr.isProvided());
            }else{
                setSuccess(false);
            }
            if(usr.isProvided()){LoginService.setLoggedIn(usr);}
            return null;

        }

        @Override
        public User getObject() {
            return usr;
        }
    }

    public static class CreateUserTask extends MyAsyncTask<DatabaseResponse>{
        private User usr;
        private DatabaseResponse response = null;
        private String host;
        private String path = "/user/create.php";
        public CreateUserTask(User user,OnCompleteAsyncTask onComplete, Context c){
            super(onComplete,c);
            usr = user;
            host = c.getString(R.string.host_database);
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            Map<String, String> map = new HashMap<>();
            map.put("login",usr.getLogin());
            map.put("password",usr.getPassword());
            map.put("name",usr.getName());
            map.put("email",usr.getEmail());
            map.put("surname",usr.getSurname());
            JSONObject obj = HTTPUtils.getObject(host,path,map);
            if(obj != null) {
                response = new DatabaseResponse(obj);
                setSuccess(response.getResult());
            }else {
                setSuccess(false);
            }
            return null;
        }

        @Override
        public DatabaseResponse getObject() {
            return response;
        }
    }
}
