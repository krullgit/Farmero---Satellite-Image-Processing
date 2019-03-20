package com.example.matthes.farmero;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/*************** CLASS NOT IN USE *****************/

/**
 * The class can be used to include a webpage as part of the application.
 */

public class UNUSEDWebViewMap extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menue_upper_right, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //WebView myWebView = (WebView) findViewById(R.id.webview);
        //WebSettings webSettings = myWebView.getSettings();
        //webSettings.setJavaScriptEnabled(true);
        //myWebView.setWebChromeClient(new WebChromeClient());


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_map);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
////        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
////        fab.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
////            }
////        });
//
//        WebView myWebView = (WebView) findViewById(R.id.webview);
//        WebSettings webSettings = myWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        myWebView.setWebViewClient(new WebViewClient());
//        //myWebView.loadUrl("http://www.example.com");
//        myWebView.loadUrl("http://10.0.2.2:9000/");


    }
}
