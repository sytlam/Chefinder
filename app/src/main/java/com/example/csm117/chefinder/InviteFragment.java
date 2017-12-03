package com.example.csm117.chefinder;

import android.net.Uri;
import android.os.Bundle;

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
import android.widget.Toast;
import java.util.ArrayList;


public class InviteFragment extends Fragment implements View.OnClickListener{

    private ListView list;
    private Button invButton;
    private TextView text;
    private static ArrayList<String> friends;
    private ArrayAdapter<String> itemsAdapter;

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
        Toast.makeText(getActivity(),"Invite Sent", Toast.LENGTH_SHORT).show();
    }

    // to be called in OnButtonPressed
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