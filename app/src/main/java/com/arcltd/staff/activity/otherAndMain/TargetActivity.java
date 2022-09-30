package com.arcltd.staff.activity.otherAndMain;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.adapter.TargetListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.TargetResponseResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class TargetActivity extends BaseActivity {
    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    String branch_code="",search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        list=findViewById(R.id.list);
        swiptoRefresh=findViewById(R.id.swiptoRefresh);


        if (RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.LOGIN_TYPE).equals("EM")){
            if (!RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE).equals("BBYRO")){
                branch_code=RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE);
            }else {
                branch_code="";
            }

        }else  {
            branch_code="";
        }

        if (RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.LOGIN_TYPE).equals("BM")){
            branch_code=RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE);

        }

        target();
        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                target();
                swiptoRefresh.setRefreshing(false);
            }
        });


    }

    private void target() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(TargetActivity.this);
                apiPresenter.target(disposable, Constants.ApiRequestCode.TARGET,
                        branch_code,search);

            } else {
                new ErrorHandlerCode(TargetActivity.this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }
    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.TARGET) {
            TargetResponseResponse targetResponseResponse = (TargetResponseResponse) object;
            getDivisionList(targetResponseResponse);
        }
    }

    private void getDivisionList(TargetResponseResponse targetResponseResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(targetResponseResponse));
            if (targetResponseResponse != null) {

                Infrastructure.dismissProgressDialog();

                list.setLayoutManager(new LinearLayoutManager(
                        this, RecyclerView.VERTICAL, false));
                TargetListAdapter listAdapter = new TargetListAdapter(this,
                        targetResponseResponse.getBooking_target(), list, targetResponseResponse);
                list.setAdapter(listAdapter);
                list.setVisibility(View.VISIBLE);

            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + targetResponseResponse.getResponseCode());
                Toast.makeText(this, targetResponseResponse.getMessage(), Toast.LENGTH_LONG).show();

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