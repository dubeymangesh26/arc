package com.arcltd.staff.activity.otherAndMain;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.adapter.RetireEmployeeListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.DeleteEmployeeResponse;
import com.arcltd.staff.response.EmployeeListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.arcltd.staff.web_view.WebEmpUploadActivity;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class RetirementActivity extends BaseActivity {
    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    LinearLayout liDeleteUpdate;
    private RetireEmployeeListAdapter.RerfreshWishList activeRerfreshWishList;
    String branch_code="",delete, url="http://onlineformsolution.in/arcMessApi/update_employee.php";
    AutoCompleteTextView atSearch;
    ProgressBar searchProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_retirement);


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        list=findViewById(R.id.list);
        swiptoRefresh=findViewById(R.id.swiptoRefresh);
        searchProgressBar=findViewById(R.id.searchProgressBar);
        atSearch=findViewById(R.id.atSearch);
        liDeleteUpdate=findViewById(R.id.liDeleteUpdate);

        delete=getIntent().getExtras().getString("D");
        branch_code = Objects.requireNonNull(getIntent().getExtras()).getString("branch_code");

        if (delete!=null) {
            liDeleteUpdate.setVisibility(View.GONE);
        }else {
            liDeleteUpdate.setVisibility(View.GONE);

        }

        empList();

        atSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                empList();
            }
        });


        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                empList();
                swiptoRefresh.setRefreshing(false);
            }
        });

    }

    private void empList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                searchProgressBar.setVisibility(View.VISIBLE);
                apiPresenter.retireList_employee(disposable, Constants.ApiRequestCode.DIVISION_LIST, branch_code,
                        atSearch.getText().toString());

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
        searchProgressBar.setVisibility(View.GONE);
        if (callAPIId == Constants.ApiRequestCode.DIVISION_LIST) {
            EmployeeListResponse employeeListResponse = (EmployeeListResponse) object;
            getEmpResp(employeeListResponse);
        }
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.DELETE_EMP) {
            DeleteEmployeeResponse deleteEmployeeResponse = (DeleteEmployeeResponse) object;
            getEmpdelete(deleteEmployeeResponse);
        }
    }

    private void getEmpdelete(DeleteEmployeeResponse deleteEmployeeResponse) {
        try {
            Log.e(TAG, "deleteEmployeeResponse: " + new GsonBuilder().create().toJson(deleteEmployeeResponse));
            if (deleteEmployeeResponse != null) {
                Infrastructure.dismissProgressDialog();
                Toast.makeText(this, deleteEmployeeResponse.getMessage(), Toast.LENGTH_LONG).show();

            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in deleteEmployeeResponse" + deleteEmployeeResponse.getResponseCode());
                Toast.makeText(this, deleteEmployeeResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in deleteEmployeeResponse", e);
        }
    }

    private void getEmpResp(EmployeeListResponse employeeListResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(employeeListResponse));
            if (employeeListResponse != null) {
                if (employeeListResponse.getEmployee_list()!=null) {
                    searchProgressBar.setVisibility(View.GONE);

                    list.setLayoutManager(new GridLayoutManager(
                            this,2, RecyclerView.VERTICAL, false));
                    RetireEmployeeListAdapter listAdapter = new RetireEmployeeListAdapter(this, employeeListResponse.getEmployee_list()
                            , list, employeeListResponse);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                }else {
                    Toast.makeText(this, employeeListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                searchProgressBar.setVisibility(View.GONE);
                ELog.e(TAG, "Exception in checkLoginResponse" + employeeListResponse.getResponseCode());
                Toast.makeText(this, employeeListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in checkLoginResponse", e);
        }
    }
    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
    }


    public void empUpdate(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RetirementActivity.this);
        builder.setTitle("Delete Alert")
                .setMessage("Are you sure, you want to Update All Employee Data ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(RetirementActivity.this, WebEmpUploadActivity.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        //Creating dialog box
        AlertDialog dialog  = builder.create();
        dialog.show();
    }

    public void dleteAllEmployee(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RetirementActivity.this);
        builder.setTitle("Delete Alert")
                .setMessage("Are you sure, you want to Delete All Employee Data ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getTruncateTaleEmployee();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        //Creating dialog box
        AlertDialog dialog  = builder.create();
        dialog.show();

    }

    private void getTruncateTaleEmployee() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(RetirementActivity.this);
                apiPresenter.deleteAllEmployee(disposable, Constants.ApiRequestCode.DELETE_EMP,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.REGION_ID));

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }

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