package com.example.csm117.chefinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IngredientsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IngredientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private OnFragmentInteractionListener mListener;
    private Button addButton;
    private EditText addIngredientText;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private ListView list;
    private ArrayAdapter itemsAdapter;

    public IngredientsFragment() {

    }

    public static IngredientsFragment newInstance() {
        IngredientsFragment fragment = new IngredientsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("create view");
        View v = inflater.inflate(R.layout.fragment_ingredients, container, false);

        addButton = (Button)v.findViewById(R.id.add_ingredient);
        addButton.setOnClickListener(this);

        addIngredientText = (EditText)v.findViewById(R.id.ingredient_text);

        user = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println(user);

        if (user != null) {
            db = FirebaseDatabase.getInstance();
            setUpDB();

            list = (ListView) v.findViewById(R.id.list);
            //list.setOnItemClickListener(this);
            list.setOnItemLongClickListener(this);
            itemsAdapter =
                    new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<String>());
            list.setAdapter(itemsAdapter);
        }
        else {
            Toast msg = Toast.makeText(getActivity(),"You must be signed in with Facebook to view/add ingredients",
                    Toast.LENGTH_LONG);
            TextView txt = (TextView) msg.getView().findViewById(android.R.id.message);
            if (txt != null) {
                txt.setGravity(Gravity.CENTER);
            }
            msg.show();
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_ingredient: // clicked add ingredient button
                addIngredient();
        }
    }

    // add ingredients to Firebase db entry for this user
    public void addIngredient() {
        String ingredient = addIngredientText.getText().toString();
        System.out.println(ingredient);

        if (ingredient.isEmpty()) { // can't enter a blank ingredient
            Toast msg = Toast.makeText(getActivity(),"Please enter an ingredient",
                    Toast.LENGTH_LONG);
            TextView txt = (TextView) msg.getView().findViewById(android.R.id.message);
            if (txt != null) {
                txt.setGravity(Gravity.CENTER);
            }
            msg.show();
            return;
        }

        if (user != null) { // user has signed in with fb, allow them to add ingredients
            String userId = user.getUid();
            System.out.println(userId);

            DatabaseReference dbRef = db.getReference("users");
            dbRef.child(userId + "/ingredients").push().setValue((Object)ingredient);
            addIngredientText.setText("");
        }
        else {
            Toast msg = Toast.makeText(getActivity(),"You must be signed in with Facebook to add ingredients",
                    Toast.LENGTH_LONG);
            TextView txt = (TextView) msg.getView().findViewById(android.R.id.message);
            if (txt != null) {
                txt.setGravity(Gravity.CENTER);
            }
            msg.show();
        }
    }

    public void setUpDB() {
        DatabaseReference dbRef = db.getReference("users");//db.getReference(user.getUid());
        dbRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemsAdapter.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.child("ingredients").getChildren()) {
                    String val = eventSnapshot.getValue(String.class);
                    itemsAdapter.add(val);
                }

                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("database error!!" + databaseError);
            }
        });
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

    /*
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        System.out.println("item " + i + " was clicked");
        DatabaseReference dbRef = db.getReference("users");
        System.out.println(itemsAdapter.getItem(i));
        Query itemQuery = dbRef.child(user.getUid() + "/ingredients");
        itemQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("delete data");
                System.out.println(dataSnapshot.getKey());
                System.out.println(dataSnapshot.getChildrenCount());
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    System.out.println(eventSnapshot.getKey());
                    System.out.println(eventSnapshot.getValue());
                    if (eventSnapshot.getValue() == (String)itemsAdapter.getItem(i)) {
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
    */

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
        System.out.println("LongPress detected");
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this.getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this.getActivity());
        }
        builder.setTitle("Delete Ingredient")
                .setMessage("Are you sure you want to delete this ingredient?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        DatabaseReference dbRef = db.getReference("users");
                        Query itemQuery = dbRef.child(user.getUid() + "/ingredients");
                        itemQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                System.out.println(dataSnapshot.getKey());
                                System.out.println(dataSnapshot.getChildrenCount());
                                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                                    System.out.println(eventSnapshot.getKey());
                                    System.out.println(eventSnapshot.getValue());
                                    if (eventSnapshot.getValue() == (String)itemsAdapter.getItem(i)) {
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
