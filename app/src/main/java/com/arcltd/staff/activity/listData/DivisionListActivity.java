package com.arcltd.staff.activity.listData;

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
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.adapter.DivisionListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.DivisionListResponse;
import com.arcltd.staff.response.OfficeGodownMessListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class DivisionListActivity extends BaseActivity {

    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    private DivisionListAdapter.RerfreshWishList activeRerfreshWishList;
    String data;
    OfficeGodownMessListResponse.OfficeMessRentList dataBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_division_list);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        list=findViewById(R.id.list);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        swiptoRefresh=findViewById(R.id.swiptoRefresh);

        divisionList();

        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                divisionList();
                swiptoRefresh.setRefreshing(false);
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
        @Override
        public void showResult(Object object, int callAPIId) {
            ELog.d("TAG", "showResult: " + object);
            Infrastructure.dismissProgressDialog();
            if (callAPIId == Constants.ApiRequestCode.DIVISION_LIST) {
                DivisionListResponse divisionListResponse = (DivisionListResponse) object;
                getDivisionList(divisionListResponse);
            }
        }

        private void getDivisionList(DivisionListResponse divisionListResponse) {
            try {
                Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(divisionListResponse));
                if (divisionListResponse != null) {
                    if (divisionListResponse.getDivision_list()!=null) {
                        Infrastructure.dismissProgressDialog();

                        list.setLayoutManager(new LinearLayoutManager(
                                this, RecyclerView.VERTICAL, true));
                        DivisionListAdapter listAdapter = new DivisionListAdapter(this, divisionListResponse.getDivision_list()
                                , list, divisionListResponse);
                        list.setAdapter(listAdapter);
                        list.setVisibility(View.VISIBLE);
                        //   listAdapter.notifyDataSetChanged();


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