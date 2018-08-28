package com.example.user.nummachine2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class RunningActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigation;
    private android.support.v4.app.FragmentManager fragmentManager;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        fragmentManager = getSupportFragmentManager();
        bottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigation.inflateMenu(R.menu.running_tab);

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        PadFragment show_frag = new PadFragment();
        transaction.replace(R.id.run_container, show_frag).commit();
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (id){
                    case R.id.calling:
                        fragment = new CallFragment();
                        transaction.replace(R.id.run_container, fragment).commit();
                    break;

                    case R.id.show_pad:
                        fragment = new PadFragment();
                        transaction.replace(R.id.run_container, fragment).commit();
                    break;
                }


                return true;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        return true;
    }

}
