package app.vodio.com.vodio.utils

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.SeekBar
import kotlinx.android.synthetic.main.bottom_sheet_record_dialog.*
import androidx.core.os.HandlerCompat.postDelayed
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import androidx.core.os.HandlerCompat.postDelayed
import java.io.File
import java.io.FileDescriptor


class MediaPlayerWithSeekBar{
    private val mediaExecutor = ThreadPoolExecutor(5,10,60, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())
    private var context : Context? = null

    private var mediaPlayer : MediaPlayer? = null
    var sourceSetted = false
    var started = false

    private var playPauseButton : PlayPauseButton? = null
    private var seekBar : SeekBar? = null

    private var progressBarRunnable : Runnable? = null

    constructor(playPauseButton: PlayPauseButton, seekBar: SeekBar, c : Context){
        this.playPauseButton = playPauseButton
        this.seekBar = seekBar
        playPauseButton.setOnPause(Runnable { pause() })
        playPauseButton.setOnStart(Runnable { start() })
        context = c
    }
    fun setDatasource(url : String?){
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, Uri.parse(url))
        sourceSetted = true
    }
    fun setDatasource(file : FileDescriptor?){
        mediaPlayer!!.reset()
        mediaPlayer!!.setDataSource(file)
        sourceSetted = true
    }

    fun start(){
        if(sourceSetted){
            if(!started) {
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
        if(sourceSetted) mediaPlayer?.stop()
        else throw Exception("Data source not setted")
    }

    fun pause(){
        if(sourceSetted) mediaPlayer?.pause()
        else throw Exception("Data source not setted")
    }

    fun clear(){
        sourceSetted = false
        started = false
    }

    fun setupOnCompleteListener(){
        mediaPlayer!!.setOnCompletionListener {
            playPauseButton?.reinit()
            mediaPlayer?.seekTo(0)
        }
    }
    fun setupProgressSeekbar(){

        if(progressBarRunnable != null) mediaExecutor.remove(progressBarRunnable)
        progressBarRunnable = Runnable  {
            while(true) {
                if(mediaPlayer != null) {
                    val position = mediaPlayer!!.currentPosition
                    seekBar?.setProgress(position)
                }
                Thread.sleep(500)
            }
        }
        mediaExecutor.execute(progressBarRunnable)
    }
}