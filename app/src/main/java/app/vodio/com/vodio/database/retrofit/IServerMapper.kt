package app.vodio.com.vodio.database.retrofit

import org.json.JSONArray
import org.json.JSONObject

interface IServerMapper{
    fun getObject(host : String, path : String, params : Map<String, String>) : JSONObject?

    fun getArray(host : String, path : String, params : Map<String, String>) : JSONArray?
}