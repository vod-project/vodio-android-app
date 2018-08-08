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
import java.util.Map;

public class ServerMapper {

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
        String paramStr="";
        String url = host + path;
        if(params.size() > 0){
            int n = params.size();
            int current = 1;
            paramStr = "?";
            for(Map.Entry<String,String> e: params.entrySet()){
                String name = e.getKey();
                String val = e.getValue();
                paramStr += name+"="+val;
                if( current != n){
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

    private static JSONObject inputStreamReaderToJSONObject(InputStreamReader inputStreamReader) throws IOException {
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
    private static JSONArray inputStreamReaderToJSonArray(InputStreamReader inputStreamReader) throws IOException{
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
