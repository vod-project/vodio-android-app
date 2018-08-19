package app.vodio.com.vodio.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton

class PlayPauseButton: ImageButton , View.OnClickListener{
    private var onPlay = false
    private var onStop : Runnable? = null
    private var onStart : Runnable? = null
    constructor(c : Context) : super(c) {
        setOnClickListener(this)
        setImageResource(android.R.drawable.ic_media_play)
    }
    constructor(c : Context, attrSet : AttributeSet) : super(c, attrSet){
        setOnClickListener(this)
        setImageResource(android.R.drawable.ic_media_play)
    }

    fun setOnStart(onS : Runnable){onStart = onS}
    fun setOnPause(onP : Runnable){onStop = onP}

    override fun onClick(p0: View?) {
        if(onPlay){
            if(onStop == null) Log.w("tag","onStop is not setted")
            else{
                onStop?.run()
                setImageResource(android.R.drawable.ic_media_play)
            }
        }else{
            if(onStart == null) Log.w("tag","onStart is not setted")
            else{
                onStart?.run()
                setImageResource(android.R.drawable.ic_media_pause)
            }
        }
        onPlay = !onPlay
    }

    fun reinit(){
        setImageResource(android.R.drawable.ic_media_play)
        setOnClickListener(this)
        onPlay = false
        Log.w(this.javaClass.simpleName,"re init button")
    }
}