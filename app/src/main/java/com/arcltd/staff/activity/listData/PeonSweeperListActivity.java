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
import com.arcltd.staff.adapter.MoterCycleListAdapter;
import com.arcltd.staff.adapter.PeonSweeperListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.MoterCycleListResponse;
import com.arcltd.staff.response.SweeperPeonListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class PeonSweeperListActivity extends BaseActivity {
    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    private MoterCycleListAdapter.RerfreshWishList activeRerfreshWishList;
    String branch_code, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peon_sweeper_list);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        list = findViewById(R.id.list);
        swiptoRefresh = findViewById(R.id.swiptoRefresh);

        branch_code = Objects.requireNonNull(getIntent().getExtras()).getString("branch_code");
        type = Objects.requireNonNull(getIntent().getExtras()).getString("type");


        peonSweeperList();

        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                peonSweeperList();
                swiptoRefresh.setRefreshing(false);
            }
        });

    }

    private void peonSweeperList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.sweeperPeonList(disposable, Constants.ApiRequestCode.MOTERCYCLELIST,
                        branch_code, type);

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
        if (callAPIId == Constants.ApiRequestCode.MOTERCYCLELIST) {
            SweeperPeonListResponse sweeperPeonListResponse = (SweeperPeonListResponse) object;
            getPeonSweeperList(sweeperPeonListResponse);
        }
    }

    private void getPeonSweeperList(SweeperPeonListResponse sweeperPeonListResponse) {
        try {
            Log.e(TAG, "moterCycleListResponse: " + new GsonBuilder().create().toJson(sweeperPeonListResponse));
            if (sweeperPeonListResponse != null) {
                if (sweeperPeonListResponse.getSweeper_peon_list()!= null) {
                    Infrastructure.dismissProgressDialog();

                    list.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, false));
                    PeonSweeperListAdapter listAdapter = new PeonSweeperListAdapter(this,
                            sweeperPeonListResponse.getSweeper_peon_list()
                            , list, sweeperPeonListResponse);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(this, sweeperPeonListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in moterCycleListResponse" + sweeperPeonListResponse.getResponseCode());
                Toast.makeText(this, sweeperPeonListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in moterCycleListResponse", e);
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