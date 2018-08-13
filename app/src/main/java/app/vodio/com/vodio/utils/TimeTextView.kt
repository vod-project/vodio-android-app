package app.vodio.com.vodio.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import java.sql.Time
import java.time.Instant
import java.util.*

class TimeTextView : TextView {
    private var seconds : Int = 0
    private var minutes : Int = 0
    private var hours   : Int = 0

    constructor(c : Context) : super(c){}
    constructor(c : Context, attrs : AttributeSet) : super(c, attrs) {}
    constructor(c : Context, attrs : AttributeSet, defStyleAttr : Int) : super(c,attrs,defStyleAttr){}
    constructor(c : Context, attrs : AttributeSet, defStyleAttr : Int, defStyleRes : Int) : super(c,attrs,defStyleAttr,defStyleRes){}

    fun setTime(s : Int){
        minutes = s/60
        seconds = s%60

        var str : String = ""
        if(seconds < 10){str = "${minutes}:0${seconds}"}
        else str =  "${minutes}:${seconds}"
        setText(str)
    }

}