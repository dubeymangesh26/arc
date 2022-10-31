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
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.AddUpdateEmployeeResponse;
import com.arcltd.staff.response.BranchListResponse;
import com.arcltd.staff.response.DivisionListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.DatePickerHelper;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AddUpdateEmployeeActivity extends BaseActivity {
    TextInputEditText editEmpCode,editTextName,etDateofBirth, editqualification,editTextDesign,
            etJoiningDate,editTextSalary,editTextUanNo;
    Spinner spdivision,spBranch,spActiveRetire;
    AutoCompleteTextView acType;

    List<String> divisionName=new ArrayList<>();
    List<String> divisionID=new ArrayList<>();
    List<String> branch_code_ID=new ArrayList<>();
    List<String> branch_name=new ArrayList<>();
    ArrayList<String> active_retire = new ArrayList<>(Arrays.asList("Active", "Retired"));

    String branch_code,branchName,divisionselected_id,divisionselected_Name,activeRetireStaus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_add_update_employee);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        editEmpCode=findViewById(R.id.editEmpCode);
        editTextName=findViewById(R.id.editTextName);
        etDateofBirth=findViewById(R.id.etDateofBirth);
        editqualification=findViewById(R.id.editqualification);
        editTextDesign=findViewById(R.id.editTextDesign);
        etJoiningDate=findViewById(R.id.etJoiningDate);
        editTextSalary=findViewById(R.id.editTextSalary);
        spdivision=findViewById(R.id.spdivision);
        spBranch=findViewById(R.id.spBranch);
        spActiveRetire=findViewById(R.id.spActiveRetire);
        editTextUanNo=findViewById(R.id.editTextUanNo);
        acType=findViewById(R.id.acType);

        ArrayAdapter<String> rent = new ArrayAdapter<String>(AddUpdateEmployeeActivity.this,
                android.R.layout.simple_spinner_item, active_retire);
        rent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spActiveRetire.setAdapter(rent);

        spActiveRetire.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    activeRetireStaus = active_retire.get(position);
                    Log.e("", "onItemClick: " + position);

                } catch (Exception e) {
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        etDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showetInsuranceuptpDatePickerDialog();
            }
        });

        etJoiningDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showJoiningInsuranceuptpDatePickerDialog();
            }
        });

        divisionList();
    }
    private void showetInsuranceuptpDatePickerDialog() {
        DatePickerHelper.showDatePicker(this, new DatePickerHelper.OnDateSelectedListener() {
            @Override
            public void onDateSelected(String selectedDateString) {

                etDateofBirth.setText(selectedDateString);
            }
        }, true);
    }

    private void showJoiningInsuranceuptpDatePickerDialog() {
        DatePickerHelper.showDatePicker(this, new DatePickerHelper.OnDateSelectedListener() {
            @Override
            public void onDateSelected(String selectedDateString) {
                etJoiningDate.setText(selectedDateString);
            }
        }, true);
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

    private void branchList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                apiPresenter.list_branch(disposable, Constants.ApiRequestCode.BRANCH_LIST,
                        "",divisionselected_id);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }

    public void onClickSignUp(View view) {

        if (validate()) {
            addEmp(
                    editEmpCode.getText().toString().trim(),
                    editTextName.getText().toString().trim(),
                    etDateofBirth.getText().toString().trim(),
                    editqualification.getText().toString().trim(),
                    editTextDesign.getText().toString().trim(),
                    etJoiningDate.getText().toString().trim(),
                    editTextSalary.getText().toString().trim(),
                    acType.getText().toString().trim());

        }
    }

    private void addEmp(String editEmpCode, String editTextName, String etDateofBirth, String editqualification,
                        String editTextDesign,String etJoiningDate, String editTextSalary, String acType) {

        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.addEmployee(disposable, Constants.ApiRequestCode.USER_REGISTRATION,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.REGION_NAME),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.REGION_ID),
                        divisionselected_Name,divisionselected_id,branch_code,branchName,activeRetireStaus,
                        editEmpCode,editTextName,editqualification,etJoiningDate,editTextDesign,editTextSalary,
                        "A",etDateofBirth,editTextUanNo.getText().toString());
                Log.e("addEmpRequest", "addEmpRequest: " + apiPresenter);
            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("Exception", "Exception in getDataFromWebservice", e);
        }

    }


    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("showResultaddEmpRequest", "showResult: "+object);
        Infrastructure.dismissProgressDialog();

        if (callAPIId == Constants.ApiRequestCode.DIVISION_LIST) {
            DivisionListResponse divisionListResponse = (DivisionListResponse) object;
            getDivisionList(divisionListResponse);
        }
        if (callAPIId == Constants.ApiRequestCode.BRANCH_LIST) {
            BranchListResponse branchListResponse = (BranchListResponse) object;
            getbranchList(branchListResponse);
        }


        if (callAPIId == Constants.ApiRequestCode.USER_REGISTRATION) {
            // LoginResponse loginResponse = new Gson().fromJson(object, LoginResponse.class);
            AddUpdateEmployeeResponse addUpdateEmployeeResponse = (AddUpdateEmployeeResponse) object;
            addEmployee(addUpdateEmployeeResponse);
        }
    }

    private void addEmployee(AddUpdateEmployeeResponse addUpdateEmployeeResponse) {

        try {
            Log.e(TAG, "addUpdateEmployeeResponse: " + new GsonBuilder().create().toJson(addUpdateEmployeeResponse));
            if (addUpdateEmployeeResponse != null) {

                    Infrastructure.dismissProgressDialog();
                    Toast.makeText(this, addUpdateEmployeeResponse.getMessage(), Toast.LENGTH_LONG).show();
                    finish();

            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + addUpdateEmployeeResponse.getResponseCode());
                Toast.makeText(this, addUpdateEmployeeResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in checkLoginResponse", e);
        }

    }

    private void getDivisionList(DivisionListResponse divisionListResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(divisionListResponse));
            if (divisionListResponse != null) {
                if (divisionListResponse.getDivision_list()!=null) {
                    Infrastructure.dismissProgressDialog();
                    divisionID.clear();
                    divisionName.clear();
                    divisionID.add("Select Division");
                    divisionName.add("Select Division");
                    for (int i = 0; i < divisionListResponse.getDivision_list().size(); i++) {
                        divisionName.add(divisionListResponse.getDivision_list().get(i).getDivision_name());
                        divisionID.add(divisionListResponse.getDivision_list().get(i).getDiv_id());
                    }

                    ArrayAdapter<String> div = new ArrayAdapter<String>(this,R.layout.spinner_textview, divisionName);
                    div.setDropDownViewResource(R.layout.spinner_textview);
                    spdivision.setAdapter(div);
                    div.notifyDataSetChanged();
                    spdivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                divisionselected_id = divisionID.get(position);
                                divisionselected_Name = divisionName.get(position);
                                Log.e("", "onItemClick_divisionName: " + position);
                                Log.e("", "onItemClick_divisionID: " + position);
                                Log.e("","Brnch divisionselected_id="+divisionselected_id+"  " +
                                        "Branch divisionselected_Name="+divisionselected_Name);
                                branchList();

                            } catch (Exception e) {
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });


                }else {
               //     Toast.makeText(this, divisionListResponse.getMessage(), Toast.LENGTH_LONG).show();

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

    private void getbranchList(BranchListResponse branchListResponse) {
        try {
            Log.e(TAG, "branchListResponse: " + new GsonBuilder().create().toJson(branchListResponse));
            if (branchListResponse != null) {
                if (branchListResponse.getBranchList()!=null) {

                    Infrastructure.dismissProgressDialog();

                    if (branchListResponse.getBranchList().size() > 0) {
                        branch_code_ID.clear();
                        branch_name.clear();
                        branch_code_ID.add("Select Branch");
                        branch_name.add("Select Branch");
                        for (int i = 0; i < branchListResponse.getBranchList().size(); i++) {
                            branch_name.add(branchListResponse.getBranchList().get(i).getBranch_name());
                            branch_code_ID.add(branchListResponse.getBranchList().get(i).getBranch_code());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, branch_name);
                        adapter.setDropDownViewResource(R.layout.spinner_textview);
                        spBranch.setAdapter(adapter);
                        spBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    branch_code = branch_code_ID.get(position);
                                    branchName = branch_name.get(position);

                                    String  branch_co = branch_code_ID.get(position);
                                    Log.e("", "onItemClick_branch_codearray: " + position);
                                    Log.e("","Brnch Code="+branch_code+"  Branch Name="+branchName);
                                    acType.setText(branch_co);

                                } catch (Exception e) {
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });


                    }


                }else {
                    Toast.makeText(this, branchListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + branchListResponse.getResponseCode());
                Toast.makeText(this, branchListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in checkLoginResponse", e);
        }
    }


    private boolean validate() {
        if (spdivision.getSelectedItem().toString().trim().equals("Select Division")) {
            Toast.makeText(getApplicationContext(), "Please Select Division!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (spBranch.getSelectedItem().toString().trim().equals("Select Branch")) {
            Toast.makeText(getApplicationContext(), "Please Select Branch!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(editTextName.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Employee Name!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etDateofBirth.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Date Of Birth!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(editqualification.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Qualification!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(editTextDesign.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Design!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etJoiningDate.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Joining Date!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(editTextSalary.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Salary!", Toast.LENGTH_SHORT).show();
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