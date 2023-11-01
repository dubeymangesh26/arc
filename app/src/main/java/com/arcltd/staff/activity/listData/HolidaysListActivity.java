package com.arcltd.staff.activity.listData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.adapter.HolidaysListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.response.HolidaysResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class HolidaysListActivity extends BaseActivity {
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holidays_list);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        list = findViewById(R.id.list);

        holidaysList();
    }

    private void holidaysList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.list_Holidays(disposable, Constants.ApiRequestCode.HOLIDAYS, "" );

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "holidaysResponse_showResult: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.HOLIDAYS) {
            HolidaysResponse holidaysResponse = (HolidaysResponse) object;
            getBookingList(holidaysResponse);
        }
    }

    private void getBookingList(HolidaysResponse holidaysResponse) {
        try {
            Log.e(TAG, "holidaysResponse: " + new GsonBuilder().create().toJson(holidaysResponse));
            if (holidaysResponse != null) {
                if (holidaysResponse.getHolidysList() != null) {
                    Infrastructure.dismissProgressDialog();

                    list.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, false));
                    HolidaysListAdapter listAdapter = new HolidaysListAdapter(this, holidaysResponse.getHolidysList()
                            , list, holidaysResponse);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(this, holidaysResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in holidaysResponse" + holidaysResponse.getResponseCode());
                Toast.makeText(this, holidaysResponse.getMessage(), Toast.LENGTH_LONG).show();

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