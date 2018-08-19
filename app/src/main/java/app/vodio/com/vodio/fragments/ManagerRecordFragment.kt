package app.vodio.com.vodio.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.vodio.com.vodio.R
import app.vodio.com.vodio.database.DatabaseResponse
import app.vodio.com.vodio.services.VodService
import app.vodio.com.vodio.utils.MediaPlayerSeekbarService
import app.vodio.com.vodio.utils.MediaPlayerSeekbarService1
import app.vodio.com.vodio.utils.OnCompleteAsyncTask
import kotlinx.android.synthetic.main.manage_vod_after_record.*
import java.io.File
import java.io.FileDescriptor

class ManagerRecordFragment : AbstractFragment(){
    var mediaControlService : MediaPlayerSeekbarService1? = null
    var parentFragment : BottomRecordFragment? = null

    var datasource : File? = null

    override fun onClick(p0: View?) {}

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
        mediaControlService!!.clear()
        Log.w(this.javaClass.simpleName,"paused")
    }
    fun clear(){
        mediaControlService?.clear()
        Log.w(this.javaClass.simpleName,"cleared")
    }

    fun initMediaControlSeekbar(){
        Log.w(this.javaClass.simpleName,"init")
        if(mediaControlService == null) {
            mediaControlService = MediaPlayerSeekbarService1(context!!)
        }
        mediaControlService?.seekbar = mediaSeekbar
        mediaControlService?.setPlayPauseButton(playButton)
        if(datasource != null)
            mediaControlService!!.setSource(datasource!!)

    }
    fun setDataSource(file : File?){
        datasource = file
        Log.w(this.javaClass.simpleName,"setting datasource")
    }

    fun performSendVod(){
        Toast.makeText(context, "not yet implemented", Toast.LENGTH_SHORT).show()
        Log.v(this.javaClass.simpleName,"send vod from : ${datasource!!.path}")
        if(datasource != null) {
            Log.v(javaClass.simpleName,"data source setted")
            VodService.getInstance(context!!)?.createVod(datasource!!, "authorLogin", "title", OnComplete(context!!))
        }else{
            Log.v(javaClass.simpleName,"data source null")
        }
    }


    class OnComplete(var c : Context) : OnCompleteAsyncTask{
        override fun onSuccess(obj: Any) {
            val o = obj as DatabaseResponse
            Toast.makeText(c,"",Toast.LENGTH_SHORT)
        }

        override fun onFail() {
            Toast.makeText(c,"failed",Toast.LENGTH_SHORT)
        }
    }
}