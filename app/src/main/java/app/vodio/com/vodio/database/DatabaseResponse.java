package app.vodio.com.vodio.database;

import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class DatabaseResponse {
    private boolean result = false;
    public DatabaseResponse(JsonReader reader){
        try {
            provideFromJson(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public DatabaseResponse(JSONObject obj){
        try {
            provideFromJsonObject(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void provideFromJsonObject(JSONObject obj) throws JSONException {
        result = Integer.parseInt(obj.get("result").toString()) == 0;
    }
    private void provideFromJson(JsonReader reader) throws IOException {
        reader.beginObject();
        reader.nextName();
        result = reader.nextInt() == 0;
    }

    public boolean getResult(){
        return result;
    }


}
