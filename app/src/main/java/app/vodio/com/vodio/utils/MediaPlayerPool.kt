package app.vodio.com.vodio.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import java.io.File
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class MediaPlayerPool private constructor(ctx: Context) {
    var c : Context? = ctx
    private val mediaUiExecutor = ThreadPoolExecutor(5,10,60, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())
    companion object {
        private var INSTANCE : MediaPlayerPool? = null
        fun getInstance(c : Context) : MediaPlayerPool {
            if(INSTANCE == null) INSTANCE = MediaPlayerPool(c)
            return INSTANCE!!
        }
    }
    private var random = Random()
    private var mediaPlayerMap = HashMap<Int, MediaPlayer>()
    private var preparedMediaPlayer = HashSet<MediaPlayer>()
    private var onCompletionMap = HashMap<Int, MediaPlayer.OnCompletionListener>()
    fun load(file : File) : Int{
        Log.v(javaClass.simpleName,"path file : ${file.path}")
        val id = random.nextInt()
        mediaUiExecutor.execute {
            val uri = Uri.fromFile(file)
            val mediaPlayer = MediaPlayer.create(c, uri)
            preparedMediaPlayer.add(mediaPlayer)
            mediaPlayerMap.put(id,mediaPlayer)
        }
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
    }

    fun start(idSound: Int){
        val mediaPlayer = mediaPlayerMap.get(idSound)
        if(mediaPlayer!!.isPlaying){
            mediaPlayer.start()
        }else{
            play(idSound)
        }
    }

    fun setOnCompletionListener(idSound: Int, onCompletionListener: MediaPlayer.OnCompletionListener){
        val onC = MediaPlayer.OnCompletionListener {
            it.seekTo(0)
            onCompletionListener.onCompletion(it)
        }
        onCompletionMap.put(idSound, onC)

    }

    fun getDuration(idSound: Int) : Int{
        return mediaPlayerMap.get(idSound)!!.duration
    }

    fun getProgress(idSound: Int) : Int{
        return mediaPlayerMap.get(idSound)!!.currentPosition
    }

}