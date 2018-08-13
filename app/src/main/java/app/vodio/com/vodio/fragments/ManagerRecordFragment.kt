package app.vodio.com.vodio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.vodio.com.vodio.R
import app.vodio.com.vodio.utils.MediaPlayerWithSeekBar
import kotlinx.android.synthetic.main.manage_vod_after_record.*
import java.io.FileDescriptor
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ManagerRecordFragment : AbstractFragment(){
    var mediaControlSeekBar : MediaPlayerWithSeekBar? = null
    var parentFragment : BottomRecordFragment? = null

    var datasource : String? = null

    override fun onClick(p0: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return inflater.inflate(R.layout.manage_vod_after_record, container, false)
    }

    override fun onResume() {
        super.onResume()

        initMediaControlSeekbar()
        retryVod.setOnClickListener { view ->
            parentFragment?.updateRecordUIMode()
        }

        modVodButton.setOnClickListener { view -> Toast.makeText(context, "not yet implemented", Toast.LENGTH_SHORT).show() }
        sendVodButton.setOnClickListener { view -> Toast.makeText(context, "not yet implemented", Toast.LENGTH_SHORT).show() }
    }

    override fun onPause() {
        super.onPause()
        datasource = null
        mediaControlSeekBar!!.clear()
    }
    fun clear(){

    }

    fun initMediaControlSeekbar(){
        if(mediaControlSeekBar == null) {
            mediaControlSeekBar = MediaPlayerWithSeekBar(playButton, mediaSeekbar, context!!)
        }
        if(datasource != null)
            mediaControlSeekBar!!.setDatasource(datasource)
    }
    fun setDataSource(url : String?){
        datasource = url
    }

    fun setDataSource(file : FileDescriptor?){
        mediaControlSeekBar?.setDatasource(file)
    }
}