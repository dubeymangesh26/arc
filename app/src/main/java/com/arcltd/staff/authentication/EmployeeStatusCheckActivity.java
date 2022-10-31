package com.arcltd.staff.authentication;

import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.constraintlayout.widget.Constraints;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.activity.otherAndMain.MainActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.CheckStatusResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;

public class EmployeeStatusCheckActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_status_check);

        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);


        if (RetrofitClient.getStringUserPreference(this, Constants.VEARIFICATION_STATUS) == null) {
            ativeStatus();
        } else {
            startActivity(new Intent(EmployeeStatusCheckActivity.this, MainActivity.class)
                    .putExtra("me","No"));
        }
    }

    private void ativeStatus() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);

                apiPresenter.checkStaus(disposable, Constants.ApiRequestCode.ACTIV_STATUS,
                        RetrofitClient.getStringUserPreference(EmployeeStatusCheckActivity.this, Constants.EMP_CODE));

            } else {
                new ErrorHandlerCode(EmployeeStatusCheckActivity.this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }


    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.ACTIV_STATUS) {
            CheckStatusResponse checkStatusResponse = (CheckStatusResponse) object;
            getStatus(checkStatusResponse);
        }

    }

    private void getStatus(CheckStatusResponse checkStatusResponse) {
        try {
            // Log.e(Constraints.TAG, "branchListResponse: " + new GsonBuilder().create().toJson(checkStatusResponse));
            if (checkStatusResponse != null) {
                Infrastructure.dismissProgressDialog();
                // Log.e("ListSize ", "ArrySize" + f);

                if (!checkStatusResponse.getUserName().getVari_status().equals("Verified")) {
                    startActivity(new Intent(EmployeeStatusCheckActivity.this,OTPActivity.class)
                            .putExtra("me","No"));
                }else {

                    RetrofitClient.saveUserPreference(EmployeeStatusCheckActivity.this,
                            Constants.VEARIFICATION_STATUS, checkStatusResponse.getUserName().getVari_status());

                    RetrofitClient.saveUserPreference(EmployeeStatusCheckActivity.this,
                            Constants.NOOF_EMPLOYEE, String.valueOf(checkStatusResponse.getEmployee().size()));



                }

            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(Constraints.TAG, "Exception in checkLoginResponse" + checkStatusResponse.getResponseCode());
                Toast.makeText(this, checkStatusResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(Constraints.TAG, "Exception in checkLoginResponse", e);
        }
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
    }


}