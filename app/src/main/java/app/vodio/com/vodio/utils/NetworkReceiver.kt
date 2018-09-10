package app.vodio.com.vodio.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData


class NetworkReceiver private constructor() : BroadcastReceiver() {

    companion object {
        private var INSTANCE : NetworkReceiver? = null
        fun getInstance() : NetworkReceiver{
            if(INSTANCE == null) INSTANCE = NetworkReceiver()
            return INSTANCE!!
        }
    }
    var isConnected = MutableLiveData<Boolean>()
    override fun onReceive(context: Context?, intent: Intent?) {
        val conn = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = conn.activeNetworkInfo

        // Checks the user prefs and the network connection. Based on the result, decides whether
        // to refresh the display or keep the current display.
        // If the userpref is Wi-Fi only, checks to see if the device has a Wi-Fi connection.
        if (networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI) {
            //Toast.makeText(context, "WiFi Connected", Toast.LENGTH_SHORT).show()
            isConnected.value = true
            // If the setting is ANY network and there is a network connection
            // (which by process of elimination would be mobile), sets refreshDisplay to true.
        } else isConnected.value = networkInfo != null

    }
}