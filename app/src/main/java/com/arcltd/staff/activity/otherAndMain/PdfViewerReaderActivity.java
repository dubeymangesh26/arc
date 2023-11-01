package com.arcltd.staff.activity.otherAndMain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.rajat.pdfviewer.PdfViewerActivity;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class PdfViewerReaderActivity extends AppCompatActivity{
   String url,doc;
    WebView webView;
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private static ImageView back, forward, refresh, close;

    private TextView txt; // You can remove if you don't want this
    private PDFView pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_pdf_viewer);

        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        url = getIntent().getExtras().getString("link");
        Log.e( "onCreateLink: ",url );



       // String pdf = "http://www.pc-hardware.hu/PDF/konfig.pdf";
     //   doc="<iframe src='http://docs.google.com/gview?embedded=true&url="+url+"' width='100%' height='100%' style='border: none;'></iframe>";
         doc="http://docs.google.com/gview?embedded=true&url=" + url;


        pdf = (PDFView) findViewById(R.id.pdfView); //github.barteksc
        progressBar = (ProgressBar) findViewById(R.id.progressBar2); //github.barteksc
        txt = findViewById(R.id.txtPdf);
        String pdfUrl = url;
        try{
            new RetrievePdfStream().execute(pdfUrl);
        }
        catch (Exception e){
            Toast.makeText(this, "Failed to load Url :" + e.toString(), Toast.LENGTH_SHORT).show();
        }


                    // Opening pdf from assets folder
/*

        PdfViewerActivity.FILE_URL(
                context = this, pdfUrl = url,
                pdfTitle = "Title", directoryName = "dir", enableDownload = true);
        )

                    PdfViewerActivity.Companion.launchPdfFromPath(
                            this,
                            "file_name.pdf",
                            "Pdf title/name",
                            "assets",
                            false,
                            true
                    );

*/


        isInternetConnected();

    }

    class RetrievePdfStream extends AsyncTask<String, Void, InputStream> {



        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                return null;
            }
            return inputStream;
        }
        @Override
        protected void onPostExecute(InputStream inputStream) {

            pdf.fromStream(inputStream).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    progressBar.setVisibility(View.GONE);
                }
            }).load();
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