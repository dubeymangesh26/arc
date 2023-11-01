package com.arcltd.staff.activity.detailsData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.adapter.TargetAchivementListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.response.TargetResponseResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class TargetAchivementDetailsActivity extends BaseActivity {
    TextView tvTotalTarget,tvFtlTarget,tvPartTarget,tvParcelTarget;
    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    String branch_code="",search;
    String data,AdBranch_Code,div_id;
    TargetResponseResponse.BookingTarget dataBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_target_achivement_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        list=findViewById(R.id.list);
        swiptoRefresh=findViewById(R.id.swiptoRefresh);
        tvParcelTarget = findViewById(R.id.tvParcelTarget);
        tvPartTarget = findViewById(R.id.tvPartTarget);
        tvFtlTarget = findViewById(R.id.tvFtlTarget);
        tvTotalTarget = findViewById(R.id.tvTotalTarget);

        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("data");
            dataBean = new Gson().fromJson(data, TargetResponseResponse.BookingTarget.class);
            Log.e("", "onCreate: " + new GsonBuilder().create().toJson(dataBean));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }

        try {
            branch_code=dataBean.getBranch_code();
            setTitle(dataBean.getBranch_name());
            tvParcelTarget.setText(dataBean.getParcel());
            tvPartTarget.setText(dataBean.getPart());
            tvFtlTarget.setText(dataBean.getFtl());
            tvTotalTarget.setText(dataBean.getTotal());
        } catch (Exception e) {
            Log.e("", "Data setData error: " + e);
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
                Infrastructure.showProgressDialog(this);
                apiPresenter.target(disposable, Constants.ApiRequestCode.TARGET,
                        branch_code,search);

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
        if (callAPIId == Constants.ApiRequestCode.TARGET) {
            TargetResponseResponse targetResponseResponse = (TargetResponseResponse) object;
            getDivisionList(targetResponseResponse);
        }
    }

    private void getDivisionList(TargetResponseResponse targetResponseResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(targetResponseResponse));
            if (targetResponseResponse.getBooking_details() != null) {

                Infrastructure.dismissProgressDialog();

                list.setLayoutManager(new LinearLayoutManager(
                        this, RecyclerView.VERTICAL, false));
                TargetAchivementListAdapter listAdapter = new TargetAchivementListAdapter(this,
                        targetResponseResponse.getBooking_details(),
                        targetResponseResponse.getBooking_target(),list, targetResponseResponse);
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