package com.arcltd.staff.web_view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.arcltd.staff.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;

public class ArcWebViewActivity extends AppCompatActivity {
    WebView webView;
    String url;
    Document document;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_web_view);

        webView = findViewById(R.id.webView);
        url = getIntent().getExtras().getString("url");
        webView.setInitialScale(1);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setScrollContainer(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        new MyAsynTask().execute();

    }



    private class MyAsynTask extends AsyncTask<Void, Void, Document> {

        @Override
        protected Document doInBackground(Void... voids) {

            document = null;
            try {
                document = Jsoup.connect(url).get();
                document.getElementsByClass("mobile-menu menu-hide").remove();
                document.getElementsByClass("bottom-info").remove();
                document.getElementsByClass("header-top").remove();
                document.getElementsByClass("header-main").remove();
                document.getElementsByClass("col-sm-6").remove();
                document.getElementsByClass("col-sm-3").remove();
                document.getElementsByClass("row voffset6").remove();
                document.getElementsByClass("ftr-widget").remove();
                document.getElementsByClass("widget_nav_menu").remove();
                document.getElementsByClass("menu-site-map-containe").remove();
                document.getElementsByClass("subscription ftr-widget").remove();
                document.getElementsByClass("main-list").remove();
                document.getElementsByClass("dropdown").remove();



            } catch (IOException e) {
                e.printStackTrace();
            }
            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);

            webView.loadDataWithBaseURL(url, document.toString(),
                    "text/html", "utf-8", "");
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                    view.loadUrl(url);

                    return super.shouldOverrideUrlLoading(view, request);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    showProgressDialog();
                }
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if(progressDialog!=null){
                        hideProgressDialog();
                    }

                }


            });
            webView.setWebChromeClient(new WebChromeClient() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onPermissionRequest(final PermissionRequest request) {
                    Log.d("LOG_TAG", "onPermissionRequest: " + Arrays.toString(request.getResources()));
                    request.grant(request.getResources());
                }


            });


        }
    }
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(ArcWebViewActivity.this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage("Please Wait");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
    }

}