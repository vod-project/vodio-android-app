package app.vodio.com.vodio.database;

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

import app.vodio.com.vodio.beans.User;
import app.vodio.com.vodio.services.LoginService;
import app.vodio.com.vodio.utils.MyAsyncTask;
import app.vodio.com.vodio.utils.OnCompleteAsyncTask;


public  class UserMapper {
    private static User currentUser;
    private static String host = "http://www.assimsen.fr/vodio/rest";

    public static class GetUserTask extends MyAsyncTask {
        private String login;private String password;
        public GetUserTask(String login, String password, OnCompleteAsyncTask onComplete){
            super(onComplete);
            this.login = login;this.password = password;
        }

        @Override
        public Object doInBackground(Object[] objects) {
            boolean res = false;
            // Create URL
            URL createUrl = null;
            try {
                String url = host + "/user/get.php";
                String parameter = String.format("?login=%s",login);
                if(password != null){
                    parameter += "&password="+password;
                }
                createUrl = new URL(url+parameter);
                // Create connection
                HttpURLConnection myConnection =
                        (HttpURLConnection) createUrl.openConnection();

                if (myConnection.getResponseCode() == 200) {
                    // Success
                    // Further processing here
                    InputStream responseBody = myConnection.getInputStream();

                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");

                    JSONObject obj = inputStreamReaderToJSONObject(responseBodyReader);

                    User usr = new User(obj);
                    if(usr.isProvided()){
                        LoginService.setLoggedIn(usr);
                        res = true;
                    }
                }
            } catch (MalformedURLException e) {} catch (IOException e) {}
            setSuccess(res);
            return null;
        }

        @Override
        public Object getObject() {
            return null;
        }
    }

    public static class CreateUserTask extends MyAsyncTask{
        private User usr;
        public CreateUserTask(User user,OnCompleteAsyncTask onComplete){
            super(onComplete);
            usr = user;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            boolean res = false;
            // Create URL
            URL createUrl = null;
            try {
                String url = host + "/user/create.php";
                String parameter = String.format("?login=%s&password=%s&name=%s",usr.getLogin(),usr.getPassword(),usr.getName());
                createUrl = new URL(url+parameter);
                // Create connection
                HttpURLConnection myConnection =
                        (HttpURLConnection) createUrl.openConnection();

                if (myConnection.getResponseCode() == 200) {
                    // Success
                    // Further processing here
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");

                    JSONObject obj = inputStreamReaderToJSONObject(responseBodyReader);
                    DatabaseResponse response = new DatabaseResponse(obj);
                    if(response.getResult()){
                        LoginService.setLoggedIn(usr);
                        res = true;
                    }
                }
            } catch (MalformedURLException e) {
                //onFail.run();
            } catch (IOException e) {
                //onFail.run();
            }
            setSuccess(res);
            return null;
        }

        @Override
        public Object getObject() {
            return null;
        }
    }

    public static JSONObject inputStreamReaderToJSONObject(InputStreamReader inputStreamReader) throws IOException {
        JSONObject obj = null;
        StringBuilder sBuilder = new StringBuilder();
        BufferedReader bReader = new BufferedReader(inputStreamReader);
        String input;
        while ((input = bReader.readLine()) != null) {
            sBuilder.append(input);
        }
        try {
            obj = new JSONObject(sBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static JSONArray inputStreamReaderToJSonArray(InputStreamReader inputStreamReader) throws IOException{
        JSONArray obj = null;
        StringBuilder sBuilder = new StringBuilder();
        BufferedReader bReader = new BufferedReader(inputStreamReader);
        String input;
        while ((input = bReader.readLine()) != null) {
            sBuilder.append(input);
        }
        try {
            obj = new JSONArray(sBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
