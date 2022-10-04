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
import com.arcltd.staff.response.AddUpdateMoterCycleResponse;
import com.arcltd.staff.response.MoterCycleListResponse;
import com.arcltd.staff.utility.DatePickerHelper;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class AddMoterCycleActivity extends BaseActivity {
       TextInputEditText etPUCtpto,etInsuranceuptp,etVehicleNO,etRegistrationExp,etMake,etModel,
               etStanderdKm, etInsuranceNo,etUsername;
       String Vehicle_id="",data;
       int vehicle_saleDate;
    MoterCycleListResponse.MoterCycle dataBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_add_moter_cycle);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        etVehicleNO =findViewById(R.id.etVehicleNO);
        etRegistrationExp =findViewById(R.id.etRegistrationExp);
        etMake =findViewById(R.id.etMake);
        etModel =findViewById(R.id.etModel);
        etPUCtpto =findViewById(R.id.etPUCtpto);
        etInsuranceuptp =findViewById(R.id.etInsuranceuptp);
        etStanderdKm =findViewById(R.id.etStanderdKm);
        etInsuranceNo =findViewById(R.id.etInsuranceNo);
        etUsername =findViewById(R.id.etUseVehiclename);
      //  setDate fromDate = new setDate(etPUCtpto, this);


        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("data");
            dataBean = new Gson().fromJson(data, MoterCycleListResponse.MoterCycle.class);
            Log.e("", "onCreate: " + new GsonBuilder().create().toJson(dataBean));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }

        try {
            etVehicleNO.setText(dataBean.getVehicle_no());
            etRegistrationExp.setText(dataBean.getRegist_no());
            etMake.setText(dataBean.getMake());
            etModel.setText(dataBean.getModel());
            etPUCtpto.setText(dataBean.getPuc_upto());
            etInsuranceuptp.setText(dataBean.getIns_upto());
            etStanderdKm.setText(dataBean.getStanderd_km());
            etInsuranceNo.setText(dataBean.getInsurance_no());
            etUsername.setText(dataBean.getUser_person());
            Vehicle_id=dataBean.getV_id();

        } catch (Exception e) {
            Log.e("", "ListingDetail error: " + e);
        }

        etRegistrationExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegistartionExpDatePickerDialog();
            }
        });

        etPUCtpto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        etInsuranceuptp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showetInsuranceuptpDatePickerDialog();
            }
        });



    }

    private void showRegistartionExpDatePickerDialog() {
        DatePickerHelper.showDatePicker(this, new DatePickerHelper.OnDateSelectedListener() {
            @Override
            public void onDateSelected(String selectedDateString) {
                etRegistrationExp.setText(selectedDateString);
            }
        },true);

    }


    private void showetInsuranceuptpDatePickerDialog() {
        DatePickerHelper.showDatePicker(this, new DatePickerHelper.OnDateSelectedListener() {
            @Override
            public void onDateSelected(String selectedDateString) {
                etInsuranceuptp.setText(selectedDateString);
            }
        },true);

    }

    private void showDatePickerDialog() {
        DatePickerHelper.showDatePicker(this, new DatePickerHelper.OnDateSelectedListener() {
            @Override
            public void onDateSelected(String selectedDateString) {
                etPUCtpto.setText(selectedDateString);
            }
        },true);
    }

    public void submitMoterCycle(View view) {
        int model= Integer.parseInt(etModel.getText().toString());
        vehicle_saleDate=model+15;
        if (validate()) {
            addMoterCycle(
                    etVehicleNO.getText().toString().trim(),
                    etRegistrationExp.getText().toString().trim(),
                    etMake.getText().toString().trim(),
                    etModel.getText().toString().trim(),
                    etInsuranceuptp.getText().toString().trim(),
                    etPUCtpto.getText().toString().trim(),
                    etStanderdKm.getText().toString().trim(),
                    etInsuranceNo.getText().toString().trim(),
                    etUsername.getText().toString().trim());

        }

    }

    private void addMoterCycle(String etVehicleNO, String etRegistrationExp,
                               String etMake, String etModel, String etInsuranceuptp, String etPUCtpto,
                               String etStanderdKm,String etInsuranceNo, String etUsername) {

        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.addMoterCycle(disposable, Constants.ApiRequestCode.MOTER_CYCLE,Vehicle_id,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.REGION_ID),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.DIVISION_ID),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.DIVISION_NAME),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_NAME),
                        etVehicleNO,etVehicleNO,etRegistrationExp,etMake,etModel, String.valueOf(vehicle_saleDate)
                        ,etInsuranceuptp,etPUCtpto,
                        etStanderdKm,etInsuranceNo,etUsername);

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
        if (callAPIId == Constants.ApiRequestCode.MOTER_CYCLE) {
            AddUpdateMoterCycleResponse addUpdateMoterCycleResponse = (AddUpdateMoterCycleResponse) object;
            submitdata(addUpdateMoterCycleResponse);
        }
    }

    private void submitdata(AddUpdateMoterCycleResponse addUpdateMoterCycleResponse) {
        try {
            Log.e(TAG, "addUpdateMoterCycleResponse: " + new GsonBuilder().create().toJson(addUpdateMoterCycleResponse));
            if (addUpdateMoterCycleResponse != null) {
                if (addUpdateMoterCycleResponse.getResponseCode().equals(200)) {
                    Infrastructure.dismissProgressDialog();
                    finish();
                    Toast.makeText(this, addUpdateMoterCycleResponse.getMessage(), Toast.LENGTH_LONG).show();



                }else {
                    Toast.makeText(this, addUpdateMoterCycleResponse.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in addUpdateMoterCycleResponse" + addUpdateMoterCycleResponse.getResponseCode());
                Toast.makeText(this, addUpdateMoterCycleResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in addUpdateMoterCycleResponse", e);
        }
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
    }


    private boolean validate() {
        if (TextUtils.isEmpty(etVehicleNO.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter  Vehicle No.!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etRegistrationExp.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Registration No!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etMake.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Manufacturer Name!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etModel.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Model!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etInsuranceuptp.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Insurance Validity!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etPUCtpto.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter PUC Validity!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etStanderdKm.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter PUC Standard Km!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etInsuranceNo.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Insurance No.!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etUsername.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Moter cycle User Name!", Toast.LENGTH_SHORT).show();
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