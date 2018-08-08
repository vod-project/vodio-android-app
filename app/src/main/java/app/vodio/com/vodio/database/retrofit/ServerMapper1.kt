package app.vodio.com.vodio.database.retrofit

import app.vodio.com.vodio.utils.InputStreamReaderUtils
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

object ServerMapper1 : IServerMapper {

    override fun getObject(host: String, path: String, params: Map<String, String>): JSONObject? {
        var obj: JSONObject? = null
        var createUrl: URL?
        try {
            createUrl = constructUrl(host, path, params)

            var responseBodyReader: InputStreamReader? = performRequest(createUrl)
            obj = InputStreamReaderUtils.inputStreamReaderToJSONObject(responseBodyReader)
        } catch (ioe : IOException) {

        }
        return obj
    }

    override fun getArray(host: String, path: String, params: Map<String, String>): JSONArray? {
        var obj: JSONArray? = null
        var createUrl: URL? = null
        try {
            createUrl = constructUrl(host, path, params)
            var responseBodyReader : InputStreamReader? = performRequest(createUrl)
            obj = InputStreamReaderUtils.inputStreamReaderToJSONArray(responseBodyReader)
        } catch (ioe: IOException) {

        }
        return obj
    }

    fun constructUrl(host: String, path: String, params: Map<String, String>): URL? {
        var paramStr = ""
        val url = host + path
        if (params.size > 0) {
            val n = params.size
            var current = 1
            paramStr = "?"
            for (e in params.entries) {
                paramStr += "${e.key}=${e.value}"
                if (current != n) {
                    paramStr += "&"
                }
                current++
            }
        }
        try {
            return URL(url + paramStr)
        } catch (e: MalformedURLException) {
            return null
        }

    }

    fun performRequest(url: URL?): InputStreamReader? {
        var myConnection: HttpURLConnection? = null
        try {
            myConnection = url?.openConnection() as HttpURLConnection?
            if (myConnection!!.responseCode == 200) {
                // Success
                // Further processing here
                val responseBody = myConnection.inputStream
                return InputStreamReader(responseBody, "UTF-8")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }
}