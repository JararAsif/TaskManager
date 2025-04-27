package com.example.taskmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load theme from SharedPreferences before setting content view
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.view_pager);

        // Setup ViewPager
        setupViewPager(viewPager);

        // Sync BottomNavigationView with ViewPager
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_schedule) {
                viewPager.setCurrentItem(0);
                return true;
            } else if (itemId == R.id.nav_past) {
                viewPager.setCurrentItem(1);
                return true;
            } else if (itemId == R.id.nav_notifications) {
                viewPager.setCurrentItem(2);
                return true;
            } else if (itemId == R.id.nav_profile) {
                viewPager.setCurrentItem(3);
                return true;
            }
            return false;
        });

        // Sync ViewPager with BottomNavigationView
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ScheduleFragment(), "Schedule");
        adapter.addFragment(new PastFragment(), "Past");
        adapter.addFragment(new NotificationFragment(), "Notifications");
        adapter.addFragment(new ProfileFragment(), "Profile");
        viewPager.setAdapter(adapter);
    }
}

// ViewPagerAdapter class to manage fragments
class ViewPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {
    private final List<androidx.fragment.app.Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(androidx.fragment.app.FragmentManager manager) {
        super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public void addFragment(androidx.fragment.app.Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @Override
    public androidx.fragment.app.Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }
}