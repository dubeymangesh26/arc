package com.arcltd.staff.activity.otherAndMain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arcltd.staff.R;
import androidx.appcompat.app.AppCompatActivity;



public class PdfViewerActivity extends AppCompatActivity implements View.OnClickListener  {
   String url,doc;
    WebView webView;
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar webViewProgressBar;
    @SuppressLint("StaticFieldLeak")
    private static ImageView back, forward, refresh, close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_video_web_view);

        url = getIntent().getExtras().getString("link");
        Log.e( "onCreateLink: ",url );



       // String pdf = "http://www.pc-hardware.hu/PDF/konfig.pdf";
     //   doc="<iframe src='http://docs.google.com/gview?embedded=true&url="+url+"' width='100%' height='100%' style='border: none;'></iframe>";
         doc="http://docs.google.com/gview?embedded=true&url=" + url;

        initViews();
        setUpWebView();
        setListeners();

    }

    private void initViews() {
        back = (ImageView) findViewById(R.id.webviewBack);
        forward = (ImageView) findViewById(R.id.webviewForward);
        refresh = (ImageView) findViewById(R.id.webviewReload);
        close = (ImageView) findViewById(R.id.webviewClose);
        webViewProgressBar = (ProgressBar) findViewById(R.id.webViewProgressBar);
    }


    private void setUpWebView() {
        webView = (WebView) findViewById(R.id.sitesWebView);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
       // webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        /*webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);*/

       // webView.loadData( doc , "text/html", "UTF-8");
        LoadWebViewUrl(doc);
    }

    private void setListeners() {
        back.setOnClickListener(PdfViewerActivity.this);
        forward.setOnClickListener(PdfViewerActivity.this);
        refresh.setOnClickListener(PdfViewerActivity.this);
        close.setOnClickListener(PdfViewerActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.webviewBack:
                isWebViewCanGoBack();
                break;
            case R.id.webviewForward:
                if (webView.canGoForward())
                    webView.goForward();
                break;
            case R.id.webviewReload:
                 String url = webView.getUrl();
                LoadWebViewUrl(url);
             //   webView.loadData( doc , "text/html", "UTF-8");
                break;
            case R.id.webviewClose:
                finish();
                break;
        }
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            refresh.setVisibility(View.GONE);
            if (!webViewProgressBar.isShown())
                webViewProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            refresh.setVisibility(View.VISIBLE);
            if (webViewProgressBar.isShown())
                webViewProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            refresh.setVisibility(View.VISIBLE);
            if (webViewProgressBar.isShown()) {
                webViewProgressBar.setVisibility(View.GONE);
                // Toast.makeText(WebViewActiviry.this, "Unexpected error occurred.Reload page again.", Toast.LENGTH_SHORT).show();
            }

        }



    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            isWebViewCanGoBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void isWebViewCanGoBack() {
        if (webView.canGoBack())
            webView.goBack();
        else
            finish();
    }


    private void LoadWebViewUrl(String url) {

        if (isInternetConnected()) {
            webView.loadUrl(url);
        }
        else {
            Toast.makeText(PdfViewerActivity.this, "Oops!! There is no internet connection. Please enable your internet connection.", Toast.LENGTH_LONG).show();

        }
    }

    public boolean isInternetConnected() {
        // At activity startup we manually check the internet status and change
        // the text status
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;

    }



}