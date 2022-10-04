package com.arcltd.staff.authentication;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.otherAndMain.MainActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.utility.Constants;

import java.util.concurrent.Executor;

public class FingerprintPinLoginActivity extends BaseActivity {
    LinearLayout btn_fppin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_fingerprint_pin_login);

        btn_fppin  = findViewById(R.id.btn_fppin);

        checkBioMetricSupported();
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

                final Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                View custom_viewError = getLayoutInflater().inflate(R.layout.error_tost, null);
                TextView message=custom_viewError.findViewById(R.id.message);
                message.setText("Login Error"+errString);
                toast.setView(custom_viewError);

               /* Toast.makeText(getApplicationContext(),
                                "Login error: " + errString, Toast.LENGTH_SHORT)
                        .show();*/


            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                if (RetrofitClient.getStringUserPreference(FingerprintPinLoginActivity.this,
                        Constants.VEARIFICATION_STATUS) == null) {
                    startActivity(new Intent(FingerprintPinLoginActivity.this, EmployeeStatusCheckActivity.class));
                } else {
                    startActivity(new Intent(FingerprintPinLoginActivity.this, MainActivity.class)
                            .putExtra("me","No"));
                }


                //  overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                finish();

                final Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                View custom_view = getLayoutInflater().inflate(R.layout.success_tost, null);
                TextView message=custom_view.findViewById(R.id.message);
                message.setText("Login Success!");
                toast.setView(custom_view);
                toast.show();
                /*Toast.makeText(getApplicationContext(),
                        "Login Success!" , Toast.LENGTH_SHORT).show();*/

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //attempt not regconized fingerprint
               /* startActivity(new Intent(FingerprintPinLoginActivity.this, LoginActivity.class));
                //  overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                finish();*/

                final Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                View custom_viewError = getLayoutInflater().inflate(R.layout.error_tost, null);
                TextView message=custom_viewError.findViewById(R.id.message);
                message.setText("Login Failed");
                toast.setView(custom_viewError);

              /*  Toast.makeText(getApplicationContext(), "Login failed",
                                Toast.LENGTH_SHORT)
                        .show();*/

            }
        });


        BiometricPrompt.PromptInfo.Builder promptInfo = dialogMetric();
        promptInfo.setDeviceCredentialAllowed(true);
        biometricPrompt.authenticate(promptInfo.build());

        btn_fppin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BiometricPrompt.PromptInfo.Builder promptInfo = dialogMetric();
                promptInfo.setDeviceCredentialAllowed(true);
                biometricPrompt.authenticate(promptInfo.build());

            }
        });

    }


    BiometricPrompt.PromptInfo.Builder dialogMetric()
    {
        //Show prompt dialog
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Log in using your biometric credential");
    }

    private void checkBioMetricSupported() {


        BiometricManager manager = BiometricManager.from(this);
        String info="";
        switch (manager.canAuthenticate(BIOMETRIC_WEAK | BIOMETRIC_STRONG))
        {
            case BiometricManager.BIOMETRIC_SUCCESS:
                info = "App can authenticate using biometrics.";

                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                info = "No biometric features available on this device.";

                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                info = "Biometric features are currently unavailable.";

                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                info = "Need register at least one finger print";
                break;
            default:
                info= "Unknown cause";

        }
        TextView txinfo =  findViewById(R.id.tx_info);
        txinfo.setText(info);
    }

    public void loginwithPassword(View view) {
        startActivity(new Intent(FingerprintPinLoginActivity.this, LoginActivity.class));
        //  overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        finish();
    }


}
