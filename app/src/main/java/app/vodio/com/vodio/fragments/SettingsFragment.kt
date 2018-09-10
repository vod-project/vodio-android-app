package app.vodio.com.vodio.fragments

import android.os.Build
import android.os.Bundle
import android.preference.CheckBoxPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

import app.vodio.com.vodio.R
import app.vodio.com.vodio.fragments.utils.FragmentIcon
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : PreferenceFragment(), FragmentIcon {
    private var slidingPreference : CheckBoxPreference? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)//inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference_settings)
        slidingPreference = findPreference("sliding_views") as CheckBoxPreference
        slidingPreference!!.setDefaultValue(PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("sliding_views",false))
        slidingPreference!!.onPreferenceChangeListener = OnPreferenceChange()
    }


    override fun getIconId(): Int? {
        return R.drawable.ic_settings_icon_white
    }

    private inner class OnPreferenceChange : Preference.OnPreferenceChangeListener {
        override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
            if(preference != null && preference == slidingPreference) {
                val value = newValue as Boolean
                val sharedPrefEditor = PreferenceManager.getDefaultSharedPreferences(activity).edit()
                sharedPrefEditor.putBoolean("sliding_views", value)
                sharedPrefEditor.apply()
                Log.v("Settings Fragment", "sliding pref value is $value")
                Toast.makeText(activity, "Restart app to make change", Toast.LENGTH_LONG).show()
                // show to restart app to make change
                return true
            }
            return false
        }
    }
}
