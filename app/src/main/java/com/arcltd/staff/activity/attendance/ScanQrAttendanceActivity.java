package com.arcltd.staff.activity.attendance;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.arcltd.staff.R;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.MarkAttendanceResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class ScanQrAttendanceActivity extends BaseActivity {

    private static final String TAG = "ScanQrAttendance";
    private static final int REQUEST_PERMISSIONS = 4001;

    private CompoundBarcodeView barcodeView;
    private TextView tvEmpInfo, tvGpsStatus;
    private ProgressBar pbScanning;

    private FusedLocationProviderClient fusedLocationClient;
    private String myEmpCode, myEmpName, myBranchCode;
    private double currentLat = 0.0;
    private double currentLng = 0.0;
    private boolean isProcessingScan = false;

    private final BarcodeCallback barcodeCallback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result != null && result.getText() != null && !isProcessingScan) {
                isProcessingScan = true;
                barcodeView.pause();
                String scannedQrCode = result.getText().trim();
                Log.d(TAG, "Scanned QR: " + scannedQrCode);
                processAttendanceScan(scannedQrCode);
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_attendance);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Scan Office QR Attendance");
        }

        myEmpCode = RetrofitClient.getStringUserPreference(this, Constants.EMP_CODE);
        if (TextUtils.isEmpty(myEmpCode)) {
            myEmpCode = RetrofitClient.getStringUserPreference(this, Constants.ID);
        }
        myEmpName = RetrofitClient.getStringUserPreference(this, Constants.FIRSTNAME);
        myBranchCode = RetrofitClient.getStringUserPreference(this, Constants.BRANCH_CODE);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initUI();
        checkPermissionsAndStartCamera();
    }

    private void initUI() {
        barcodeView = findViewById(R.id.barcode_scanner);
        tvEmpInfo = findViewById(R.id.tv_emp_info);
        tvGpsStatus = findViewById(R.id.tv_gps_status);
        pbScanning = findViewById(R.id.pb_scanning);

        String branchInfo = !TextUtils.isEmpty(myBranchCode) ? " | Branch: " + myBranchCode : "";
        tvEmpInfo.setText(String.format("Employee: %s (%s)%s", !TextUtils.isEmpty(myEmpName) ? myEmpName : "Staff", !TextUtils.isEmpty(myEmpCode) ? myEmpCode : "N/A", branchInfo));
        barcodeView.decodeContinuous(barcodeCallback);
    }

    private void checkPermissionsAndStartCamera() {
        boolean cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!cameraPermission || !locationPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS);
        } else {
            fetchLocationAndResumeCamera();
        }
    }

    private void fetchLocationAndResumeCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currentLat = location.getLatitude();
                        currentLng = location.getLongitude();
                        tvGpsStatus.setText(String.format(Locale.getDefault(), "GPS: Lat: %.6f, Lng: %.6f (Accuracy: %.1fm)", currentLat, currentLng, location.getAccuracy()));
                    } else {
                        tvGpsStatus.setText("GPS: Unable to lock location. Turn on Location.");
                    }
                }
            });
        }
        barcodeView.resume();
    }

    private void processAttendanceScan(String scannedQrCode) {
        if (currentLat == 0.0 || currentLng == 0.0) {
            Toast.makeText(this, "Fetching GPS location... Please try again in a moment.", Toast.LENGTH_LONG).show();
            isProcessingScan = false;
            barcodeView.resume();
            return;
        }

        // Client-Side Pre-Validation: Extract scanned branch code & check if it matches employee's assigned branch
        String scannedBranchCode = extractBranchCodeFromQr(scannedQrCode);
        if (!TextUtils.isEmpty(myBranchCode) && !TextUtils.isEmpty(scannedBranchCode)) {
            if (!isSameBranch(myBranchCode, scannedBranchCode)) {
                new AlertDialog.Builder(this)
                        .setTitle("❌ Wrong Branch QR Code")
                        .setMessage(String.format("Attendance Restricted!\n\nYou are assigned to Branch '%s', but scanned the QR code for Branch '%s'.\n\nPlease scan your assigned branch office QR code.", myBranchCode, scannedBranchCode))
                        .setPositiveButton("Try Again", (dialog, which) -> {
                            isProcessingScan = false;
                            barcodeView.resume();
                        })
                        .setCancelable(false)
                        .show();
                return;
            }
        }

        if (Infrastructure.isInternetPresent()) {
            pbScanning.setVisibility(View.VISIBLE);
            apiPresenter.markQrAttendance(
                    disposable,
                    Constants.ApiRequestCode.MARK_ATTENDANCE,
                    myEmpCode,
                    scannedQrCode,
                    String.valueOf(currentLat),
                    String.valueOf(currentLng)
            );
        } else {
            Toast.makeText(this, "No internet connection.", Toast.LENGTH_SHORT).show();
            isProcessingScan = false;
            barcodeView.resume();
        }
    }

    private boolean isSameBranch(String myBranch, String scannedBranch) {
        if (TextUtils.isEmpty(myBranch) || TextUtils.isEmpty(scannedBranch)) return true;

        String cleanMy = extractBranchCodeFromQr(myBranch);
        String cleanScanned = extractBranchCodeFromQr(scannedBranch);

        if (cleanMy.equalsIgnoreCase(cleanScanned)) return true;
        if (cleanMy.contains(cleanScanned) || cleanScanned.contains(cleanMy)) return true;

        return false;
    }

    private String extractBranchCodeFromQr(String inputQr) {
        if (TextUtils.isEmpty(inputQr)) return "";
        String qr = inputQr.trim();

        try {
            if (qr.startsWith("{") && qr.endsWith("}")) {
                JSONObject obj = new JSONObject(qr);
                if (obj.has("branch_code")) {
                    return obj.getString("branch_code").trim().toUpperCase();
                }
            }
        } catch (Exception ignored) {}

        if (qr.contains("ARC_BRANCH_")) {
            String[] parts = qr.split("_");
            for (String part : parts) {
                if (!part.equalsIgnoreCase("ARC") && !part.equalsIgnoreCase("BRANCH") && part.length() >= 2 && part.length() <= 6) {
                    return part.toUpperCase();
                }
            }
        }
        if (qr.contains("ARC_OFFICE_QR_")) {
            return qr.replace("ARC_OFFICE_QR_", "").trim().toUpperCase();
        }
        if (qr.contains("BRANCH_")) {
            return qr.replace("BRANCH_", "").trim().toUpperCase();
        }
        return qr.toUpperCase();
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        pbScanning.setVisibility(View.GONE);

        if (callAPIId == Constants.ApiRequestCode.MARK_ATTENDANCE) {
            if (object instanceof MarkAttendanceResponse) {
                MarkAttendanceResponse response = (MarkAttendanceResponse) object;
                if ("200".equals(response.getResponseCode())) {
                    String title = "TIME_IN".equalsIgnoreCase(response.getAttendanceType()) ? "✅ TIME IN MARKED" : "✅ TIME OUT MARKED";
                    String msg = String.format(Locale.getDefault(),
                            "%s\n\nBranch: %s\nDate: %s\nTime In: %s\nTime Out: %s\n\nOffice Distance: %.1f meters (Within %.0fm geofence)",
                            response.getMessage(),
                            !TextUtils.isEmpty(response.getBranchCode()) ? response.getBranchCode() : myBranchCode,
                            response.getAttendanceDate() != null ? response.getAttendanceDate() : "",
                            response.getTimeIn() != null ? response.getTimeIn() : "-",
                            response.getTimeOut() != null ? response.getTimeOut() : "-",
                            response.getDistanceMeters(),
                            response.getAllowedRadiusMeters());

                    new AlertDialog.Builder(this)
                            .setTitle(title)
                            .setMessage(msg)
                            .setPositiveButton("OK", (dialog, which) -> finish())
                            .setCancelable(false)
                            .show();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("❌ Attendance Restricted")
                            .setMessage(response.getMessage())
                            .setPositiveButton("Try Again", (dialog, which) -> {
                                isProcessingScan = false;
                                barcodeView.resume();
                            })
                            .setCancelable(false)
                            .show();
                }
            }
        }
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        pbScanning.setVisibility(View.GONE);
        new AlertDialog.Builder(this)
                .setTitle("Attendance Restricted")
                .setMessage(!TextUtils.isEmpty(message) ? message : "Error marking attendance")
                .setPositiveButton("Try Again", (dialog, which) -> {
                    isProcessingScan = false;
                    barcodeView.resume();
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            fetchLocationAndResumeCamera();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
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
