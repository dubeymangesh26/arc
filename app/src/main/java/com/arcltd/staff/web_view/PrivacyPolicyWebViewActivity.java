package com.arcltd.staff.web_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arcltd.staff.R;

public class PrivacyPolicyWebViewActivity extends AppCompatActivity implements View.OnClickListener {
    WebView webView;
    String url;
    private static ProgressBar webViewProgressBar;
    private static ImageView back, forward, refresh, close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_web_view);

        url = "http://onlineformsolution.in/arcMessApi/arcPrivacypoclicy.php";
      /*  webView.setInitialScale(1);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setScrollContainer(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);*/
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
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);
        LoadWebViewUrl(url);
    }

    private void setListeners() {
        back.setOnClickListener(PrivacyPolicyWebViewActivity.this);
        forward.setOnClickListener(PrivacyPolicyWebViewActivity.this);
        refresh.setOnClickListener(PrivacyPolicyWebViewActivity.this);
        close.setOnClickListener(PrivacyPolicyWebViewActivity.this);
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
        if (isInternetConnected())
            webView.loadUrl(url);
        else {
            refresh.setVisibility(View.VISIBLE);
            Toast.makeText(PrivacyPolicyWebViewActivity.this, "Oops!! There is no internet connection. Please enable your internet connection.", Toast.LENGTH_LONG).show();

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