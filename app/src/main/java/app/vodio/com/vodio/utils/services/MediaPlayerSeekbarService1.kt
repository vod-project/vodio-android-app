package app.vodio.com.vodio.utils.services

import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.widget.SeekBar

open class MediaPlayerSeekbarService1(c: Context) : MediaPlayerService(c){
    var seekbar : SeekBar? = null

    override fun start(): Boolean {
        val res = super.start()
        if(res){
            seekbar?.max = mediaPlayer!!.duration
            //val animator = ValueAnimator.ofInt(0, seekbar?.max!!)
            //val animator = ObjectAnimator.ofInt(seekbar,"progress", seekbar!!.max)
            //animator.setDuration(500)
            //animator.addUpdateListener { Anim() }
            //animator.start()
            mediaExecutor.execute(ProgressBarRunnable())
        }
        return res
    }

    inner class ProgressBarRunnable : Runnable{
        var killed = false
        override fun run() {
            while(!killed) {
                if(mediaPlayer != null) {
                    val position = mediaPlayer!!.currentPosition
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        seekbar?.setProgress(position,true)
                    }else{
                        seekbar?.setProgress(position)
                    }
                }
                Thread.sleep(500)
            }
        }
        fun kill(){
            killed = true
        }
    }

    inner class Anim : ValueAnimator.AnimatorUpdateListener {
        override fun onAnimationUpdate(animation: ValueAnimator?) {
            val position = mediaPlayer!!.currentPosition
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                seekbar?.setProgress(position,true)
            }else{
                seekbar?.setProgress(position)
            }
        }
    }
}