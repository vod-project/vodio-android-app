package app.vodio.com.vodio.utils

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.MediaController
import android.widget.SeekBar
import kotlinx.android.synthetic.main.bottom_sheet_record_dialog.*
import androidx.core.os.HandlerCompat.postDelayed
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import androidx.core.os.HandlerCompat.postDelayed
import java.io.File
import java.io.FileDescriptor
import kotlin.system.exitProcess


class MediaPlayerSeekbarService {
    private val mediaExecutor = ThreadPoolExecutor(5,10,60, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())
    private var context : Context? = null

    private var mediaPlayer : MediaPlayer? = null
    var sourceSetted = false
    var started = false

    var playPauseButton : PlayPauseButton? = null
    var seekBar : SeekBar? = null

    private var progressBarRunnable : ProgressBarRunnable? = null

    constructor(playPauseButton: PlayPauseButton, seekBar: SeekBar, c : Context){
        context = c
        init(playPauseButton,seekBar)
    }
    private fun setOnPause(){
        playPauseButton?.setOnPause(Runnable { pause() })

    }
    private fun setOnStart(){
        playPauseButton?.setOnStart(Runnable { start() })
    }
    fun init(playPauseButton: PlayPauseButton, seekBar: SeekBar){
        Log.w(this.javaClass.simpleName,"init")
        this.playPauseButton = playPauseButton
        this.seekBar = seekBar
        setOnPause()
        setOnStart()
    }
    fun setDatasource(url : String?){
        Log.w(this.javaClass.simpleName,"setting datasource")
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(context, Uri.fromFile(File(url)))
        }
        else{
            mediaPlayer?.reset()
            mediaPlayer?.setDataSource(context, Uri.fromFile(File(url)))
            mediaPlayer?.prepare()
        }
        sourceSetted = true
    }
    fun setDatasource(file : FileDescriptor?){
        mediaPlayer!!.reset()
        mediaPlayer!!.setDataSource(file)
        sourceSetted = true
    }

    fun start(){
        Log.w(this.javaClass.simpleName,"try to start")
        if(sourceSetted){
            if(!started) {
                Log.w(this.javaClass.simpleName,"started")
                setupOnCompleteListener()
                seekBar?.max = mediaPlayer!!.duration
                setupProgressSeekbar()
            }
            mediaPlayer?.start()
            started = true
        }
        else throw Exception("Data source not setted")
    }

    fun stop(){
        Log.w(this.javaClass.simpleName,"try to stop")
        if(sourceSetted) mediaPlayer?.stop()
        else throw Exception("Data source not setted")
    }

    fun pause(){
        Log.w(this.javaClass.simpleName,"try to pause")
        if(sourceSetted) mediaPlayer?.pause()
        else throw Exception("Data source not setted")
    }

    fun clear(){
        sourceSetted = false
        started = false
        if(progressBarRunnable != null) progressBarRunnable!!.kill()
        mediaPlayer?.reset()
        seekBar?.setProgress(0)
        Log.v("tag","cleared")
    }

    fun setupOnCompleteListener(){
        Log.w(this.javaClass.simpleName,"setup on media complete seekbar")
        mediaPlayer!!.setOnCompletionListener {
            playPauseButton?.reinit()
            mediaPlayer?.seekTo(0)
        }
    }
    fun setupProgressSeekbar(){
        Log.w(this.javaClass.simpleName,"setup progress bar media")
        progressBarRunnable = ProgressBarRunnable()
        mediaExecutor.execute(progressBarRunnable)
    }

    inner class ProgressBarRunnable : Runnable{
        var killed = false
        override fun run() {
            while(!killed) {
                if(mediaPlayer != null) {
                    val position = mediaPlayer!!.currentPosition
                    seekBar?.setProgress(position)
                }
                Thread.sleep(500)
            }
        }

        fun kill(){
            killed = true
        }
    }
}