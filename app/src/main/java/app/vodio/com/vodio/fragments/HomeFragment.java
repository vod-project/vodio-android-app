package app.vodio.com.vodio.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private Button signOut;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        signOut = v.findViewById(R.id.sign_out);
        TextView tv = v.findViewById(R.id.welcomeText);
        //User usr = LoginService.getLoggedIn();
        //tv.setText("You are logged in as "+usr.getLogin());
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        signOut.setOnClickListener(this);
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
