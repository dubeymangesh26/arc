package com.arcltd.staff.activity.addData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
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
import com.arcltd.staff.activity.listData.BranchListActivity;
import com.arcltd.staff.activity.listData.PlacedVehicleListActivity;
import com.arcltd.staff.adapter.BranchLocalListAdapter;
import com.arcltd.staff.adapter.VehiclePlacedItemAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.AddFaildVehicleResponse;
import com.arcltd.staff.response.AddPlacedVehicleResponse;
import com.arcltd.staff.response.VehiclePlacedItemListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.DatePickerHelper;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddPlacedVehicleActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    TextInputEditText etDate,etDestination;
    Spinner spNoOfVehicle;
    AutoCompleteTextView actvpeVehicle;
    DateFormat yearofmonth,yeardae;
    String month,year;
    RecyclerView list;
    ArrayList<String> noOfVehicle_issue = new ArrayList<>(Arrays.asList("Select",
            "1", "2","3","4","5","6","7","8","9","10"));
    String noOfVehicle;
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
        setContentView(R.layout.activity_add_placed_vehicle);

        month= String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
        year= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        etDate=findViewById(R.id.etDate);
        etDestination=findViewById(R.id.etDestination);
        
        spNoOfVehicle=findViewById(R.id.spNoOfVehicle);
        actvpeVehicle=findViewById(R.id.actvpeVehicle);
        list=findViewById(R.id.list);

        String current_date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        etDate.setText(current_date);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vehicleType);

        actvpeVehicle.setAdapter(adapter);
        actvpeVehicle.setOnItemClickListener(AddPlacedVehicleActivity.this);


        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showetInsuranceuptpDatePickerDialog();
            }
        });



        ArrayAdapter<String> cast = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, noOfVehicle_issue);
        cast.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spNoOfVehicle.setAdapter(cast);

        spNoOfVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    noOfVehicle = noOfVehicle_issue.get(position);
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

        vehicleItemList();
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
                    etDestination.getText().toString().trim());

        }
    }


    private void submit(String etDate, String actvpeVehicle, String etDestination) {

        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.addVehicle(disposable, Constants.ApiRequestCode.VEHICLE_PLACED,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.REGION_NAME),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.DIVISION_NAME),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_NAME),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.EMP_CODE),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.FIRSTNAME),
                        actvpeVehicle,etDate,noOfVehicle,etDestination,month,year);
                Log.e("login_Rquest", "login_Rquest: " + apiPresenter);
            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("Exception", "Exception in getDataFromWebservice", e);
        }
    }

    private void vehicleItemList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.listVehicleitem(disposable, Constants.ApiRequestCode.VEHICLE_LIST_ITEM,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE),"");

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }



    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult_addPlacedVehicleResponse: " + object);
        Infrastructure.dismissProgressDialog();

        if (callAPIId == Constants.ApiRequestCode.VEHICLE_PLACED) {
            AddPlacedVehicleResponse addPlacedVehicleResponse = (AddPlacedVehicleResponse) object;
            addvehi(addPlacedVehicleResponse);
        }

        if (callAPIId == Constants.ApiRequestCode.VEHICLE_LIST_ITEM) {
            VehiclePlacedItemListResponse vehiclePlacedItemListResponse = (VehiclePlacedItemListResponse) object;
            vehicle_Item(vehiclePlacedItemListResponse);
        }
    }




    private void addvehi(AddPlacedVehicleResponse addPlacedVehicleResponse) {
        try {
            Log.e(TAG, "addPlacedVehicleResponse: " + new GsonBuilder().create().toJson(addPlacedVehicleResponse));
            if (addPlacedVehicleResponse != null) {
                if (addPlacedVehicleResponse.getMessage()!=null) {
                    Infrastructure.dismissProgressDialog();
                    vehicleItemList();
                    etDestination.setText("");
                    actvpeVehicle.setText("");
                    spNoOfVehicle.setPrompt("Select");

                    Toast.makeText(this, addPlacedVehicleResponse.getMessage(), Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(this, addPlacedVehicleResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in addPlacedVehicleResponse" + addPlacedVehicleResponse.getResponseCode());
                Toast.makeText(this, addPlacedVehicleResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in addPlacedVehicleResponse", e);
        }


    }


    private void vehicle_Item(VehiclePlacedItemListResponse vehiclePlacedItemListResponse) {
        try {
            Log.e(TAG, "vehiclePlacedItemListResponse: " + new GsonBuilder().create().toJson(vehiclePlacedItemListResponse));
            if (vehiclePlacedItemListResponse != null) {

                list.setLayoutManager(new LinearLayoutManager(
                        this, RecyclerView.VERTICAL, false));

                if (vehiclePlacedItemListResponse.getPlace_no() != null) {
                    VehiclePlacedItemAdapter listAdapter = new VehiclePlacedItemAdapter(AddPlacedVehicleActivity.this,
                            vehiclePlacedItemListResponse.getPlace_no(), list, vehiclePlacedItemListResponse);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in addPlacedVehicleResponse" + vehiclePlacedItemListResponse.getResponseCode());
                Toast.makeText(this, vehiclePlacedItemListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in addPlacedVehicleResponse", e);
        }

    }





    private boolean validate() {
        if (TextUtils.isEmpty(etDate.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please Select Date!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etDestination.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Vehicle Cost!", Toast.LENGTH_SHORT).show();
            return false;
        }if (TextUtils.isEmpty(actvpeVehicle.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Vehicle Type!", Toast.LENGTH_SHORT).show();
            return false;
        }if (spNoOfVehicle.getSelectedItem() != null && spNoOfVehicle.getSelectedItem().toString().trim().equals("Select Reason")) {
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