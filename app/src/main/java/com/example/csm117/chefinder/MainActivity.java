package com.example.csm117.chefinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class MainActivity extends AppCompatActivity {

    private Button homepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //testMethod();
        homepage = (Button)findViewById(R.id.homepage_button);

        homepage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                gotoHomepage();
            }
        });
    }



    public void testMethod() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("message"); // Key
        ref.setValue("This is a test message"); // Value
    }

    private void gotoHomepage() {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }


}
