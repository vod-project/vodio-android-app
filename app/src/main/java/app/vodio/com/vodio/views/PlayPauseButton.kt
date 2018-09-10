package app.vodio.com.vodio.views

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageButton

class PlayPauseButton: ImageButton , View.OnClickListener{
    private var onPlay = false
    private var onStop : Runnable? = null
    private var onStart : Runnable? = null
    private var errorMode : Boolean = false

    private var drawableResource = android.R.drawable.ic_dialog_info
    constructor(c : Context) : super(c) {
        setOnClickListener(this)
        setImageResource(drawableResource)
    }
    constructor(c : Context, attrSet : AttributeSet) : super(c, attrSet){
        setOnClickListener(this)
        setImageResource(drawableResource)
    }

    fun setOnStart(onS : Runnable?){onStart = onS}
    fun setOnPause(onP : Runnable?){onStop = onP}

    fun setLoading(b : Boolean){
        if(b){
            drawableResource = android.R.drawable.ic_dialog_info
            errorMode = true
        }else{
            drawableResource = android.R.drawable.ic_media_play
            errorMode = false
        }
        if (Build.VERSION.SDK_INT >= 28) {
            context.mainExecutor.execute { setImageResource(drawableResource) }
        }else{
            Handler(Looper.getMainLooper()).post { setImageResource(drawableResource) }
        }
    }

    override fun onClick(p0: View?) {
        if(this.isEnabled) {
            if(!errorMode) {
                if (onPlay) {
                    if (onStop == null) Log.w(javaClass.simpleName, "onStop is not setted")
                    else {
                        onStop?.run()
                        setImageResource(android.R.drawable.ic_media_play)
                    }
                } else {
                    if (onStart == null) Log.w(javaClass.simpleName, "onStart is not setted")
                    else {
                        onStart?.run()
                        setImageResource(android.R.drawable.ic_media_pause)
                    }
                }
                onPlay = !onPlay
            }else{
                Log.w(javaClass.simpleName, "on error mode")
            }
        }
    }

    fun reinit(){
        setImageResource(drawableResource)
        setOnClickListener(this)
        onPlay = false
        Log.w(this.javaClass.simpleName,"re init button")
    }
}