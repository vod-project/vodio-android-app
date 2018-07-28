package app.vodio.com.vodio.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.Stack;

import app.vodio.com.vodio.fragments.utils.FragmentCallBack;

public abstract class AbstractActivity extends AppCompatActivity implements FragmentCallBack {
    protected Stack<Fragment> fragmentsBack = new Stack<>();

    public abstract int getMainLayout();
    public abstract int getBackgroundId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackgroundImage();
    }

    @Override
    public void onBackPressed() {
        synchronized (fragmentsBack) {
            if (fragmentsBack.size() > 1) {
                super.onBackPressed();
                fragmentsBack.pop();
            } else {
                finish();
            }
        }
    }

    protected void showFragment(final Fragment fragment) {
        if (fragment == null) {
            return;
        }
        // Begin a fragment transaction.
        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        // We can also animate the changing of fragment.
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        // Replace current fragment by the new one.
        ft.replace(getMainLayout(), fragment);
        // Null on the back stack to return on the previous fragment when user
        // press on back button.
        ft.addToBackStack(null);

        // Commit changes.
        ft.commit();
        synchronized (fragmentsBack) {
            fragmentsBack.push(fragment);
        }
    }

    protected void setBackgroundImage(){
        getWindow().setBackgroundDrawable(getDrawable(getBackgroundId()));
    }
}
