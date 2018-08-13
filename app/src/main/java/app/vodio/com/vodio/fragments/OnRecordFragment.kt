package app.vodio.com.vodio.fragments

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.vodio.com.vodio.R
import app.vodio.com.vodio.utils.MediaPlayerWithSeekBar
import app.vodio.com.vodio.utils.recording.MediaRecorderFactory
import kotlinx.android.synthetic.main.record_vod_layout.*
import java.security.Permission
import java.util.*
import android.widget.Toast



class OnRecordFragment(): AbstractFragment(){
    var timerTask : TTask? = null
    var timer : Timer? = null
    var parentFragment : BottomRecordFragment? = null
    val MY_PERMISSIONS_RECORD_AUDIO = 10

    var mediaRecorder : MediaRecorder? = null

    override fun onClick(p0: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.record_vod_layout, container, false)
    }

    override fun onResume() {
        super.onResume()
        startRecord()
        recordVodBSF.setOnClickListener { view ->
            run {
                stopRecord()
                val file = MediaRecorderFactory.getInstance()?.getOutputFile(mediaRecorder)
                parentFragment?.setDataSource(file!!.path)
                parentFragment?.updateManageUIMode()
            }
        }
    }

    fun startRecord(){
        if(timerTask == null) timerTask = TTask()
        if(timerTask!!.started) timerTask?.cancel()
        timerTask = TTask()
        timer = Timer()

        requestAudioPermissions()
    }

    fun continueRecordAfterPermissionGranted(){
        mediaRecorder = MediaRecorderFactory.getInstance()?.create()
        mediaRecorder?.start()
        timer?.scheduleAtFixedRate(timerTask, 1000, 1000)
    }

    fun cancelRecordAfterPermissionNotGranted(){
        timerTask = null
        timer = null
        Toast.makeText(context,"cancelling...",Toast.LENGTH_SHORT).show()
    }

    fun stopRecord(){
        timerTask?.cancel()
        timer?.cancel()
        mediaRecorder?.stop()
    }

    fun clear(){
        stopRecord()
    }

    fun requestAudioPermissions(){
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
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_RECORD_AUDIO -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
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

    inner class TTask : TimerTask() {
        var count = 0
        var started = false
        override fun run() {
            if(!started) started = !started
            count++
            if(context != null) {
                val c = context as Activity
                c.runOnUiThread(Runnable { timeRecordTv.setTime(count) })
            }
        }
    }
}