package app.vodio.com.vodio.beans

import android.util.JsonReader;
import org.json.JSONObject;

data class User(var name : String?, var surname : String?, var login : String?, var email : String?, var password : String?){
    constructor(jsonObject: JSONObject): this(null,null,null,null,null){
        provideFromJsonObject(jsonObject)
    }

    fun isProvided() : Boolean {
        return ! (name == null && surname == null && login == null && email == null)
    }
    fun provideFromJsonObject(obj: JSONObject){
        name = checkStringJsonData(obj.get("name").toString());
        surname = checkStringJsonData(obj.get("surname").toString());
        login = checkStringJsonData(obj.get("login").toString());
        email = checkStringJsonData(obj.get("email").toString());
    }
    fun checkStringJsonData(str : String) : String?{
        if(str.equals("null")){
            return null
        }
        return str;
    }
}