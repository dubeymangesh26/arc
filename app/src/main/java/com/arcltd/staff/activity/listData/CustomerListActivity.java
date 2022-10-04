package com.arcltd.staff.activity.listData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.addData.AddUpdateCustomerActivity;
import com.arcltd.staff.adapter.CustomerListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.CustomerListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class CustomerListActivity extends BaseActivity {

    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    private CustomerListAdapter.RerfreshWishList activeRerfreshWishList;
    String data;
    AutoCompleteTextView atSearch;
    ProgressBar searchProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_customer_list);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        list=findViewById(R.id.list);
        swiptoRefresh=findViewById(R.id.swiptoRefresh);
        searchProgressBar=findViewById(R.id.searchProgressBar);
        atSearch=findViewById(R.id.atSearch);

        custoimerList();

        atSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                custoimerList();
            }
        });


        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                custoimerList();
                swiptoRefresh.setRefreshing(false);
            }
        });

    }

    private void custoimerList() {

        try {
            if (Infrastructure.isInternetPresent()) {
                searchProgressBar.setVisibility(View.VISIBLE);
                apiPresenter.list_Customer(disposable, Constants.ApiRequestCode.CUSTOMER_LIST,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.DIVISION_ID),
                        atSearch.getText().toString());

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }
    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult_customerListResponse: " + object);
        searchProgressBar.setVisibility(View.GONE);
        if (callAPIId == Constants.ApiRequestCode.CUSTOMER_LIST) {
            CustomerListResponse customerListResponse = (CustomerListResponse) object;
            getCustList(customerListResponse);
        }
    }

    private void getCustList(CustomerListResponse customerListResponse) {
        try {
            Log.e(TAG, "customerListResponse: " + new GsonBuilder().create().toJson(customerListResponse));
            if (customerListResponse != null) {
                if (customerListResponse.getCustomer_list()!=null) {
                    searchProgressBar.setVisibility(View.GONE);

                    list.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, false));
                    CustomerListAdapter listAdapter = new CustomerListAdapter(this,
                            customerListResponse.getCustomer_list(), list, customerListResponse);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                }else {
                    Toast.makeText(this, customerListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in customerListResponse" + customerListResponse.getResponseCode());
                Toast.makeText(this, customerListResponse.getMessage(), Toast.LENGTH_LONG).show();

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

    public void addCustomerClick(View view) {
        startActivity(new Intent(this, AddUpdateCustomerActivity.class));

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