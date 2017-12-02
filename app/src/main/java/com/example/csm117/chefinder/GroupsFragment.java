package com.example.csm117.chefinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupsFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View v;
    private static final int REQUEST_CODE_ADD_GROUP = 1;
    private FirebaseUser user;
    private FirebaseDatabase db;
    ListView listView;
    //ArrayList<String> groupsList;
    ArrayAdapter<String> listAdapater;


    public GroupsFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment GroupsFragment.
//     */
    // TODO: Rename and change types and number of parameters
    public static GroupsFragment newInstance() {
        GroupsFragment fragment = new GroupsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //groupsList = new ArrayList<String>();
        //String[] groupNames = {};
        //groupsList.addAll(Arrays.asList(groupNames));
        listAdapater = new ArrayAdapter<String>(getActivity(),R.layout.group_row, new ArrayList<String>());
        user = FirebaseAuth.getInstance().getCurrentUser();

        v = inflater.inflate(R.layout.fragment_groups, container, false);
        FloatingActionButton b = v.findViewById(R.id.addGroupsButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button Clicked");
                goToAddGroupActivity();
            }
        });

        if (user != null) {
            db = FirebaseDatabase.getInstance();
            setUpDB();

            listView = v.findViewById(R.id.groupList);
            listView.setAdapter(listAdapater);
            listView.setOnItemClickListener(this);
            listView.setOnItemLongClickListener(this);
        }
        else {
            Toast msg = Toast.makeText(getActivity(),"You must be signed in with Facebook to view/add groups",
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_ADD_GROUP == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                final String groupName = data.getStringExtra(AddingGroupsActivity.EXTRA_GROUP_NAME);
                //addGroupToListView(groupName);
            }
        }
        else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void goToAddGroupActivity() {
        Intent i = new Intent(getActivity(),AddingGroupsActivity.class);
        startActivityForResult(i,REQUEST_CODE_ADD_GROUP);
    }



    private void addGroupToDb(){

    }

    private void addGroupToListView(String name){
        listView = v.findViewById(R.id.groupList);

        listAdapater.add(name);
        listView.setAdapter(listAdapater);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println("item " + i + " was clicked");
        System.out.println(listAdapater.getItem(i));

        Intent intent = new Intent(getActivity(), GroupInfoActivity.class);

        intent.putExtra("GROUP_NAME", listAdapater.getItem(i));
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
        System.out.println("LongPress detected");
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this.getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this.getActivity());
        }
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        System.out.println("item " + i + " was clicked");
                        DatabaseReference dbRef = db.getReference("groups");
                        System.out.println(listAdapater.getItem(i));
                        Query itemQuery = dbRef.child(listAdapater.getItem(i) + "/members");
                        itemQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //System.out.println("delete data");
                                //System.out.println(dataSnapshot.getKey());
                                //System.out.println(dataSnapshot.getChildrenCount());
                                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                                    //System.out.println(eventSnapshot.getKey());
                                    System.out.println("Checking " + eventSnapshot.getValue() + " vs " + user.getUid());

                                    if (eventSnapshot.getValue().equals(user.getUid())) {
                                        System.out.println("Removing" + eventSnapshot.getValue());
                                        eventSnapshot.getRef().removeValue();
                                        break;
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("Cancelled");
                            }
                        });
                        DatabaseReference dbRef2 = db.getReference("users");
                        Query itemQuery2 = dbRef2.child(user.getUid() + "/groups");
                        itemQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                                    //System.out.println(eventSnapshot.getKey());
                                    System.out.println("Checking " + eventSnapshot.getValue() + " vs " + listAdapater.getItem(i));
                                    if (eventSnapshot.getValue().equals(listAdapater.getItem(i))) {
                                        System.out.println("Removing" + eventSnapshot.getValue());
                                        eventSnapshot.getRef().removeValue();
                                        break;
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("Cancelled");
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


        return true;
    }


    public void setUpDB() {
        DatabaseReference dbRef = db.getReference("users");//db.getReference(user.getUid());
        dbRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listAdapater.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.child("groups").getChildren()) {
                    System.out.println("data change");
                    String val = eventSnapshot.getValue(String.class);
                    System.out.println(val);
                    listAdapater.add(val);
                }

                /*
                for (int i = 0; i < ingredients.size(); i++)
                    System.out.println(ingredients.get(i));
                    */
                listAdapater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("database error!!" + databaseError);
            }
        });
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
