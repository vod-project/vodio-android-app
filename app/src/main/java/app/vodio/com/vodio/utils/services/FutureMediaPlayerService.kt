package app.vodio.com.vodio.utils.services

import android.content.Context
import java.io.*
import java.util.concurrent.*


class FutureMediaPlayerService(c: Context) : MediaPlayerSeekbarService1(c){
    private var futureFileSource : Future<File>? = null

    fun setSource(file: Future<File>) {
        reinitialize()
        futureFileSource = file
        state = State.SOURCED
    }

    override fun start(): Boolean {
        fileSource = futureFileSource?.get()
        return super.start()
    }


}