package app.vodio.com.vodio.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import app.vodio.com.vodio.R
import app.vodio.com.vodio.database.retrofit.repositories.LoginRepository
import app.vodio.com.vodio.database.retrofit.repositories.VodRepository
import app.vodio.com.vodio.fragments.BottomNavFragment
import app.vodio.com.vodio.fragments.BottomRecordFragment
import app.vodio.com.vodio.utils.MediaPlayerService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class HomeActivity : AbstractActivity(), IMultiFragmentActivity, BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener{
    private val fragmentPositionMap = HashMap<Int, Fragment>()
    private var currentPosition = -1
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        if(p0.itemId == R.id.signOutNav){
            performSignOut()
            return true
        }
        if(p0.itemId == R.id.settingsNav){
            startActivity(Intent(applicationContext, SettingsActivity::class.java))
            return true
        }
        showFragmentByItemId(p0)
        return true
    }

    fun showFragmentByItemId(menuItem: MenuItem){
        val position = bottomNavFragment!!.getPositionByItemId(menuItem.itemId)
        if(position != currentPosition) {
            currentPosition = position
            val fragment : Fragment
            if(!fragmentPositionMap.containsKey(position)) {
                fragment = bottomNavFragment!!.fragments.get(position)
                fragmentPositionMap.put(position, fragment)
            }
            else
                fragment = fragmentPositionMap.get(position)!!
            supportFragmentManager.beginTransaction().replace(R.id.mainHomeLayout, fragment).commit()
            bottomNavFragment!!.setSelected(position)
        }
    }

    override fun setFragment(fragment: Fragment) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var bottomNavFragment: BottomNavFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNavFragment = supportFragmentManager.findFragmentById(R.id.bottom_static_fragment) as BottomNavFragment
        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)
    }

    override fun getBackgroundId(): Int {return R.drawable.background_home}
    override fun getMainLayout(): Int {return R.id.mainHomeLayout}
    override fun getLayoutContentView(): Int {return R.layout.activity_home}

    override fun onItemSelected(itemId: Int) {
        when(itemId){
            R.id.recordVodFHome -> performRecording()
        //R.id.signOut -> {performSignOut()}
        //R.id.signOut -> {performSignOut()}
        }
    }

    fun performRecording(){
        //val mediaRecorder = MediaRecorderFactory.getInstance()?.create()
        val bottomSheetRecord = BottomRecordFragment()
        bottomSheetRecord.show(supportFragmentManager,"recordBottomSheet")
        // show audio encoder
    }

    fun performSignOut() {
        LoginRepository.getInstance(applicationContext).signOut()
        VodRepository.getInstance(applicationContext).clear()
        MediaPlayerService.getInstance(applicationContext).clear()
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }
}