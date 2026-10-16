package com.arcltd.staff.activity.listData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.addData.AddPlacedVehicleActivity;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.adapter.VehiclePlacedItemAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.AddPlacedVehicleResponse;
import com.arcltd.staff.response.VehiclePlacedItemListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;

public class BranchVehicleListActivity extends BaseActivity {
    RecyclerView list;
    String branch_code;
    SwipeRefreshLayout swiptoRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_vehicle_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        if (getIntent().getExtras() != null) {
            branch_code = getIntent().getExtras().getString("data");
        }

        list=findViewById(R.id.list);
        swiptoRefresh=findViewById(R.id.swiptoRefresh);


        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                vehicleItemList();
                swiptoRefresh.setRefreshing(false);
            }
        });


        vehicleItemList();
    }

    private void vehicleItemList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.listVehicleitem(disposable, Constants.ApiRequestCode.VEHICLE_LIST_ITEM,branch_code,"");

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }



    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult_addPlacedVehicleResponse: " + object);
        Infrastructure.dismissProgressDialog();

        if (callAPIId == Constants.ApiRequestCode.VEHICLE_LIST_ITEM) {
            VehiclePlacedItemListResponse vehiclePlacedItemListResponse = (VehiclePlacedItemListResponse) object;
            vehicle_Item(vehiclePlacedItemListResponse);
        }
    }



    private void vehicle_Item(VehiclePlacedItemListResponse vehiclePlacedItemListResponse) {
        try {
            Log.e(TAG, "vehiclePlacedItemListResponse: " + new GsonBuilder().create().toJson(vehiclePlacedItemListResponse));
            if (vehiclePlacedItemListResponse != null) {

                list.setLayoutManager(new LinearLayoutManager(
                        this, RecyclerView.VERTICAL, false));

                VehiclePlacedItemAdapter listAdapter = new VehiclePlacedItemAdapter(BranchVehicleListActivity.this,
                        vehiclePlacedItemListResponse.getPlace_no(), list, vehiclePlacedItemListResponse);
                list.setAdapter(listAdapter);
                list.setVisibility(View.VISIBLE);
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in addPlacedVehicleResponse" + vehiclePlacedItemListResponse.getResponseCode());
                Toast.makeText(this, vehiclePlacedItemListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in addPlacedVehicleResponse", e);
        }

    }



}