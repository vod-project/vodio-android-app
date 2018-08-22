package app.vodio.com.vodio.database.retrofit.services

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.services.VodService
import okhttp3.ResponseBody
import java.io.*
import java.net.URL
import java.util.*
import java.util.concurrent.*
import android.content.Context.DOWNLOAD_SERVICE
import android.content.pm.PackageManager
import android.media.SoundPool
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import kotlin.collections.ArrayList


class DownloadService (var c : Context){
    private val downloadExecutor = ThreadPoolExecutor(5,10,60, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())
    private val list = arrayListOf<String>()

    fun provideAudioData(vod : Vod) : Future<File>?{
        if(!list.contains(vod.audioFilePath)) {
            list.add(vod.audioFilePath)
            var future: Future<File>? = null
            if (vod.audioFilePath != null && vod.audioFilePath.length > 0) {
                if (vod.file == null) {
                    //val f = File.createTempFile("temp",".aac")
                    val f = File(c.externalCacheDir,"Atemp${Random().nextInt()}.aac")
                    if(!f.exists())f.createNewFile()

                    //val f = File.createTempFile("record",".aac")
                    f.setReadable(true,false )
                    future = downloadExecutor.submit(Callable {
                        download(VodService.getVodFilePath(vod.audioFilePath), f)
                        vod.futureFile = future
                        f
                    })
                }
            }
            return future
        }
        return null
    }
    private fun download(url : String, file : File){
        val realUrl = URL(url)
        val inputStream = realUrl.openStream()

        val dis = DataInputStream(inputStream)

        val buffer = ByteArray(1024)
        var length: Int

        val fos = FileOutputStream(file)
        length = dis.read(buffer)
        while (length > 0) {
            fos.write(buffer, 0, length)
            length = dis.read(buffer)
        }

        fos.close()
        inputStream.close()
    }
}