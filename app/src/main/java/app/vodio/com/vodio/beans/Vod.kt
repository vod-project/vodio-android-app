package app.vodio.com.vodio.beans

import android.media.MediaMetadataRetriever
import java.io.File
import java.util.*
import java.util.concurrent.Future

data class Vod(var idVod : Int = -1, var timeInSecond: Int? = 0, var authorLogin: String = "",
                    var title: String = "", var tags: List<String> = ArrayList<String>(), var audioFilePath: String? = "",
                        var futureFile : Future<File>? = null, var timestamp: Long = Calendar.getInstance().timeInMillis
                            , var likedByFollower : Int = 0)
    {
        private var file : File? = null
        private var isLocal = false
        fun isLikedByFollower() : Boolean{
            return likedByFollower == 1
        }

        fun setFile(f : File){
            file = f
            if(timeInSecond == 0) {
                val mmr = MediaMetadataRetriever()
                mmr.setDataSource(file!!.path)
                val durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                timeInSecond = Integer.parseInt(durationStr) / 1000
            }
        }

        fun getFile() : File? {
            if(futureFile != null && futureFile!!.isDone){
                file = futureFile!!.get()
                futureFile = null
            }
            return file
        }

        fun initTimestamp(){
            timestamp = Calendar.getInstance().timeInMillis
        }

        fun setIsLocal(b : Boolean){
            isLocal = b
        }


        fun isLocal() : Boolean{
            return isLocal
        }

}
