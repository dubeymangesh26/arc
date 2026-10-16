package com.arcltd.staff.activity.attendance;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.arcltd.staff.R;
import com.arcltd.staff.base.BaseActivity;

public class MarketingVisitDetailsActivity extends BaseActivity {

    private TextView tvCompanyName, tvVisitDateTime, tvEmpInfo, tvBranchInfo, tvPurpose, tvLocation;
    private ImageView ivVisitImage;
    private Button btnCloseDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketing_visit_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Visit Details");
        }

        initUI();
        displayVisitDetails();
    }

    private void initUI() {
        tvCompanyName = findViewById(R.id.tv_company_name);
        tvVisitDateTime = findViewById(R.id.tv_visit_datetime);
        tvEmpInfo = findViewById(R.id.tv_emp_info);
        tvBranchInfo = findViewById(R.id.tv_branch_info);
        tvPurpose = findViewById(R.id.tv_purpose);
        tvLocation = findViewById(R.id.tv_location);
        ivVisitImage = findViewById(R.id.iv_visit_image);
        btnCloseDetails = findViewById(R.id.btn_close_details);

        ImageView btnCloseTop = findViewById(R.id.btn_close_top);
        if (btnCloseTop != null) {
            btnCloseTop.setOnClickListener(v -> finish());
        }

        if (btnCloseDetails != null) {
            btnCloseDetails.setOnClickListener(v -> finish());
        }
    }

    private void displayVisitDetails() {
        String companyName = getIntent().getStringExtra("company_name");
        String empName = getIntent().getStringExtra("emp_name");
        String empCode = getIntent().getStringExtra("emp_code");
        String branchCode = getIntent().getStringExtra("branch_code");
        String purpose = getIntent().getStringExtra("purpose");
        String locationName = getIntent().getStringExtra("location_name");
        String lat = getIntent().getStringExtra("latitude");
        String lng = getIntent().getStringExtra("longitude");
        String visitDate = getIntent().getStringExtra("visit_date");
        String visitTime = getIntent().getStringExtra("visit_time");
        String imageUrl = getIntent().getStringExtra("image_url");

        tvCompanyName.setText(!TextUtils.isEmpty(companyName) ? companyName : "Company");

        String dateStr = !TextUtils.isEmpty(visitDate) ? visitDate : "";
        String timeStr = !TextUtils.isEmpty(visitTime) ? visitTime : "";
        tvVisitDateTime.setText(String.format("Date: %s %s", dateStr, timeStr).trim());

        String nameStr = !TextUtils.isEmpty(empName) ? empName : "Employee";
        String codeStr = !TextUtils.isEmpty(empCode) ? empCode : "";
        tvEmpInfo.setText(String.format("%s (%s)", nameStr, codeStr));

        tvBranchInfo.setText(String.format("Branch Code: %s", !TextUtils.isEmpty(branchCode) ? branchCode : "N/A"));
        tvPurpose.setText(!TextUtils.isEmpty(purpose) ? purpose : "N/A");

        String locStr = !TextUtils.isEmpty(locationName) ? locationName : "N/A";
        String gpsStr = "";
        if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lng)) {
            gpsStr = String.format("\nGPS: Lat %s, Lng %s", lat, lng);
        }
        tvLocation.setText(String.format("📍 %s%s", locStr, gpsStr));

        if (!TextUtils.isEmpty(imageUrl)) {
            ivVisitImage.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.person_24)
                    .error(R.drawable.avatar_10)
                    .into(ivVisitImage);
        } else {
            ivVisitImage.setVisibility(View.GONE);
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
