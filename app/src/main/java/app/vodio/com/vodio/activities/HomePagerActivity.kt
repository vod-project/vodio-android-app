package app.vodio.com.vodio.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.AttributeSet
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import app.vodio.com.vodio.R
import app.vodio.com.vodio.fragments.BottomNavFragment
import app.vodio.com.vodio.fragments.BottomRecordFragment
import app.vodio.com.vodio.database.retrofit.repositories.LoginRepository
import app.vodio.com.vodio.database.retrofit.repositories.VodRepository
import app.vodio.com.vodio.utils.MediaPlayerService
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class HomePagerActivity : AbstractPagerActivity(), ViewPager.OnPageChangeListener, NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener{
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
        return false
    }

    fun showFragmentByItemId(menuItem: MenuItem){
        val position = bottomNavFragment!!.getPositionByItemId(menuItem.itemId)
        if(pager!!.currentItem != position) {
            if (position != -1) {
                setPagePosition(position)
                bottomNavFragment!!.setSelected(position)
            }
        }
    }

    var bottomNavFragment: BottomNavFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNavFragment = supportFragmentManager.findFragmentById(R.id.bottom_static_fragment) as BottomNavFragment
        //showFragment(bottomNavFragment, R.id.layoutBottomBar)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        setSlideEnabled(PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("sliding_views", false))
    }

    override fun onResume() {
        super.onResume()
        requestPermissions()
        if(LoginRepository.getInstance(applicationContext).getLoggedUser().value == null){
            performSignOut()
        }else{
            // stay in this activity
        }
    }

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

    // Abstract pager activity
    override fun getBackgroundId(): Int {return R.drawable.background_home}
    override fun getMainLayout(): Int {return R.id.mainHomeLayout}
    override fun getViewPager(): Int {return R.id.pager}
    override fun getPagerAdapter(): PagerAdapter {return ScreenSlidePagerAdapter(supportFragmentManager,getFragmentsList())}
    override fun getFragmentsList(): List<Fragment> {return bottomNavFragment!!.fragments as List<Fragment>}
    override fun getOnPageChangeListener(): ViewPager.OnPageChangeListener {return this}
    override fun getLayoutContentView(): Int {return R.layout.activity_home_pager}
    // On page change listener
    override fun onPageScrollStateChanged(state: Int) {}
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        bottomNavFragment!!.setSelected(position)
    }

    class ScreenSlidePagerAdapter(fm: FragmentManager, var fragments: List<Fragment>) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment? {
            return fragments.get(position)
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            11 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.w(this.javaClass.simpleName,"permission granted")
                    //continueRecordAfterPermissionGranted()
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    //cancelRecordAfterPermissionNotGranted()
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        // Add other 'when' lines to check for other
        // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


    fun requestPermissions(){
        Log.w(this.javaClass.simpleName,"request permission")
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        11)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            //continueRecordAfterPermissionGranted()
            Log.w(this.javaClass.simpleName,"permission granted")
        }
    }
}