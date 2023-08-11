package com.teddy.catatharimu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

// Eddy Rochman
// 10120052
// IF-2
public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        ViewPager viewPager = findViewById(R.id.viewPagerTab);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }
}

