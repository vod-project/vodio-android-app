package app.vodio.com.vodio.database;

import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class DatabaseResponse {
    private int resultCode = 0;
    public DatabaseResponse(JSONObject obj){
        try {
            provideFromJsonObject(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void provideFromJsonObject(JSONObject obj) throws JSONException {
        resultCode = Integer.parseInt(obj.get("result").toString());
    }
    private void provideFromJson(JsonReader reader) throws IOException {
        reader.beginObject();
        reader.nextName();
        resultCode = reader.nextInt();
    }

    public int getResultCode(){
        return resultCode;
    }


}
