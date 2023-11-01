package com.arcltd.staff.activity.addData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.AddFaildVehicleResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.DatePickerHelper;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class AddVehiclePlaceFaildActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    TextInputEditText etDate,etVehicleCost,etCustomerRate,etRemark;
    Spinner spFaildBy;
    AutoCompleteTextView actvpeVehicle;
    ArrayList<String> vehiclefaid_issue = new ArrayList<>(Arrays.asList("Select Reason",
            "Failed By Trafic", "Failed By Customer","Failed Due To Date Change"));
    String failType;
    String vehicleType[] = {
            "TATA ACE 1 MT",
            "Tata 407 2.5 MT",
            "Tata - 12 Feet  3 MT",
            "Canter - 14 Feet 3.5 MT",
            "LPT - 17 feet  5 MT",
            "1109 -  19 Feet 7 MT",
            "LP Truck - 18 Feet 9 MT",
            "Taurus - 22 Feet  15 MT",
            "Taurus - 24-25 Feet 20 MT",
            "Taurus - 25-26 Feet 25  M.T.",
            "20' Container 6.5 MT",
            "22' Container 6.5 MT",
            "24' Container 8 MT",
            "28' Container 8 MT",
            "32' Container S. AXL- 7 MT",
            "32` Container M. AXL- 14 MT",
            "32' Container HQ- 7 MT",
            "34' Container 7 MT",
            "40' Container 15 MT",
            "Platfarm Truck 20 feet- 9 MT",
            "Platfarm Truck 22 feet- 8 to 15 MT",
            "Platfarm Half Body JCB Truck 28 FT.- 8 MT",
            "40 Feet High Bed Trailer- 20 to 35 MT",
            "50 Feet Trailer Semi Trailor-20 to 35 MT",
            "40 Feet Semi Trailer-20 to 35 MT",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_addvehicle_place_faild);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        etDate=findViewById(R.id.etDate);
        etVehicleCost=findViewById(R.id.etVehicleCost);
        etCustomerRate=findViewById(R.id.etCustomerRate);
        etRemark=findViewById(R.id.etRemark);
        spFaildBy=findViewById(R.id.spFaildBy);
        actvpeVehicle=findViewById(R.id.actvpeVehicle);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vehicleType);

        actvpeVehicle.setAdapter(adapter);
        actvpeVehicle.setOnItemClickListener(AddVehiclePlaceFaildActivity.this);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showetInsuranceuptpDatePickerDialog();
            }
        });



        ArrayAdapter<String> cast = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, vehiclefaid_issue);
        cast.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spFaildBy.setAdapter(cast);

        spFaildBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    failType = vehiclefaid_issue.get(position);
                    Log.e("", "onItemClick: " + position);
                    /*if (cookAvailable.equals("YES")){
                        coolSalary.setVisibility(View.VISIBLE);
                    }else {
                        coolSalary.setVisibility(View.GONE);
                    }*/


                } catch (Exception e) {
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }
    private void showetInsuranceuptpDatePickerDialog() {
        DatePickerHelper.showDatePicker(this, new DatePickerHelper.OnDateSelectedListener() {
            @Override
            public void onDateSelected(String selectedDateString) {

                etDate.setText(selectedDateString);
            }
        }, true);
    }

    public void onFaildVehicle(View view) {
        if (validate()) {
            submit(
                    etDate.getText().toString().trim(),
                    actvpeVehicle.getText().toString().trim(),
                    etVehicleCost.getText().toString().trim(),
                    etCustomerRate.getText().toString().trim(),
                    etRemark.getText().toString().trim());

        }
    }

    private void submit(String etDate, String actvpeVehicle, String etVehicleCost, String etCustomerRate, String etRemark) {

        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.addFaild(disposable, Constants.ApiRequestCode.VEHICLE_FAILD,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.REGION_ID),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.DIVISION_ID),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.EMP_CODE),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.FIRSTNAME),
                        actvpeVehicle,etDate,etRemark,failType,etVehicleCost,etCustomerRate);
                Log.e("login_Rquest", "login_Rquest: " + apiPresenter);
            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("Exception", "Exception in getDataFromWebservice", e);
        }
    }


    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult: " + object);
        Infrastructure.dismissProgressDialog();

        if (callAPIId == Constants.ApiRequestCode.VEHICLE_FAILD) {
            AddFaildVehicleResponse addFaildVehicleResponse = (AddFaildVehicleResponse) object;
            addFaildvehi(addFaildVehicleResponse);
        }
    }


    private void addFaildvehi(AddFaildVehicleResponse addFaildVehicleResponse) {
        try {
            Log.e(TAG, "addFaildVehicleResponse: " + new GsonBuilder().create().toJson(addFaildVehicleResponse));
            if (addFaildVehicleResponse != null) {
                if (addFaildVehicleResponse.getMessage()!=null) {
                    Infrastructure.dismissProgressDialog();
                    Toast.makeText(this, addFaildVehicleResponse.getMessage(), Toast.LENGTH_LONG).show();
                    finish();

                }else {
                    Toast.makeText(this, addFaildVehicleResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in addFaildVehicleResponse" + addFaildVehicleResponse.getResponseCode());
                Toast.makeText(this, addFaildVehicleResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in addFaildVehicleResponse", e);
        }


    }

    private boolean validate() {
        if (TextUtils.isEmpty(etDate.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please Select Date!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etVehicleCost.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Vehicle Cost!", Toast.LENGTH_SHORT).show();
            return false;
        }if (TextUtils.isEmpty(etCustomerRate.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Customer Rate!", Toast.LENGTH_SHORT).show();
            return false;
        }if (TextUtils.isEmpty(actvpeVehicle.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Vehicle Type!", Toast.LENGTH_SHORT).show();
            return false;
        }if (TextUtils.isEmpty(etRemark.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please Write Something In Remark!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spFaildBy.getSelectedItem().toString().trim().equals("Select Reason")) {
            Toast.makeText(getApplicationContext(), "Select Reason", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        String item = parent.getItemAtPosition(position).toString();

        // create Toast with user selected value
        Toast.makeText(this, "Selected Item is: \t" + item, Toast.LENGTH_LONG).show();

        // set user selected value to the TextView
        actvpeVehicle.setText(item);
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