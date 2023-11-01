package com.arcltd.staff.activity.employee;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.activity.detailsData.TransferHistoryActivity;
import com.arcltd.staff.adapter.EmployeeRemarkListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.response.AddRemarkResponse;
import com.arcltd.staff.response.DeleteResponse;
import com.arcltd.staff.response.EmpRemarkListResponse;
import com.arcltd.staff.response.EmployeeActive_InctiveResponse;
import com.arcltd.staff.response.EmployeeListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;
import java.util.StringTokenizer;

public class EmployeeDetailsActivity extends BaseActivity implements EmployeeRemarkListAdapter.DeleteRemark {
    RecyclerView list;
    TextView tvRegion,tvDivision,tvEmpName,tvEmpCode,tvEmpQuali,tvAppoimentDate,
            tvDesign,tvSalary,tvUpdatedDate,branchCode,tvBranchName,tvDateofBirth,tvRetirementDate,tvUan,tvEmpMobile,
            tvFleet,tvBasic,tvHra,tvTga,tvPFNo,tvEmpGrade;
    String data,empCode,activeStatus,status,branch_code;
    Button tvActiveInactive;
    EmployeeListResponse.EmployeeList dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_employee_details);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("data");
            dataBean = new Gson().fromJson(data, EmployeeListResponse.EmployeeList.class);
            Log.e("", "onCreate: " + new GsonBuilder().create().toJson(dataBean));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }
        list=findViewById(R.id.list);
        tvRegion=findViewById(R.id.tvRegion);
        tvDivision=findViewById(R.id.tvDivision);
        tvEmpName=findViewById(R.id.tvEmpName);
        tvEmpCode=findViewById(R.id.tvEmpCode);
        tvEmpQuali=findViewById(R.id.tvEmpQuali);
        tvAppoimentDate=findViewById(R.id.tvAppoimentDate);
        tvDesign=findViewById(R.id.tvDesign);
        tvSalary=findViewById(R.id.tvSalary);
        tvUpdatedDate=findViewById(R.id.tvUpdatedDate);
        tvActiveInactive=findViewById(R.id.tvActiveInactive);
        branchCode=findViewById(R.id.branchCode);
        tvBranchName=findViewById(R.id.tvBranchName);
        tvDateofBirth=findViewById(R.id.tvDateofBirth);
        tvRetirementDate=findViewById(R.id.tvRetirementDate);
        tvUan=findViewById(R.id.tvUan);
        tvEmpMobile=findViewById(R.id.tvEmpMobile);
        tvFleet=findViewById(R.id.tvFleet);
        tvBasic=findViewById(R.id.tvBasic);
        tvHra=findViewById(R.id.tvHra);
        tvTga=findViewById(R.id.tvTga);
        tvPFNo=findViewById(R.id.tvPFNo);
        tvEmpGrade=findViewById(R.id.tvEmpGrade);
        try {
            tvRegion.setText(dataBean.getRegion_name());
            tvDivision.setText(dataBean.getDivision_name());
            tvEmpName.setText(dataBean.getEmp_name());
            tvEmpCode.setText(dataBean.getEmp_code());
            tvEmpQuali.setText(dataBean.getEmp_eqalification());
            tvAppoimentDate.setText(dataBean.getEmp_joining_date());
            tvDesign.setText(dataBean.getEmp_desig());
            tvSalary.setText(dataBean.getEmp_salary());
            tvUpdatedDate.setText(dataBean.getUpdated_date());
            branchCode.setText(dataBean.getBranch_code());
            tvBranchName.setText(dataBean.getBranch_name());
            tvDateofBirth.setText(dataBean.getDat_of_birth());
            tvUan.setText(dataBean.getUan_no());
            tvEmpMobile.setText(dataBean.getEmp_mob());
            tvFleet.setText(dataBean.getSal_fleet());
            tvBasic.setText(dataBean.getSal_basic());
            tvHra.setText(dataBean.getSal_hra());
            tvTga.setText(dataBean.getSal_tga());
            tvPFNo.setText(dataBean.getPf_number());
            tvEmpGrade.setText(dataBean.getGread());

            empCode=dataBean.getEmp_code();
            branch_code=dataBean.getBranch_code();
            activeStatus=dataBean.getStatus();

            tvEmpMobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", tvEmpMobile.getText().toString(), null));
                    startActivity(intent);

                }
            });

        } catch (Exception e) {
            Log.e("", "ListingDetail error: " + e);
        }

        try {
            String date=dataBean.getRetirement_date();
            StringTokenizer tk = new StringTokenizer(date);
            String showDate = tk.nextToken();  // <---  yyyy-mm-dd
            String[] items1 = showDate.split("-");
            String date1=items1[2];
            String month=items1[1];
            String year=items1[0];
            tvRetirementDate.setText(date1+"-"+month+"-"+year);

        }catch (Exception e){
            Log.e("","Error"+e);
        }


        getStatus();
        empRemarkList();


    }

    public void submitEmTransfer(View view) {
        startActivity(new Intent(this, EmployeeTransferActivity.class).putExtra("data", data));


    }

    private void empRemarkList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.empRemarkList(disposable, Constants.ApiRequestCode.REMARK_LIST,
                        empCode);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }

    public void activeInctive(View view) {
        ativeInactive();
    }

    private void ativeInactive() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.empActiveInacytive(disposable, Constants.ApiRequestCode.ACTIV_INACTIVE,
                        empCode,status);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in EmployeeActive_InctiveResponse", e);
        }
    }
    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.ACTIV_INACTIVE) {
            EmployeeActive_InctiveResponse employeeActive_inctiveResponse = (EmployeeActive_InctiveResponse) object;
            getActiveInactive(employeeActive_inctiveResponse);
        }
        if (callAPIId == Constants.ApiRequestCode.ADD_REMARK) {
            AddRemarkResponse addRemarkResponse = (AddRemarkResponse) object;
            addRemarkList(addRemarkResponse);
        }
        if (callAPIId == Constants.ApiRequestCode.REMARK_LIST) {
            EmpRemarkListResponse empRemarkListResponse = (EmpRemarkListResponse) object;
            empShowRemarkList(empRemarkListResponse);
        }
        if (callAPIId == Constants.ApiRequestCode.DELETE) {
            DeleteResponse deleteResponse = (DeleteResponse) object;
            getDelete(deleteResponse);
        }
    }

    private void empShowRemarkList(EmpRemarkListResponse empRemarkListResponse) {

        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(empRemarkListResponse));
            if (empRemarkListResponse != null) {
                if (empRemarkListResponse.getEmployee_remark_list()!=null) {
                    Infrastructure.dismissProgressDialog();

                    list.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, true));
                    EmployeeRemarkListAdapter listAdapter = new EmployeeRemarkListAdapter(this, empRemarkListResponse.getEmployee_remark_list()
                            , list, empRemarkListResponse,this);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                }else {
                   // Toast.makeText(this, empRemarkListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + empRemarkListResponse.getResponseCode());
                Toast.makeText(this, empRemarkListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in checkLoginResponse", e);
        }
    }

    private void addRemarkList(AddRemarkResponse addRemarkResponse) {
        try {
            Log.e(TAG, "addRemarkResponse: " + new GsonBuilder().create().toJson(addRemarkResponse));
            if (addRemarkResponse != null) {
                if (addRemarkResponse.getResponseCode() != null) {
                    Infrastructure.dismissProgressDialog();
                    empRemarkList();
                    finish();

                } else {
                    Toast.makeText(this, addRemarkResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in feedbackListResponse" + addRemarkResponse.getResponseCode());
                Toast.makeText(this, addRemarkResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in feedbackListResponse", e);
        }
    }



    private void getActiveInactive(EmployeeActive_InctiveResponse employeeActive_inctiveResponse) {
        try {
            Log.e(TAG, "EmployeeActive_InctiveResponse: " + new GsonBuilder().create().toJson(employeeActive_inctiveResponse));
            if (employeeActive_inctiveResponse != null) {
                Infrastructure.dismissProgressDialog();
                activeStatus=employeeActive_inctiveResponse.getUserStatus().getStatus();
                getStatus();
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in EmployeeActive_InctiveResponse" + employeeActive_inctiveResponse.getResponseCode());
                Toast.makeText(this, employeeActive_inctiveResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in EmployeeActive_InctiveResponse", e);
        }
    }

    private void getDelete(DeleteResponse deleteResponse) {

        try {
            Log.e(TAG, "deleteResponse: " + new GsonBuilder().create().toJson(deleteResponse));
            if (deleteResponse != null) {
                if (deleteResponse.getResponseCode().equals("200")) {
                    Infrastructure.dismissProgressDialog();
                    Toast.makeText(this, deleteResponse.getMessage(), Toast.LENGTH_LONG).show();
                    empRemarkList();

                }else {
                    Toast.makeText(this, deleteResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in deleteResponse" + deleteResponse.getResponseCode());
                Toast.makeText(this, deleteResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in deleteResponse", e);
        }

    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
    }

    private void getStatus() {
        if (activeStatus.equals("A")){
            tvActiveInactive.setText("Active");
            tvActiveInactive.setTextColor(getResources().getColor(R.color.whiteTextColor));
            status="E";

        }else {
            tvActiveInactive.setText("Inactive");
            tvActiveInactive.setTextColor(getResources().getColor(R.color.whiteTextColor));
            status="A";
        }

    }

    public void TransferHistory(View view) {
        startActivity(new Intent(this, TransferHistoryActivity.class)
                .putExtra("emp_code",empCode));

    }

    public void addRrmark(View view) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_add_feedbacklist);
        dialog.getWindow()
                .setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        TextInputEditText etRemark=dialog.findViewById(R.id.etRemark);

        Button submit = dialog.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate(etRemark)) {
                    addRemark(etRemark.getText().toString().trim());
                    dialog.dismiss();
                }
            }
        });


        ImageView close = (ImageView) dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    private void addRemark(String remark) {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.addEmpRemark(disposable, Constants.ApiRequestCode.ADD_REMARK,
                        branch_code,empCode,remark);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }

    }

    private boolean validate(TextInputEditText etRemark) {
        if (TextUtils.isEmpty(etRemark.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Remark!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    private void deleade(String r_id) {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.deleteEmpRemark(disposable, Constants.ApiRequestCode.DELETE,
                        r_id);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in deleteResponse", e);
        }

    }

    @Override
    public void remark(String r_id) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Remark")
                .setMessage("Are you sure you want to delete this remark?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        deleade(r_id);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

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