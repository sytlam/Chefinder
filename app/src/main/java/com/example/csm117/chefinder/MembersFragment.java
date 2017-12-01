package com.example.csm117.chefinder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceIdService;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MembersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MembersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MembersFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String GROUP_NAME = "default group name";
    private static final String EXTRA_NEW_MEMBER_LIST = "com.my.application.AddingMembersActivityActivity.NEW_MEMBER_LIST";
    private String groupName;

    private OnFragmentInteractionListener mListener;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private ListView list;
    private ArrayAdapter<String> itemsAdapter;
    private static final int REQUEST_CODE_ADD_MEMBERS = 1;

    public MembersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * //@param param1 Parameter 1.
     * //@param param2 Parameter 2.
     * @return A new instance of fragment MembersFragment.
     */
    public static MembersFragment newInstance(String name) {
        MembersFragment fragment = new MembersFragment();
        Bundle args = new Bundle();
        args.putString(GROUP_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupName = getArguments().getString(GROUP_NAME);
            System.out.println("members fragment group name is " + groupName);
        }
        getActivity().setTitle(R.string.title_members);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_members, container, false);

        FloatingActionButton b = v.findViewById(R.id.addMembersButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button Clicked");
                goToAddMemberActivity();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println(user);

        if (user != null) {
            db = FirebaseDatabase.getInstance();
            setUpDB();

            list = (ListView) v.findViewById(R.id.members_list);
            //list.setOnItemClickListener(this);
            itemsAdapter =
                    new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<String>());
            list.setAdapter(itemsAdapter);
        }
        else {
            Toast msg = Toast.makeText(getActivity(),"You must be signed in with Facebook to view/add group members",
                    Toast.LENGTH_LONG);
            TextView txt = (TextView) msg.getView().findViewById(android.R.id.message);
            if (txt != null) {
                txt.setGravity(Gravity.CENTER);
            }
            msg.show();
        }

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setUpDB() {
        DatabaseReference dbRef = db.getReference("groups");//db.getReference(user.getUid());
        dbRef.child(groupName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemsAdapter.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.child("members").getChildren()) {

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

                    Query query = ref.child(eventSnapshot.getValue() + "/name");
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // dataSnapshot is the "issue" node with all children with id 0
                                System.out.println(dataSnapshot.getValue());
                                itemsAdapter.add((String)dataSnapshot.getValue());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("database error!!" + databaseError);
            }
        });

    }

    public void goToAddMemberActivity(){
        Intent i = new Intent(getActivity(),AddingMembersActivity.class);
        startActivityForResult(i,REQUEST_CODE_ADD_MEMBERS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_ADD_MEMBERS == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                ArrayList<String> newMembersList = data.getStringArrayListExtra(EXTRA_NEW_MEMBER_LIST);
                addMembersToDB(newMembersList);
            }
        }
        else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void addMembersToDB(ArrayList<String> l){
        //for each member
        //query members key
        //add this group to their groups
        //query group name and add the new member
        //add the new member to the list view as well
        final ArrayList<String> userIds = new ArrayList<String>();
        final DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("users");
        final DatabaseReference dbRef2 = FirebaseDatabase.getInstance().getReference("groups");
        for (int i = 0; i < l.size(); i++)   {
            final String name = l.get(i);
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                        if (item_snapshot.child("name").getValue().equals(name)) {
                            userIds.add(item_snapshot.getKey());
                            sendNotification(item_snapshot.child("instanceID").getValue().toString());
                            dbRef.child(item_snapshot.getKey() + "/groups").push().setValue(groupName);
                            dbRef2.child(groupName + "/members").push().setValue(item_snapshot.getKey());
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }



    }

    public static final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
    @SuppressLint("StaticFieldLeak")
    private void sendNotification(final String reg_token) {
            new AsyncTask<Void,Void,Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        OkHttpClient client = new OkHttpClient();
                        JSONObject json=new JSONObject();
                        JSONObject dataJson=new JSONObject();
                        dataJson.put("body","You were added to " + groupName);
                        dataJson.put("title","You've been added to a group!");
                        json.put("notification",dataJson);
                        System.out.println("Sending notification to: " + reg_token);
                        json.put("to",reg_token);
                        RequestBody body = RequestBody.create(JSON, json.toString());
                        Request request = new Request.Builder()
                                .header("Authorization","key="+"AIzaSyDmV0UuVIPmtxgqRlyOvhbzglDuf4TM994")
                                .url("https://fcm.googleapis.com/fcm/send")
                                .post(body)
                                .build();
                        Response response = client.newCall(request).execute();
                        String finalResponse = response.body().string();
                    }catch (Exception e){
                        //Log.d(TAG,e+"");
                    }
                    return null;
                }
            }.execute();

        }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
