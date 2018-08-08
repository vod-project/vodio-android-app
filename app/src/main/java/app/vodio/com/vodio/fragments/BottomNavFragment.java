package app.vodio.com.vodio.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tbuonomo.morphbottomnavigation.MorphBottomNavigationView;

import java.util.Collections;

import app.vodio.com.vodio.R;
import app.vodio.com.vodio.activities.HomeActivity;


public class BottomNavFragment extends AbstractFragment {
    private ImageButton recordVodBottom;
    private BottomNavigationView navbar;

    private int[] bottomItemIds = {R.id.bedroom_navbar,R.id.salon_navbar, R.id.profile_navbar,R.id.actu_navbar,R.id.settings_navbar};
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
        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                HomeActivity homeActivity = (HomeActivity) parent;
                int position = getPositionByItemId(menuItem.getItemId());
                homeActivity.setPagePosition(position);
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        onItemSelected(view.getId());
    }

    public void setSelected(int position){
        navbar.setSelectedItemId(getItemIdByPosition(position));
    }

    private int getItemIdByPosition(int position){
        return bottomItemIds[position];
    }
    private int getPositionByItemId(int itemId){
        for(int i = 0 ; i < bottomItemIds.length; i++){
            if(bottomItemIds[i] == itemId){
                return i;
            }
        }
        return 0;
    }
}
