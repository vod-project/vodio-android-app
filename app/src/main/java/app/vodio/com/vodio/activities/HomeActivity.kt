package app.vodio.com.vodio.activities

import android.content.DialogInterface
import android.content.Intent
import android.icu.util.VersionInfo
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.widget.toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import app.vodio.com.vodio.R
import app.vodio.com.vodio.fragments.*
import app.vodio.com.vodio.services.LoginService
import app.vodio.com.vodio.utils.recording.MediaRecorderFactory
import kotlinx.android.synthetic.main.fragment_bottom_nav.*
import java.io.File
import java.util.*

class HomeActivity : AbstractPagerActivity(), ViewPager.OnPageChangeListener {
    val bottomNavFragment: BottomNavFragment = BottomNavFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // show fragment
        showFragment(bottomNavFragment, R.id.layoutBottomBar)
    }

    override fun onResume() {
        super.onResume()
        if(LoginService.Companion.getInstance()?.loggedIn == null){
            performSignOut()
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
    override fun getFragmentsList(): List<Fragment> {return listOf(ChambreFragment(), SalonFragment(), ProfileFragment(), ActuFragment(), SettingsFragment())}
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
}