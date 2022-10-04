package com.arcltd.staff.activity.listData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.adapter.DivisionListAdapter;
import com.arcltd.staff.adapter.OfiiceGodownMessListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.OfficeGodownMessListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class MessOfficeGodownListActivity extends BaseActivity {
    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    private OfiiceGodownMessListAdapter.RerfreshWishList activeRerfreshWishList;
    String div_id,search;
    AutoCompleteTextView atSearch;
    ProgressBar searchProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_mess_office_godown_list);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        try {
            div_id = Objects.requireNonNull(getIntent().getExtras()).getString("div_id");

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }
        list=findViewById(R.id.list);
        swiptoRefresh=findViewById(R.id.swiptoRefresh);
        searchProgressBar=findViewById(R.id.searchProgressBar);
        atSearch=findViewById(R.id.atSearch);
        messOfficeGdnList();

        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                messOfficeGdnList();
                swiptoRefresh.setRefreshing(false);
            }
        });

        atSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                messOfficeGdnList();
            }
        });

    }

    private void messOfficeGdnList() {
        try {
            if (Infrastructure.isInternetPresent()) {
               // Infrastructure.showProgressDialog(this);
                searchProgressBar.setVisibility(View.VISIBLE);
                apiPresenter.list_ofiicemessgdn(disposable, Constants.ApiRequestCode.OFFICEMESSGDN,
                        div_id,atSearch.getText().toString());

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
       // Infrastructure.dismissProgressDialog();
        searchProgressBar.setVisibility(View.GONE);
        if (callAPIId == Constants.ApiRequestCode.OFFICEMESSGDN) {
            OfficeGodownMessListResponse officeGodownMessListResponse = (OfficeGodownMessListResponse) object;
            getDivisionList(officeGodownMessListResponse);
        }
    }

    private void getDivisionList(OfficeGodownMessListResponse officeGodownMessListResponse) {
        try {
            searchProgressBar.setVisibility(View.GONE);
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(officeGodownMessListResponse));
            if (officeGodownMessListResponse != null) {
                if (officeGodownMessListResponse.getOffice_mess_rent_list()!=null) {
                  //  Infrastructure.dismissProgressDialog();
                    searchProgressBar.setVisibility(View.GONE);
                    list.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, false));
                    OfiiceGodownMessListAdapter listAdapter = new OfiiceGodownMessListAdapter(this, officeGodownMessListResponse.getOffice_mess_rent_list()
                            , list, officeGodownMessListResponse);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                }else {
                    Toast.makeText(this, officeGodownMessListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + officeGodownMessListResponse.getResponseCode());
                Toast.makeText(this, officeGodownMessListResponse.getMessage(), Toast.LENGTH_LONG).show();

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