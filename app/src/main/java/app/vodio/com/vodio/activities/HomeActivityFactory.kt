package app.vodio.com.vodio.activities

import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity

class HomeActivityFactory (val context: Context){
    fun startHomeActivity(){
        val sliding = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("sliding_views", false)
        context.startActivity(Intent(context, HomePagerActivity::class.java))
        /*if(sliding){
            context.startActivity(Intent(context, HomePagerActivity::class.java))
        }else{
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
        */
    }
}