package com.arcltd.staff.authentication;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.response.UpdatePassResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Objects;

public class ResetPassword extends BaseActivity {
    TextView signup;
    TextInputEditText oPassword, nPassword, emails, cPassword;
    Button changePassword;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        signup = (TextView) findViewById(R.id.signupfromchanepass);
        oPassword = findViewById(R.id.oldpassword);
        nPassword = findViewById(R.id.newpassword);
        emails = findViewById(R.id.emailid);
        cPassword = findViewById(R.id.conformpassword);
        changePassword = (Button) findViewById(R.id.resetpasswrd);
        /*signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResetPassword.this, Sign.class);
                startActivity(i);
            }
        });*/

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String oldPassw = oPassword.getText().toString().trim();
                final String newPassw = nPassword.getText().toString().trim();
                final String emailadd = emails.getText().toString().trim();
                final String cnfrmpass = cPassword.getText().toString().trim();
                if (validate())
                    changePassword(newPassw, oldPassw, emailadd, cnfrmpass);

            }
        });


    }

    private void changePassword(String newPass, String oldPass, final String email, String cnfrmpasrd) {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(ResetPassword.this);
                apiPresenter.userResetPass(disposable, Constants.ApiRequestCode.RESET_PASSWORD, email, oldPass, newPass);
                Log.e("login_Rquest", "login_Rquest: " + apiPresenter);
            } else {
                new ErrorHandlerCode(ResetPassword.this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("Exception", "Exception in getDataFromWebservice", e);
        }

    }

    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("showResult", "showResult: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.RESET_PASSWORD) {
            // LoginResponse loginResponse = new Gson().fromJson(object, LoginResponse.class);
            UpdatePassResponse updatePassResponse = (UpdatePassResponse) object;
            updatePass(updatePassResponse);
        }

    }

    private void updatePass(UpdatePassResponse updatePassResponse) {
        try {
            Log.e(TAG, "updatePassResponse: " + new GsonBuilder().create().toJson(updatePassResponse));
            if (updatePassResponse != null) {
                Infrastructure.dismissProgressDialog();
                Toast.makeText(this, updatePassResponse.getMessage(), Toast.LENGTH_LONG).show();
                finish();

            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in updatePassResponse" + updatePassResponse.getResponsecode());
                Toast.makeText(this, updatePassResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in updatePassResponse", e);
        }
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        Infrastructure.dismissProgressDialog();
        Log.e("login", "login: " + errorCode);
        if (callAPIId == Constants.ApiRequestCode.USER_LOGIN) {
            new ErrorHandlerCode(ResetPassword.this, errorCode, message);
        }
    }


    @Override
    public void showArrayResult(List genericObj, int callAPIId) {
    }


    private boolean validate() {
        String pass = Objects.requireNonNull(nPassword.getText()).toString();
        String cpass = Objects.requireNonNull(cPassword.getText()).toString();
        if (Objects.requireNonNull(oPassword.getText()).toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Pleas Enter Old Password!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (nPassword.getText().toString().length() != 0) {
            if (!pass.equals(cpass)) {
                Toast.makeText(getApplicationContext(), "Password Not matching!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Pleas Enter New Password!", Toast.LENGTH_SHORT).show();
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
