package app.vodio.com.vodio.utils.recording

import android.media.MediaRecorder
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.widget.toast
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class MediaRecorderFactory{
    private val fileMap = HashMap<MediaRecorder, File>();
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
        val outputFile = File.createTempFile("${Random().nextInt()}","record")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mediaRecorder = MediaRecorder()
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS)
            mediaRecorder.setOutputFile(outputFile)
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            fileMap.put(mediaRecorder, outputFile)
            // show media encoder
        }else{
            throw Exception("not yet implemented for this version of Android")
        }
        mediaRecorder?.prepare()
        return mediaRecorder
    }
}