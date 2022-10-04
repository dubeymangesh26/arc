package com.arcltd.staff.activity.detailsData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.adapter.DivisionListAdapter;
import com.arcltd.staff.adapter.EmpHistoryListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.DivisionListResponse;
import com.arcltd.staff.response.EmployeeTransferHistoryResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class TransferHistoryActivity extends BaseActivity {
    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    String emp_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_transfer_history);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        list=findViewById(R.id.list);
        swiptoRefresh=findViewById(R.id.swiptoRefresh);
        emp_code=getIntent().getExtras().getString("emp_code");
        historyList();
    }

    private void historyList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.empTransHistory(disposable, Constants.ApiRequestCode.DIVISION_LIST,
                        emp_code);

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
            EmployeeTransferHistoryResponse employeeTransferHistoryResponse = (EmployeeTransferHistoryResponse) object;
            getDivisionList(employeeTransferHistoryResponse);
        }
    }

    private void getDivisionList(EmployeeTransferHistoryResponse employeeTransferHistoryResponse) {
        try {
            Log.e(TAG, "employeeTransferHistoryResponse: " + new GsonBuilder().create().toJson(employeeTransferHistoryResponse));
            if (employeeTransferHistoryResponse != null) {
                if (employeeTransferHistoryResponse.getEmp_tr_history_list()!=null) {
                    Infrastructure.dismissProgressDialog();

                    list.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, true));
                    EmpHistoryListAdapter listAdapter = new EmpHistoryListAdapter(this,
                            employeeTransferHistoryResponse.getEmp_tr_history_list()
                            , list, employeeTransferHistoryResponse);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                }else {
                    Toast.makeText(this, employeeTransferHistoryResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in employeeTransferHistoryResponse" + employeeTransferHistoryResponse.getResponseCode());
                Toast.makeText(this, employeeTransferHistoryResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in employeeTransferHistoryResponse", e);
        }
    }
    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
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