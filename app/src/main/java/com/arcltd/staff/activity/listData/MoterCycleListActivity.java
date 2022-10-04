package com.arcltd.staff.activity.listData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.addData.AddMoterCycleActivity;
import com.arcltd.staff.adapter.DivisionListAdapter;
import com.arcltd.staff.adapter.MoterCycleListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.DivisionListResponse;
import com.arcltd.staff.response.MoterCycleListResponse;
import com.arcltd.staff.response.SoldMoterCycleResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class MoterCycleListActivity extends BaseActivity implements MoterCycleListAdapter.SoldVehicle {
    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    private MoterCycleListAdapter.RerfreshWishList activeRerfreshWishList;
    String branch_code,status="";
    AutoCompleteTextView atSearch;
    ProgressBar searchProgressBar;
    TabLayout tabLayout;
    Button addMoterCycle;
    int tabPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_moter_cycle_list);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        list=findViewById(R.id.list);
        swiptoRefresh=findViewById(R.id.swiptoRefresh);
        searchProgressBar=findViewById(R.id.searchProgressBar);
        atSearch=findViewById(R.id.atSearch);
        tabLayout=findViewById(R.id.tabLayout);
        addMoterCycle=findViewById(R.id.addMoterCycle);

        if (RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.LOGIN_TYPE).equals("BM")){
            addMoterCycle.setVisibility(View.VISIBLE);
        }else {
            addMoterCycle.setVisibility(View.GONE);
        }

        addMoterCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoterCycleListActivity.this, AddMoterCycleActivity.class));
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();


                switch (tab.getPosition()) {
                    case 0:
                        // using native contacts selection
                        status="A";
                        motercycleList();

                        break;
                    case 1:
                        status="N";
                        motercycleList();
                        break;
                    case 2:
                        status="R";
                        motercycleList();
                        break;

                    case 3:
                        status="I";
                        motercycleList();
                        break;

                    case 4:
                        status="S";
                        motercycleList();
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



      /*  cbSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    status="Y";
                    motercycleList();
                }
                else{
                    status="";
                    motercycleList();
                    // Do your coding
                }
            }
        });*/


        try {
            if (!getIntent().getExtras().getString("branch_code").equals("T")) {
                branch_code = getIntent().getExtras().getString("branch_code");
            } else {
                branch_code = RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE);
            }
            status = getIntent().getExtras().getString("status");
            if(getIntent().getExtras().getString("status").equals("B")){
                tabLayout.setVisibility(View.GONE);
            }else {
                tabLayout.setVisibility(View.VISIBLE);
            }

        }catch (Exception e){
            Log.e(TAG, "onCreate: ",e );
        }

        motercycleList();


        atSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                motercycleList();
            }
        });

        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                motercycleList();
                swiptoRefresh.setRefreshing(false);
            }
        });

    }
    private void motercycleList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                searchProgressBar.setVisibility(View.VISIBLE);
                apiPresenter.motercycleList(disposable, Constants.ApiRequestCode.MOTERCYCLELIST,
                        branch_code,atSearch.getText().toString(),status);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }

    private void motercycleSolded(String vehicle_no) {
        try {
            if (Infrastructure.isInternetPresent()) {
                searchProgressBar.setVisibility(View.VISIBLE);
                apiPresenter.motercyclesld(disposable, Constants.ApiRequestCode.MOTERCYCLE_SOLD,
                        vehicle_no,"Y");

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
            MoterCycleListResponse moterCycleListResponse = (MoterCycleListResponse) object;
            getmoterCycleList(moterCycleListResponse);
        }
        if (callAPIId == Constants.ApiRequestCode.MOTERCYCLE_SOLD) {
            SoldMoterCycleResponse soldMoterCycleResponse = (SoldMoterCycleResponse) object;
            getmoterCyclesold(soldMoterCycleResponse);
        }
    }



    @SuppressLint("NotifyDataSetChanged")
    private void getmoterCycleList(MoterCycleListResponse moterCycleListResponse) {
        try {
            Log.e(TAG, "moterCycleListResponse: " + new GsonBuilder().create().toJson(moterCycleListResponse));

            if (moterCycleListResponse != null) {
                if (moterCycleListResponse.getMoter_cycle()!=null) {
                    searchProgressBar.setVisibility(View.GONE);

                    list.setVisibility(View.VISIBLE);
                    list.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, false));
                    MoterCycleListAdapter listAdapter = new MoterCycleListAdapter(this,
                            moterCycleListResponse.getMoter_cycle()
                            , list, moterCycleListResponse,this);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                     listAdapter.notifyDataSetChanged();


                }else {
                    list.setVisibility(View.GONE);
                    Toast.makeText(this, moterCycleListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                list.setVisibility(View.GONE);
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in moterCycleListResponse" + moterCycleListResponse.getResponseCode());
                Toast.makeText(this, moterCycleListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in moterCycleListResponse", e);
        }
    }


    private void getmoterCyclesold(SoldMoterCycleResponse soldMoterCycleResponse) {
        try {
            Log.e(TAG, "soldMoterCycleResponse: " + new GsonBuilder().create().toJson(soldMoterCycleResponse));
            if (soldMoterCycleResponse != null) {
                 motercycleList();
                Toast.makeText(this, soldMoterCycleResponse.getMessage(), Toast.LENGTH_LONG).show();

            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in moterCycleListResponse" + soldMoterCycleResponse.getResponseCode());
                Toast.makeText(this, soldMoterCycleResponse.getMessage(), Toast.LENGTH_LONG).show();

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
    public void soldVehi(String vehicle_no) {
        motercycleSolded(vehicle_no);
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

    @Override
    protected void onResume() {

        super.onResume();
    }
}