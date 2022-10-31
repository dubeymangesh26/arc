package com.arcltd.staff.activity.updateData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.activity.listData.LandLordListActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.response.LandlordGodownListResponse;
import com.arcltd.staff.response.LandlordUpdateResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.DatePickerHelper;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class UpdateLandlordDetailsActivity extends BaseActivity {

    TextInputEditText etBranchCode,etLocationAddress,etLandlordName,etLandlordPan,etLandlordAddress,etCastCode,etPlacetpe,
            etfrom,etTo,etRentAmt,etOfsGdnArea,etOpenArea;

    String landLord_id,data,branch_code;
    LandlordGodownListResponse.LandlordGodownList dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_update_landlord_details);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);


        etBranchCode=findViewById(R.id.etBranchCode);
        etLocationAddress=findViewById(R.id.etLocationAddress);
        etLandlordName=findViewById(R.id.etLandlordName);
        etLandlordPan=findViewById(R.id.etLandlordPan);
        etLandlordAddress=findViewById(R.id.etLandlordAddress);
        etCastCode=findViewById(R.id.etCastCode);
        etPlacetpe=findViewById(R.id.etPlacetpe);
        etfrom=findViewById(R.id.etfrom);
        etTo=findViewById(R.id.etTo);
        etRentAmt=findViewById(R.id.etRentAmt);
        etOfsGdnArea=findViewById(R.id.etOfsGdnArea);
        etOpenArea=findViewById(R.id.etOpenArea);

        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("data");
            dataBean = new Gson().fromJson(data, LandlordGodownListResponse.LandlordGodownList.class);
            Log.e("", "onCreate: " + new GsonBuilder().create().toJson(dataBean));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }


        try {
            etBranchCode.setText(dataBean.getBranch_code());
            etLocationAddress.setText(dataBean.getLocation_address());
            etLandlordName.setText(dataBean.getLandlord_name());
            etLandlordPan.setText(dataBean.getPan_no());
            etLandlordAddress.setText(dataBean.getLandlord_address());
            etCastCode.setText(dataBean.getCost_code());
            etPlacetpe.setText(dataBean.getOff_gdn_residence());
            etfrom.setText(dataBean.getPeriod_from());
            etTo.setText(dataBean.getPeriod_to());
            etRentAmt.setText(dataBean.getRent_amount());
            etOfsGdnArea.setText(dataBean.getOfgdn_area());
            etOpenArea.setText(dataBean.getOpen_area());
            landLord_id=dataBean.getLandlord_id();
        } catch (Exception e) {
            Log.e("", "ListingDetail error: " + e);
        }

        etfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFromDatePickerDialog();
            }
        });
        etTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showetToDatePickerDialog();
            }
        });


    }

    private void showetToDatePickerDialog() {
        DatePickerHelper.showDatePicker(this, new DatePickerHelper.OnDateSelectedListener() {
            @Override
            public void onDateSelected(String selectedDateString) {
                etTo.setText(selectedDateString);
            }
        },true);

    }

    private void showFromDatePickerDialog() {
        DatePickerHelper.showDatePicker(this, new DatePickerHelper.OnDateSelectedListener() {
            @Override
            public void onDateSelected(String selectedDateString) {
                etfrom.setText(selectedDateString);
            }
        },true);
    }


    private void updateLanlord() {

        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.landlordUpdate(disposable, Constants.ApiRequestCode.UPDATELANDLORD,
                         etBranchCode.getText().toString(),landLord_id,etCastCode.getText().toString()
                        ,etLocationAddress.getText().toString(),
                        etLandlordName.getText().toString(),etLandlordAddress.getText().toString(),
                        etLandlordPan.getText().toString(), etPlacetpe.getText().toString(),
                        etfrom.getText().toString(),etTo.getText().toString(),
                        etRentAmt.getText().toString(),etOfsGdnArea.getText().toString(),
                        etOpenArea.getText().toString());

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataADMINI_EXP", e);
        }
    }
    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.UPDATELANDLORD) {
            LandlordUpdateResponse landlordUpdateResponse = (LandlordUpdateResponse) object;
            getAdvRes(landlordUpdateResponse);
        }
    }



    private void getAdvRes(LandlordUpdateResponse landlordUpdateResponse) {
        try {
            Log.e(TAG, "landlordUpdateResponse: " + new GsonBuilder().create().toJson(landlordUpdateResponse));
            if (landlordUpdateResponse != null) {
                if (landlordUpdateResponse.getResponseCode().equals("200")) {
                    Infrastructure.dismissProgressDialog();
                    Toast.makeText(this, landlordUpdateResponse.getMessage(), Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(this, LandLordListActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(this, landlordUpdateResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + landlordUpdateResponse.getResponseCode());
                Toast.makeText(this, landlordUpdateResponse.getMessage(), Toast.LENGTH_LONG).show();

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

    public void onClickupdate(View view) {
        updateLanlord();
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