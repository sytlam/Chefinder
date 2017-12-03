package com.example.csm117.chefinder;

import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button homepage;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private AccessTokenTracker tracker;
    private ArrayList<String> listFriend = new ArrayList<String>(0);

    private int temp = 0;

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
        mAuth = FirebaseAuth.getInstance();

        // Facebook login button
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile", "user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("login success: " + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                gotoHomepage();
            }

            @Override
            public void onCancel() {
                System.out.println("login cancelled");
                updateUI(null);
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("login error" + exception);
                updateUI(null);
            }
        });

        // token tracker to track if user logs out
        tracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    //write your code here what to do when user logout
                    System.out.println("logout");
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();
                }
            }
        };
    }

    public ArrayList<String> getFriendList()    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        //AccessToken.getCurrentAccessToken().getPermissions();
                        JSONObject friends = response.getJSONObject();
                        JSONArray friendList = null;
                        try {
                            friendList = friends.getJSONArray("data");
                        } catch(JSONException e)    {
                            e.printStackTrace();
                        }
                        //final DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("users");
                        //dbRef.child(mAuth.getCurrentUser().getUid()).child("friends").setValue(null);
                        for (int i = 0; i < friendList.length(); i++)   {
                            try {
                                JSONObject amigo = friendList.getJSONObject(i);
                                listFriend.add(amigo.getString("name"));
                                //final String name = amigo.getString("name");
                                //dbRef.child(mAuth.getCurrentUser().getUid()).child("friends").push().setValue(name);

//                                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
//                                            if (item_snapshot.child("name").getValue().equals(name)) {
//                                                dbRef.child(mAuth.getCurrentUser().getUid() + "/friends").
//                                                        push().setValue(item_snapshot.getKey());
//                                                System.out.println(item_snapshot.getKey());
//                                                System.out.println(mAuth.getCurrentUser());
//                                            }
//                                        }
//                                    }
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });


                            } catch(JSONException e)    {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).executeAndWait();
        return listFriend;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.out.println("task is successful");
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");
                            //db.getReference(userId);
                            dbRef.child(user.getUid()).child("name").setValue(user.getDisplayName());
                            FirebaseInstanceId instanceID = FirebaseInstanceId.getInstance();
                            dbRef.child(user.getUid()).child("instanceID").setValue(instanceID.getToken());
                            System.out.println("facebook access user: " + user);
                            updateUI(user);
                            getFriendList();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            System.out.println("login success!");
            if (temp == 0) {
                gotoHomepage();
                temp = 1;
            }
        } else {
            System.out.println("not logged in");
        }
    }

    private void gotoHomepage() {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }


}