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
import com.arcltd.staff.adapter.LinkListAdapter;
import com.arcltd.staff.adapter.PDFListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.response.LinkListResponse;
import com.arcltd.staff.response.PDFListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;

public class LinkListActivity extends BaseActivity {
    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    String branch_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_list);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        list=findViewById(R.id.list);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        swiptoRefresh=findViewById(R.id.swiptoRefresh);


        ArcLinkList();

        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                ArcLinkList();
                swiptoRefresh.setRefreshing(false);
            }
        });



    }

    private void ArcLinkList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.list_link(disposable, Constants.ApiRequestCode.LINK_LIST,"");

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }
    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "pdfListResponse_showResult: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.LINK_LIST) {
            LinkListResponse linkListResponse = (LinkListResponse) object;
            getPdfList(linkListResponse);
        }
    }

    private void getPdfList(LinkListResponse linkListResponse) {
        try {
            Log.e(TAG, "bookingListResponse: " + new GsonBuilder().create().toJson(linkListResponse));
            if (linkListResponse != null) {
                if (linkListResponse.getPdf_list()!=null) {
                    Infrastructure.dismissProgressDialog();

                    list.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, false));
                    LinkListAdapter listAdapter = new LinkListAdapter(this, linkListResponse.getPdf_list()
                            , list, linkListResponse);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                }else {
                    Toast.makeText(this, linkListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in LinkListResponse" + linkListResponse.getResponseCode());
                Toast.makeText(this, linkListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in LinkListResponse", e);
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