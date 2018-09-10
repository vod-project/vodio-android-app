package app.vodio.com.vodio.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

class TimeTextView : TextView {
    private var seconds : Int = 0
    private var minutes : Int = 0

    constructor(c : Context) : super(c)
    constructor(c : Context, attrs : AttributeSet) : super(c, attrs)
    constructor(c : Context, attrs : AttributeSet, defStyleAttr : Int) : super(c,attrs,defStyleAttr)

    fun setTime(s : Int){
        minutes = s/60
        seconds = s%60

        var str : String = ""
        if(seconds < 10){str = "${minutes}:0${seconds}"}
        else str =  "${minutes}:${seconds}"
        text = str
    }

}