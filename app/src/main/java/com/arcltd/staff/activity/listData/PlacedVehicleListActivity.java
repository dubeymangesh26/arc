package com.arcltd.staff.activity.listData;

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
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.adapter.VehiclePlaceFailedListAdapter;
import com.arcltd.staff.adapter.VehiclePlacedItemAdapter;
import com.arcltd.staff.adapter.VehiclePlacedListAdapter;
import com.arcltd.staff.adapter.VehiclePlacedNoAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.VehicleFailListResponse;
import com.arcltd.staff.response.VehiclePlacedItemListResponse;
import com.arcltd.staff.response.VehiclePlacedListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class PlacedVehicleListActivity extends BaseActivity {
    RecyclerView list,NoOfVehicle;
    SwipeRefreshLayout swiptoRefresh;
    String region_id, division_id,branch_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_vehicle_list);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        list=findViewById(R.id.list);
        NoOfVehicle=findViewById(R.id.NoOfVehicle);
        swiptoRefresh=findViewById(R.id.swiptoRefresh);

        division_id= RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.DIVISION_ID);

        VehicleNo();
        VehicleList();
        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                VehicleNo();
                VehicleList();
                swiptoRefresh.setRefreshing(false);
            }
        });


    }

    private void VehicleNo() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.listPlacedVehicle(disposable, Constants.ApiRequestCode.VEHICLE_NO,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.REGION_NAME),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.DIVISION_NAME),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.LOGIN_TYPE));

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }


    private void VehicleList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.listPlacedVehicle(disposable, Constants.ApiRequestCode.VEHICLE_LIST,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.REGION_NAME),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.DIVISION_NAME),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.LOGIN_TYPE));

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

        if (callAPIId == Constants.ApiRequestCode.VEHICLE_NO) {
            VehiclePlacedListResponse vehiclePlacedListResponse = (VehiclePlacedListResponse) object;
            getVehicleNO(vehiclePlacedListResponse);
        }

        if (callAPIId == Constants.ApiRequestCode.VEHICLE_LIST) {
            VehiclePlacedListResponse vehiclePlacedListResponse = (VehiclePlacedListResponse) object;
            getVehicleList(vehiclePlacedListResponse);
        }
    }

    private void getVehicleNO(VehiclePlacedListResponse vehiclePlacedListResponse) {
        try {
            Log.e(TAG, "vehiclePlacedListResponse: " + new GsonBuilder().create().toJson(vehiclePlacedListResponse));
            if (vehiclePlacedListResponse != null) {
                if (vehiclePlacedListResponse.getPlace_no()!=null) {
                    Infrastructure.dismissProgressDialog();

                    NoOfVehicle.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.HORIZONTAL, false));
                    VehiclePlacedNoAdapter listAdapter = new VehiclePlacedNoAdapter(this,
                            vehiclePlacedListResponse.getPlace_no(),NoOfVehicle, vehiclePlacedListResponse);
                    NoOfVehicle.setAdapter(listAdapter);
                    NoOfVehicle.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                }else {
                    Toast.makeText(this, vehiclePlacedListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in vehiclePlacedListResponse" + vehiclePlacedListResponse.getResponseCode());
                Toast.makeText(this, vehiclePlacedListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in vehiclePlacedListResponse", e);
        }
    }


    private void getVehicleList(VehiclePlacedListResponse vehiclePlacedListResponse) {
        try {
            Log.e(TAG, "vehiclePlacedListResponse: " + new GsonBuilder().create().toJson(vehiclePlacedListResponse));
            if (vehiclePlacedListResponse != null) {
                if (vehiclePlacedListResponse.getDivplace_list()!=null) {
                    Infrastructure.dismissProgressDialog();

                    list.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, false));
                    VehiclePlacedListAdapter listAdapter = new VehiclePlacedListAdapter(this,
                            vehiclePlacedListResponse.getDivplace_list(),list, vehiclePlacedListResponse);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                }else {
                    Toast.makeText(this, vehiclePlacedListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in vehiclePlacedListResponse" + vehiclePlacedListResponse.getResponseCode());
                Toast.makeText(this, vehiclePlacedListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in vehiclePlacedListResponse", e);
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