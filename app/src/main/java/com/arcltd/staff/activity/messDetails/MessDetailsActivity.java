package com.arcltd.staff.activity.messDetails;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.adapter.MessEmpListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.response.MessEmpListResponse;
import com.arcltd.staff.response.MessListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class MessDetailsActivity extends BaseActivity {
    TextView tvDivision,tvMessName,tvOwnRented,tvCookAvl,tvCookSalary,tvNoOfEmp,tvResAddress,
            tvDescription,tvTelevision,tvRefrigerator,tvBedOccupied,tvBedCapacity,
            tvNoOfRoom,tvRentAmount,tvMessunderBranch;
    String data,mess_id;
    MessListResponse.MessList dataBean;
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_mess_details);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);
        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("dataDetails");
            dataBean = new Gson().fromJson(data, MessListResponse.MessList.class);
            Log.e("", "Mess List: " + new GsonBuilder().create().toJson(dataBean));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }
        tvDivision=findViewById(R.id.tvDivision);
        tvMessName=findViewById(R.id.tvMessName);
        tvOwnRented=findViewById(R.id.tvOwnRented);
        tvCookAvl=findViewById(R.id.tvCookAvl);
        tvCookSalary=findViewById(R.id.tvCookSalary);
        tvNoOfEmp=findViewById(R.id.tvNoOfEmp);
        tvResAddress=findViewById(R.id.tvResAddress);
        tvDescription=findViewById(R.id.tvDescription);
        tvTelevision=findViewById(R.id.tvTelevision);
        tvRefrigerator=findViewById(R.id.tvRefrigerator);
        tvBedOccupied=findViewById(R.id.tvBedOccupied);
        tvBedCapacity=findViewById(R.id.tvBedCapacity);
        tvNoOfRoom=findViewById(R.id.tvNoOfRoom);
        tvRentAmount=findViewById(R.id.tvRentAmount);
        tvMessunderBranch=findViewById(R.id.tvMessunderBranch);

        list=findViewById(R.id.list);

        try {
            tvDivision.setText(dataBean.getDiv_name());
            tvMessName.setText(dataBean.getMess_name());
            tvOwnRented.setText(dataBean.getOwn_rented());
            tvNoOfEmp.setText(dataBean.getNo_emp());
            tvResAddress.setText(dataBean.getMess_address());
            tvCookAvl.setText(dataBean.getCook_avl());
            tvCookSalary.setText(dataBean.getCook_salary());
            tvDescription.setText(dataBean.getDescription());
            tvTelevision.setText(dataBean.getTv());
            tvRefrigerator.setText(dataBean.getRefrigerator());
            tvBedOccupied.setText(dataBean.getCurrent_bed());
            tvBedCapacity.setText(dataBean.getCapacity_bed());
            tvNoOfRoom.setText(dataBean.getNo_of_room());
            tvRentAmount.setText(dataBean.getRent_amount());
            tvMessunderBranch.setText(dataBean.getUnder_branch());
            mess_id=dataBean.getMes_id();


        } catch (Exception e) {
            Log.e("", "ListingDetail error: " + e);
        }

       // messEmpList();
    }
    private void messEmpList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.messEMPList(disposable, Constants.ApiRequestCode.DIVISION_LIST,
                        mess_id);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getData messEmpListResponse", e);
        }
    }
    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.DIVISION_LIST) {
            MessEmpListResponse messEmpListResponse = (MessEmpListResponse) object;
            getMessList(messEmpListResponse);
        }
    }

    private void getMessList(MessEmpListResponse messEmpListResponse) {
        try {
            Log.e(TAG, "messEmpListResponse: " + new GsonBuilder().create().toJson(messEmpListResponse));
            if (messEmpListResponse != null) {
                if (messEmpListResponse.getMess_Emp_list()!=null) {
                    Infrastructure.dismissProgressDialog();

                    list.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, true));
                    MessEmpListAdapter listAdapter = new MessEmpListAdapter(this,
                            messEmpListResponse.getMess_Emp_list()
                            , list);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                }else {
                    Toast.makeText(this, messEmpListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in messEmpListResponse" + messEmpListResponse.getResponseCode());
                Toast.makeText(this, messEmpListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in messEmpListResponse", e);
        }
    }
    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
    }

    public void viewEmployee(View view) {
       startActivity(new Intent(this,MessEmployeeDeetailsActivity.class)
               .putExtra("me_id",dataBean.getMes_id()));
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