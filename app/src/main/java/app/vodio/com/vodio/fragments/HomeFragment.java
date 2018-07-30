package app.vodio.com.vodio.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import app.vodio.com.vodio.R;
import app.vodio.com.vodio.beans.User;
import app.vodio.com.vodio.services.LoginService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.

 * create an instance of this fragment.
 */
public class HomeFragment extends AbstractFragment{
    private ListView listPosts;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        listPosts = v.findViewById(R.id.listOfPost);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayAdapter<String>  adapter = new ArrayAdapter(getContext(), android.R.layout.simple_expandable_list_item_1, getList());
        listPosts.setAdapter(adapter);
    }
    private List<String> getList(){
        List<String> l = new ArrayList<>();
        for(int i = 0 ; i < 20 ; i++ ){
            l.add("Element "+i);
        }
        return l;
    }

    @Override
    public void onClick(View v) {
        onItemSelected(v.getId());
    }

    public void onItemSelected(int itemId){
        if(parent != null) {
            parent.onItemSelected(itemId);
        }
    }
}
