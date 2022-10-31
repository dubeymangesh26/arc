package com.arcltd.staff.activity.addData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.activity.otherAndMain.MainActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.AddMessResponse;
import com.arcltd.staff.response.DivisionListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AddMessActivity extends BaseActivity {
    EditText etMessName,etmessAddress,etCookSalary;
    TextInputLayout coolSalary;
    Spinner spCook,division,spMessOwnRent;
    String cookAvailable,mesOwnRent,division_id,division_Name;
    private List<String> addDivisionlist;
    ArrayList<String> divisionData = new ArrayList<String>();
    ArrayList<String> divisionDataid = new ArrayList<String>();

    ArrayList<String> cook = new ArrayList<>(Arrays.asList("Select Cook Available Or Not",
            "YES", "NO"));
    ArrayList<String> own_rent = new ArrayList<>(Arrays.asList("Select Mess Type",
            "Own", "Rented"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_add_mess);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        etMessName=findViewById(R.id.etMessName);
        etmessAddress=findViewById(R.id.etmessAddress);
        etCookSalary=findViewById(R.id.etCookSalary);
        coolSalary=findViewById(R.id.coolSalary);
        spCook=findViewById(R.id.spCook);
        division=findViewById(R.id.division);
        spMessOwnRent=findViewById(R.id.spMessOwnRent);

        divisionList();

        ArrayAdapter<String> cast = new ArrayAdapter<String>(AddMessActivity.this,
                android.R.layout.simple_spinner_item, cook);
        cast.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spCook.setAdapter(cast);

        spCook.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    cookAvailable = cook.get(position);
                    Log.e("", "onItemClick: " + position);
                    if (cookAvailable.equals("YES")){
                        coolSalary.setVisibility(View.VISIBLE);
                    }else {
                        coolSalary.setVisibility(View.GONE);
                    }


                } catch (Exception e) {
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> rent = new ArrayAdapter<String>(AddMessActivity.this,
                android.R.layout.simple_spinner_item, own_rent);
        rent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spMessOwnRent.setAdapter(rent);

        spMessOwnRent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mesOwnRent = own_rent.get(position);
                    Log.e("", "onItemClick: " + position);

                } catch (Exception e) {
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void divisionList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.list_division(disposable, Constants.ApiRequestCode.DIVISION_LIST,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.REGION_ID));

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }

    private void addDivision() {
        ArrayAdapter<String> div = new ArrayAdapter<String>(AddMessActivity.this,
                android.R.layout.simple_spinner_item, divisionData);
        div.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        division.setAdapter(div);

        division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    division_Name = divisionData.get(position);

                    Log.e("", "onItemClick: " + position);

                } catch (Exception e) {
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
    }

    public void onClickFormSubmit(View view) {
        if (validate()) {
            submit(
                    etMessName.getText().toString().trim(),
                    etmessAddress.getText().toString().trim(),
                    etCookSalary.getText().toString().trim());

        }
    }

    private void submit(String etMessName, String etmessAddress, String etCookSalary) {

        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(AddMessActivity.this);
                apiPresenter.addMess(disposable, Constants.ApiRequestCode.MESS_ADD,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.REGION_ID),
                        etMessName,etmessAddress,etCookSalary,division_Name,mesOwnRent,cookAvailable);
                Log.e("login_Rquest", "login_Rquest: " + apiPresenter);
            } else {
                new ErrorHandlerCode(AddMessActivity.this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("Exception", "Exception in getDataFromWebservice", e);
        }
    }


    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.DIVISION_LIST) {
            DivisionListResponse divisionListResponse = (DivisionListResponse) object;
            getDivisionList(divisionListResponse);
        }
        if (callAPIId == Constants.ApiRequestCode.MESS_ADD) {
            AddMessResponse addMessResponse = (AddMessResponse) object;
            addMess(addMessResponse);
        }
    }



    private void getDivisionList(DivisionListResponse divisionListResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(divisionListResponse));
            if (divisionListResponse != null) {
                if (divisionListResponse.getDivision_list()!=null) {
                    Infrastructure.dismissProgressDialog();
                  /* division =divisionListResponse.getDivision_list().get(0).getDiv_id();*/
                 /*   division =divisionListResponse.getDivision_list().get(0).getDivision_name();*/
                    divisionData.add("Select Division");
                    for(int i =0; i < divisionListResponse.getDivision_list().size(); i++) {
                        divisionData.add(divisionListResponse.getDivision_list().get(i).getDivision_name());
                        divisionDataid.add(divisionListResponse.getDivision_list().get(i).getDiv_id());
                    }
                    addDivision();

                }else {
                    Toast.makeText(this, divisionListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + divisionListResponse.getResponseCode());
                Toast.makeText(this, divisionListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in checkLoginResponse", e);
        }
    }

    private void addMess(AddMessResponse addMessResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(addMessResponse));
            if (addMessResponse != null) {
                if (addMessResponse.getMessage()!=null) {
                    Infrastructure.dismissProgressDialog();
                    Toast.makeText(this, addMessResponse.getMessage(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();

                }else {
                    Toast.makeText(this, addMessResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + addMessResponse.getResponseCode());
                Toast.makeText(this, addMessResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in checkLoginResponse", e);
        }


    }



    private boolean validate() {
        if (TextUtils.isEmpty(etMessName.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Mess name!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etmessAddress.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Mess Address!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (division.getSelectedItem().toString().trim().equals("Select Division")) {
            Toast.makeText(getApplicationContext(), "Select Division", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spMessOwnRent.getSelectedItem().toString().trim().equals("Select Mess Type")) {
            Toast.makeText(getApplicationContext(), "Select Mess Type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spCook.getSelectedItem().toString().trim().equals("Select Cook Available Or Not")) {
            Toast.makeText(getApplicationContext(), "Select Cook Available Or Not", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spCook.getSelectedItem().toString().trim().equals("YES")){
        if (TextUtils.isEmpty(etCookSalary.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Cook Salary!", Toast.LENGTH_SHORT).show();
            return false;
        }
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