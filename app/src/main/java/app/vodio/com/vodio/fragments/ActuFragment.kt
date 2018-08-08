package app.vodio.com.vodio.fragments

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import app.vodio.com.vodio.R
import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.database.VodMapper
import app.vodio.com.vodio.utils.MyAsyncTask
import app.vodio.com.vodio.utils.OnCompleteAsyncTask
import app.vodio.com.vodio.views.VodAdapter


class ActuFragment : AbstractFragment() {

    private var listPosts: ListView? = null

    private val list: List<String>
        get() {
            val l = ArrayList<String>()
            for (i in 0..19) {
                l.add("Element $i")
            }
            return l
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_actu, container, false)
        listPosts = v.findViewById(R.id.listOfPost)
        // Inflate the layout for this fragment
        return v
    }

    override fun onResume() {
        super.onResume()
        val list = ArrayList<Vod>()
        val adapter = VodAdapter(context, android.R.layout.simple_expandable_list_item_1, list)
        listPosts!!.adapter = adapter
        val taskActus = VodMapper.GetListVodTask(object : OnCompleteAsyncTask {
            override fun onSuccess(obj: Any) {
                // stop load view
                val list = obj as List<Vod>
                adapter.addAll(list)
            }

            override fun onFail() {
                // stop load view
            }
        }, context)
        // load view
        taskActus.execute()
    }

    override fun onClick(view: View) {

    }
}
