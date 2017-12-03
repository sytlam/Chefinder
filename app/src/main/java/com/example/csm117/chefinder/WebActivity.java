package com.example.csm117.chefinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;


public class WebActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

//Get a reference to your WebView//
        WebView webView = (WebView) findViewById(R.id.webview);

//Specify the URL you want to display//
        Intent myIntent = getIntent();
        String site = myIntent.getStringExtra("url");
        webView.loadUrl(site);
    }

}
