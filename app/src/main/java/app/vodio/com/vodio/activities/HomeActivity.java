package app.vodio.com.vodio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tbuonomo.morphbottomnavigation.MorphBottomNavigationView;

import androidx.fragment.app.Fragment;
import app.vodio.com.vodio.R;
import app.vodio.com.vodio.fragments.BottomNavFragment;
import app.vodio.com.vodio.fragments.HomeFragment;
import app.vodio.com.vodio.services.LoginService;

public class HomeActivity extends AbstractActivity {
    private Button signOutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        signOutButton = findViewById(R.id.sign_out);
        showFragment(new BottomNavFragment(), R.id.layoutBottomBar);
        showFragment(new HomeFragment(),R.id.mainHomeLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSignOut();
            }
        });
    }

    @Override
    public void onItemSelected(int itemId) {
        if(itemId == R.id.sign_out){
            performSignOut();
        }if(itemId == R.id.recordVodFHome){
            Toast.makeText(getApplicationContext(),"recording",Toast.LENGTH_LONG).show();
        }
    }

    private void performSignOut(){
        LoginService.setLoggedIn(null);
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public int getMainLayout() {
        return R.id.mainHomeLayout;
    }

    @Override
    public int getBackgroundId() {
        return R.drawable.background_home;
    }
}
