package com.example.csm117.chefinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddingGroupsActivity extends AppCompatActivity {

    public static final String EXTRA_GROUP_NAME = "com.my.application.CarActivity.GROUP_NAME";
    private Button b;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_groups);
            b = findViewById(R.id.addNewGroup);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = findViewById(R.id.groupName);
                    String s = editText.getText().toString();
                    addGroupToDb(s);

                       Intent i = new Intent();
                        i.putExtra(EXTRA_GROUP_NAME,s);
                        setResult(RESULT_OK,i);
                        finish();

                }
            });
        }


    private void addGroupToDb(String groupName) {
        
    }



}
