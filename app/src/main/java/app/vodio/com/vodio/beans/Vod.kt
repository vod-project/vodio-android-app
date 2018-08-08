package app.vodio.com.vodio.beans;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

data class Vod(var timeInSecond : Float?, var authorLogin : String?, var title : String?, var tags : List<String>?, var audioFilePath : String?){
    constructor(obj: JSONObject) : this(null,null,null, null, null){
        timeInSecond = (obj.get("timeInSecond").toString()).toFloat();
        authorLogin = obj.get("authorLogin").toString();
        title = obj.get("title").toString();
    }
}