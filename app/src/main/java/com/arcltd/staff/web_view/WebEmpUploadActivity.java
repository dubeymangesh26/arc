package com.arcltd.staff.web_view;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arcltd.staff.R;

public class WebEmpUploadActivity extends AppCompatActivity {
    private WebView mainWebView;
    String webViewUrl = "http://onlineformsolution.in/arcMessApi/webEmployeeDateUpdate.php";
    // variables para manejar la subida de archivos
    private final static int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri[]> mUploadMessage;
    private static ProgressBar webViewProgressBar;
    SwipeRefreshLayout swiptoRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_emp_upload);

        // Define url that will open in webview


        mainWebView = findViewById(R.id.webView);
        webViewProgressBar = findViewById(R.id.progressBar1);
        swiptoRefresh=findViewById(R.id.swiptoRefresh);

        mainWebView.setWebViewClient(new MyWebViewClient());
        mainWebView.setWebChromeClient(new MyWebChromeClient());
        mainWebView.getSettings().setJavaScriptEnabled(true);
        mainWebView.clearCache(true);

        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                mainWebView.loadUrl(webViewUrl);
                swiptoRefresh.setRefreshing(false);
            }
        });


        // cargamos la pagina
        LoadWebViewUrl(webViewUrl);

    }

    private void LoadWebViewUrl(String webViewUrl) {
        if (isInternetConnected())
            mainWebView.loadUrl(webViewUrl);
        else {
            Toast.makeText(WebEmpUploadActivity.this, "Oops!! There is no internet connection. Please enable your internet connection.", Toast.LENGTH_LONG).show();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // manejo de seleccion de archivo
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FILECHOOSER_RESULTCODE) {

            if (null == mUploadMessage || intent == null || resultCode != RESULT_OK) {
                return;
            }

            Uri[] result = null;
            String dataString = intent.getDataString();

            if (dataString != null) {
                result = new Uri[]{Uri.parse(dataString)};
            }

            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }


    // ====================
    // Web clients classes
    // ====================

    /**
     * Clase para configurar el webview
     */
    private class MyWebViewClient extends WebViewClient {

        // permite la navegacion dentro del webview
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    /**
     * Clase para configurar el chrome client para que nos permita seleccionar archivos
     */
    private class MyWebChromeClient extends WebChromeClient {

        // maneja la accion de seleccionar archivos
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

            // asegurar que no existan callbacks
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }

            mUploadMessage = filePathCallback;

            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*"); // set MIME type to filter

            WebEmpUploadActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), WebEmpUploadActivity.FILECHOOSER_RESULTCODE);

            return true;
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
