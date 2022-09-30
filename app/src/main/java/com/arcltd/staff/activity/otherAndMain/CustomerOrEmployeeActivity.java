package com.arcltd.staff.activity.otherAndMain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.without_login.WithoutLoginActivity;
import com.arcltd.staff.authentication.LoginActivity;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.utility.Constants;

public class CustomerOrEmployeeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_or_employee);


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