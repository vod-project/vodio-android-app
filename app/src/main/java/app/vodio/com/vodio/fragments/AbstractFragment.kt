package app.vodio.com.vodio.fragments

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.LayoutDirection
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import app.vodio.com.vodio.R
import app.vodio.com.vodio.activities.AbstractActivity
import app.vodio.com.vodio.fragments.utils.FragmentCallBack
import com.google.android.material.snackbar.Snackbar
import android.view.Gravity
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import android.view.ViewGroup
import app.vodio.com.vodio.fragments.utils.FragmentIcon


abstract class AbstractFragment : Fragment(), View.OnClickListener, FragmentIcon {
    var parent: FragmentCallBack? = null

    override fun onAttach(act: Activity?) {
        super.onAttach(act)
        parent = act as FragmentCallBack
    }

    fun onItemSelected(itemId: Int) {
        parent?.onItemSelected(itemId)
    }

    override fun onPause() {
        super.onPause()
        Log.v(this.javaClass.canonicalName, "paused")
    }

    override fun onStart() {
        super.onStart()
        Log.v(this.javaClass.canonicalName, "started")
    }

    override fun onResume() {
        super.onResume()
        Log.v(this.javaClass.canonicalName, "resumed")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(this.javaClass.canonicalName, "created")
    }
}