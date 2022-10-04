package com.arcltd.staff.authentication;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arcltd.staff.R;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.response.FinalSignUpResponseResponse;
import com.arcltd.staff.response.ForgotPassResponse;
import com.arcltd.staff.response.GetOTPResponseResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.GsonBuilder;

public class ForgotPassword extends BaseActivity {

    TextView reset,resendTime,messageText;
    TextInputEditText emaiId,etOtp,conformpassword,newpassword;
    Button submitPassword,btnGetOtp;
    String email,otp,message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_forgot_password);
        reset = (TextView) findViewById(R.id.resetpass);
        etOtp = findViewById(R.id.etOtp);
        emaiId = findViewById(R.id.member_email);
        conformpassword = findViewById(R.id.conformpassword);
        resendTime = findViewById(R.id.resendTime);
        newpassword = findViewById(R.id.newpassword);
        messageText = findViewById(R.id.messageText);
        btnGetOtp = findViewById(R.id.btnGetOtp);
        email=getIntent().getExtras().getString("email");
        emaiId.setText(email);
        getOTP(emaiId.getText().toString());

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForgotPassword.this, SignupActivity.class);
                startActivity(i);
            }
        });
        emaiId = findViewById(R.id.member_email);
        submitPassword = (Button) findViewById(R.id.submit_password);

        submitPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick Edit Text Otp: "+etOtp.getText().toString()+ "  Server OTP is --"+otp );
                if (etOtp.getText().toString().equals(otp)) {
                    if (validate()) {
                        final String id = emaiId.getText().toString().trim();
                        final String pass = newpassword.getText().toString().trim();
                        final String cnfpass = conformpassword.getText().toString().trim();
                        submitPass(id, pass, cnfpass);
                    }
                }else {
                    Toast.makeText(ForgotPassword.this,"Please Enter Valid OTP",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOTP(emaiId.getText().toString());
            }
        });

    }

    private void showResetTime() {

        new CountDownTimer(60000, 1000) {
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
                        getOTP(emaiId.getText().toString());
//                Log.e("showRadioButtonDialog",nameCity);
                    }
                });
            }
        }.start();
    }


    private void getOTP(String email) {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(ForgotPassword.this);
                apiPresenter.getSignupOtp(disposable, Constants.ApiRequestCode.EMPLOYEE_OTP, email);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }

    }

    private void submitPass(String email, String pass, String cnfPass) {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(ForgotPassword.this);
                apiPresenter.getForgotPass(disposable, Constants.ApiRequestCode.FORGOT_PASSWORD,
                        email,pass,cnfPass);

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
        Infrastructure.dismissProgressDialog();

        if (callAPIId == Constants.ApiRequestCode.EMPLOYEE_OTP) {
            GetOTPResponseResponse getOTPResponseResponse = (GetOTPResponseResponse) object;
            getVaelidOTp(getOTPResponseResponse);
        }
        if (callAPIId == Constants.ApiRequestCode.FORGOT_PASSWORD) {
            ForgotPassResponse forgotPassResponse = (ForgotPassResponse) object;
            getsubmot(forgotPassResponse);
        }

    }



    private void getVaelidOTp(GetOTPResponseResponse getOTPResponseResponse) {
        try {
            Log.e(TAG, "getOTPResponseResponse: " + new GsonBuilder().create().toJson(getOTPResponseResponse));
            if (getOTPResponseResponse != null) {
                Infrastructure.dismissProgressDialog();
                if (getOTPResponseResponse.getResponseCode().equals("200")){
                    otp= String.valueOf(getOTPResponseResponse.getOtp().getCode());
                    message=getOTPResponseResponse.getMessage();
                    showResetTime();
                    messageText.setText("OTP Send At Your Register Email Id");
                    messageText.setTextColor(Color.BLUE);
                    emaiId.setTextColor(Color.BLUE);
                    btnGetOtp.setVisibility(View.GONE);
                    etOtp.setFocusableInTouchMode(true);
                    etOtp.requestFocus();

                } else {
                    Toast.makeText(this, getOTPResponseResponse.getMessage(), Toast.LENGTH_LONG).show();
                    emaiId.setTextColor(Color.RED);
                    messageText.setText("You have enter wrong email id");
                    messageText.setTextColor(Color.RED);
                    btnGetOtp.setVisibility(View.VISIBLE);


                }
            } else {
                emaiId.setTextColor(Color.RED);
                messageText.setText("You have enter wrong email id");
                messageText.setTextColor(Color.RED);
                btnGetOtp.setVisibility(View.VISIBLE);
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in getOTPResponseResponse" + getOTPResponseResponse.getResponseCode());
                Toast.makeText(this, getOTPResponseResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in getOTPResponseResponse", e);
        }
    }

    private void getsubmot(ForgotPassResponse forgotPassResponse) {
        try {
            Log.e(TAG, "forgotPassResponse: " + new GsonBuilder().create().toJson(forgotPassResponse));
            if (forgotPassResponse != null) {
                Infrastructure.dismissProgressDialog();
                if (forgotPassResponse.getResponseCode().equals("200")){
                   startActivity(new Intent(this,LoginActivity.class));
                   finish();

                } else {
                    Toast.makeText(this, forgotPassResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in forgotPassResponse" + forgotPassResponse.getResponseCode());
                Toast.makeText(this, forgotPassResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in forgotPassResponse", e);
        }
    }

    private boolean validate() {
        if (emaiId.getText().toString().length() == 0) {

            return false;
        }else if (newpassword.getText().toString().length() == 0) {
            Toast.makeText(this, "Enter New Password", Toast.LENGTH_LONG).show();
            return false;
        }else if (conformpassword.getText().toString().length() == 0) {
            Toast.makeText(this, "Enter Confirm Password", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }


}
