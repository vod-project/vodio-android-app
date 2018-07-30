package app.vodio.com.vodio.activities;

import android.content.Intent;
import android.os.Bundle;

import app.vodio.com.vodio.R;
import app.vodio.com.vodio.beans.User;
import app.vodio.com.vodio.fragments.LoginFragment;
import app.vodio.com.vodio.fragments.RegisterFragment;
import app.vodio.com.vodio.services.LoginService;

public class LoginActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        User usr = LoginService.getLoggedIn();
        if(usr != null){
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
            finish();
        }else{
            showFragment(new LoginFragment(),getMainLayout());
        }
    }


    @Override
    public void onItemSelected(int itemId) {
        Intent intent = null;
        switch(itemId){
            case R.id.signUpButton:
                showFragment(new RegisterFragment(),getMainLayout());
                break;
            case R.id.signInPerformButton:
                //intent = new Intent(getApplicationContext(),HomeActivity.class);
                // passage du login/idUser
                break;
            case R.id.registerPerformButton:
                //intent = new Intent(getApplicationContext(),HomeActivity.class);
                // passage du login/idUser
                break;
        }
        if(intent != null){
            startActivity(intent);
        }
    }

    public void authenticated(){
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public int getMainLayout() {
        return R.id.mainLoginLayout;
    }

    @Override
    public int getBackgroundId() {
        return R.drawable.background_login_light;
    }
}
