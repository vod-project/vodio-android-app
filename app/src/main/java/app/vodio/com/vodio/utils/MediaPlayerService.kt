package app.vodio.com.vodio.utils

import android.animation.ValueAnimator
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import app.vodio.com.vodio.application.MyApplication
import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.services.DownloadService
import app.vodio.com.vodio.services.FileVodProvider
import app.vodio.com.vodio.views.PlayPauseButton
import app.vodio.com.vodio.views.TimeTextView
import java.io.File
import java.net.SocketException
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class MediaPlayerService private constructor(ctx: Context) {
    var c : Context? = ctx
    companion object {
        private var INSTANCE : MediaPlayerService? = null
        fun getInstance(c : Context) : MediaPlayerService {
            if(INSTANCE == null){
                INSTANCE = MediaPlayerService(c)
            }
            return INSTANCE!!
        }
    }

    private var vodToIdSoundMap = HashMap<Int, Int>()
    private var mediaPool : MediaPlayerPool? = null
    private var playPauseButtonMap = HashMap<Int, PlayPauseButton>()
    private var seekbarMap = HashMap<Int, SeekBar>()
    private var progressTimeTvMap =  HashMap<Int, TimeTextView>()
    private var idVods = HashSet<Int>()

    fun clear(){
        playPauseButtonMap.clear()
        seekbarMap.clear()
        progressTimeTvMap.clear()
        idVods.clear()
    }

    fun start(vod : Vod, playPauseButton: PlayPauseButton, seekBar: SeekBar, progressTimeTv : TimeTextView? = null) : Boolean{
        if(!idVods.contains(vod.idVod))
            playPauseButton.setLoading(true)
        if(!vod.isLocal())
            idVods.add(vod.idVod)
        playPauseButton.setLoading(true)
        executeOnMainThread(Runnable {
            //playPauseButton.isEnabled = false
        })
        val onLoaded = OnLoadedFile(vod, playPauseButton, seekBar,progressTimeTv)
        FileVodProvider.getInstance(c!!).provide(vod, onLoaded)
        return true
    }

    private inner class OnLoadedFile(var vod : Vod, var playPauseButton: PlayPauseButton,
                                     var seekBar: SeekBar,
                                     var progressTimeTv : TimeTextView? = null) : FileVodProvider.OnLoadedListener{
        override fun loaded(file : File){
            val idSound : Int
            if(vodToIdSoundMap.containsKey(vod.idVod)){
                idSound = vodToIdSoundMap.get(vod.idVod)!!
            }
            else{
                idSound = mediaPool!!.load(file)
                if(!vod.isLocal())
                    vodToIdSoundMap.put(vod.idVod, idSound)
            }

            playPauseButtonMap.put(idSound, playPauseButton)
            seekbarMap.put(idSound, seekBar)
            if (progressTimeTv != null) progressTimeTvMap.put(idSound, progressTimeTv!!)
            // play pause on start and on pause
            playPauseButton.setOnPause(Runnable { pause(idSound) })
            playPauseButton.setOnStart(Runnable { start(idSound) })
            playPauseButton.setLoading(false)
            executeOnMainThread(Runnable {
                //playPauseButton.isEnabled = true
            })
            vod.setFile(file)
        }

        override fun errorOnLoading(){
            playPauseButton.setLoading(true)
            /*if(errorBtn != null) {
                executeOnMainThread(Runnable {
                    playPauseButton.visibility = View.GONE
                    errorBtn!!.visibility = View.VISIBLE
                })
                errorBtn!!.setOnClickListener {
                    executeOnMainThread(Runnable {
                        errorBtn!!.visibility = View.GONE
                        playPauseButton.visibility = View.VISIBLE
                        start(vod, playPauseButton, seekBar, errorBtn, progressTimeTv)
                    })
                }
            }*/
        }
    }

    private fun pause(idSound : Int){
        mediaPool!!.pause(idSound)
    }

    private fun start(idSound : Int){
        seekbarMap.get(idSound)!!.max = mediaPool?.getDuration(idSound)!!
        val playPauseButton = playPauseButtonMap.get(idSound)!!

        startSeekBarAnim(idSound)
        startTimeProgressAnim(idSound)

        mediaPool!!.setOnCompletionListener(idSound, MediaPlayer.OnCompletionListener {
            playPauseButton.reinit()
        })
        mediaPool!!.play(idSound)
    }

    private fun startSeekBarAnim(idSound: Int){
        val duration = mediaPool?.getDuration(idSound)!!
        val seekBar = seekbarMap.get(idSound)!!
        val seekbarAnim = ValueAnimator.ofInt(0, duration)
        seekbarAnim.repeatCount = seekBar.max / 1000
        seekbarAnim.addUpdateListener {
            seekBar.progress = mediaPool!!.getProgress(idSound)
        }
        seekbarAnim.duration = 1000
        seekbarAnim.start()
    }
    private fun startTimeProgressAnim(idSound: Int){
        val duration = mediaPool?.getDuration(idSound)!!
        val timeProgressAnim = ValueAnimator.ofInt(0, duration)
        timeProgressAnim.repeatCount = seekbarMap.get(idSound)!!.max / 1000
        timeProgressAnim.addUpdateListener {
            progressTimeTvMap.get(idSound)!!.setTime( mediaPool!!.getProgress(idSound) / 1000)
        }
        timeProgressAnim.duration = 1000
        timeProgressAnim.start()
    }

    fun executeOnMainThread(run : Runnable){
        if (Build.VERSION.SDK_INT >= 28) {
            c!!.mainExecutor.execute(run)
        }else{
            Handler(Looper.getMainLooper()).post(run)
        }
    }

    init {
        mediaPool = MediaPlayerPool.getInstance(c!!)
    }
}