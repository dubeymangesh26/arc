package com.arcltd.staff.authentication;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.activity.updateData.UpdateProfileActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.WebConstants;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.ProfileResponseResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDetailsActivity extends BaseActivity {
    TextView etBranchCode, etEmployeeId, etEmpName, etDisign, etEmailId, etMobile, etEduction,
            etJoiningDate, etSalary, etRetirement, etESI, etUan, tvFleet, tvBasic, tvHra, tvTga, tvPFNo;
    LinearLayout liEsi;
    String data, emp_code, profileImage;
    CircleImageView profilePicture;
    Bitmap bitmap;
    String encodeImageString;

    // Direct Photo Picker contract launcher
    private ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_profile_details);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        etBranchCode = findViewById(R.id.etBranchCode);
        etEmployeeId = findViewById(R.id.etEmployeeId);
        etEmpName = findViewById(R.id.etEmpName);
        etDisign = findViewById(R.id.etDisign);
        etEmailId = findViewById(R.id.etEmailId);
        etMobile = findViewById(R.id.etMobile);
        etEduction = findViewById(R.id.etEduction);
        etJoiningDate = findViewById(R.id.etJoiningDate);
        etSalary = findViewById(R.id.etSalary);
        etRetirement = findViewById(R.id.etRetirement);
        etESI = findViewById(R.id.etESI);
        etUan = findViewById(R.id.etUan);
        profilePicture = findViewById(R.id.profilePicture);
        tvFleet = findViewById(R.id.tvFleet);
        tvBasic = findViewById(R.id.tvBasic);
        tvHra = findViewById(R.id.tvHra);
        tvTga = findViewById(R.id.tvTga);
        tvPFNo = findViewById(R.id.tvPFNo);
        liEsi = findViewById(R.id.liEsi);
        emp_code = RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.EMP_CODE);

        // Register modern Photo Picker
        pickMediaLauncher = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            profilePicture.setImageBitmap(bitmap);
                            encodeBitmapImage(bitmap);
                            uploaddatatodb();
                        } catch (Exception ex) {
                            Log.e(TAG, "Failed to load selected image: ", ex);
                            Toast.makeText(ProfileDetailsActivity.this, "Failed to read image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        profileDetails();
        profilePicture.setOnClickListener(v -> getPicturefromGallery());

        String savedProfilePic = RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.PROFILE_PICTURE);
        if (savedProfilePic != null && !savedProfilePic.isEmpty() && !savedProfilePic.startsWith("http")) {
            String baseUrl = WebConstants.baseURL;
            if (baseUrl.endsWith("/") && savedProfilePic.startsWith("/")) {
                savedProfilePic = baseUrl + savedProfilePic.substring(1);
            } else if (!baseUrl.endsWith("/") && !savedProfilePic.startsWith("/")) {
                savedProfilePic = baseUrl + "/" + savedProfilePic;
            } else {
                savedProfilePic = baseUrl + savedProfilePic;
            }
        }

        if (savedProfilePic != null && !savedProfilePic.isEmpty()) {
            Glide.with(this)
                    .load(savedProfilePic)
                    .signature(new ObjectKey(System.currentTimeMillis()))
                    .placeholder(R.drawable.person_24)
                    .error(R.drawable.avatar_10)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .override(200, 200)
                    .centerCrop()
                    .into(profilePicture);
        }
    }

    private void profileDetails() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.profile(disposable, Constants.ApiRequestCode.OFFICEMESSGDN,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.EMP_CODE));
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
        if (callAPIId == Constants.ApiRequestCode.TARGET) {
            String response = (String) object;
            getUpdatePictureList(response);
        }
        if (callAPIId == Constants.ApiRequestCode.OFFICEMESSGDN) {
            ProfileResponseResponse profileResponseResponse = (ProfileResponseResponse) object;
            getprofile(profileResponseResponse);
        }
    }

    private void getUpdatePictureList(String response) {
        if (response != null) {
            Toast.makeText(this, response, Toast.LENGTH_LONG).show();
            new Handler(Looper.getMainLooper()).postDelayed(this::profileDetails, 2000);
        }
    }

    private void getprofile(ProfileResponseResponse profileResponseResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(profileResponseResponse));
            if (profileResponseResponse != null) {
                Infrastructure.dismissProgressDialog();
                etBranchCode.setText(profileResponseResponse.getUser().getBranch_code());
                emp_code = profileResponseResponse.getUser().getEmp_code();
                profileImage = profileResponseResponse.getUser().getProfilepic();
                RetrofitClient.saveUserPreference(ProfileDetailsActivity.this, Constants.PROFILE_PICTURE,
                        profileResponseResponse.getUser().getProfilepic());

                String currentProfilePic = profileResponseResponse.getUser().getProfilepic();
                if (currentProfilePic != null && !currentProfilePic.isEmpty() && !currentProfilePic.startsWith("http")) {
                    String baseUrl = WebConstants.baseURL;
                    if (baseUrl.endsWith("/") && currentProfilePic.startsWith("/")) {
                        currentProfilePic = baseUrl + currentProfilePic.substring(1);
                    } else if (!baseUrl.endsWith("/") && !currentProfilePic.startsWith("/")) {
                        currentProfilePic = baseUrl + "/" + currentProfilePic;
                    } else {
                        currentProfilePic = baseUrl + currentProfilePic;
                    }
                }

                if (currentProfilePic != null && !currentProfilePic.isEmpty()) {
                    Glide.with(this)
                            .load(currentProfilePic)
                            .signature(new ObjectKey(System.currentTimeMillis()))
                            .placeholder(R.drawable.person_24)
                            .error(R.drawable.avatar_10)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .override(200, 200)
                            .centerCrop()
                            .into(profilePicture);
                }

                etEmployeeId.setText(profileResponseResponse.getUser().getEmp_code());
                etEmpName.setText(profileResponseResponse.getUser().getName());
                etDisign.setText(profileResponseResponse.getUser().getDesign());
                etEmailId.setText(profileResponseResponse.getUser().getEmail());
                etMobile.setText(profileResponseResponse.getUser().getContactno());
                etEduction.setText(profileResponseResponse.getEmployee_details().getEmp_eqalification());
                etJoiningDate.setText(profileResponseResponse.getEmployee_details().getEmp_joining_date());

                if ("Pending".equals(profileResponseResponse.getEmployee_details().getEsi_no())) {
                    liEsi.setVisibility(View.GONE);
                } else {
                    liEsi.setVisibility(View.VISIBLE);
                }
                etSalary.setText(profileResponseResponse.getEmployee_details().getEmp_salary());
                etRetirement.setText(profileResponseResponse.getEmployee_details().getRetirement_date());
                etESI.setText(profileResponseResponse.getEmployee_details().getEsi_no());
                etUan.setText(profileResponseResponse.getEmployee_details().getUan_no());
                data = new Gson().toJson(profileResponseResponse.getEmployee_details());
                tvFleet.setText(profileResponseResponse.getEmployee_details().getSal_fleet());
                tvBasic.setText(profileResponseResponse.getEmployee_details().getSal_basic());
                tvHra.setText(profileResponseResponse.getEmployee_details().getSal_hra());
                tvTga.setText(profileResponseResponse.getEmployee_details().getSal_tga());
                tvPFNo.setText(profileResponseResponse.getEmployee_details().getPf_number());
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
        startActivity(new Intent(this, UpdateProfileActivity.class).putExtra("data", data));
    }

    public void choosePicture(View view) {
        getPicturefromGallery();
    }

    private void getPicturefromGallery() {
        // Launches the standard picker directly without Dexter or runtime permissions
        pickMediaLauncher.launch(
                new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build()
        );
    }

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // Compressing to 80% to avoid OutOfMemoryError and base64 payload bloat
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
        encodeImageString = Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }

    private void uploaddatatodb() {
        Infrastructure.showProgressDialog(this);
        apiPresenter.updateProfilePicture(disposable, Constants.ApiRequestCode.TARGET, emp_code, encodeImageString);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}