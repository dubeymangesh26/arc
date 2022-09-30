package com.arcltd.staff.activity.employee;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.appcompat.app.AppCompatActivity;

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
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.AddSweeperPeonResponse;
import com.arcltd.staff.response.BranchListResponse;
import com.arcltd.staff.response.DivisionListResponse;
import com.arcltd.staff.response.EmployeeListResponse;
import com.arcltd.staff.response.SweeperPeonListResponse;
import com.arcltd.staff.response.TransferEmployeeResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EmployeeTransferActivity extends BaseActivity {

    TextInputEditText etCurrentBranch,etEmpCode,etEmpName;
    Spinner spdivision,spBranch,spEmpCode;

    AutoCompleteTextView acType;
    String divisionselected_id,divisionselected_Name;

    List<String> divisionName=new ArrayList<>();
    List<String> divisionID=new ArrayList<>();

    List<String> branch_codearray=new ArrayList<>();
    List<String> branch_code_ID=new ArrayList<>();

    String branch_code,division_id,division_name,subBranchCode,current_branch;

    String data="";
    EmployeeListResponse.EmployeeList dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_transfer);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        etCurrentBranch =findViewById(R.id.etCurrentBranch);
        etEmpCode =findViewById(R.id.etEmpCode);
        etEmpName =findViewById(R.id.etEmpName);
        spBranch=findViewById(R.id.spBranch);
        spdivision=findViewById(R.id.spdivision);
        spEmpCode=findViewById(R.id.spEmpCode);
        acType =  findViewById(R.id.acType);

        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("data");
            dataBean = new Gson().fromJson(data, EmployeeListResponse.EmployeeList.class);
            Log.e("", "onCreate: " + new GsonBuilder().create().toJson(dataBean));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }

       /* if (data.equals("")){
            liAddData.setVisibility(View.VISIBLE);
            divisionList();
        }else {
            liAddData.setVisibility(View.GONE);

        }*/

        try {

            etEmpName.setText(dataBean.getEmp_name());
            etEmpCode.setText(dataBean.getEmp_code());
            etCurrentBranch.setText(dataBean.getBranch_name());
            acType.setText(dataBean.getBranch_code());
            division_id=dataBean.getDivision_id();
            division_name=dataBean.getDivision_name();
            current_branch=dataBean.getBranch_code();

        } catch (Exception e) {
            Log.e("", "ListingDetail error: " + e);
        }

        divisionList();

    }

    public void submitEmployeeTransfer(View view) {
        if (validate()) {
            addSweeperPeon();
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



    private void addSweeperPeon() {

        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.empTransfer(disposable, Constants.ApiRequestCode.TRANSFER,etEmpCode.getText().toString(),
                        division_id,division_name,current_branch, acType.getText().toString(),subBranchCode);

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

        if (callAPIId == Constants.ApiRequestCode.TRANSFER) {
            TransferEmployeeResponse transferEmployeeResponse = (TransferEmployeeResponse) object;
            submitdata(transferEmployeeResponse);
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
                            branch_code_ID.add(branchListResponse.getBranchList().get(i).getBranch_name());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, branch_codearray);
                        adapter.setDropDownViewResource(R.layout.spinner_textview);
                        spBranch.setAdapter(adapter);
                        spBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    branch_code = branch_codearray.get(position-1);
                                    subBranchCode = branch_code_ID.get(position-1);
                                    String  branch_co = branch_codearray.get(position);
                                    Log.e("", "onItemClick_branch_codearray: " + position);
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
                ELog.e(TAG, "Exception in transferEmployeeResponse" + branchListResponse.getResponseCode());
                Toast.makeText(this, branchListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in transferEmployeeResponse", e);
        }
    }


    private void submitdata(TransferEmployeeResponse transferEmployeeResponse) {
        try {
            Log.e(TAG, "transferEmployeeResponse: " + new GsonBuilder().create().toJson(transferEmployeeResponse));
            if (transferEmployeeResponse != null) {

                Infrastructure.dismissProgressDialog();
              //  startActivity(new Intent(this, MessOfficeGodownListActivity.class));
                finish();
                Toast.makeText(this, transferEmployeeResponse.getMessage(), Toast.LENGTH_LONG).show();

            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in transferEmployeeResponse" + transferEmployeeResponse.getResponseCode());
                Toast.makeText(this, transferEmployeeResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in transferEmployeeResponse", e);
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
            }
        }if (TextUtils.isEmpty(etCurrentBranch.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Current Branch. !", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etEmpCode.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter  Employee Code!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etEmpName.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Employee Name!", Toast.LENGTH_SHORT).show();
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