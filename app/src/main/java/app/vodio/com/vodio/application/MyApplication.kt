package app.vodio.com.vodio.application

import android.app.Application
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import app.vodio.com.vodio.R
import app.vodio.com.vodio.utils.NetworkReceiver
import com.google.android.material.snackbar.Snackbar

class MyApplication : Application() , LifecycleOwner{
    companion object {
        val receiver  = NetworkReceiver.getInstance()
        fun isConnected() : Boolean{
            if(receiver.isConnected.value != null)
                return receiver.isConnected.value!!
            return false
        }
    }

    override fun getLifecycle(): Lifecycle {
        return ProcessLifecycleOwner.get().lifecycle
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        this.registerReceiver(receiver, filter)

        receiver.isConnected.observe(this, androidx.lifecycle.Observer {
            if(it){
                Toast.makeText(applicationContext, resources!!.getString(R.string.online_mode_string), Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(applicationContext, resources!!.getString(R.string.offline_mode_string), Toast.LENGTH_LONG).show()
            }
        })
    }


}