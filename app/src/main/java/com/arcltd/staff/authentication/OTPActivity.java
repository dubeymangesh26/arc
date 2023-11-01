package com.arcltd.staff.authentication;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.activity.otherAndMain.MainActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.FinalSignUpResponseResponse;
import com.arcltd.staff.response.GetOTPResponseResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Objects;

public class OTPActivity extends BaseActivity {
    ProgressBar searchProgressBar;
    TextView confirm_button,resendTime,di_mobilenoOrEmail,tvErMessege;
    EditText etOtp;
    String email,otp,message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_otpactivity);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);


        confirm_button=findViewById(R.id.confirm_button);
         di_mobilenoOrEmail=findViewById(R.id.di_mobilenoOrEmail);
         resendTime=findViewById(R.id.resendTime);
        tvErMessege=findViewById(R.id.tvErMessege);
         etOtp = (EditText) findViewById(R.id.etOTP);
        searchProgressBar = findViewById(R.id.searchProgressBar);

         if (getIntent().getExtras().getString("me").equals("No")){
             tvErMessege.setVisibility(View.VISIBLE);
             email=RetrofitClient.getStringUserPreference(this, Constants.EMAIL_ID);
             getOTP(email);
         }else {
             email=getIntent().getExtras().getString("me");
             getOTP(email);
         }
        //  otpReceive = String.valueOf(loginResponse.getResponse().getData().getOtp());
        di_mobilenoOrEmail.setText(email);



        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                resendTime.setText("You may request again in " + millisUntilFinished / 1000+"  seconds");
            }

            public void onFinish() {
                resendTime.setText("RESEND THE CODE");
                resendOTP();
                resendTime.setTextColor(getResources().getColor(R.color.appColour));
            }

            private void resendOTP() {
                resendTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getOTP(email);
//                Log.e("showRadioButtonDialog",nameCity);
                    }
                });
            }
        }.start();

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick Edit Text Otp: "+etOtp.getText().toString()+ "  Server OTP is --"+otp );
                if (etOtp.getText().toString().equals(otp)) {
                    confirmOTP(di_mobilenoOrEmail.getText().toString(),
                            etOtp.getText().toString());

                }else {
                    Toast.makeText(OTPActivity.this,"Please Enter Valid OTP",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void getOTP(String email) {
        try {
            if (Infrastructure.isInternetPresent()) {
                searchProgressBar.setVisibility(View.VISIBLE);
                apiPresenter.getSignupOtp(disposable, Constants.ApiRequestCode.EMPLOYEE_OTP, email);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }

    }

    private void confirmOTP(String email,String otp) {
        try {
            if (Infrastructure.isInternetPresent()) {
                searchProgressBar.setVisibility(View.VISIBLE);
                apiPresenter.submitSignOTP(disposable, Constants.ApiRequestCode.EMPLOYEE_OTP_SUBMIT,
                        email,otp,"A","Verified");

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }

    }

    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("showResult", "showResult: " + object);
        searchProgressBar.setVisibility(View.GONE);

        if (callAPIId == Constants.ApiRequestCode.EMPLOYEE_OTP) {
            GetOTPResponseResponse getOTPResponseResponse = (GetOTPResponseResponse) object;
            getVaelidOTp(getOTPResponseResponse);
        }

        if (callAPIId == Constants.ApiRequestCode.EMPLOYEE_OTP_SUBMIT) {
            FinalSignUpResponseResponse employeeListResponse = (FinalSignUpResponseResponse) object;
            setSubmitOTp(employeeListResponse);
        }
    }



    private void getVaelidOTp(GetOTPResponseResponse getOTPResponseResponse) {
        try {
            Log.e(TAG, "getOTPResponseResponse: " + new GsonBuilder().create().toJson(getOTPResponseResponse));
            if (getOTPResponseResponse != null) {
                searchProgressBar.setVisibility(View.GONE);
                if (getOTPResponseResponse.getResponseCode().equals("200")){
                     otp= String.valueOf(getOTPResponseResponse.getOtp().getCode());
                     message=getOTPResponseResponse.getMessage();


                } else {
                    Toast.makeText(this, getOTPResponseResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in getOTPResponseResponse" + getOTPResponseResponse.getResponseCode());
                Toast.makeText(this, getOTPResponseResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in getOTPResponseResponse", e);
        }
    }

    private void setSubmitOTp(FinalSignUpResponseResponse finalSignUpResponseResponse) {
        try {
            Log.e(TAG, "finalSignUpResponseResponse: " + new GsonBuilder().create().toJson(finalSignUpResponseResponse));
            if (finalSignUpResponseResponse != null) {
                searchProgressBar.setVisibility(View.GONE);
                if (finalSignUpResponseResponse.getResponseCode().equals("200")) {
                    RetrofitClient.saveUserPreference(OTPActivity.this,
                            Constants.VEARIFICATION_STATUS, "Verified");

                    startActivity(new Intent(OTPActivity.this, MainActivity.class));
                    finish();
                    Toast.makeText(this, finalSignUpResponseResponse.getMessage(), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, finalSignUpResponseResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in finalSignUpResponseResponse" + finalSignUpResponseResponse.getResponseCode());
                Toast.makeText(this, finalSignUpResponseResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in finalSignUpResponseResponse", e);
        }

    }




    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        Infrastructure.dismissProgressDialog();
        Log.e("login", "login: " + errorCode);
        if (callAPIId == Constants.ApiRequestCode.EMPLOYEE_OTP) {
            new ErrorHandlerCode(OTPActivity.this, errorCode, message);
        }
        if (callAPIId == Constants.ApiRequestCode.EMPLOYEE_OTP_SUBMIT) {
            new ErrorHandlerCode(OTPActivity.this, errorCode, message);
        }
    }


    @Override
    public void showArrayResult(List genericObj, int callAPIId) {
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