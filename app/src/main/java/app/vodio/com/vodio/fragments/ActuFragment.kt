package app.vodio.com.vodio.fragments

import android.os.Bundle

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


class ActuFragment : AbstractFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_actu, container, false)
        // Inflate the layout for this fragment
        return v
    }

    override fun onResume() {
        super.onResume()
        val list = ArrayList<Vod>()
        val adapter = VodAdapter(context, android.R.layout.simple_expandable_list_item_1, list)
        listOfPost.adapter = adapter
        VodService().getVods(object : OnCompleteAsyncTask {
            override fun onSuccess(obj: Any) {
                // stop load view
                val array = obj as Array<Vod>
                adapter.addAll(array.toList())
            }
            override fun onFail() {
                // stop load view
                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onClick(view: View) {

    }
}
