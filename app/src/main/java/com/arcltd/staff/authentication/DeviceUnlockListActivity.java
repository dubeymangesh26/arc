package com.arcltd.staff.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.GenericResponse;
import com.arcltd.staff.response.UnlockRequestListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.Infrastructure;

import java.util.ArrayList;
import java.util.List;

public class DeviceUnlockListActivity extends BaseActivity {

    private RecyclerView rvUnlockRequests;
    private ProgressBar progressBar;
    private DeviceUnlockAdapter adapter;
    private List<UnlockRequestListResponse.UnlockRequestModel> dataList = new ArrayList<>();
    
    private UnlockRequestListResponse.UnlockRequestModel currentProcessingModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_unlock_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Device Unlock Requests");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        rvUnlockRequests = findViewById(R.id.rvUnlockRequests);
        progressBar = findViewById(R.id.progressBar);

        rvUnlockRequests.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeviceUnlockAdapter(dataList, new DeviceUnlockAdapter.OnApproveClickListener() {
            @Override
            public void onApproveClick(UnlockRequestListResponse.UnlockRequestModel model) {
                approveRequest(model);
            }
        });
        rvUnlockRequests.setAdapter(adapter);

        fetchRequests();
    }

    private void fetchRequests() {
        if (Infrastructure.isInternetPresent()) {
            progressBar.setVisibility(View.VISIBLE);
            String regionId = RetrofitClient.getStringUserPreference(this, Constants.REGION_ID);
            apiPresenter.listDeviceUnlockRequests(disposable, Constants.ApiRequestCode.LIST_DEVICE_UNLOCK_REQUESTS, regionId);
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void approveRequest(UnlockRequestListResponse.UnlockRequestModel model) {
        if (Infrastructure.isInternetPresent()) {
            currentProcessingModel = model;
            Infrastructure.showProgressDialog(this);
            apiPresenter.approveDeviceUnlock(disposable, Constants.ApiRequestCode.APPROVE_DEVICE_UNLOCK, model.getId(), model.getEmp_code());
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        super.showResult(object, callAPIId);
        progressBar.setVisibility(View.GONE);
        Infrastructure.dismissProgressDialog();

        if (callAPIId == Constants.ApiRequestCode.LIST_DEVICE_UNLOCK_REQUESTS) {
            if (object instanceof UnlockRequestListResponse) {
                UnlockRequestListResponse response = (UnlockRequestListResponse) object;
                if ("200".equals(response.getResponsecode()) && response.getData() != null) {
                    dataList.clear();
                    dataList.addAll(response.getData());
                    adapter.notifyDataSetChanged();
                } else {
                    dataList.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        } else if (callAPIId == Constants.ApiRequestCode.APPROVE_DEVICE_UNLOCK) {
            if (object instanceof GenericResponse) {
                GenericResponse response = (GenericResponse) object;
                if ("200".equals(response.getResponsecode())) {
                    Toast.makeText(this, "Device unlocked successfully.", Toast.LENGTH_LONG).show();
                    if (currentProcessingModel != null) {
                        dataList.remove(currentProcessingModel);
                        adapter.notifyDataSetChanged();
                        currentProcessingModel = null;
                    }
                } else {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        progressBar.setVisibility(View.GONE);
        Infrastructure.dismissProgressDialog();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}