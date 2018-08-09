package app.vodio.com.vodio.utils.recording

import android.media.MediaRecorder
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.widget.toast
import java.io.File
import java.util.*

class MediaRecorderFactory{
    companion object {
        private var INSTANCE : MediaRecorderFactory? = null
        fun getInstance() : MediaRecorderFactory?{
            if(INSTANCE == null) INSTANCE = MediaRecorderFactory()
            return INSTANCE
        }
    }

    fun create() : MediaRecorder?{
        var mediaRecorder : MediaRecorder? = null
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val mediaRecorder = MediaRecorder()
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS)
            // output file
            val outputFile = File.createTempFile("${Random().nextInt()}","record")
            mediaRecorder.setOutputFile(outputFile)
            // show media encoder
        }else{

        }
        return mediaRecorder
    }
}