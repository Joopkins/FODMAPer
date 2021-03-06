package com.b3sk.fodmaper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.b3sk.fodmaper.MyApplication;
import com.b3sk.fodmaper.R;
import com.b3sk.fodmaper.adapters.SectionsPagerAdapter;
import com.b3sk.fodmaper.fragments.FodmapFragment;
import com.b3sk.fodmaper.fragments.FodmapFriendlyFragment;
import com.b3sk.fodmaper.fragments.ModerateFodmapFragment;
import com.b3sk.fodmaper.helpers.PrefManager;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


import de.cketti.mailto.EmailIntentBuilder;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager viewPager;
    private Tracker tracker;
    private boolean tablet;
    private NavigationView navigationView;


    @Override
    public void onResume() {
        super.onResume();
        tracker.setScreenName("Image~" + "MainActivity");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        //Set the current item in the nav drawer to reflect the current viewpager page
        if (!tablet) {
            navigationView.getMenu().getItem(viewPager.getCurrentItem()).setChecked(true);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.container_one) != null) {
            tablet = true;
        }

        if (!tablet) {
            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            viewPager = (ViewPager) findViewById(R.id.container);
            viewPager.setOffscreenPageLimit(3);
            viewPager.setAdapter(sectionsPagerAdapter);
            viewPager.addOnPageChangeListener(this);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

        } else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.container_one,
                        FodmapFragment.newInstance()).commit();
                getSupportFragmentManager().beginTransaction().add(R.id.container_two,
                        FodmapFriendlyFragment.newInstance()).commit();
                getSupportFragmentManager().beginTransaction().add(R.id.container_three,
                        ModerateFodmapFragment.newInstance()).commit();
            }
        }


        MobileAds.initialize(getApplicationContext(),
                getResources().getString(R.string.banner_ad_app_id));
        MyApplication application = (MyApplication) getApplication();
        tracker = application.getDefaultTracker();

    }

    @Override
    public void onBackPressed() {
        if (tablet) {
            super.onBackPressed();
        } else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_friendly) {
            viewPager.setCurrentItem(3, true);
        } else if (id == R.id.nav_moderate) {
            viewPager.setCurrentItem(2, true);
        } else if (id == R.id.nav_high) {
            viewPager.setCurrentItem(1, true);
        } else if (id == R.id.nav_search) {
            viewPager.setCurrentItem(0, true);
        } else if (id == R.id.nav_share) {
            sendShareIntent();
        } else if (id == R.id.refresher) {
            PrefManager manager = new PrefManager(this);
            manager.setFirstTimeLaunch(true);
            //Delay launching of new activity to prevent jank in nav drawer
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(intent);
            }, 200);
        } else if (id == R.id.feedback) {
            boolean success = EmailIntentBuilder.from(this)
                    .to("fodmaper.app@gmail.com")
                    .subject("FODMAPer Feedback")
                    .body("Suggestions and Feedback")
                    .start();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void sendShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_string));
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_tag)));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        //Set the current item in the nav drawer to reflect the current viewpager page
        if (!tablet) {
            navigationView.getMenu().getItem(position).setChecked(true);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
