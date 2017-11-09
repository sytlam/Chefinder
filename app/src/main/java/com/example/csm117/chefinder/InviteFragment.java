package com.example.csm117.chefinder;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;


public class InviteFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        Button invite = (Button)findViewById(R.id.button);
        invite.setOnClickListener(new View.OnClickListener()    {
            @Override
            public void onClick(View view)  {
                sendInvite();
            }
        });

        setTitle(R.string.title_groups);

    }

    private void createGroup() {
        TextView t = (TextView) findViewById(R.id.message);
        t.setText(R.string.title_activity_groups);
    }


    @SuppressWarnings( "deprecation" )
    private void sendInvite()   {
        String appLinkUrl;

        appLinkUrl = "https://www.needone.com/applink";

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .build();
            AppInviteDialog.show(this, content);
        }
    }
}