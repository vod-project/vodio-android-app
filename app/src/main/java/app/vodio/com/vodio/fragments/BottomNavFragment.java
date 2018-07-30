package app.vodio.com.vodio.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tbuonomo.morphbottomnavigation.MorphBottomNavigationView;

import app.vodio.com.vodio.R;
import app.vodio.com.vodio.activities.AbstractActivity;


public class BottomNavFragment extends AbstractFragment {
    private ImageButton recordVodBottom;
    private BottomNavigationView navbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bottom_nav, container, false);
        recordVodBottom=v.findViewById(R.id.recordVodFHome);
        navbar = v.findViewById(R.id.bottomnavbar);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        recordVodBottom.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onItemSelected(view.getId());
    }

    public void onItemSelected(int itemId){
        if(parent != null) {
            parent.onItemSelected(itemId);
        }
    }
}
