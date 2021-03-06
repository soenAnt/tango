/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tango;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.tango.fragment.Favorites;
import com.tango.fragment.MyPosts;
import com.tango.fragment.MyTopPosts;
import com.tango.fragment.RecentPosts;

public class FeedActivity extends BaseActivity {

    private static final String TAG = "FeedActivity";

    private FragmentPagerAdapter page_Adapter;
    private ViewPager view_Page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.NightTheme);

        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        if(Build.VERSION.SDK_INT>=21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                window.setStatusBarColor(this.getResources().getColor(R.color.black));
            } else {
                window.setStatusBarColor(this.getResources().getColor(R.color.blue));
            }

        }
        // Create the adapter that will return a fragment for each section
        page_Adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[]{
                    new RecentPosts(),
                    new MyTopPosts(),
                    new MyPosts(),
                    new Favorites()
                    //new Favorites()
            };
            private final String[] mFragmentNames = new String[]{
                    getString(R.string.heading_recent),
                    getString(R.string.heading_my_top_posts),
                    getString(R.string.heading_my_posts),
                    getString(R.string.heading_favorites)
            };

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        // Set up the ViewPager with the sections adapter.
        view_Page = findViewById(R.id.container);
        view_Page.setAdapter(page_Adapter);
        TabLayout tab_Layout = findViewById(R.id.tabs);
        tab_Layout.setupWithViewPager(view_Page);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            tab_Layout.setTabTextColors(getResources().getColorStateList(R.color.grey_100));
        } else {
            tab_Layout.setTabTextColors(getResources().getColorStateList(R.color.black));
        }

        // Button launches QuestionPageActivity
        findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedActivity.this, QuestionPageActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            menu.findItem(R.id.switch_dark).setIcon(R.drawable.ic_dark_mode);

        } else {
            menu.findItem(R.id.switch_dark).setIcon(R.drawable.ic_light_mode);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();
        if (itemClicked == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, GoogleSignInActivity.class));
            finish();
            return true;
        } else if (itemClicked == R.id.action_profile) {
            startActivity(new Intent(this, ProfilePage.class));
            finish();
            return true;
        }
        else if (itemClicked == R.id.switch_dark) {

            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                setTheme(R.style.AppTheme);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                item.setIcon(R.drawable.ic_dark_mode);
                restartApp();
                return true;

            } else {
                setTheme(R.style.NightTheme);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                item.setIcon(R.drawable.ic_light_mode);
                restartApp();
                return true;
            }
            

        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
    public void restartApp(){
        Intent nextActivity = new Intent(getApplicationContext(),FeedActivity.class);
        startActivity(nextActivity);
        finish();
    }

}
