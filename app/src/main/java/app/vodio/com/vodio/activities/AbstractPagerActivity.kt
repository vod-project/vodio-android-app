package app.vodio.com.vodio.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

abstract class AbstractPagerActivity : AbstractActivity() {
    var adapter : PagerAdapter? = null
    var fragments : List<Fragment> = ArrayList()
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

    fun setPagePosition(position: Int) {
        pager?.setCurrentItem(position.toInt())
    }

    override fun onResume() {
        super.onResume()
        pager?.addOnPageChangeListener(getOnPageChangeListener())
    }
}