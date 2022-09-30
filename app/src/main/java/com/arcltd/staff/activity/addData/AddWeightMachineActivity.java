package com.arcltd.staff.activity.addData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.otherAndMain.MainActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.AddUpdatWeightMachineResponse;
import com.arcltd.staff.response.WeightMachineListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.DatePickerHelper;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class AddWeightMachineActivity extends BaseActivity {
    TextInputEditText etRenueUpTo, etWetMcNo, etMake, etModel;
    String WeightMC_id = "", data;
    WeightMachineListResponse.WeightMachine dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ewight_machine);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        etRenueUpTo = findViewById(R.id.etRenueUpTo);
        etWetMcNo = findViewById(R.id.etWetMcNo);
        etMake = findViewById(R.id.etMake);
        etModel = findViewById(R.id.etModel);

        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("data");
            dataBean = new Gson().fromJson(data, WeightMachineListResponse.WeightMachine.class);
            Log.e("", "onCreate: " + new GsonBuilder().create().toJson(dataBean));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }

        try {
            etRenueUpTo.setText(dataBean.getRenew_upto());
            etWetMcNo.setText(dataBean.getWeightMc_No());
            etMake.setText(dataBean.getMake());
            etModel.setText(dataBean.getModel());
            WeightMC_id=dataBean.getW_id();
        } catch (Exception e) {
            Log.e("", "ListingDetail error: " + e);
        }


        etRenueUpTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showetInsuranceuptpDatePickerDialog();
            }
        });


    }

    private void showetInsuranceuptpDatePickerDialog() {
        DatePickerHelper.showDatePicker(this, new DatePickerHelper.OnDateSelectedListener() {
            @Override
            public void onDateSelected(String selectedDateString) {
                etRenueUpTo.setText(selectedDateString);
            }
        }, true);
    }

    public void submitWeight(View view) {
        if (validate()) {
            addWeightMachine();
        }
    }

    private void addWeightMachine() {

        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.addWeightMC(disposable, Constants.ApiRequestCode.WEIGHT_MACHINE, WeightMC_id,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE),
                        etWetMcNo.getText().toString(), etMake.getText().toString(), etModel.getText().toString(),
                        etRenueUpTo.getText().toString());

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
        if (callAPIId == Constants.ApiRequestCode.WEIGHT_MACHINE) {
            AddUpdatWeightMachineResponse addUpdatWeightMachineResponse = (AddUpdatWeightMachineResponse) object;
            submitdata(addUpdatWeightMachineResponse);
        }
    }

    private void submitdata(AddUpdatWeightMachineResponse addUpdatWeightMachineResponse) {
        try {
            Log.e(TAG, "addUpdatWeightMachineResponse: " + new GsonBuilder().create().toJson(addUpdatWeightMachineResponse));
            if (addUpdatWeightMachineResponse != null) {
                if (addUpdatWeightMachineResponse.getResponseCode().equals(200)) {
                    Infrastructure.dismissProgressDialog();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    Toast.makeText(this, addUpdatWeightMachineResponse.getMessage(), Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(this, addUpdatWeightMachineResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in addUpdatWeightMachineResponse" + addUpdatWeightMachineResponse.getResponseCode());
                Toast.makeText(this, addUpdatWeightMachineResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in addUpdatWeightMachineResponse", e);
        }
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
    }


    private boolean validate() {
        if (TextUtils.isEmpty(etWetMcNo.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter  Weight Machine No.!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(etRenueUpTo.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Renewal Date!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(etMake.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Manufacturer Name!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(etModel.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Model!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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