package com.example.csm117.chefinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.view.View.GONE;

public class HomePageActivity extends AppCompatActivity {



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_groups:
                    setTitle(R.string.title_groups);
                    //button.setVisibility(View.VISIBLE);
                    displayGroups();
                    return true;
                case R.id.navigation_ingredients:
                    setTitle(R.string.title_ingredient_list);
                    //button.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_notifications:
                    //button.setVisibility(View.GONE);
                    setTitle(R.string.title_notifications);

                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setTitle(R.string.title_groups);
    }

    private void displayGroups() {

    }

    private void goToGroups(){
        Intent intent = new Intent(this,GroupsActivity.class);
        startActivity(intent);
    }

}
