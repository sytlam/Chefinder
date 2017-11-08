package com.example.csm117.chefinder;


import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.TextView;

public class GroupsActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        FloatingActionButton b = findViewById(R.id.AddGroupButton);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createGroup();
            }
        });
        setTitle(R.string.title_groups);


    }

    private void createGroup() {
        TextView t = (TextView) findViewById(R.id.message);
        t.setText(R.string.title_activity_groups);
    }



}
