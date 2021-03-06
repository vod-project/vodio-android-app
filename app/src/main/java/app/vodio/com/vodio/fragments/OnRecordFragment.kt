package app.vodio.com.vodio.fragments

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.vodio.com.vodio.R
import app.vodio.com.vodio.utils.recording.MediaRecorderFactory
import kotlinx.android.synthetic.main.record_vod_layout.*
import java.io.File
import java.util.*


class OnRecordFragment : AbstractFragment(){
    private var timerTask : TTask? = null
    private var timer : Timer? = null
    var parentFragment : BottomRecordFragment? = null
    private val MY_PERMISSIONS_RECORD_AUDIO = 10

    private var mediaRecorder : MediaRecorder? = null

    fun getFile() : File {
        return MediaRecorderFactory.getInstance()?.getOutputFile(mediaRecorder)!!
    }

    override fun onClick(p0: View?) {
        parentFragment!!.onClick(p0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.record_vod_layout, container, false)
    }

    override fun onResume() {
        super.onResume()
        startRecord()
        recordVodBSF.setOnClickListener(this)
    }

    private fun startRecord(){
        Log.w(this.javaClass.simpleName,"try to start record")
        if(timerTask == null) timerTask = TTask()
        if(timerTask!!.started) timerTask?.cancel()
        timerTask = TTask()
        timer = Timer()

        requestAudioPermissions()
    }

    private fun continueRecordAfterPermissionGranted(){
        mediaRecorder?.release() // if there is already a recorder, release it before recording
        mediaRecorder = MediaRecorderFactory.getInstance()?.create()
        mediaRecorder?.start()
        timer?.scheduleAtFixedRate(timerTask, 1000, 1000)
        Log.w(this.javaClass.simpleName,"record started")
    }

    private fun cancelRecordAfterPermissionNotGranted(){
        Log.w(this.javaClass.simpleName,"permission refused")
        stopTimer()
    }

    fun stopRecord(){
        stopTimer()
        mediaRecorder?.stop()
        Log.w(this.javaClass.simpleName,"record stopped")
    }

    private fun stopTimer(){
        timerTask?.cancel()
        timer?.cancel()
        Log.w(this.javaClass.simpleName,"timer stopped")
    }

    fun clear(){
        stopTimer()
        mediaRecorder?.reset()
        Log.w(this.javaClass.simpleName,"cleared")
    }

    private fun requestAudioPermissions(){
        Log.w(this.javaClass.simpleName,"request permission")
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity!!,
                        Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                            Manifest.permission.RECORD_AUDIO)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity!!,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        MY_PERMISSIONS_RECORD_AUDIO)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            continueRecordAfterPermissionGranted()
            Log.w(this.javaClass.simpleName,"permission granted")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_RECORD_AUDIO -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.w(this.javaClass.simpleName,"permission granted")
                    continueRecordAfterPermissionGranted()
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    cancelRecordAfterPermissionNotGranted()
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private inner class TTask : TimerTask() {
        var count = 0
        var started = false
        override fun run() {
            if(!started) started = !started
            count++
            if(context != null) {
                val c = context as Activity
                c.runOnUiThread { timeRecordTv.setTime(count) }
            }
        }
    }

    override fun getIconId(): Int? {
        return null
    }
}