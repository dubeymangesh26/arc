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
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.otherAndMain.MainActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.AddConveyanceMobileResponse;
import com.arcltd.staff.response.BranchListResponse;
import com.arcltd.staff.response.ConveyanceMobileListResponse;
import com.arcltd.staff.response.DivisionListResponse;
import com.arcltd.staff.response.EmployeeListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddConveyanceMobileActivity extends BaseActivity {
    TextInputEditText etEmpName,etConveyance,etMobileExp;
    Spinner spdivision,spBranch,spEmpCode;
    String ps_id="";
    AutoCompleteTextView acType;
    LinearLayout liAddData;
    String divisionselected_id,divisionselected_Name;

    List<String> divisionName=new ArrayList<>();
    List<String> divisionID=new ArrayList<>();

    List<String> empCode=new ArrayList<>();
    List<String> empCodeSub=new ArrayList<>();
    List<String> empName=new ArrayList<>();
    List<String> empDesign=new ArrayList<>();

    List<String> branch_codearray=new ArrayList<>();
    List<String> branch_code_ID=new ArrayList<>();

    String branch_code,employ_code,empppCode,employ_name,employ_design,data="";

    ConveyanceMobileListResponse.ConveyanceMobileList dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_add_conveyance_mobile);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        etEmpName =findViewById(R.id.etEmpName);
        etConveyance =findViewById(R.id.etConveyance);
        etMobileExp =findViewById(R.id.etMobileExp);
        spBranch=findViewById(R.id.spBranch);
        spdivision=findViewById(R.id.spdivision);
        spEmpCode=findViewById(R.id.spEmpCode);
        acType =  findViewById(R.id.acType);
        liAddData =  findViewById(R.id.liAddData);

        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("data");
            dataBean = new Gson().fromJson(data, ConveyanceMobileListResponse.ConveyanceMobileList.class);
            Log.e("", "onCreate: " + new GsonBuilder().create().toJson(dataBean));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }

        if (data.equals("")){
            liAddData.setVisibility(View.VISIBLE);
            divisionList();
        }else {
            liAddData.setVisibility(View.GONE);

        }

        try {

            etEmpName.setText(dataBean.getEmp_name());
            etConveyance.setText(dataBean.getConveyance());
            etMobileExp.setText(dataBean.getMobile());
            acType.setText(dataBean.getBranch_code());
            empppCode=dataBean.getEmp_code();

        } catch (Exception e) {
            Log.e("", "ListingDetail error: " + e);
        }





    }

    public void submitConveyanceMobile(View view) {
        if (validate()) {
            addConveyance();
        }

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


    private void empList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.list_employee(disposable, Constants.ApiRequestCode.EMPLOYEE,
                        acType.getText().toString(),"");

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }




    private void addConveyance() {

        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.addConveyanceMobile(disposable, Constants.ApiRequestCode.CONYANCE,ps_id,
                        acType.getText().toString(), empppCode,
                        etEmpName.getText().toString(),etConveyance.getText().toString(),
                        etMobileExp.getText().toString());

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

        if (callAPIId == Constants.ApiRequestCode.DIVISION_LIST) {
            DivisionListResponse divisionListResponse = (DivisionListResponse) object;
            getDivisionList(divisionListResponse);
        }
        if (callAPIId == Constants.ApiRequestCode.BRANCH_LIST) {
            BranchListResponse branchListResponse = (BranchListResponse) object;
            getbranchList(branchListResponse);
        }

        if (callAPIId == Constants.ApiRequestCode.EMPLOYEE) {
            EmployeeListResponse employeeListResponse = (EmployeeListResponse) object;
            getEmpResp(employeeListResponse);
        }

        if (callAPIId == Constants.ApiRequestCode.CONYANCE) {
            AddConveyanceMobileResponse addConveyanceMobileResponse = (AddConveyanceMobileResponse) object;
            submitdata(addConveyanceMobileResponse);
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
                                divisionselected_id = divisionID.get(position-1);
                                divisionselected_Name = divisionName.get(position);
                                Log.e("", "onItemClick_divisionName: " + position);
                                Log.e("", "onItemClick_divisionID: " + position);
                                branchList();

                            } catch (Exception e) {
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });


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

    private void getbranchList(BranchListResponse branchListResponse) {
        try {
            Log.e(TAG, "branchListResponse: " + new GsonBuilder().create().toJson(branchListResponse));
            if (branchListResponse != null) {
                if (branchListResponse.getBranchList()!=null) {

                    Infrastructure.dismissProgressDialog();

                    if (branchListResponse.getBranchList().size() > 0) {
                        branch_codearray.clear();
                        branch_codearray.add("Select Branch");
                        for (int i = 0; i < branchListResponse.getBranchList().size(); i++) {
                            branch_codearray.add(branchListResponse.getBranchList().get(i).getBranch_code());
                            branch_code_ID.add(branchListResponse.getBranchList().get(i).getBranch_code());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, branch_codearray);
                        adapter.setDropDownViewResource(R.layout.spinner_textview);
                        spBranch.setAdapter(adapter);
                        spBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    branch_code = branch_codearray.get(position-1);
                                    String  branch_co = branch_codearray.get(position);
                                    Log.e("", "onItemClick_branch_codearray: " + position);
                                    acType.setText(branch_co);
                                    empList();

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




    private void getEmpResp(EmployeeListResponse employeeListResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(employeeListResponse));
            if (employeeListResponse != null) {
                if (employeeListResponse.getEmployee_list()!=null) {
                    Infrastructure.dismissProgressDialog();

                    if (employeeListResponse.getEmployee_list().size() > 0) {
                        empName.clear();
                        empCode.clear();
                        empDesign.clear();
                        empCode.add("Select Employee Code");
                        for (int i = 0; i < employeeListResponse.getEmployee_list().size(); i++) {
                            empName.add(employeeListResponse.getEmployee_list().get(i).getEmp_name());
                            empCode.add(employeeListResponse.getEmployee_list().get(i).getEmp_code());
                            empCodeSub.add(employeeListResponse.getEmployee_list().get(i).getEmp_code());
                            empDesign.add(employeeListResponse.getEmployee_list().get(i).getEmp_desig());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, empCode);
                        adapter.setDropDownViewResource(R.layout.spinner_textview);
                        spEmpCode.setAdapter(adapter);
                        spEmpCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    employ_code = empCode.get(position-1);
                                    empppCode = empCode.get(position);
                                    employ_name = empName.get(position-1);
                                    employ_design = empDesign.get(position-1);
                                    Log.e("", "onItemClickempCode: " + position);
                                    etEmpName.setText(employ_name);

                                } catch (Exception e) {
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });


                    }

                }else {
                    Toast.makeText(this, employeeListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + employeeListResponse.getResponseCode());
                Toast.makeText(this, employeeListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in checkLoginResponse", e);
        }
    }

    private void submitdata(AddConveyanceMobileResponse addConveyanceMobileResponse) {
        try {
            Log.e(TAG, "addUpdateMoterCycleResponse: " + new GsonBuilder().create().toJson(addConveyanceMobileResponse));
            if (addConveyanceMobileResponse != null) {
                    Infrastructure.dismissProgressDialog();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    Toast.makeText(this, addConveyanceMobileResponse.getMessage(), Toast.LENGTH_LONG).show();

            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in addConveyanceMobileResponse" + addConveyanceMobileResponse.getResponseCode());
                Toast.makeText(this, addConveyanceMobileResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in addConveyanceMobileResponse", e);
        }
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
    }


    private boolean validate() {
        if (Objects.requireNonNull(getIntent().getExtras()).getString("data")==null) {
            if (spdivision.getSelectedItem().toString().trim().equals("Select Division")) {
                Toast.makeText(getApplicationContext(), "Please Select Division!", Toast.LENGTH_SHORT).show();
                return false;
            } else if (spBranch.getSelectedItem().toString().trim().equals("Select Branch")) {
                Toast.makeText(getApplicationContext(), "Please Select Branch!", Toast.LENGTH_SHORT).show();
                return false;
            } else if (spEmpCode.getSelectedItem().toString().trim().equals("Select Employee Code")) {
                Toast.makeText(getApplicationContext(), "Please Select Employee Code!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (TextUtils.isEmpty(etEmpName.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Employee Name!", Toast.LENGTH_SHORT).show();
            return false;
        }/*else if (TextUtils.isEmpty(etConveyance.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Model!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etMobileExp.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter !", Toast.LENGTH_SHORT).show();
            return false;
        }*/
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