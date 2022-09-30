package com.arcltd.staff.activity.detailsData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arcltd.staff.BuildConfig;
import com.arcltd.staff.R;
import com.arcltd.staff.response.BranchListResponse;
import com.arcltd.staff.response.BranchLocalListResponse;
import com.arcltd.staff.response.OfficeGodownMessListResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Objects;

public class BranchDetailsActivity extends AppCompatActivity {
    TextView tvRegion,tvDivision,tvBranchName,tvBraMangNo,tvPhNo,tvBranchMobil,tvEmailID,tvBranchAddress;
    String data;
    BranchLocalListResponse dataBean;
    String uriString,path,folderPath;
    Uri bmpUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_details);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("data");
            dataBean = new Gson().fromJson(data, BranchLocalListResponse.class);
            Log.e("", "onCreate: " + new GsonBuilder().create().toJson(dataBean));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }

        tvRegion=findViewById(R.id.tvRegion);
        tvDivision=findViewById(R.id.tvDivision);
        tvBranchName=findViewById(R.id.tvBranchName);
        tvBraMangNo=findViewById(R.id.tvBraMangNo);
        tvBranchAddress=findViewById(R.id.tvBranchAddress);
        tvPhNo=findViewById(R.id.tvPhNo);
        tvBranchMobil=findViewById(R.id.tvBranchMobil);
        tvEmailID=findViewById(R.id.tvEmailID);

        try {
            tvRegion.setText(dataBean.getSc_region_name());
            tvDivision.setText(dataBean.getSc_division_name());
            tvBranchName.setText(dataBean.getSc_branch_name());
            tvBraMangNo.setText(dataBean.getSc_branch_mngr_mob());
            tvBranchAddress.setText(dataBean.getSc_branch_address());
            tvPhNo.setText(dataBean.getSc_std_code()+" "+dataBean.getSc_phone_number());
            tvBranchMobil.setText(dataBean.getSc_branch_mobileno());
            tvEmailID.setText(dataBean.getSc_email_id());

            tvBraMangNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", tvBraMangNo.getText().toString(), null));
                    startActivity(intent);

                }
            });

            tvPhNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", tvPhNo.getText().toString(), null));
                    startActivity(intent);

                }
            });

            tvBranchMobil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", tvBranchMobil.getText().toString(), null));
                    startActivity(intent);

                }
            });

            tvEmailID.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("IntentReset")
                @Override
                public void onClick(View v) {
                    String[] TO = {tvEmailID.getText().toString()};
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");


                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Write Here");

                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        finish();
                        Log.i("Finished sending email...", "");
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(BranchDetailsActivity.this,
                                "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }


                }
            });

        } catch (Exception e) {
            Log.e("", "ListingDetail error: " + e);
        }

        if (ContextCompat.checkSelfPermission(BranchDetailsActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(BranchDetailsActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(BranchDetailsActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }

    }



    private void invitYourFriend() {
        Intent shareIntent;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arc_name);
        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Share.jpg";
        folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "";
        OutputStream out = null;
        File file = new File(path);
// if path not found create folder and get path
        if (!file.exists()) {
            File folder_path = new File(folderPath);
            folder_path.mkdirs();
            file = new File(path);
        }
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        path = file.getPath();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            bmpUri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider", file);
        } else {
            bmpUri = Uri.parse("file://" + path);
        }
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "ARC LIMITED");
        shareIntent.putExtra(Intent.EXTRA_TEXT, dataBean.getSc_branch_name()
                + "\n" +"Branch Manager Mobile No  "+ dataBean.getSc_branch_mngr_mob() +"\n"+
                "Branch Ph/ Mobile No "+"0"+dataBean.getSc_std_code()+"-"+dataBean.getSc_phone_number()+
                " "+dataBean.getSc_branch_mobileno()+
                "\n "+ "Email ID- "+dataBean.getSc_email_id()+
                "\n"+"Branch Address "+"\n"+dataBean.getSc_branch_address());
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "Share Using"));

    }

    public void sharedata(View view) {
        invitYourFriend();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}