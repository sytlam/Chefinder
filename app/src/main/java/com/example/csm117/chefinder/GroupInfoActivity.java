package com.example.csm117.chefinder;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class GroupInfoActivity extends AppCompatActivity implements MembersFragment.OnFragmentInteractionListener, IngredientsGroupFragment.OnFragmentInteractionListener {

    private String groupName;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_members:
                    selectedFragment = MembersFragment.newInstance(groupName);
                    break;
                case R.id.navigation_ingredients_group:
                    selectedFragment = IngredientsGroupFragment.newInstance(groupName);
                    break;
                case R.id.navigation_recipes:
                    break;
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
        setContentView(R.layout.activity_group_info);

        groupName = getIntent().getStringExtra("GROUP_NAME");
        System.out.println("group name is " + groupName);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.group_info_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setTitle(R.string.title_members);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, MembersFragment.newInstance(groupName));
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
