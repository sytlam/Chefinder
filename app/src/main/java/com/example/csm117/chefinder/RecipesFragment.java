package com.example.csm117.chefinder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import java.util.List;
import java.net.URLEncoder;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipesFragment extends Fragment {

    private static final String GROUP_NAME = "default group name";

    private String groupName;

    private GridView gridView;
    private ArrayList<String> groupIng = new ArrayList<String>();;
    private OnFragmentInteractionListener mListener;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private OkHttpClient client = new OkHttpClient();
    private String queryString = "";

    private List<String> foodName = new ArrayList<>();
    private List<String> foodPics = new ArrayList<>();
    private List<String> foodRecipe = new ArrayList<>();



    public RecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipesFragment newInstance(String name) {
        RecipesFragment fragment = new RecipesFragment();
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
        }
        getActivity().setTitle(R.string.title_recipes);
        //setContentView(R.layout.fragment_recipes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final View v = inflater.inflate(R.layout.fragment_recipes, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println(user);

        if (user != null) {
            db = FirebaseDatabase.getInstance();
            final RecipesFragment meme = this;

            DatabaseReference dbRef = db.getReference("groups");//db.getReference(user.getUid());
            dbRef.child(groupName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    groupIng.clear();
                    for (DataSnapshot eventSnapshot : dataSnapshot.child("members").getChildren()) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

                        Query query = ref.child(eventSnapshot.getValue() + "/ingredients");
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // dataSnapshot is the "issue" node with all children with id 0
                                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                        groupIng.add((String) issue.getValue());
                                    }
                                    String parameters = "";
                                    boolean first = true;
                                    for (int i = 0; i < groupIng.size(); i++) {
                                        if (first) {
                                            String currentString = groupIng.get(i);
                                            parameters += currentString;
                                            first = false;
                                        }
                                        else {
                                            String currentString = ", " + groupIng.get(i);
                                            parameters += currentString;
                                        }
                                    }
                                    //parameters = URLEncoder.encode(parameters, "UTF-8");
                                    queryString = "http://food2fork.com/api/search?key=a53266eff3482baeae56e93836fcc011&q=" + parameters;
                                    JSONObject response = run(queryString);
                                    //System.out.println(queryString);
                                    //System.out.println(response);

                                    extractData(response);
                                    String[] names = foodName.toArray(new String[foodName.size()]);
                                    String[] pictures = foodPics.toArray(new String[foodPics.size()]);
                                    String[] recipes = foodRecipe.toArray(new String[foodRecipe.size()]);
                                    System.out.println("length is" + foodPics.size());

                                    gridView = (GridView) v.findViewById(R.id.gridview);
                                    gridView.setAdapter(new ImageAdapter(meme, names, pictures, recipes));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    //itemsAdapter.notifyDataSetChanged();
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("database error!!" + databaseError);
                }
            });

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

//    public void setUpDB() {
//
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        DatabaseReference dbRef = db.getReference("groups");//db.getReference(user.getUid());
//        dbRef.child(groupName).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                groupIng.clear();
//                for (DataSnapshot eventSnapshot : dataSnapshot.child("members").getChildren()) {
//                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
//
//                    Query query = ref.child(eventSnapshot.getValue() + "/ingredients");
//                    query.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                // dataSnapshot is the "issue" node with all children with id 0
//                                for (DataSnapshot issue : dataSnapshot.getChildren()) {
//                                    groupIng.add((String) issue.getValue());
//                                }
//                                String parameters = "";
//                                boolean first = true;
//                                for (int i = 0; i < groupIng.size(); i++) {
//                                    if (first) {
//                                        String currentString = groupIng.get(i);
//                                        parameters += currentString;
//                                        first = false;
//                                    }
//                                    else {
//                                        String currentString = ", " + groupIng.get(i);
//                                        parameters += currentString;
//                                    }
//                                }
//                                //parameters = URLEncoder.encode(parameters, "UTF-8");
//                                queryString = "http://food2fork.com/api/search?key=a53266eff3482baeae56e93836fcc011&q=" + parameters;
//                                JSONObject response = run(queryString);
//                                //System.out.println(queryString);
//                                //System.out.println(response);
//                                extractData(response);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//
//                //itemsAdapter.notifyDataSetChanged();
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("database error!!" + databaseError);
//            }
//        });
//
//    }

    public void extractData(JSONObject response)    {
        int recipeCount;
        JSONArray recipes;

        try {
            recipeCount = response.getInt("count");
            recipes = response.getJSONArray("recipes");
            for(int i = 0; i < recipes.length(); i++)   {
                String name = recipes.getJSONObject(i).getString("title");
                foodName.add(name);
                String picURL = recipes.getJSONObject(i).getString("image_url");
                foodPics.add(picURL);
                String recipeURL = recipes.getJSONObject(i).getString("source_url");
                foodRecipe.add(recipeURL);

            }
        } catch(JSONException e)    {
        }
        System.out.println(foodName.size());
        System.out.println(foodPics.size());
        System.out.println(foodRecipe.size());

    }

    public String search(String query) {
        try {
            final String url = "http://food2fork.com/api/search?key=a53266eff3482baeae56e93836fcc011&q=" + URLEncoder.encode(query, "UTF-8");
            return url;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public JSONObject run(String url)  {
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
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

