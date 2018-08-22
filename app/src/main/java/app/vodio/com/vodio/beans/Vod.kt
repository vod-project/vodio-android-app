package app.vodio.com.vodio.beans;

import android.util.Log
import app.vodio.com.vodio.database.retrofit.services.DownloadService
import app.vodio.com.vodio.services.VodService
import org.json.JSONObject;
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.util.concurrent.Callable
import java.util.concurrent.Future
import java.util.concurrent.FutureTask
import java.util.concurrent.ThreadPoolExecutor

data class Vod(var idVod : Int = -1, var timeInSecond: Int? = 0, var authorLogin: String = "", var title: String = "", var tags: List<String> = ArrayList<String>(), var audioFilePath: String = "", var futureFile : Future<File>? = null, var file : File? = null){
    constructor(obj: JSONObject) : this(){
        timeInSecond = (obj.get("timeInSecond").toString()).toInt();
        authorLogin = obj.get("authorLogin").toString();
        title = obj.get("title").toString();
        audioFilePath = obj.get("audioFilePath").toString()
        Log.v(javaClass.simpleName,"${audioFilePath}")
    }

    fun prepareFile(){
        if(file != null){
            // fichier deja present

        }else if(futureFile != null){
            // en cours
        }else{
            // a telecharger
        }
    }
}