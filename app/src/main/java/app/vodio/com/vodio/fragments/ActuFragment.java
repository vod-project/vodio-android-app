package app.vodio.com.vodio.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import app.vodio.com.vodio.R;


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
        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_expandable_list_item_1, getList());
        listPosts.setAdapter(adapter);
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
