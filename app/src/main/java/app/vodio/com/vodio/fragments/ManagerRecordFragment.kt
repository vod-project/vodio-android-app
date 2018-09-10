package app.vodio.com.vodio.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.vodio.com.vodio.R
import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.utils.MediaPlayerService
import kotlinx.android.synthetic.main.manage_vod_after_record.*
import kotlinx.android.synthetic.main.media_seekbar_layout.*
import java.io.File

class ManagerRecordFragment : AbstractFragment(){
    var parentFragment : BottomRecordFragment? = null

    override fun onClick(p0: View?) {
        parentFragment!!.onClick(p0)
    }

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return inflater.inflate(R.layout.manage_vod_after_record, container, false)
    }

    override fun onResume() {
        super.onResume()
        initMediaControlSeekbar()
        retryVod.setOnClickListener(this)
        modVodButton.setOnClickListener(this)
        sendVodButton.setOnClickListener(this)
    }

    override fun onPause() {
        super.onPause()
        clear()
    }

    fun clear(){}

    fun setDataSource(file : File?){
        val vod = Vod()
        vod.setFile(file!!)
        vod.setIsLocal(true)

        parentFragment!!.vod = vod
    }

    private fun initMediaControlSeekbar(){
        val vod = parentFragment!!.vod
        timeTv.setTime(vod!!.timeInSecond!!)
        MediaPlayerService.getInstance(context!!).start(vod,playButton,mediaSeekbar,progressTimeTv)


    }

    override fun getIconId(): Int? {
        return null
    }
}