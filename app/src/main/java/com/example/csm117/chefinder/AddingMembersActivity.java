package com.example.csm117.chefinder;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddingMembersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String EXTRA_NEW_MEMBER_LIST = "com.my.application.AddingMembersActivityActivity.NEW_MEMBER_LIST";
    private Button b;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private ListView lv;
    private ArrayAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_members);
        user = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println(user);
        if (user != null) {
            db = FirebaseDatabase.getInstance();
            populateListView();
        }

        lv = (ListView) findViewById(R.id.friendSelectList);
        lv.setOnItemClickListener(this);
        itemsAdapter = new ArrayAdapter<String>(this, R.layout.member_checklist, new ArrayList<String>());
        lv.setAdapter(itemsAdapter);


        b = findViewById(R.id.addMembersToGroup);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EditText editText = findViewById(R.id.groupName);
//                String s = editText.getText().toString();
//                addGroupToDb(s);
//                editText.setText("");
                addMembers();


            }
        });


    }


    private void addMembers() {
        ArrayList<String>  l = new ArrayList<String>();
        for (int i=0;i<itemsAdapter.getCount();i++){

            CheckedTextView cv = (CheckedTextView)itemsAdapter.getView(i,null,lv);
            if (cv.isChecked()) {
                l.add(cv.getText().toString());

            }
        }

        Intent i = new Intent();
        i.putExtra(EXTRA_NEW_MEMBER_LIST,l.toString());
        setResult(RESULT_OK,i);
        finish();
    }


    private void populateListView() {
        DatabaseReference dbRef = db.getReference("users");//db.getReference(user.getUid());
        dbRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemsAdapter.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.child("friends").getChildren()) {
                    System.out.println("data change");
                    String val = eventSnapshot.getValue(String.class);

                    System.out.println(val);
                    itemsAdapter.add(val);
                }

                /*
                for (int i = 0; i < ingredients.size(); i++)
                    System.out.println(ingredients.get(i));
                    */

                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("database error!!" + databaseError);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        CheckedTextView v = (CheckedTextView) view;
        v.setChecked(!v.isChecked());
        v.setCompoundDrawablesWithIntrinsicBounds(v.isChecked() ? android.R.drawable.checkbox_on_background : android.R.drawable.checkbox_off_background,0,0,0);

    }


}
