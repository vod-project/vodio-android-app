package app.vodio.com.vodio.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import app.vodio.com.vodio.R
import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.utils.MediaPlayerSeekbarService1
import app.vodio.com.vodio.utils.PlayPauseButton
import app.vodio.com.vodio.utils.TimeTextView

class VodAdapter : ArrayAdapter<Vod>, View.OnClickListener {
    internal var list: List<Vod>? = null

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

        if (p != null) {
            val authorLoginTv = v!!.findViewById<View>(R.id.authorTv) as TextView
            val titleTv = v.findViewById<View>(R.id.titleTv) as TextView
            val timeTv = v.findViewById<View>(R.id.timeTv) as TimeTextView

            val playPauseBtn = v.findViewById<PlayPauseButton>(R.id.playButton)
            val seekbar = v.findViewById<SeekBar>(R.id.mediaSeekbar)

            authorLoginTv.text = p.authorLogin

            titleTv.text = p.title

            timeTv?.setTime(p.timeInSecond!!)

            val file = p.getAudioFile()
            if (file != null) {
                val mediaPlayerService = MediaPlayerSeekbarService1(context)
                mediaPlayerService.seekbar = seekbar
                mediaPlayerService.setPlayPauseButton(playPauseBtn)
                mediaPlayerService.setSource(p.getAudioFile()!!)
            }else{
                seekbar.setEnabled(false)
            }
        }

        v!!.setOnClickListener(this)
        return v
    }

    override fun onClick(v: View) {
        Toast.makeText(context, v.toString(), Toast.LENGTH_LONG).show()
    }
}