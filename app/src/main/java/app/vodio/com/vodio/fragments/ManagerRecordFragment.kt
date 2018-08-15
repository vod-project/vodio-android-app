package app.vodio.com.vodio.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.vodio.com.vodio.R
import app.vodio.com.vodio.utils.MediaPlayerWithSeekBar
import kotlinx.android.synthetic.main.manage_vod_after_record.*
import java.io.File
import java.io.FileDescriptor
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ManagerRecordFragment : AbstractFragment(){
    var mediaControlSeekBar : MediaPlayerWithSeekBar? = null
    var parentFragment : BottomRecordFragment? = null

    var datasource : File? = null

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
        sendVodButton.setOnClickListener { view -> performSendVod() }
    }

    override fun onPause() {
        super.onPause()
        datasource = null
        mediaControlSeekBar!!.clear()
        Log.w(this.javaClass.simpleName,"paused")
    }
    fun clear(){
        mediaControlSeekBar?.clear()
        Log.w(this.javaClass.simpleName,"cleared")
    }

    fun initMediaControlSeekbar(){
        Log.w(this.javaClass.simpleName,"init")
        if(mediaControlSeekBar == null) {
            mediaControlSeekBar = MediaPlayerWithSeekBar(playButton, mediaSeekbar, context!!)
        }
        mediaControlSeekBar!!.init(playButton,mediaSeekbar)
        mediaControlSeekBar!!.playPauseButton = playButton
        mediaControlSeekBar!!.seekBar = mediaSeekbar
        if(datasource != null)
            mediaControlSeekBar!!.setDatasource(datasource!!.path)

    }
    fun setDataSource(file : File?){
        datasource = file
        Log.w(this.javaClass.simpleName,"setting datasource")
    }

    fun setDataSource(file : FileDescriptor?){
        mediaControlSeekBar?.setDatasource(file)
    }

    fun performSendVod(){
        Toast.makeText(context, "not yet implemented", Toast.LENGTH_SHORT).show()
        Log.v(this.javaClass.simpleName,"send vod from : ${datasource!!.path}")
    }
}