package app.vodio.com.vodio.beans;

import org.json.JSONObject;
import java.io.File

data class Vod(var timeInSecond: Int? = 0, var authorLogin: String = "", var title: String = "", var tags: List<String> = ArrayList<String>(), var audioFilePath: String = ""){
    constructor(obj: JSONObject) : this(){
        timeInSecond = (obj.get("timeInSecond").toString()).toInt();
        authorLogin = obj.get("authorLogin").toString();
        title = obj.get("title").toString();
    }

    fun getAudioFile() : File? {
        val file = null
        if(audioFilePath != null && !audioFilePath.isEmpty()){
             //provide File from vod service
        }else{

        }
        return file
    }
}