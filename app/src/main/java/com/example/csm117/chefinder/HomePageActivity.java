package com.example.csm117.chefinder;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class HomePageActivity extends AppCompatActivity implements GroupsFragment.OnFragmentInteractionListener,
        IngredientsFragment.OnFragmentInteractionListener, NotificationsFragment.OnFragmentInteractionListener {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_groups:
                    setTitle(R.string.title_groups);
                    // temp to avoid crashes
                    selectedFragment = GroupsFragment.newInstance();
                    break;
                case R.id.navigation_ingredients:
                    setTitle(R.string.title_ingredient_list);
                    selectedFragment = IngredientsFragment.newInstance();
                    break;
                case R.id.navigation_notifications:
                    setTitle(R.string.title_notifications);
                    // temp to avoid crashes
                    selectedFragment = NotificationsFragment.newInstance();
                    break;
                case R.id.navigation_invitation:
                    setTitle(R.string.title_invite);
                    selectedFragment = IngredientsFragment.newInstance();
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setTitle(R.string.title_groups);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, GroupsFragment.newInstance());
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}