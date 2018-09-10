package app.vodio.com.vodio.database.retrofit.utils

import org.json.JSONObject

class DatabaseResponse{
    var resultCode : Int = 0
    var message : String? = null
    var idObject : Int? = null
    private fun provideFromJsonObject( obj : JSONObject){
        resultCode = obj.get("result").toString().toInt()
        if(obj.get("message") != null){
            message = obj.get("message").toString()
        }
    }

    override fun toString(): String {
        return "resultCode : ${resultCode}, msg : ${message}, idObject : ${idObject}"
    }
}