package app.vodio.com.vodio.activities

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import app.vodio.com.vodio.fragments.utils.FragmentIcon
import android.view.MotionEvent
import app.vodio.com.vodio.fragments.utils.MyPager


abstract class AbstractPagerActivity : AbstractActivity() {
    var adapter : PagerAdapter? = null
    var fragments : List<Fragment> = ArrayList()
    var pager : MyPager? = null
    abstract fun getViewPager() : Int

    abstract fun getPagerAdapter() : PagerAdapter

    abstract fun getFragmentsList() : List<Fragment>

    abstract fun getOnPageChangeListener() : ViewPager.OnPageChangeListener

    fun setSlideEnabled(b : Boolean){
        pager?.setPagingEnabled(b)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pager = findViewById(getViewPager())
    }

    override fun onStart() {
        super.onStart()
        fragments = getFragmentsList()
        adapter = getPagerAdapter()
        pager?.adapter = adapter
    }

    fun setPagePosition(position: Int) {
        pager?.currentItem = position
    }

    override fun onResume() {
        super.onResume()
        pager?.addOnPageChangeListener(getOnPageChangeListener())
    }
}