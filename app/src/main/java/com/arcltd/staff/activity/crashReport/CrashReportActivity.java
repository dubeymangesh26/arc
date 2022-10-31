package com.arcltd.staff.activity.crashReport;

import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arcltd.staff.authentication.SplashScreenActivity;
import com.arcltd.staff.response.CrashReportREsponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;
import com.arcltd.staff.R;

import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;

import java.util.Objects;

public class CrashReportActivity extends BaseActivity {
    private TextView _report;
    private Button _btnclose,appclose;
    String deviceInfo="Device Info:",crashReport="",customerID="";
    private ProgressDialog pDialog;
    private String TAG=CrashReportActivity.class.getSimpleName();
    private String report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_report);

        Intent intent  = getIntent();
        _report = (TextView) findViewById(R.id.report);
        _report.setText(intent.getStringExtra("stackTrace"));
        _btnclose = (Button) findViewById(R.id.btnclose);
        appclose = (Button) findViewById(R.id.appclose);
        crashReport=intent.getStringExtra("stackTrace");

        Log.e(TAG, Objects.requireNonNull(intent.getStringExtra("stackTrace")));

        if (RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.CUSTOMER_ID) != null && !
                RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.CUSTOMER_ID).equals("")) {
            customerID = RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.CUSTOMER_ID);
        } else {
            customerID = "";
        }

        deviceInfo += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        deviceInfo += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        deviceInfo += "\n Device: " + android.os.Build.DEVICE;
        deviceInfo += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";


        report=deviceInfo+" :=> "+crashReport;

        _btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CrashReportActivity.this, SplashScreenActivity.class));
                finish();
            }
        });

        appclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                moveTaskToBack(true);
            }
        });

        postCrashReport();
    }
    private void postCrashReport() {
       // customerID,deviceInfo,crashReport
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(CrashReportActivity.this);
                apiPresenter.getCrashReport(disposable, Constants.ApiRequestCode.CRASH_REPORT,
                        report ,"C");
                Log.e(TAG, "crashReport: " + apiPresenter);
            } else {
                new ErrorHandlerCode(CrashReportActivity.this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in getDataFromWebservice", e);
        }
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d(TAG, "showResult: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.CRASH_REPORT) {
            // LoginResponse loginResponse = new Gson().fromJson(object, LoginResponse.class);
            CrashReportREsponse crashReportResponse = (CrashReportREsponse) object;
            crashReportRep(crashReportResponse);
        }
    }

    private void crashReportRep(CrashReportREsponse crashReportResponse) {
        try {
            Log.e(TAG, "crashReportResponse: " + new GsonBuilder().create().toJson(crashReportResponse));
            if (crashReportResponse != null) {
                Infrastructure.dismissProgressDialog();

            } else {
                Infrastructure.dismissProgressDialog();
                // ELog.e(TAG, "Exception in checkLoginResponse" + registerResponse.getResponse().getResp_code());
                // Toast.makeText(CompleateOrderDetails.this, registerResponse.getResponse().getMsg(), Toast.LENGTH_LONG).show();
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



  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        moveTaskToBack(true);
    }*/

}
