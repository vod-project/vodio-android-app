package app.vodio.com.vodio.database;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import app.vodio.com.vodio.services.LoginService;

import static app.vodio.com.vodio.database.UserMapper.inputStreamReaderToJSONObject;
import static app.vodio.com.vodio.database.UserMapper.inputStreamReaderToJSonArray;

public class HTTPUtils {

    public static JSONObject getObject(String host, String path, Map<String,String> params){
        JSONObject obj = null;
        // Create URL
        URL createUrl = null;
        try {
            createUrl = constructStringUrl(host,path,params);
            // Create connection
            InputStreamReader responseBodyReader = performRequest(createUrl);
            obj = inputStreamReaderToJSONObject(responseBodyReader);
        }catch (IOException e) {
            //onFail.run();
        }
        return obj;
    }

    public static JSONArray getArray(String host, String path, Map<String,String> params){
        JSONArray obj = null;
        // Create URL
        URL createUrl = null;
        try{
            createUrl = constructStringUrl(host,path,params);
            // Create connection
            InputStreamReader responseBodyReader = performRequest(createUrl);
            obj = inputStreamReaderToJSonArray(responseBodyReader);
        } catch (IOException e) {
            //onFail.run();
        }
        return obj;
    }

    private static URL constructStringUrl(String host, String path, Map<String, String> params){
        String url = host + path;
        String paramStr="";
        if(params.size() > 0){
            int n = params.size();
            int current = 1;
            paramStr = "?";
            for(Map.Entry<String,String> e: params.entrySet()){
                String name = e.getKey();
                String val = e.getValue();
                paramStr += name+"="+val;
                if(!( current != n)){
                    paramStr+="&";
                }
                current++;
            }
        }
        try {
            return new URL(url + paramStr);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private static InputStreamReader performRequest(URL createUrl){
        HttpURLConnection myConnection =
                null;
        try {
            myConnection = (HttpURLConnection) createUrl.openConnection();
            if (myConnection.getResponseCode() == 200) {
                // Success
                // Further processing here
                InputStream responseBody = myConnection.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");
                return responseBodyReader;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}