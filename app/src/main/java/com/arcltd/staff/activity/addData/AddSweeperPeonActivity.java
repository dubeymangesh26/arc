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
import com.arcltd.staff.response.AddSweeperPeonResponse;
import com.arcltd.staff.response.BranchListResponse;
import com.arcltd.staff.response.DivisionListResponse;
import com.arcltd.staff.response.SweeperPeonListResponse;
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

public class AddSweeperPeonActivity extends BaseActivity {
    TextInputEditText etSweeperPeonCode,etSweeperPeonName,etSalary;
    Spinner spdivision,spBranch,spSwipperPeon;
    LinearLayout liAddData;

    AutoCompleteTextView acType;
    String divisionselected_id,divisionselected_Name;

    List<String> divisionName=new ArrayList<>();
    List<String> divisionID=new ArrayList<>();

    List<String> branch_codearray=new ArrayList<>();
    List<String> branch_code_ID=new ArrayList<>();

    ArrayList<String> sweeperPeon = new ArrayList<>(Arrays.asList("Select Sweeper Or Peon",
            "Sweeper", "Peon"));

    String branch_code,type,swpPeonType;

    String sp_id="",sweep_peon,data="";
    SweeperPeonListResponse.SweeperPeonList dataBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_add_sweeper_peon);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        etSweeperPeonCode =findViewById(R.id.etSweeperPeonCode);
        etSweeperPeonName =findViewById(R.id.etSweeperPeonName);
        etSalary =findViewById(R.id.etSalary);
        spBranch=findViewById(R.id.spBranch);
        spdivision=findViewById(R.id.spdivision);
        spSwipperPeon=findViewById(R.id.spSwipperPeon);
        liAddData=findViewById(R.id.liAddData);
        acType =  findViewById(R.id.acType);

        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("data");
            dataBean = new Gson().fromJson(data, SweeperPeonListResponse.SweeperPeonList.class);
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

            etSweeperPeonCode.setText(dataBean.getEmp_code());
            etSweeperPeonName.setText(dataBean.getEmp_name());
            etSalary.setText(dataBean.getEmp_salary());
            acType.setText(dataBean.getBranch_code());
            sp_id=dataBean.getSp_id();
            type=dataBean.getSweeper_peon();

        } catch (Exception e) {
            Log.e("", "ListingDetail error: " + e);
        }


        ArrayAdapter<String> cast = new ArrayAdapter<String>(AddSweeperPeonActivity.this,
                android.R.layout.simple_spinner_item, sweeperPeon);
        cast.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spSwipperPeon.setAdapter(cast);

        spSwipperPeon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    swpPeonType = sweeperPeon.get(position);
                    Log.e("", "onItemClick: " + position);
                    if (swpPeonType.equals("Sweeper")){
                        type="S";
                    }else {
                        type="P";
                    }


                } catch (Exception e) {
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    public void submitSweeperPeon(View view) {
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
                apiPresenter.addSweeperPeon(disposable, Constants.ApiRequestCode.SWEEPERPEON,sp_id,
                        acType.getText().toString(),
                        etSweeperPeonCode.getText().toString(),etSweeperPeonName.getText().toString()
                        ,etSalary.getText().toString(),type);

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

        if (callAPIId == Constants.ApiRequestCode.SWEEPERPEON) {
            AddSweeperPeonResponse addSweeperPeonResponse = (AddSweeperPeonResponse) object;
            submitdata(addSweeperPeonResponse);
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


    private void submitdata(AddSweeperPeonResponse addSweeperPeonResponse) {
        try {
            Log.e(TAG, "addSweeperPeonResponse: " + new GsonBuilder().create().toJson(addSweeperPeonResponse));
            if (addSweeperPeonResponse != null) {

                    Infrastructure.dismissProgressDialog();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    Toast.makeText(this, addSweeperPeonResponse.getMessage(), Toast.LENGTH_LONG).show();

            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in addSweeperPeonResponse" + addSweeperPeonResponse.getResponseCode());
                Toast.makeText(this, addSweeperPeonResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in addSweeperPeonResponse", e);
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
        }if (TextUtils.isEmpty(etSweeperPeonCode.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Sweeper/Peon Code. !", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etSweeperPeonName.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter  Name!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etSalary.getText().toString())) {
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