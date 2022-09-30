package com.arcltd.staff.authentication;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.annotation.Nullable;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arcltd.staff.R;
import com.arcltd.staff.activity.updateData.UpdateProfileActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.WebConstants;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.ProfileResponseResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDetailsActivity extends BaseActivity {
    TextView etBranchCode,etEmployeeId,etEmpName,etDisign,etEmailId,etMobile,etEduction,
            etJoiningDate,etSalary,etRetirement, etESI, etUan;
    String data,emp_code,profileImage;
    CircleImageView profilePicture;
    Bitmap bitmap;
    String encodeImageString;
    //TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        etBranchCode=findViewById(R.id.etBranchCode);
        etEmployeeId=findViewById(R.id.etEmployeeId);
        etEmpName=findViewById(R.id.etEmpName);
        etDisign=findViewById(R.id.etDisign);
        etEmailId=findViewById(R.id.etEmailId);
        etMobile=findViewById(R.id.etMobile);
        etEduction=findViewById(R.id.etEduction);
        etJoiningDate=findViewById(R.id.etJoiningDate);
        etSalary=findViewById(R.id.etSalary);
        etRetirement=findViewById(R.id.etRetirement);
        etESI=findViewById(R.id.etESI);
        etUan=findViewById(R.id.etUan);
        profilePicture=findViewById(R.id.profilePicture);

        profileDetails();
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPicturefromGallery();
                }
        });
        Glide.with(this)
                .load(RetrofitClient.getStringUserPreference(getApplicationContext(),Constants.PROFILE_PICTURE)) // image url
                .placeholder(R.drawable.person_24) // any placeholder to load at start
                .error(R.drawable.avatar_10)  // any image in case of error
                .override(200, 200) // resizing
                .centerCrop()
                .into(profilePicture);

    }



    private void profileDetails() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.profile(disposable, Constants.ApiRequestCode.OFFICEMESSGDN,
                        RetrofitClient.getStringUserPreference(getApplicationContext(),Constants.EMP_CODE));

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult: " + object);
        Infrastructure.dismissProgressDialog();
       /* if (callAPIId == Constants.ApiRequestCode.TARGET) {
            ProfilePictureUpdateResponse profilePictureUpdateResponse = (ProfilePictureUpdateResponse) object;
            getUpdatePictureList(profilePictureUpdateResponse);
        }*/
        if (callAPIId == Constants.ApiRequestCode.OFFICEMESSGDN) {
            ProfileResponseResponse profileResponseResponse = (ProfileResponseResponse) object;
            getprofile(profileResponseResponse);
        }
    }


    private void getprofile(ProfileResponseResponse profileResponseResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(profileResponseResponse));
            if (profileResponseResponse != null) {

                Infrastructure.dismissProgressDialog();
                etBranchCode.setText(profileResponseResponse.getUser().getBranch_code());
                emp_code=profileResponseResponse.getUser().getEmp_code();
                profileImage=profileResponseResponse.getUser().getProfilepic();
                RetrofitClient.saveUserPreference(ProfileDetailsActivity.this, Constants.PROFILE_PICTURE,
                        profileResponseResponse.getUser().getProfilepic());
                Glide.with(this)
                        .load(profileResponseResponse.getUser().getProfilepic()) // image url
                        .placeholder(R.drawable.person_24) // any placeholder to load at start
                        .error(R.drawable.avatar_10)  // any image in case of error
                        .override(200, 200) // resizing
                        .centerCrop()
                        .into(profilePicture);

                etEmployeeId.setText(profileResponseResponse.getUser().getEmp_code());
                etEmpName.setText(profileResponseResponse.getUser().getName());
                etDisign.setText(profileResponseResponse.getUser().getDesign());
                etEmailId.setText(profileResponseResponse.getUser().getEmail());
                etMobile.setText(profileResponseResponse.getUser().getContactno());
                etEduction.setText(profileResponseResponse.getEmployee_details().getEmp_eqalification());
                etJoiningDate.setText(profileResponseResponse.getEmployee_details().getEmp_joining_date());
                etSalary.setText(profileResponseResponse.getEmployee_details().getEmp_salary());
                etRetirement.setText(profileResponseResponse.getEmployee_details().getRetirement_date());
                etESI.setText(profileResponseResponse.getEmployee_details().getEsi_no());
                etUan.setText(profileResponseResponse.getEmployee_details().getUan_no());
                data = new Gson().toJson(profileResponseResponse.getEmployee_details());
                Log.e("TAG", "EmpDetails: " + data);

            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + profileResponseResponse.getResponseCode());
                Toast.makeText(this, profileResponseResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in checkLoginResponse", e);
        }
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
    }

    public void empEdit(View view) {
        startActivity(new Intent(this, UpdateProfileActivity.class)
                .putExtra("data",data));
    }



    /* UpdateProfile ******************** Picture*/
    public void choosePicture(View view) {
        getPicturefromGallery();
    }

    private void getPicturefromGallery() {

        Dexter.withActivity(ProfileDetailsActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response)
                    {
                        Intent intent=new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Browse Image"),1);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==1 && resultCode==RESULT_OK)
        {
            Uri filepath=data.getData();
            try
            {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                profilePicture.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);
                uploaddatatodb();
            }catch (Exception ex)
            {
                Log.e(TAG, "onActivityResult: ",ex );
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void encodeBitmapImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytesofimage=byteArrayOutputStream.toByteArray();
        encodeImageString= Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }

    private void uploaddatatodb()
    {
        final String name=emp_code;

        Infrastructure.showProgressDialog(this);
        StringRequest request=new StringRequest(Request.Method.POST, WebConstants.profileImage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
              //  Infrastructure.dismissProgressDialog();
                //profilePicture.setImageResource(R.drawable.person_24);
                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                profileDetails();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Infrastructure.dismissProgressDialog();
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> map=new HashMap<String, String>();
                map.put("t1",name);
                map.put("upload",encodeImageString);
                return map;
            }
        };


        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
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