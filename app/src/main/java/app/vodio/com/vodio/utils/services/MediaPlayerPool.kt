package app.vodio.com.vodio.utils.services

import android.annotation.TargetApi
import android.content.Context
import android.media.MediaDataSource
import android.media.MediaMetadata
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.DropBoxManager
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class MediaPlayerPool{
    var c : Context? = null
    private constructor(ctx : Context){
        c = ctx
    }
    companion object {
        private var INSTANCE : MediaPlayerPool? = null
        fun getInstance(c : Context) : MediaPlayerPool{
            if(INSTANCE == null) INSTANCE = MediaPlayerPool(c)
            return INSTANCE!!
        }
    }
    private var random = Random()
    private var mediaPlayerMap = HashMap<Int, MediaPlayer>()
    private var preparedMediaPlayer = HashSet<MediaPlayer>()
    private var onCompletionMap = HashMap<Int, MediaPlayer.OnCompletionListener>()
    private var runningMediaPlayer : MediaPlayer? = null
    fun load(file : File) : Int{
        Log.v(javaClass.simpleName,"path file : ${file.path}")
        val id = random.nextInt()
        val uri = Uri.fromFile(file)
        var mediaPlayer = MediaPlayer.create(c, uri)
        preparedMediaPlayer.add(mediaPlayer)
        mediaPlayerMap.put(id,mediaPlayer)
        return id
    }

    fun play(idSound : Int){
        val mediaPlayer = mediaPlayerMap.get(idSound)
        if(onCompletionMap.containsKey(idSound)) {
            mediaPlayer?.setOnCompletionListener(onCompletionMap.get(idSound))
        }
        if(!preparedMediaPlayer.contains(mediaPlayer)){ // si le media player n'est pas prepare
            mediaPlayer?.prepareAsync()
            mediaPlayer?.setOnPreparedListener {
                mediaPlayer.start()
            }
        }else{
            mediaPlayer?.start()
        }

    }

    fun pause(idSound: Int){
        val mediaPlayer = mediaPlayerMap.get(idSound)
        mediaPlayer?.pause()
        runningMediaPlayer = null
    }

    fun start(idSound: Int){
        runningMediaPlayer?.pause()
        val mediaPlayer = mediaPlayerMap.get(idSound)
        runningMediaPlayer = mediaPlayer
        if(mediaPlayer!!.isPlaying){
            mediaPlayer.start()
        }else{
            play(idSound)
        }
    }

    fun setOnCompletionListener(idSound: Int, onCompletionListener: MediaPlayer.OnCompletionListener){
        val onC = MediaPlayer.OnCompletionListener {
            it.seekTo(0)
            runningMediaPlayer = null
            onCompletionListener.onCompletion(it)
        }
        onCompletionMap.put(idSound, onC)

    }

    fun getDuration(idSound: Int) : Int{
        return mediaPlayerMap.get(idSound)?.duration!!
    }

    fun getProgress(idSound: Int) : Int{
        return mediaPlayerMap.get(idSound)?.currentPosition!!
    }
}