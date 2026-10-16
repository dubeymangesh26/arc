package com.arcltd.staff.activity.otherAndMain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;

import android.os.Build;
import android.os.ext.SdkExtensions;
import androidx.annotation.RequiresExtension;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.pdf.viewer.fragment.PdfViewerFragment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class PdfViewerReaderActivity extends AppCompatActivity {
    private String url;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_pdf_viewer);

        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("link");
        }
        Log.e("onCreateLink: ", String.valueOf(url));

        progressBar = findViewById(R.id.progressBar2);

        if (url != null && isInternetConnected()) {
            new DownloadPdfTask().execute(url);
        } else if (url == null) {
            Toast.makeText(this, "Invalid PDF link", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadPdfTask extends AsyncTask<String, Void, File> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected File doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    File file = new File(getCacheDir(), "temp_pdf.pdf");
                    FileOutputStream outputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                    }
                    outputStream.close();
                    inputStream.close();
                    return file;
                }
            } catch (IOException e) {
                Log.e("DownloadPdfTask", "Error downloading PDF", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            progressBar.setVisibility(View.GONE);
            if (file != null) {
                showPdf(file);
            } else {
                Toast.makeText(PdfViewerReaderActivity.this, "Failed to download PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showPdf(File file) {
        if (SdkExtensions.getExtensionVersion(Build.VERSION_CODES.S) >= 13) {
            loadPdfFragment(file);
        } else {
            // Fallback: Open in external PDF viewer
            openPdfExternally(file);
        }
    }

    private void openPdfExternally(File file) {
        try {
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No PDF viewer app found. Please install one.", Toast.LENGTH_LONG).show();
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 13)
    private void loadPdfFragment(File file) {
        try {
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            PdfViewerFragment pdfViewerFragment = new PdfViewerFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.pdfViewContainer, pdfViewerFragment)
                    .commit();

            getSupportFragmentManager().executePendingTransactions();
            pdfViewerFragment.setDocumentUri(uri);
        } catch (Exception e) {
            Log.e("PdfViewer", "Error showing PDF", e);
            Toast.makeText(this, "Error opening PDF", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
