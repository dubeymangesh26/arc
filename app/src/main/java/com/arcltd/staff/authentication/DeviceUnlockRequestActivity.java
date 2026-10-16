package com.arcltd.staff.authentication;

import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.response.GenericResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;

public class DeviceUnlockRequestActivity extends BaseActivity {

    private TextInputEditText etEmpCode;
    private Button btnSubmitRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_unlock_request);

        etEmpCode = findViewById(R.id.etEmpCode);
        btnSubmitRequest = findViewById(R.id.btnSubmitRequest);

        btnSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRequest();
            }
        });
    }

    private void submitRequest() {
        String empCode = etEmpCode.getText().toString().trim();
        if (TextUtils.isEmpty(empCode)) {
            Toast.makeText(this, "Please enter your Employee Code", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Infrastructure.isInternetPresent()) {
            Infrastructure.showProgressDialog(this);
            String deviceKey = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            apiPresenter.requestDeviceUnlock(disposable, Constants.ApiRequestCode.REQUEST_DEVICE_UNLOCK, empCode, deviceKey);
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        super.showResult(object, callAPIId);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.REQUEST_DEVICE_UNLOCK) {
            if (object instanceof GenericResponse) {
                GenericResponse response = (GenericResponse) object;
                if ("200".equals(response.getResponsecode())) {
                    Toast.makeText(this, "Unlock request submitted successfully.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}