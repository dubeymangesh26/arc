package com.arcltd.staff.activity.addData;

import static androidx.constraintlayout.widget.Constraints.TAG;

import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.otherAndMain.MainActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.AddUpdateDailyBookingResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class AddDailyBusinessAmountActivity extends BaseActivity implements View.OnClickListener {
    TextInputEditText etParcel,etPart,etFtl,etTotal,etDate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String month,years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_add_daily_business_amount);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        etParcel = findViewById(R.id.etParcel);
        etPart = findViewById(R.id.etPart);
        etFtl = findViewById(R.id.etFtl);
        etTotal = findViewById(R.id.etTotal);
        etDate = findViewById(R.id.etDate);

        etParcel.addTextChangedListener(textWatcher);
        etPart.addTextChangedListener(textWatcher);
        etFtl.addTextChangedListener(textWatcher);

        etDate.setOnClickListener(this);

    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            try {
            if (!TextUtils.isEmpty(Objects.requireNonNull(etParcel.getText()).toString().trim())
                    || !TextUtils.isEmpty(Objects.requireNonNull(etPart.getText()).toString().trim())
                    || !TextUtils.isEmpty(Objects.requireNonNull(etFtl.getText()).toString().trim()))
            {

                    int answer = Integer.parseInt(etParcel.getText().toString().trim()) +
                            Integer.parseInt(Objects.requireNonNull(etPart.getText()).toString().trim()) +
                            Integer.parseInt(Objects.requireNonNull(etFtl.getText()).toString().trim());

                    Log.e("RESULT", String.valueOf(answer));
                    etTotal.setText(String.valueOf(answer));




            }else {
                etTotal.setText("");
            }
            }catch (Exception e) {
                Log.e("", "RESULT: " + e);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    public void submitBusinessDetails(View view) {
        if (validate()) {
            addDailyBusiness(
                    etParcel.getText().toString().trim(),
                    etPart.getText().toString().trim(),
                    etFtl.getText().toString().trim(),
                    etTotal.getText().toString().trim(),
                    etDate.getText().toString().trim());

        }
    }

    private void addDailyBusiness(String parcel, String part, String ftl, String total, String date) {


        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.addDailyBookig(disposable, Constants.ApiRequestCode.DAILY_BOOKING,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE),
                        years,month,date,parcel,part,ftl,total);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in AddDailyBusinessAmountActivity", e);
        }
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "AddDailyBusinessAmountActivity showResult: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.DAILY_BOOKING) {
            AddUpdateDailyBookingResponse addUpdateDailyBookingResponse = (AddUpdateDailyBookingResponse) object;
            submitdata(addUpdateDailyBookingResponse);
        }
    }

    private void submitdata(AddUpdateDailyBookingResponse addUpdateDailyBookingResponse) {
        try {
            Log.e(TAG, "addUpdateDailyBookingResponse: " + new GsonBuilder().create().toJson(addUpdateDailyBookingResponse));
            if (addUpdateDailyBookingResponse != null) {

                    Infrastructure.dismissProgressDialog();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    Toast.makeText(this, addUpdateDailyBookingResponse.getMessage(), Toast.LENGTH_LONG).show();


            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in addUpdateDailyBookingResponse" + addUpdateDailyBookingResponse.getResponseCode());
                Toast.makeText(this, addUpdateDailyBookingResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in addUpdateDailyBookingResponse", e);
        }
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
    }



    @Override
    public void onClick(View view) {
        if (view == etDate) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {


                            etDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            month= String.valueOf(monthOfYear+1);
                            years= String.valueOf(year);

                            Log.e(TAG, "month:-" + month+"  Year:-"+years);


                        }

                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        }

    }
    private boolean validate() {
        if (TextUtils.isEmpty(etParcel.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter  Parcel Amount!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etPart.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Part Load Amount!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etFtl.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Full Load Amount!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etDate.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Select Date!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etTotal.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Total Amount!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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