package com.arcltd.staff.activity.attendance;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.arcltd.staff.R;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.AddMarketingVisitResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddMarketingVisitActivity extends BaseActivity {

    private static final String TAG = "AddMarketingVisit";
    private static final int REQUEST_LOCATION_PERMISSION = 5001;
    private static final int REQUEST_CAMERA_PERMISSION = 5002;
    private static final int REQUEST_CAMERA_PHOTO = 5003;

    private EditText etCompanyName, etPurpose, etLocationName;
    private TextView tvGpsCoords;
    private Button btnGetGps, btnSelectPhoto, btnSubmitVisit;
    private ImageView ivVisitPhoto;

    private FusedLocationProviderClient fusedLocationClient;
    private double currentLat = 0.0, currentLng = 0.0;
    private File selectedImageFile = null;

    private String empCode, empName, regionId, divisionId, branchCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marketing_visit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Record Marketing Visit");
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        empCode = RetrofitClient.getStringUserPreference(this, Constants.EMP_CODE);
        empName = RetrofitClient.getStringUserPreference(this, Constants.FIRSTNAME);
        regionId = RetrofitClient.getStringUserPreference(this, Constants.REGION_ID);
        divisionId = RetrofitClient.getStringUserPreference(this, Constants.DIVISION_ID);
        branchCode = RetrofitClient.getStringUserPreference(this, Constants.BRANCH_CODE);

        initUI();
        captureGpsLocation();
    }

    private void initUI() {
        etCompanyName = findViewById(R.id.et_company_name);
        etPurpose = findViewById(R.id.et_purpose);
        etLocationName = findViewById(R.id.et_location_name);
        tvGpsCoords = findViewById(R.id.tv_gps_coords);

        btnGetGps = findViewById(R.id.btn_get_gps);
        btnSelectPhoto = findViewById(R.id.btn_select_photo);
        btnSubmitVisit = findViewById(R.id.btn_submit_visit);
        ivVisitPhoto = findViewById(R.id.iv_visit_photo);

        btnGetGps.setOnClickListener(v -> captureGpsLocation());
        btnSelectPhoto.setOnClickListener(v -> openCameraOnly());
        btnSubmitVisit.setOnClickListener(v -> submitMarketingVisit());
    }

    private void captureGpsLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLat = location.getLatitude();
                    currentLng = location.getLongitude();
                    tvGpsCoords.setText(String.format(Locale.getDefault(), "GPS: Lat: %.6f, Lng: %.6f", currentLat, currentLng));

                    // Attempt reverse geocoding if location name field is empty
                    if (TextUtils.isEmpty(etLocationName.getText().toString().trim())) {
                        try {
                            Geocoder geocoder = new Geocoder(AddMarketingVisitActivity.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(currentLat, currentLng, 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                String addr = addresses.get(0).getAddressLine(0);
                                if (!TextUtils.isEmpty(addr)) {
                                    etLocationName.setText(addr);
                                }
                            }
                        } catch (Exception ignored) {}
                    }
                    Toast.makeText(AddMarketingVisitActivity.this, "GPS Location Captured!", Toast.LENGTH_SHORT).show();
                } else {
                    tvGpsCoords.setText("GPS: Unable to acquire location.");
                }
            }
        });
    }

    private void openCameraOnly() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return;
        }

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePhotoIntent, REQUEST_CAMERA_PHOTO);
        } catch (Exception e) {
            Toast.makeText(this, "No Camera app available on device.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA_PHOTO && resultCode == RESULT_OK) {
            try {
                Bitmap bitmap = null;
                if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                } else if (data != null && data.getData() != null) {
                    Uri imageUri = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                }

                if (bitmap != null) {
                    ivVisitPhoto.setImageBitmap(bitmap);

                    // Save to temporary compressed file for uploading
                    File cachePath = new File(getCacheDir(), "visit_images");
                    if (!cachePath.exists()) {
                        cachePath.mkdirs();
                    }
                    selectedImageFile = new File(cachePath, "visit_" + System.currentTimeMillis() + ".jpg");
                    FileOutputStream stream = new FileOutputStream(selectedImageFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);
                    stream.close();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error processing camera photo", e);
                Toast.makeText(this, "Error capturing photo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void submitMarketingVisit() {
        String companyName = etCompanyName.getText().toString().trim();
        String purpose = etPurpose.getText().toString().trim();
        String locationName = etLocationName.getText().toString().trim();

        if (TextUtils.isEmpty(companyName)) {
            etCompanyName.setError("Company name is required!");
            etCompanyName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(purpose)) {
            etPurpose.setError("Purpose of visit is required!");
            etPurpose.requestFocus();
            return;
        }

        if (selectedImageFile == null || !selectedImageFile.exists()) {
            Toast.makeText(this, "Please capture or select a visit photo!", Toast.LENGTH_LONG).show();
            return;
        }

        if (!Infrastructure.isInternetPresent()) {
            Toast.makeText(this, "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }

        Infrastructure.showProgressDialog(this);

        RequestBody reqEmpCode = createPartFromString(empCode);
        RequestBody reqEmpName = createPartFromString(empName);
        RequestBody reqRegionId = createPartFromString(regionId);
        RequestBody reqDivisionId = createPartFromString(divisionId);
        RequestBody reqBranchCode = createPartFromString(branchCode);
        RequestBody reqCompanyName = createPartFromString(companyName);
        RequestBody reqPurpose = createPartFromString(purpose);
        RequestBody reqLocationName = createPartFromString(locationName);
        RequestBody reqLat = createPartFromString(String.valueOf(currentLat));
        RequestBody reqLng = createPartFromString(String.valueOf(currentLng));

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/jpeg"), selectedImageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("visit_image", selectedImageFile.getName(), fileReqBody);

        apiPresenter.addMarketingVisit(
                disposable,
                Constants.ApiRequestCode.ADD_MARKETING_VISIT,
                reqEmpCode,
                reqEmpName,
                reqRegionId,
                reqDivisionId,
                reqBranchCode,
                reqCompanyName,
                reqPurpose,
                reqLocationName,
                reqLat,
                reqLng,
                imagePart
        );
    }

    private RequestBody createPartFromString(String val) {
        return RequestBody.create(MediaType.parse("text/plain"), !TextUtils.isEmpty(val) ? val : "");
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        Infrastructure.dismissProgressDialog();

        if (callAPIId == Constants.ApiRequestCode.ADD_MARKETING_VISIT) {
            if (object instanceof AddMarketingVisitResponse) {
                AddMarketingVisitResponse response = (AddMarketingVisitResponse) object;
                if ("200".equals(response.getResponseCode())) {
                    Toast.makeText(this, response.getMessage() != null ? response.getMessage() : "Visit recorded successfully!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, response.getMessage() != null ? response.getMessage() : "Failed to record visit", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        Infrastructure.dismissProgressDialog();
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            captureGpsLocation();
        } else if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraOnly();
            } else {
                Toast.makeText(this, "Camera permission is required to capture visit photo.", Toast.LENGTH_SHORT).show();
            }
        }
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
