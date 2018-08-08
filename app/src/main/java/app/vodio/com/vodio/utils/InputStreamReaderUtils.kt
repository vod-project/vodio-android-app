package app.vodio.com.vodio.utils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class InputStreamReaderUtils{
    companion object {
        fun inputStreamReaderToJSONObject(inputStreamReader: InputStreamReader?) : JSONObject?{
            var obj : JSONObject? = null
            var sBuilder : StringBuilder = StringBuilder()
            var bReader : BufferedReader = BufferedReader(inputStreamReader)
            var input : String = bReader.readLine()
            while(input != null){
                sBuilder.append(input)
                input = bReader.readLine()
            }
            try{
                obj = JSONObject(sBuilder.toString())
            }catch(jsonE : JSONException){

            }
            return obj

        }

        fun inputStreamReaderToJSONArray(inputStreamReader: InputStreamReader?) : JSONArray?{
            var obj : JSONArray? = null
            var sBuilder = StringBuilder()
            var bReader = BufferedReader(inputStreamReader)
            var input = bReader.readLine()
            while(input != null){
                sBuilder.append(input)
                input = bReader.readLine()
            }
            try{
                obj = JSONArray(sBuilder.toString())
            }catch(jsonE : JSONException){

            }
            return obj
        }
    }
}