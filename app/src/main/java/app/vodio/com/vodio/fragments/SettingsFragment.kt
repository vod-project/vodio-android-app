package app.vodio.com.vodio.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle

import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

import app.vodio.com.vodio.R
import app.vodio.com.vodio.activities.AbstractActivity
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : AbstractFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var v : View = inflater.inflate(R.layout.fragment_settings, container, false)
        // Inflate the layout for this fragment
        return v
    }

    override fun onResume() {
        super.onResume()
        signOut.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val itemId : Int = view.id
        super.onItemSelected(itemId)
    }
}
