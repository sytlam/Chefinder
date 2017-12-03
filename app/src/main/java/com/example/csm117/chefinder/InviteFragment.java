package com.example.csm117.chefinder;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

import android.support.v4.app.Fragment;
import android.widget.Toast;




public class InviteFragment extends Fragment implements View.OnClickListener{

    private ListView list;
    private Button invButton;

    public InviteFragment() {
        //empty constructor
    }

    public static InviteFragment newInstance()  {
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
        //list = (ListView) v.findViewById(R.id.NotificationList);
        invButton = (Button) v.findViewById(R.id.invite_button);
        invButton.setOnClickListener(this);
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