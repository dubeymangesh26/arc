package com.arcltd.staff.activity.employee;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.adapter.EmployeeConveyanceListAdapter;
import com.arcltd.staff.adapter.MoterCycleListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.ConveyanceMobileListResponse;
import com.arcltd.staff.response.MoterCycleListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class EmployeeConvenceListActivity extends BaseActivity {
    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    private EmployeeConveyanceListAdapter.RerfreshWishList activeRerfreshWishList;
    String branch_code,data;
    AutoCompleteTextView atSearch;
    ProgressBar searchProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_employee_convence_list);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        list=findViewById(R.id.list);
        swiptoRefresh=findViewById(R.id.swiptoRefresh);
        searchProgressBar=findViewById(R.id.searchProgressBar);
        atSearch=findViewById(R.id.atSearch);

        try {
            branch_code = Objects.requireNonNull(getIntent().getExtras()).getString("branch_code");
        }catch (Exception e){
            Log.e(TAG, "onCreate: ",e );
        }

        conenyanceMobileList();

        atSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                conenyanceMobileList();
            }
        });

        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                conenyanceMobileList();
                swiptoRefresh.setRefreshing(false);
            }
        });

    }
    private void conenyanceMobileList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                searchProgressBar.setVisibility(View.VISIBLE);
                apiPresenter.convenceList(disposable, Constants.ApiRequestCode.MOTERCYCLELIST,
                        branch_code,atSearch.getText().toString());

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
        if (callAPIId == Constants.ApiRequestCode.MOTERCYCLELIST) {
            ConveyanceMobileListResponse conveyanceMobileListResponse = (ConveyanceMobileListResponse) object;
            getConvenceMobileList(conveyanceMobileListResponse);
        }
    }

    private void getConvenceMobileList(ConveyanceMobileListResponse conveyanceMobileListResponse) {
        try {
            Log.e(TAG, "conveyanceMobileListResponse: " + new GsonBuilder().create().toJson(conveyanceMobileListResponse));
            if (conveyanceMobileListResponse != null) {
                if (conveyanceMobileListResponse.getConveyance_mobile_list()!=null) {
                    searchProgressBar.setVisibility(View.GONE);

                    list.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, false));
                    EmployeeConveyanceListAdapter listAdapter = new EmployeeConveyanceListAdapter(this,
                            conveyanceMobileListResponse.getConveyance_mobile_list()
                            , list, conveyanceMobileListResponse);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                }else {
                    Toast.makeText(this, conveyanceMobileListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in conveyanceMobileListResponse" + conveyanceMobileListResponse.getResponseCode());
                Toast.makeText(this, conveyanceMobileListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in conveyanceMobileListResponse", e);
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