package app.vodio.com.vodio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import app.vodio.com.vodio.R;
import app.vodio.com.vodio.fragments.ActuFragment;
import app.vodio.com.vodio.fragments.BottomNavFragment;
import app.vodio.com.vodio.fragments.ChambreFragment;
import app.vodio.com.vodio.fragments.ProfileFragment;
import app.vodio.com.vodio.fragments.SalonFragment;
import app.vodio.com.vodio.fragments.SettingsFragment;
import app.vodio.com.vodio.services.LoginService;

public class HomeActivity extends AbstractActivity {
    private ViewPager viewPager;
    private FragmentStatePagerAdapter pagerAdapter;

    private Fragment[] fragments = {new ChambreFragment(),new SalonFragment(), new ProfileFragment(), new ActuFragment(), new SettingsFragment()};

    // fragment
    private BottomNavFragment bottomFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomFragment = new BottomNavFragment();

        // Instantiate a ViewPager and a PagerAdapter.
        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);


        showFragment(bottomFragment, R.id.layoutBottomBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                bottomFragment.setSelected(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public void onItemSelected(int itemId) {
        if(itemId == R.id.sign_out){
            performSignOut();
        }else if(itemId == R.id.recordVodFHome){
            Toast.makeText(getApplicationContext(),"recording",Toast.LENGTH_LONG).show();
        }
    }

    public void setPagePosition(int position){
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {}

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

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }

}
