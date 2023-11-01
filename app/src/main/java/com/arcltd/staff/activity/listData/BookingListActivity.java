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
import com.arcltd.staff.adapter.BookingListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.BookingListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class BookingListActivity extends BaseActivity {

    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    String branch_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        list=findViewById(R.id.list);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        swiptoRefresh=findViewById(R.id.swiptoRefresh);

        if(RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE).equals("BBY RO")){
            branch_code="";
        }else {
            branch_code=RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE);
        }

        bookingList();

        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                bookingList();
                swiptoRefresh.setRefreshing(false);
            }
        });


    }

    private void bookingList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.list_booking(disposable, Constants.ApiRequestCode.DAILY_BOOKING,branch_code,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.DIVISION_ID)
                );

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }
    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "bookingListResponse_showResult: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.DAILY_BOOKING) {
            BookingListResponse bookingListResponse = (BookingListResponse) object;
            getBookingList(bookingListResponse);
        }
    }

    private void getBookingList(BookingListResponse bookingListResponse) {
        try {
            Log.e(TAG, "bookingListResponse: " + new GsonBuilder().create().toJson(bookingListResponse));
            if (bookingListResponse != null) {
                if (bookingListResponse.getBooking_list()!=null) {
                    Infrastructure.dismissProgressDialog();

                    list.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, false));
                    BookingListAdapter listAdapter = new BookingListAdapter(this, bookingListResponse.getBooking_list()
                            , list, bookingListResponse);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                }else {
                    Toast.makeText(this, bookingListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in bookingListResponse" + bookingListResponse.getResponseCode());
                Toast.makeText(this, bookingListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in bookingListResponse", e);
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