package com.arcltd.staff.activity.listData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.adapter.PDFListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.response.PDFListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;

public class ArcStructurePDFListActivity extends BaseActivity {
    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    String branch_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_structure_pdflist);

        list=findViewById(R.id.list);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        swiptoRefresh=findViewById(R.id.swiptoRefresh);


        ArcStructurePDF();

        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                ArcStructurePDF();
                swiptoRefresh.setRefreshing(false);
            }
        });



    }

    private void ArcStructurePDF() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.list_pdf(disposable, Constants.ApiRequestCode.DAILY_BOOKING,"");

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
        if (callAPIId == Constants.ApiRequestCode.DAILY_BOOKING) {
            PDFListResponse pdfListResponse = (PDFListResponse) object;
            getPdfList(pdfListResponse);
        }
    }

    private void getPdfList(PDFListResponse pdfListResponse) {
        try {
            Log.e(TAG, "bookingListResponse: " + new GsonBuilder().create().toJson(pdfListResponse));
            if (pdfListResponse != null) {
                if (pdfListResponse.getPdf_list()!=null) {
                    Infrastructure.dismissProgressDialog();

                    list.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, false));
                    PDFListAdapter listAdapter = new PDFListAdapter(this, pdfListResponse.getPdf_list()
                            , list, pdfListResponse);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                }else {
                    Toast.makeText(this, pdfListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in pdfListResponse" + pdfListResponse.getResponseCode());
                Toast.makeText(this, pdfListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in pdfListResponse", e);
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