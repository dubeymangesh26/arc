package com.arcltd.staff.activity.attendance;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.adapter.BranchQrAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.BranchQrListResponse;
import com.arcltd.staff.response.DeleteBranchQrResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.Infrastructure;

import java.util.ArrayList;
import java.util.List;

public class ManageBranchQrActivity extends BaseActivity implements BranchQrAdapter.OnDeleteClickListener {

    private RecyclerView rvBranchQrs;
    private TextView tvNoData;

    private String myEmpCode, myLoginType;
    private List<BranchQrListResponse.BranchQrItem> qrList = new ArrayList<>();
    private BranchQrAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_branch_qr);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Manage Branch QR Codes");
        }

        myEmpCode = RetrofitClient.getStringUserPreference(this, Constants.EMP_CODE);
        myLoginType = RetrofitClient.getStringUserPreference(this, Constants.LOGIN_TYPE);

        initUI();
        loadAllBranchQrs();
    }

    private void initUI() {
        rvBranchQrs = findViewById(R.id.rv_branch_qrs);
        tvNoData = findViewById(R.id.tv_no_data);

        rvBranchQrs.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadAllBranchQrs() {
        if (!Infrastructure.isInternetPresent()) {
            Toast.makeText(this, "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }

        Infrastructure.showProgressDialog(this);
        apiPresenter.getAllBranchQrs(
                disposable,
                Constants.ApiRequestCode.GET_ALL_BRANCH_QRS,
                myEmpCode,
                myLoginType
        );
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        Infrastructure.dismissProgressDialog();

        if (callAPIId == Constants.ApiRequestCode.GET_ALL_BRANCH_QRS) {
            if (object instanceof BranchQrListResponse) {
                BranchQrListResponse response = (BranchQrListResponse) object;
                if ("200".equals(response.getResponseCode()) && response.getBranchList() != null) {
                    qrList = response.getBranchList();
                    if (!qrList.isEmpty()) {
                        adapter = new BranchQrAdapter(this, qrList, this);
                        rvBranchQrs.setAdapter(adapter);
                        rvBranchQrs.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.GONE);
                    } else {
                        rvBranchQrs.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    rvBranchQrs.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                    tvNoData.setText(response.getMessage() != null ? response.getMessage() : "No Branch QR Codes found.");
                }
            }
        } else if (callAPIId == Constants.ApiRequestCode.DELETE_BRANCH_QR) {
            if (object instanceof DeleteBranchQrResponse) {
                DeleteBranchQrResponse response = (DeleteBranchQrResponse) object;
                if ("200".equals(response.getResponseCode())) {
                    Toast.makeText(this, response.getMessage() != null ? response.getMessage() : "Branch QR Code deleted successfully.", Toast.LENGTH_SHORT).show();
                    loadAllBranchQrs(); // Refresh list after deletion
                } else {
                    Toast.makeText(this, response.getMessage() != null ? response.getMessage() : "Failed to delete Branch QR Code.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onDeleteClick(BranchQrListResponse.BranchQrItem item, int position) {
        if (item == null || TextUtils.isEmpty(item.getBranchCode())) return;

        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete QR Code")
                .setMessage(String.format("Are you sure you want to delete and reset the QR Code and Office GPS location for Branch '%s' (%s)?",
                        !TextUtils.isEmpty(item.getBranchName()) ? item.getBranchName() : "Branch",
                        item.getBranchCode()))
                .setPositiveButton("Yes, Delete", (dialog, which) -> deleteBranchQrApi(item.getBranchCode()))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteBranchQrApi(String branchCode) {
        if (!Infrastructure.isInternetPresent()) {
            Toast.makeText(this, "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }

        Infrastructure.showProgressDialog(this);
        apiPresenter.deleteBranchQr(
                disposable,
                Constants.ApiRequestCode.DELETE_BRANCH_QR,
                branchCode,
                myEmpCode,
                myLoginType
        );
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        Infrastructure.dismissProgressDialog();
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
