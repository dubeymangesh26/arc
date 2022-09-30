package com.arcltd.staff.authentication;

import static android.content.ContentValues.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.otherAndMain.MainActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.LoginRespponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.arcltd.staff.utility.UserSessionManager;
import com.arcltd.staff.web_view.PrivacyPolicyWebViewActivity;
import com.arcltd.staff.web_view.TermAndConditionWebViewActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.GsonBuilder;

import java.security.KeyStore;
import java.util.List;

import javax.crypto.Cipher;

public class LoginActivity extends BaseActivity {
    private SharedPreferences pref;
    private KeyStore keyStore;
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "androidHive";
    private Cipher cipher;

    private static final int RC_SIGN_IN = 9001;
    TextInputEditText editTextEmail, editTextPassword;

    private ProgressBar progressBar;
    ImageView image_logo,signup;
    TextView reGister,logout;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
// to make status bar transparent
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        image_logo = findViewById(R.id.image_logo);
        reGister = findViewById(R.id.reGister);
        logout = findViewById(R.id.logout);
        signup = findViewById(R.id.signup);
        // loginButton = findViewById(R.id.loginButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Glide.with(this).load(R.drawable.eagle).into(image_logo);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast

                        Log.d(TAG, token);
                        RetrofitClient.saveUserPreference(LoginActivity.this, Constants.DEVICE_TOKEN, token);

                    }
                });

        if (RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.FIRSTNAME)!=null){
              editTextEmail.setText(RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.EMAIL_ID));
            editTextEmail.setEnabled(false);
            reGister.setVisibility(View.GONE);
            signup.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);
        }else {
            reGister.setVisibility(View.VISIBLE);
            signup.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Are you sure you want to Logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (Boolean.valueOf(RetrofitClient.getStringUserPreference(LoginActivity.this, Constants.MEMBER_ID)) == true) {
                                    session.logoutUser();
                                } else {

                                }
                                RetrofitClient.clearAllPreference(LoginActivity.this);
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();
                                sharedPreferences.edit().clear().apply();
                                sharedPreferences.edit().remove("COU").commit();
                                reGister.setVisibility(View.VISIBLE);
                                signup.setVisibility(View.VISIBLE);
                                logout.setVisibility(View.GONE);
                                editTextEmail.setText("");
                                editTextEmail.setEnabled(true);
                                /*    HomeActivity.this.finish();*/
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(Color.rgb(30, 144, 255));
                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setTextColor(Color.rgb(30, 144, 255));

            }
        });

    }


    public void onClickForgotPass(View view) {
        if (validate_two()) {
          //  forgotPassoword();
           // editTextEmail.getText().toString().trim();

            startActivity(new Intent(this,ForgotPassword.class)
                    .putExtra("email",editTextEmail.getText().toString()));


        }
    }


    public void onRegisterClick(View view) {
        startActivity(new Intent(this, SignupActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }

    public void SignIn(View view) {

    }

    public void onClicklogin(View view) {
        if (validate()) {
            login(
                    editTextEmail.getText().toString().trim(),
                    editTextPassword.getText().toString().trim());
        }


        // progressBar.setVisibility(View.VISIBLE);
        //create user

    }

    private boolean validate() {
        if (editTextEmail.getText().toString().equals("")) {
            Toast.makeText(LoginActivity.this, "Enter email address!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (editTextPassword.getText().toString().equals("")) {
            Toast.makeText(LoginActivity.this, "Enter password!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    private boolean validate_two() {
        if (editTextEmail.getText().toString().equals("")) {
            Toast.makeText(LoginActivity.this, "Enter email address!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void login(String email, String passward) {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(LoginActivity.this);
                apiPresenter.userLogin(disposable, Constants.ApiRequestCode.USER_LOGIN, email, passward);
                Log.e("login_Rquest", "login_Rquest: " + apiPresenter);
            } else {
                new ErrorHandlerCode(LoginActivity.this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("Exception", "Exception in getDataFromWebservice", e);
        }
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("showResult", "showResult: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.USER_LOGIN) {
            // LoginResponse loginResponse = new Gson().fromJson(object, LoginResponse.class);
            LoginRespponse loginResponse = (LoginRespponse) object;
            finallogin(loginResponse);
        }

    }


    private void finallogin(LoginRespponse loginResponse) {
        try {
            Log.e("login", "login: " + new GsonBuilder().create().toJson(loginResponse));
            if (loginResponse.getResponsecode().equals("200")) {

                RetrofitClient.saveUserPreference(LoginActivity.this, Constants.BRANCH_CODE, loginResponse.getUser().getBranch_code());
                RetrofitClient.saveUserPreference(LoginActivity.this, Constants.BRANCH_NAME, loginResponse.getUser().getBranch_name());
                RetrofitClient.saveUserPreference(LoginActivity.this, Constants.EMP_CODE, loginResponse.getUser().getEmp_code());
                RetrofitClient.saveUserPreference(LoginActivity.this, Constants.DESIGN, loginResponse.getUser().getDesign());
                RetrofitClient.saveUserPreference(LoginActivity.this, Constants.FIRSTNAME, loginResponse.getUser().getName());
                RetrofitClient.saveUserPreference(LoginActivity.this, Constants.EMAIL_ID, loginResponse.getUser().getEmail());
                RetrofitClient.saveUserPreference(LoginActivity.this, Constants.MOBILE_NO, loginResponse.getUser().getContactno());
                RetrofitClient.saveUserPreference(LoginActivity.this, Constants.LOGIN_TYPE, loginResponse.getUser().getLogin_type());
                RetrofitClient.saveUserPreference(LoginActivity.this, Constants.STATUS, loginResponse.getUser().getStatus());
                RetrofitClient.saveUserPreference(LoginActivity.this, Constants.REGION_ID, loginResponse.getUser().getRegion_id());
                RetrofitClient.saveUserPreference(LoginActivity.this, Constants.REGION_NAME, loginResponse.getUser().getRegion_name());
                RetrofitClient.saveUserPreference(LoginActivity.this, Constants.VEARIFICATION_STATUS, loginResponse.getUser().getVari_status());
                RetrofitClient.saveUserPreference(LoginActivity.this, Constants.PROFILE_PICTURE, loginResponse.getUser().getProfilepic());
                if (!loginResponse.getUser().getDivision_id().equals("")) {
                    RetrofitClient.saveUserPreference(LoginActivity.this, Constants.DIVISION_ID, loginResponse.getUser().getDivision_id());
                    RetrofitClient.saveUserPreference(LoginActivity.this, Constants.DIVISION_NAME, loginResponse.getUser().getDivision_name());
                }
                if (loginResponse.getUser().getVari_status().equals("Verified")) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(LoginActivity.this, EmployeeStatusCheckActivity.class));
                    finish();
                }

            } else if (loginResponse.getResponsecode().equals("404")) {
                Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
            } else if (loginResponse.getResponsecode().equals("304")) {
                Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
            } else if (loginResponse.getResponsecode().equals("400")) {
                Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
            } else if (loginResponse.getResponsecode().equals("401")) {
                Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
            } else if (loginResponse.getResponsecode().equals("405")) {
                Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
            }
            //  new ErrorHandlerCode(LoginActivity.this, loginResponse.getResponse().getCode(), loginResponse.getResponse().getMsg());

        } catch (Exception e) {
            ELog.e("Exception", "Exception in checkLoginResponse", e);
        }

    }


    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        Infrastructure.dismissProgressDialog();
        Log.e("login", "login: " + errorCode);
        if (callAPIId == Constants.ApiRequestCode.USER_LOGIN) {
            new ErrorHandlerCode(LoginActivity.this, errorCode, message);
        }
    }


    @Override
    public void showArrayResult(List genericObj, int callAPIId) {
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Really Exit ??");
        builder.setTitle(R.string.app_name);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new MyListener());
        builder.setNegativeButton("Cancel", null);
        builder.show();

    }

    public void onPrivacyPolicyClick(View view) {
        startActivity(new Intent(LoginActivity.this, PrivacyPolicyWebViewActivity.class));
    }

    public void onTermandConditionClick(View view) {
        startActivity(new Intent(LoginActivity.this, TermAndConditionWebViewActivity.class));
    }


    private class MyListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  progressBar.setVisibility(View.GONE);
    }


}