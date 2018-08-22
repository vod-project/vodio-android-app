package app.vodio.com.vodio.views

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import app.vodio.com.vodio.R
import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.database.retrofit.services.DownloadService
import app.vodio.com.vodio.utils.services.FutureMediaPlayerService
import app.vodio.com.vodio.utils.services.MediaPlayerSeekbarService1
import app.vodio.com.vodio.utils.PlayPauseButton
import app.vodio.com.vodio.utils.TimeTextView
import app.vodio.com.vodio.utils.services.MediaPlayerServiceBis
import java.util.ArrayList

class VodAdapter : ArrayAdapter<Vod>, View.OnClickListener {
    internal var list: List<Vod>? = null
    private var alreadyConfigured = ArrayList<Vod>()

    constructor(context: Context, textViewResourceId: Int) : super(context, textViewResourceId) {}

    constructor(context: Context, resource: Int, items: List<Vod>) : super(context, resource, items) {
        list = items
    }

    override fun getItem(position: Int): Vod? {
        return list?.get(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var v = convertView

        if (v == null) {
            val vi: LayoutInflater
            vi = LayoutInflater.from(context)
            v = vi.inflate(R.layout.vod_item, null)
        }

        val p = getItem(position)
        if(!alreadyConfigured.contains(p)) {
            alreadyConfigured.add(p!!)
            if (p != null) {
                val authorLoginTv = v!!.findViewById<View>(R.id.authorTv) as TextView
                val titleTv = v.findViewById<View>(R.id.titleTv) as TextView
                val timeTv = v.findViewById<View>(R.id.timeTv) as TimeTextView

                val playPauseBtn = v.findViewById<PlayPauseButton>(R.id.playButton)
                val seekbar = v.findViewById<SeekBar>(R.id.mediaSeekbar)

                authorLoginTv.text = p.authorLogin

                titleTv.text = p.title

                timeTv?.setTime(p.timeInSecond!!)

                val filePath = p.audioFilePath
                if (filePath != null) {
                    if (p.file != null) {
                        MediaPlayerServiceBis.getInstance(context)
                                .start(p.file!!, playPauseBtn, seekbar)
                    } else if (p.futureFile != null) {
                        Log.v(javaClass.simpleName, "future file")
                        val file = p.futureFile!!.get()
                        MediaPlayerServiceBis.getInstance(context)
                                .start(file, playPauseBtn, seekbar)
                    } else {
                        Log.v(javaClass.simpleName, "download future file")
                        val file = DownloadService(context).provideAudioData(p)!!.get()
                        MediaPlayerServiceBis.getInstance(context)
                                .start(file, playPauseBtn, seekbar)
                    }
                } else {
                    seekbar.setEnabled(false)
                }
            }
        }
        v!!.setOnClickListener(this)
        return v
    }

    override fun onClick(v: View) {
        Toast.makeText(context, v.toString(), Toast.LENGTH_LONG).show()
    }
}