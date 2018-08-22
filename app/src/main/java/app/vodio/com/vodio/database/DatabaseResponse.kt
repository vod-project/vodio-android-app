package app.vodio.com.vodio.database;

import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

class DatabaseResponse{
    var resultCode : Int = 0
    var message : String? = null
    constructor(obj : JSONObject){
        provideFromJsonObject(obj)
    }
    private fun provideFromJsonObject( obj : JSONObject){
        resultCode = obj.get("result").toString().toInt()
        if(obj.get("message") != null){
            message = obj.get("message").toString()
        }
    }

    override fun toString(): String {
        return "resultCode : ${resultCode}, msg : ${message}"
    }
}