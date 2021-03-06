package app.vodio.com.vodio.utils.recording

import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class MediaRecorderFactory{
    private val fileMap = HashMap<MediaRecorder, File>()

    companion object {
        private var INSTANCE : MediaRecorderFactory? = null
        fun getInstance() : MediaRecorderFactory?{
            if(INSTANCE == null) INSTANCE = MediaRecorderFactory()
            return INSTANCE
        }
    }
    fun getOutputFile(mediaRecorder: MediaRecorder?) : File?{
        return fileMap.get(mediaRecorder)
    }

    fun create() : MediaRecorder?{
        var mediaRecorder : MediaRecorder? = null
        val outputFile = File.createTempFile("${kotlin.math.abs(Random().nextInt())}","record.aac")
        releaseAll()

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mediaRecorder.setOutputFile(outputFile)
        }
        else{
            mediaRecorder.setOutputFile(outputFile.path)
        }
        mediaRecorder.prepare()
        fileMap.put(mediaRecorder, outputFile)
        return mediaRecorder
    }

    private fun releaseAll(){
        for(e in fileMap.entries){
            e.key.release()
        }
    }
}