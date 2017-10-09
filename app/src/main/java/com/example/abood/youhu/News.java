package com.example.abood.youhu;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class News extends Fragment {

    public News() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public WebView  mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.activity_news, container, false);



        mWebView = (WebView) v.findViewById(R.id.webview);
        mWebView.loadUrl("https://hu.edu.jo/news/_3alaqat_archive.aspx?f=0&last=10");

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

        mWebView.getSettings().setBuiltInZoomControls(true);


       /* v.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {onBackPressed();
                return false;
            }
                return false;
            }
        } );*/

        return v;

    }



   /* public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            onBackPressed();
   }    } */

    }



