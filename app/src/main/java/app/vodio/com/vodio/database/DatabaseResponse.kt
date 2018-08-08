package app.vodio.com.vodio.database;

import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

class DatabaseResponse{
    var resultCode : Int = 0
    constructor(obj : JSONObject){
        provideFromJsonObject(obj)
    }
    private fun provideFromJsonObject( obj : JSONObject){
        resultCode = obj.get("result").toString().toInt()
    }
}