package app.vodio.com.vodio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import app.vodio.com.vodio.R;
import app.vodio.com.vodio.fragments.HomeFragment;
import app.vodio.com.vodio.services.LoginService;

public class HomeActivity extends AbstractActivity {
    private Button signOutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        signOutButton = findViewById(R.id.sign_out);
        showFragment(new HomeFragment());
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
