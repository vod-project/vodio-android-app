package app.vodio.com.vodio.fragments

import android.os.Bundle
import android.util.SparseArray

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import app.vodio.com.vodio.R
import app.vodio.com.vodio.activities.HomePagerActivity
import app.vodio.com.vodio.fragments.utils.FragmentIcon
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_bottom_nav.*


class BottomNavFragment : AbstractFragment() {
    private val mapFragmentMenu = SparseArray<FragmentIcon>()
    var fragments : List<Fragment> = listOf(ChambreFragment(), SalonFragment(), ProfileFragment(), ActuFragment(), SearchFragment())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_nav, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        for(fragment in fragments){
            val fragmentIcon = fragment as FragmentIcon
            val id = if(fragmentIcon.getIconId() != null) fragmentIcon.getIconId() else R.drawable.ic_settings_icon_white
            mapFragmentMenu.put(id!!, fragment)
        }
    }

    override fun onResume() {
        super.onResume()
        recordVodFHome.setOnClickListener(this)
        bottomnavbar.setOnNavigationItemSelectedListener { menuItem ->
            val homeActivity = parent as BottomNavigationView.OnNavigationItemSelectedListener?
            homeActivity!!.onNavigationItemSelected(menuItem)

            //val position = getPositionByItemId(menuItem.itemId)
            //setSelected(position)
            //homeActivity!!.setPagePosition(position)
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
        return (fragments[position] as FragmentIcon).getIconId()!!
    }

    fun getPositionByItemId(itemId: Int): Int {
        for (i in fragments.indices) {
            if ((fragments.get(i)!! as FragmentIcon).getIconId()  == itemId) {
                return i
            }
        }
        return -1
    }

    override fun getIconId(): Int? {
        return null
    }
}
