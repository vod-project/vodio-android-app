package app.vodio.com.vodio.services

import android.content.Context
import android.net.ConnectivityManager
import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.database.retrofit.repositories.VodRepository
import java.io.*
import java.net.URL
import java.util.*
import java.util.concurrent.*


class DownloadService private constructor(val c: Context) {
    companion object {
        private var INSTANCE : DownloadService? = null
        fun getInstance(c: Context) : DownloadService{
            if(INSTANCE == null) INSTANCE = DownloadService(c)
            return INSTANCE!!
        }
    }
    private val downloadExecutor = ThreadPoolExecutor(5,10,60, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())

    fun provideAudioData(vod : Vod) : Future<File>?{
        val connected = (c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo != null

        if(vod.futureFile != null && vod.futureFile!!.isDone){
            return vod.futureFile
        }
        if(connected) {
            var future: Future<File>? = null
            if (vod.audioFilePath != null && vod.audioFilePath!!.length > 0) {
                if (vod.getFile() == null) {
                    //val f = File.createTempFile("temp",".aac")
                    val f = File(c.externalCacheDir, "Atemp${Random().nextInt()}.aac")
                    if (!f.exists()) f.createNewFile()

                    //f.setReadable(true, false)
                    future = downloadExecutor.submit(Callable {
                        download(VodRepository.getVodFilePath(vod.audioFilePath!!), f)
                        vod.futureFile = future
                        f
                    })
                }
            }
            return future

        }else{
            // no network provide file from local
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