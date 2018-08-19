package app.vodio.com.vodio.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import java.io.File
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

open class MediaPlayerService(var c : Context) {
    protected val mediaExecutor = ThreadPoolExecutor(5,10,60, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())

    var mediaPlayer : MediaPlayer? = null
    var state : State? = State.EMPTY
    private var fileSource  : File? = null
    private var playPauseButton : PlayPauseButton? = null

    fun setSource(file : File){
        reinitialize()
        fileSource = file
        state = State.SOURCED
    }

    fun setPlayPauseButton(playPauseButton: PlayPauseButton){
        this.playPauseButton = playPauseButton
        playPauseButton.setOnStart(Runnable { start() })
        playPauseButton.setOnPause(Runnable { pause() })
    }

    open fun start() : Boolean{
        if(state == State.SOURCED) {
            checkParams()
            mediaPlayer = MediaPlayer.create(c, Uri.fromFile(fileSource))
            mediaPlayer?.setOnCompletionListener { mp: MediaPlayer? -> reinitialize()}
            mediaPlayer?.start()
            state = State.PLAYING
            return true
        }else if(state == State.PAUSED){
            restart()
            return true
        }else{
            Log.w(javaClass.simpleName,"not sourced")
            return false
        }
    }

    private fun restart() : Boolean{
        if(state == State.PAUSED){
            mediaPlayer?.start()
            state = State.PLAYING
            return true
        }else{
            Log.w(javaClass.simpleName,"not paused")
            return false
        }
    }

    fun pause() : Boolean{
        if(state == State.PLAYING){
            mediaPlayer?.pause()
            state = State.PAUSED
            return true
        }else{
            Log.w(javaClass.simpleName,"not playing")
            return false
        }
    }

    fun stop() : Boolean{
        if(state != State.STOPPED){
            mediaPlayer?.stop()
            state = State.STOPPED
            return true
        }else{
            Log.w(javaClass.simpleName,"already stopped")
            return false
        }
    }

    protected fun reinitialize(){
        playPauseButton?.reinit()
        state = State.SOURCED
        mediaPlayer?.seekTo(0)
    }

    fun clear(){
        playPauseButton = null
        mediaPlayer?.release()
        mediaPlayer = null
        state = State.EMPTY
        fileSource = null
    }

    fun checkParams(){
        if(playPauseButton == null ){
            throw Exception("playPause button not initialized")
        }
        if(fileSource == null){
            throw Exception("filesource not initilialized")
        }
    }

    enum class  State{
        EMPTY, SOURCED, PLAYING, PAUSED, STOPPED
    }
}