package app.vodio.com.vodio.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import app.vodio.com.vodio.R
import app.vodio.com.vodio.fragments.utils.FragmentCallBack
import java.util.*

abstract class AbstractActivity : AppCompatActivity(), FragmentCallBack {
    val fragmentsBack: Stack<Fragment> = Stack()

    abstract fun getMainLayout(): Int
    abstract fun getBackgroundId(): Int
    abstract fun getLayoutContentView() : Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutContentView())
    }

    override fun onResume() {
        super.onResume()
        setBackgroundImage();
    }

    override fun onBackPressed() {
        synchronized(fragmentsBack) {
            if (fragmentsBack.size > 1) {
                super.onBackPressed()
                fragmentsBack.pop()
            } else {
                finish()
            }
        }
    }

    fun showFragment(fragment: Fragment, idContainerView: Int) {
        val fm: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()

        // We can also animate the changing of fragment.
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        // Replace current fragment by the new one.
        ft.replace(idContainerView, fragment)
        // Null on the back stack to return on the previous fragment when user
        // press on back button.
        ft.addToBackStack(null)

        // Commit changes.
        ft.commit()
        synchronized(fragmentsBack) {
            fragmentsBack.push(fragment)
        }


    }

    fun setBackgroundImage() {
        window.setBackgroundDrawable(getDrawable(getBackgroundId()))
    }
}