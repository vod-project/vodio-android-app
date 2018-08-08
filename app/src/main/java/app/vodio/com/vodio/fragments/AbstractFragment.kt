package app.vodio.com.vodio.fragments;

import android.app.Activity;
import android.content.Context
import androidx.fragment.app.Fragment;
import android.view.View;

import app.vodio.com.vodio.fragments.utils.FragmentCallBack;

abstract class AbstractFragment : Fragment(), View.OnClickListener {
    protected var parent: FragmentCallBack? = null

    override fun onAttach(act: Activity?) {
        super.onAttach(act)
        parent = act as FragmentCallBack
    }

    fun onItemSelected(itemId: Int) {
        parent?.onItemSelected(itemId)
    }
}