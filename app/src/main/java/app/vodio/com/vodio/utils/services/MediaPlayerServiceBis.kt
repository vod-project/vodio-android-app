package app.vodio.com.vodio.utils.services

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.util.Log
import android.widget.ProgressBar
import android.widget.SeekBar
import app.vodio.com.vodio.R
import app.vodio.com.vodio.utils.PlayPauseButton
import java.io.File
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class MediaPlayerServiceBis{
    var c : Context? = null
    private constructor(ctx : Context){
        c = ctx
        init()
    }
    private val mediaUiExecutor = ThreadPoolExecutor(5,10,60, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())
    companion object {
        private var INSTANCE : MediaPlayerServiceBis? = null
        fun getInstance(c : Context) : MediaPlayerServiceBis{
            if(INSTANCE == null){
                INSTANCE = MediaPlayerServiceBis(c)
            }
            return INSTANCE!!
        }
    }
    fun init (){
        mediaPool = MediaPlayerPool.getInstance(c!!)
    }
    var mediaPool : MediaPlayerPool? = null
    var playPauseButtonMap = HashMap<Int, PlayPauseButton>()
    var seekbarMap = HashMap<Int, SeekBar>()
    var progressBarRunnableMap = HashMap<Int, ProgressBarRunnable>()

    fun start(file : File, playPauseButton: PlayPauseButton, seekBar: SeekBar){
        val idSound = mediaPool!!.load(file)
        playPauseButtonMap.put(idSound, playPauseButton)
        seekbarMap.put(idSound, seekBar)
        // play pause on start and on pause
        playPauseButton.setOnPause(Runnable { pause(idSound) })
        playPauseButton.setOnStart(Runnable { start(idSound) })
        mediaPool!!.setOnCompletionListener(idSound, MediaPlayer.OnCompletionListener {
            playPauseButton.reinit()
            seekBar.progress = 0
        })
        seekBar.max = mediaPool?.getDuration(idSound)!!
        val progressBarRunnable = ProgressBarRunnable(idSound,seekBar)
        progressBarRunnableMap.put(idSound,progressBarRunnable)
        mediaUiExecutor.execute(progressBarRunnable)

        // init seekbar anim
        //mediaPool.play(idSound)
    }

    private fun pause(idSound : Int){
        mediaPool!!.pause(idSound)
    }

    private fun start(idSound : Int){
        //soundPool.resume(idSound)
        mediaPool!!.play(idSound)
    }

    inner class ProgressBarRunnable(var idSound : Int, var seekbar: SeekBar) : Runnable{
        var killed = false
        var paused = false
        override fun run() {
            while(!killed) {
                if(!paused) {
                    val position = mediaPool?.getProgress(idSound)!!
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        seekbar?.setProgress(position, true)
                    } else {
                        seekbar?.setProgress(position)
                    }
                }
                Thread.sleep(500)
            }
        }
        fun kill(){
            killed = true
        }
    }
}