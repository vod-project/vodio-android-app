package app.vodio.com.vodio.fragments

import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast

import java.util.ArrayList

import app.vodio.com.vodio.R
import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.services.VodService
import app.vodio.com.vodio.utils.OnCompleteAsyncTask
import app.vodio.com.vodio.views.VodAdapter
import kotlinx.android.synthetic.main.fragment_actu.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


class ActuFragment : AbstractFragment() {
    val vodIdProvided = HashSet<Int>()
    private val provideDataExecutor = ThreadPoolExecutor(5,10,60, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_actu, container, false)
        // Inflate the layout for this fragment
        return v
    }

    override fun onResume() {
        super.onResume()
        val list = ArrayList<Vod>()
        val adapter = VodAdapter(context!!, android.R.layout.simple_expandable_list_item_1, list)
        listOfPost.adapter = adapter
        Log.v(javaClass.simpleName,"retrieve vods ....")

        provideDataExecutor.execute {
            while(true){
                provideVods(adapter)
                Thread.sleep(5000)
            }
        }

    }

    override fun onClick(view: View) {

    }

    fun provideVods(adapter: VodAdapter){
        VodService().getVods(object : OnCompleteAsyncTask {
            override fun onSuccess(obj: Any) {
                Log.v(javaClass.simpleName,"retrieve vods successfully")
                // stop load view
                val array = obj as Array<Vod>
                for(vod in array){
                    if(!vodIdProvided.contains(vod.idVod)) {
                        adapter.add(vod)
                        vodIdProvided.add(vod.idVod)
                    }
                }
                //adapter.addAll(array.toList())
            }
            override fun onFail(t : Throwable) {
                // stop load view
                Log.v(javaClass.simpleName,"retrieve vods failed")
                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
