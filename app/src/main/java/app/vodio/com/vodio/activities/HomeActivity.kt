package app.vodio.com.vodio.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import app.vodio.com.vodio.R
import app.vodio.com.vodio.fragments.*
import app.vodio.com.vodio.services.LoginService

class HomeActivity : AbstractPagerActivity(), ViewPager.OnPageChangeListener {
    val bottomNavFragment: BottomNavFragment = BottomNavFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // show fragment
        showFragment(bottomNavFragment, R.id.layoutBottomBar)
    }

    override fun onResume() {
        super.onResume()
        requestPermissions()
        if(LoginService.Companion.getInstance()?.loggedIn == null){
            //performSignOut()
        }else{
            // stay in this activity
        }
    }

    override fun onItemSelected(itemId: Int) {
        when(itemId){
            R.id.recordVodFHome -> performRecording()
            R.id.signOut -> {performSignOut()}
        }
    }

    fun performRecording(){
        //val mediaRecorder = MediaRecorderFactory.getInstance()?.create()
        val bottomSheetRecord = BottomRecordFragment()
        bottomSheetRecord.show(supportFragmentManager,"recordBottomSheet")
        // show audio encoder


    }
    fun performSignOut() {
        LoginService.getInstance()?.loggedIn = null
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }

    // Abstract pager activity
    override fun getBackgroundId(): Int {return R.drawable.background_home}
    override fun getMainLayout(): Int {return R.id.mainHomeLayout}
    override fun getViewPager(): Int {return R.id.pager}
    override fun getPagerAdapter(): PagerAdapter {return ScreenSlidePagerAdapter(supportFragmentManager,getFragmentsList())}
    override fun getFragmentsList(): List<Fragment> {return bottomNavFragment.fragments}
    override fun getOnPageChangeListener(): ViewPager.OnPageChangeListener {return this}
    override fun getLayoutContentView(): Int {return R.layout.activity_home}
    // On page change listener
    override fun onPageScrollStateChanged(state: Int) {}
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        bottomNavFragment?.setSelected(position)
    }

    class ScreenSlidePagerAdapter(var fm: FragmentManager, var fragments: List<Fragment>) : FragmentStatePagerAdapter(fm) {
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