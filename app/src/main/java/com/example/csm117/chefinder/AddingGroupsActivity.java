package com.example.csm117.chefinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddingGroupsActivity extends AppCompatActivity {

    public static final String EXTRA_GROUP_NAME = "com.my.application.CarActivity.GROUP_NAME";
    private Button b;
    private FirebaseUser user;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_groups);

        user = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println(user);
        if (user != null) {
            db = FirebaseDatabase.getInstance();
            //setUpDB();
        }

        b = findViewById(R.id.addNewGroup);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.groupName);
                String s = editText.getText().toString();
                addGroupToDb(s);
                editText.setText("");

                Intent i = new Intent();
                i.putExtra(EXTRA_GROUP_NAME,s);
                setResult(RESULT_OK,i);
                finish();

            }
        });
    }


    private void addGroupToDb(String groupName) {
        if (groupName.isEmpty()) { // can't enter a blank ingredient
            Toast msg = Toast.makeText(this,"Please enter a group name",
                    Toast.LENGTH_LONG);
            TextView txt = (TextView) msg.getView().findViewById(android.R.id.message);
            if (txt != null) {
                txt.setGravity(Gravity.CENTER);
            }
            msg.show();
            return;
        }

        if (user != null) { // user has signed in with fb, allow them to create group
            String userId = user.getUid();
            System.out.println(userId);

            DatabaseReference dbRef = db.getReference("groups");
            //db.getReference(userId);
            //dbRef.child(userId + "/ingredients").push().setValue((Object)ingredient);
            dbRef.child(groupName).child("name").setValue(groupName);
            dbRef.child(groupName).child("users").push().setValue(userId);

            dbRef = db.getReference("users");
            dbRef.child(userId).child("groups").push().setValue(groupName);
        }
        else {
            Toast msg = Toast.makeText(this,"You must be signed in with Facebook to add groups",
                    Toast.LENGTH_LONG);
            TextView txt = (TextView) msg.getView().findViewById(android.R.id.message);
            if (txt != null) {
                txt.setGravity(Gravity.CENTER);
            }
            msg.show();
        }
    }



}
