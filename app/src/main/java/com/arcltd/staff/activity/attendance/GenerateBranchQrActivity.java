package com.arcltd.staff.activity.attendance;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.arcltd.staff.R;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.BranchLocationResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

public class GenerateBranchQrActivity extends BaseActivity {

    private static final String TAG = "GenerateBranchQr";
    private static final int REQUEST_LOCATION_PERMISSION = 3001;

    private TextView tvBranchTitle, tvBranchCode, tvLocationStatus, tvQrKey;
    private EditText etRadius;
    private Button btnGetLocation, btnGenerateQr, btnShareQr;
    private CardView cvQrDisplay;
    private ImageView ivQrCode;

    private FusedLocationProviderClient fusedLocationClient;
    private String branchCode, branchName, loginType;
    private double currentLat = 0.0;
    private double currentLng = 0.0;
    private boolean isQrAlreadyExists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_branch_qr);

        loginType = RetrofitClient.getStringUserPreference(this, Constants.LOGIN_TYPE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Branch Office QR Code");
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        branchCode = getIntent().getStringExtra("branch_code");
        branchName = getIntent().getStringExtra("branch_name");

        if (TextUtils.isEmpty(branchCode)) {
            branchCode = RetrofitClient.getStringUserPreference(this, Constants.BRANCH_CODE);
            branchName = RetrofitClient.getStringUserPreference(this, Constants.BRANCH_NAME);
        }
        branchCode = cleanBranchCode(branchCode);

        initUI();
        fetchExistingBranchQr();
    }

    private String cleanBranchCode(String inputCode) {
        if (TextUtils.isEmpty(inputCode)) return "";
        String code = inputCode.trim();

        if (code.contains("ARC_BRANCH_")) {
            String[] parts = code.split("_");
            for (String part : parts) {
                if (!part.equalsIgnoreCase("ARC") && !part.equalsIgnoreCase("BRANCH") && part.length() >= 2 && part.length() <= 6) {
                    return part.toUpperCase();
                }
            }
        }
        if (code.contains("ARC_OFFICE_QR_")) {
            return code.replace("ARC_OFFICE_QR_", "").trim().toUpperCase();
        }
        if (code.contains("BRANCH_")) {
            return code.replace("BRANCH_", "").trim().toUpperCase();
        }
        return code.toUpperCase();
    }

    private void initUI() {
        tvBranchTitle = findViewById(R.id.tv_branch_title);
        tvBranchCode = findViewById(R.id.tv_branch_code);
        tvLocationStatus = findViewById(R.id.tv_location_status);
        tvQrKey = findViewById(R.id.tv_qr_key);

        etRadius = findViewById(R.id.et_radius);
        btnGetLocation = findViewById(R.id.btn_get_current_location);
        btnGenerateQr = findViewById(R.id.btn_generate_qr);
        btnShareQr = findViewById(R.id.btn_share_qr);
        Button btnManageAllQrs = findViewById(R.id.btn_manage_all_qrs);

        cvQrDisplay = findViewById(R.id.cv_qr_display);
        ivQrCode = findViewById(R.id.iv_qr_code);

        tvBranchTitle.setText(String.format("Branch: %s", !TextUtils.isEmpty(branchName) ? branchName : "Office"));
        tvBranchCode.setText(String.format("Branch Code: %s", !TextUtils.isEmpty(branchCode) ? branchCode : "N/A"));

        btnGetLocation.setOnClickListener(v -> captureCurrentGpsLocation());
        btnGenerateQr.setOnClickListener(v -> saveLocationAndGenerateQr());
        btnShareQr.setOnClickListener(v -> shareQrCode());

        if (btnManageAllQrs != null) {
            if ("RG".equalsIgnoreCase(loginType) || "DGM".equalsIgnoreCase(loginType) || "BM".equalsIgnoreCase(loginType)) {
                btnManageAllQrs.setVisibility(View.VISIBLE);
                btnManageAllQrs.setOnClickListener(v -> startActivity(new Intent(this, ManageBranchQrActivity.class)));
            } else {
                btnManageAllQrs.setVisibility(View.GONE);
            }
        }
    }

    private void fetchExistingBranchQr() {
        if (!TextUtils.isEmpty(branchCode) && Infrastructure.isInternetPresent()) {
            Infrastructure.showProgressDialog(this);
            apiPresenter.getBranchQr(disposable, Constants.ApiRequestCode.BRANCH_LOCATION, branchCode);
        }
    }

    private void captureCurrentGpsLocation() {
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
                    tvLocationStatus.setText(String.format(Locale.getDefault(), "GPS Captured: Lat: %.6f, Lng: %.6f", currentLat, currentLng));
                    Toast.makeText(GenerateBranchQrActivity.this, "Office location captured successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GenerateBranchQrActivity.this, "Cannot get GPS location. Please turn on Location/GPS.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveLocationAndGenerateQr() {
        if (isQrAlreadyExists && !"RG".equalsIgnoreCase(loginType) && !"DGM".equalsIgnoreCase(loginType) && !"BM".equalsIgnoreCase(loginType)) {
            Toast.makeText(this, "An office QR code already exists. Only RG, DGM, or BM can update an existing QR code.", Toast.LENGTH_LONG).show();
            return;
        }

        if (currentLat == 0.0 || currentLng == 0.0) {
            Toast.makeText(this, "Please click 'Capture Current Office Location' first.", Toast.LENGTH_LONG).show();
            return;
        }

        String radiusStr = etRadius.getText().toString().trim();
        int radius = 100;
        try {
            if (!TextUtils.isEmpty(radiusStr)) {
                radius = Integer.parseInt(radiusStr);
            }
        } catch (Exception ignored) {
        }

        if (Infrastructure.isInternetPresent()) {
            Infrastructure.showProgressDialog(this);
            apiPresenter.updateBranchLocation(
                    disposable,
                    Constants.ApiRequestCode.BRANCH_LOCATION,
                    branchCode,
                    String.valueOf(currentLat),
                    String.valueOf(currentLng),
                    radius,
                    loginType
            );
        } else {
            Toast.makeText(this, "No internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private void renderQrCodeBitmap(String qrKey) {
        if (TextUtils.isEmpty(qrKey)) return;

        // Clean QR payload so it encodes pure clean branch code (e.g. 'PNA')
        String cleanQrPayload = cleanBranchCode(qrKey);
        if (TextUtils.isEmpty(cleanQrPayload)) {
            cleanQrPayload = !TextUtils.isEmpty(branchCode) ? cleanBranchCode(branchCode) : "OFFICE";
        }

        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(cleanQrPayload, BarcodeFormat.QR_CODE, 600, 600);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            ivQrCode.setImageBitmap(bitmap);
            tvQrKey.setText(String.format("QR Payload: %s", cleanQrPayload));
            cvQrDisplay.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e(TAG, "QR generation error", e);
            Toast.makeText(this, "Error rendering QR bitmap", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareQrCode() {
        if (ivQrCode == null || ivQrCode.getDrawable() == null) {
            Toast.makeText(this, "No QR Code available to share.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            BitmapDrawable drawable = (BitmapDrawable) ivQrCode.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            File cachePath = new File(getCacheDir(), "images");
            if (!cachePath.exists()) {
                cachePath.mkdirs();
            }
            File imageFile = new File(cachePath, "branch_qr_" + (!TextUtils.isEmpty(branchCode) ? branchCode : "office") + ".png");
            FileOutputStream stream = new FileOutputStream(imageFile, false);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            Uri imageUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", imageFile);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Branch Office QR Code - " + (!TextUtils.isEmpty(branchName) ? branchName : "Office"));
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Office Attendance QR Code for Branch: " + (!TextUtils.isEmpty(branchName) ? branchName : "Office") + " (" + (!TextUtils.isEmpty(branchCode) ? branchCode : "N/A") + ")");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share Branch QR Code via"));

        } catch (Exception e) {
            Log.e(TAG, "Error sharing QR code", e);
            Toast.makeText(this, "Failed to share QR Code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        Infrastructure.dismissProgressDialog();

        if (callAPIId == Constants.ApiRequestCode.BRANCH_LOCATION) {
            if (object instanceof BranchLocationResponse) {
                BranchLocationResponse response = (BranchLocationResponse) object;
                if ("200".equals(response.getResponseCode())) {
                    if (!TextUtils.isEmpty(response.getLat()) && !TextUtils.isEmpty(response.getLng())) {
                        try {
                            currentLat = Double.parseDouble(response.getLat());
                            currentLng = Double.parseDouble(response.getLng());
                            tvLocationStatus.setText(String.format(Locale.getDefault(), "Office Location: Lat: %.6f, Lng: %.6f", currentLat, currentLng));
                        } catch (Exception ignored) {}
                    }
                    if (response.getAllowedRadiusMeters() > 0) {
                        etRadius.setText(String.valueOf(response.getAllowedRadiusMeters()));
                    }

                    if (!TextUtils.isEmpty(response.getBranchQrCode())) {
                        isQrAlreadyExists = true;
                        renderQrCodeBitmap(response.getBranchQrCode());

                        // If QR code ALREADY EXISTS, check if user is RG, DGM, or BM for updating permission
                        if ("RG".equalsIgnoreCase(loginType) || "DGM".equalsIgnoreCase(loginType) || "BM".equalsIgnoreCase(loginType)) {
                            btnGenerateQr.setText("✨ Update Office QR Code");
                            btnGenerateQr.setVisibility(View.VISIBLE);
                            btnGetLocation.setVisibility(View.VISIBLE);
                            etRadius.setEnabled(true);
                        } else {
                            btnGenerateQr.setVisibility(View.GONE);
                            btnGetLocation.setVisibility(View.GONE);
                            etRadius.setEnabled(false);
                        }
                    } else {
                        // QR code does NOT exist yet in DB -> ALLOW EVERYONE to generate & save for the first time
                        isQrAlreadyExists = false;
                        btnGenerateQr.setText("✨ Generate & Save Office QR Code");
                        btnGenerateQr.setVisibility(View.VISIBLE);
                        btnGetLocation.setVisibility(View.VISIBLE);
                        etRadius.setEnabled(true);
                    }
                } else {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        Infrastructure.dismissProgressDialog();
        if (!TextUtils.isEmpty(message)) {
            if (message.toLowerCase().contains("required") || message.toLowerCase().contains("not set")) {
                tvLocationStatus.setText("GPS Location: Not set yet. Click 'Capture Current Office Location' below.");
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if ("RG".equalsIgnoreCase(loginType) || "DGM".equalsIgnoreCase(loginType) || "BM".equalsIgnoreCase(loginType)) {
            menu.add(0, 1001, 0, "Manage All QRs")
                    .setIcon(android.R.drawable.ic_menu_manage)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == 1001) {
            startActivity(new Intent(this, ManageBranchQrActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
