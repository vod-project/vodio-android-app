package app.vodio.com.vodio.services

import android.content.Context
import app.vodio.com.vodio.application.MyApplication
import app.vodio.com.vodio.beans.Vod
import java.io.File
import java.net.SocketException
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class FileVodProvider private constructor(private val c: Context) {
    companion object {
        private var INSTANCE : FileVodProvider? = null
        fun getInstance(c : Context) : FileVodProvider{
            if(INSTANCE == null) INSTANCE = FileVodProvider(c)
            return INSTANCE!!
        }
    }
    private val executor = ThreadPoolExecutor(5,10,60, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())

    fun provide(vod : Vod, listener : OnLoadedListener ){
        val innerListener = InnerLoadedListener(listener, vod.idVod)
        if(vod.getFile() != null){
            executor.execute {
                val file = vod.getFile()!!
                if(file == null){
                    innerListener.errorOnLoading()
                }else {
                    innerListener.loaded(file)
                }
            }
        }else if(vod.futureFile != null){ // get future file
            executor.execute {
                val file = vod.futureFile!!.get()
                if(file == null){
                    innerListener.errorOnLoading()
                }else {
                    innerListener.loaded(file)
                }
            }
        }else if(MyApplication.isConnected()){ // download file first
            executor.execute{
                val future = DownloadService.getInstance(c).provideAudioData(vod)
                if(future == null){
                    innerListener.errorOnLoading()
                }
                else {
                    var file : File? = null
                    try {
                        file = future.get()
                    }catch(e : SocketException){
                        file = null
                    }
                    if (file == null) {
                        innerListener.errorOnLoading()
                    } else {
                        innerListener.loaded(file)
                    }
                }
            }
        }else{//provide from local
            val file = VodFilePathLocalDb(c).provideFile(vod.idVod)
            if(file != null && file.exists())
                innerListener.loaded(file)
            else
                innerListener.errorOnLoading()
        }
    }

    interface OnLoadedListener{
        fun errorOnLoading()

        fun loaded(file : File)
    }

    private inner class InnerLoadedListener(private var onLoadedListener : OnLoadedListener, private var idVod : Int) : OnLoadedListener {
        override fun errorOnLoading() {
            onLoadedListener.errorOnLoading()
        }

        override fun loaded(file: File) {
            VodFilePathLocalDb(c).storeVod(idVod, file)
            onLoadedListener.loaded(file)
        }
    }
}