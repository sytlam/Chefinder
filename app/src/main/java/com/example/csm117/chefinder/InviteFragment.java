package com.example.csm117.chefinder;

import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.widget.Toast;
import java.util.ArrayList;


public class InviteFragment extends Fragment implements View.OnClickListener{

    private ListView list;
    private Button invButton;
    private TextView text;
    private static ArrayList<String> friends;
    private ArrayAdapter<String> itemsAdapter;
    public FragmentManager fm = getFragmentManager();

    public InviteFragment() {
        //empty constructor
    }

    public static InviteFragment newInstance(ArrayList<String> input)  {
        friends = input;
        InviteFragment fragment = new InviteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        View v = inflater.inflate(R.layout.fragment_invite, container, false);
        invButton = (Button) v.findViewById(R.id.invite_button);
        invButton.setOnClickListener(this);
        text = (TextView) v.findViewById(R.id.friendText);
        list = (ListView) v.findViewById(R.id.friendList);
        itemsAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, friends);
        list.setAdapter(itemsAdapter);

        return v;
    }

    // send invite from button press
    @Override
    public void onClick(View v) {
        sendInvite();

        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());

        ad.setCancelable(true);
        ad.setIcon(R.drawable.chefinder_logo);
        ad.setTitle("Invite Friends");
        ad.setMessage("This will send app installation requests to your facebook friends.");
        ad.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),"Invite Sent", Toast.LENGTH_SHORT).show();
            }
        });

        // Negative Button
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,	int which) {
                // Do something else
            }
        });
        AlertDialog alert = ad.create();
        alert.show();

        //ImageFragment frag = new ImageFragment();
        //frag.show(fm, "Invite Friend");
    }

    // to be called in OnButtonPressed - won't work right now cause app is not official
    @SuppressWarnings( "deprecation" )
    private void sendInvite()   {
        String appLinkUrl, previewImageUrl;

        // the deep link from fb
        appLinkUrl = "https://fb.me/736802985294351";
        previewImageUrl = "https://i.imgur.com/JlU2TYU.png";

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .build();
            AppInviteDialog.show(this, content);
        }
    }
}