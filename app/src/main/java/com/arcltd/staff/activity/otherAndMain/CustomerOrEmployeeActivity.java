package com.arcltd.staff.activity.otherAndMain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.activity.without_login.WithoutLoginActivity;
import com.arcltd.staff.authentication.LoginActivity;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.utility.Constants;

public class CustomerOrEmployeeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_customer_or_employee);

        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);
    }

    public void Customer(View view) {
        RetrofitClient.saveUserPreference(CustomerOrEmployeeActivity.this, Constants.CUSTOMER_LOGIN, "Customer");
        startActivity(new Intent(this, WithoutLoginActivity.class));
    }

    public void Employees(View view) {
        RetrofitClient.saveUserPreference(CustomerOrEmployeeActivity.this, Constants.CUSTOMER_LOGIN, "Employee");

        startActivity(new Intent(this, LoginActivity.class));

    }
}