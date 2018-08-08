package app.vodio.com.vodio.activities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import app.vodio.com.vodio.R

abstract class AbstractPagerActivity : AbstractActivity() {
    var adapter : PagerAdapter? = null
    var fragments : List<Fragment>? = null
    var pager : ViewPager? = null
    abstract fun getViewPager() : Int

    abstract fun getPagerAdapter() : PagerAdapter

    abstract fun getFragmentsList() : List<Fragment>

    abstract fun getOnPageChangeListener() : ViewPager.OnPageChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pager = findViewById(getViewPager())
        fragments = getFragmentsList()
        adapter = getPagerAdapter()
        pager?.adapter = adapter
    }

    fun setPagePosition(position: Integer) {
        pager?.setCurrentItem(position.toInt())
    }

    override fun onResume() {
        super.onResume()
        pager?.addOnPageChangeListener(getOnPageChangeListener())
    }
}