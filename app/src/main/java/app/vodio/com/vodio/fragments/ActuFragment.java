package app.vodio.com.vodio.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.vodio.com.vodio.R;
import app.vodio.com.vodio.beans.Vod;
import app.vodio.com.vodio.database.VodMapper;
import app.vodio.com.vodio.utils.MyAsyncTask;
import app.vodio.com.vodio.utils.OnCompleteAsyncTask;
import app.vodio.com.vodio.views.VodAdapter;


public class ActuFragment extends AbstractFragment {

    private ListView listPosts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_actu, container, false);
        listPosts = v.findViewById(R.id.listOfPost);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        final List<Vod> list = new ArrayList<>();
        final ArrayAdapter<Vod> adapter = new VodAdapter(getContext(), android.R.layout.simple_expandable_list_item_1, list);
        listPosts.setAdapter(adapter);
        MyAsyncTask<List<Vod>> taskActus = new VodMapper.GetListVodTask(new OnCompleteAsyncTask() {
            @Override
            public void onSuccess(Object obj) {
                // stop load view
                List<Vod> list = (List<Vod>)obj;
                adapter.addAll(list);
            }
            @Override
            public void onFail() {
                // stop load view
            }
        });
        // load view
        taskActus.execute();
    }

    @Override
    public void onClick(View view) {

    }

    private List<String> getList(){
        List<String> l = new ArrayList<>();
        for(int i = 0 ; i < 20 ; i++ ){
            l.add("Element "+i);
        }
        return l;
    }
}
