package app.vodio.com.vodio.fragments

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.MediaController
import androidx.fragment.app.Fragment

import com.google.android.material.bottomnavigation.BottomNavigationView

import app.vodio.com.vodio.R
import app.vodio.com.vodio.activities.HomeActivity
import kotlinx.android.synthetic.main.fragment_bottom_nav.*
import java.util.HashMap


class BottomNavFragment : AbstractFragment() {
    private val bottomItemIds = intArrayOf(R.id.bedroom_navbar, R.id.salon_navbar, R.id.profile_navbar, R.id.actu_navbar, R.id.search_navbar)
    val mapFragmentMenu = HashMap<Int, Fragment>()
    var fragments : List<Fragment> = listOf(ChambreFragment(), SalonFragment(), ProfileFragment(), ActuFragment(), SearchFragment())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_bottom_nav, container, false)
        // Inflate the layout for this fragment
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapFragmentMenu.put(R.id.bedroom_navbar, ChambreFragment())
        mapFragmentMenu.put(R.id.salon_navbar, SalonFragment())
        mapFragmentMenu.put(R.id.profile_navbar, ProfileFragment())
        mapFragmentMenu.put(R.id.actu_navbar, ActuFragment())
        mapFragmentMenu.put(R.id.search_navbar, SearchFragment())

    }

    override fun onResume() {
        super.onResume()
        recordVodFHome.setOnClickListener(this)
        bottomnavbar.setOnNavigationItemSelectedListener { menuItem ->
            val homeActivity = parent as HomeActivity?
            val position = getPositionByItemId(menuItem.itemId)
            homeActivity!!.setPagePosition(position as Int)
            false
        }
    }

    override fun onClick(view: View) {
        onItemSelected(view.id)
    }

    fun setSelected(position: Int) {
        bottomnavbar.selectedItemId = getItemIdByPosition(position)
    }

    private fun getItemIdByPosition(position: Int): Int {
        return bottomItemIds[position]
    }

    private fun getPositionByItemId(itemId: Int): Int {
        for (i in bottomItemIds.indices) {
            if (bottomItemIds[i] == itemId) {
                return i
            }
        }
        return 0
    }
}
