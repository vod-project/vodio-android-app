package app.vodio.com.vodio.fragments

import android.app.Activity
import android.content.DialogInterface
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.vodio.com.vodio.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_record_dialog.*
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class BottomRecordFragment : BottomSheetDialogFragment( ) {
    var mediaPlayer : MediaPlayer? = null
    var timerTask : TTask? = null
    val mediaExecutor = ThreadPoolExecutor(5,10,60,TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_record_dialog, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        updateRecordUIMode()
        recordVodBSF.setOnClickListener { view -> updateManageUIMode() }
        retryVod.setOnClickListener { view -> updateRecordUIMode()}
        tagButton.setOnClickListener{view ->  Toast.makeText(context,"not yet implemented", Toast.LENGTH_SHORT).show()}
        modVodButton.setOnClickListener{view ->  Toast.makeText(context,"not yet implemented", Toast.LENGTH_SHORT).show()}
        sendVodButton.setOnClickListener{view ->  Toast.makeText(context,"not yet implemented", Toast.LENGTH_SHORT).show()}
        playButton.setOnPause(StopStream())
        playButton.setOnStart(StartStream())
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        mediaPlayer?.stop()
    }

    fun updateRecordUIMode(){
        recordManagerLayout.visibility = View.GONE
        mediaControlLayout.visibility = View.GONE

        recordVodBSF.visibility = View.VISIBLE
        timeRecordTv.visibility = View.VISIBLE
        cleanUI()
        clearMediaPlayer()

        startRecord()
    }

    fun updateManageUIMode(){
        recordManagerLayout.visibility = View.VISIBLE
        mediaControlLayout.visibility = View.VISIBLE

        recordVodBSF.visibility = View.GONE
        timeRecordTv.visibility = View.GONE
    }

    fun startRecord(){
        if(timerTask == null) timerTask = TTask()
        if(timerTask!!.started) timerTask?.cancel()
        timerTask = TTask()
        Timer().scheduleAtFixedRate(timerTask, 1000, 1000)
    }

    fun setTimeRecord(t : String){
        timeRecordTv.setText(t)
    }

    fun cleanUI(){
        timeRecordTv.setText("0:00")
        playButton.reinit()
    }

    fun clearMediaPlayer(){
        mediaPlayer?.stop()
        mediaPlayer = null
    }

    inner class TTask : TimerTask() {
        var count = 0
        var started = false
        override fun run() {
            if(!started) started = !started
            count++
            if(context != null) {
                val c = context as Activity
                c.runOnUiThread(Runnable { setTimeRecord(secondsToStrMin(count)) })
            }
        }
    }

    inner class StartStream : Runnable{
        override fun run() {
            if(mediaPlayer == null) {
                val url = "https://www.sample-videos.com/audio/mp3/crowd-cheering.mp3"
                mediaPlayer = MediaPlayer().apply {
                    setAudioStreamType(AudioManager.STREAM_MUSIC)
                    setDataSource(url)
                    prepare()
                }
            }
            mediaPlayer?.start()
        }

    }

    inner class StopStream : Runnable{
        override fun run() {
            mediaPlayer?.pause()
        }
    }

    fun secondsToStrMin(sec : Int)  : String{
        var m =sec/60
        var s =sec%60
        if(s < 10){return "${m}:0${s}"}
        return "${m}:${s}"
    }
}