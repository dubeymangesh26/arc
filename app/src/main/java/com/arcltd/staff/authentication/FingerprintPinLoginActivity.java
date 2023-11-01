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
import com.arcltd.staff.remote.RetrofitClient;
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


        checkBiometricSupported();
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                handleAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                handleAuthenticationSuccess();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                handleAuthenticationFailed();
            }
        });



        BiometricPrompt.PromptInfo.Builder promptInfo = buildBiometricPromptInfo();
        promptInfo.setDeviceCredentialAllowed(true);
        biometricPrompt.authenticate(promptInfo.build());

        btn_fppin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BiometricPrompt.PromptInfo.Builder promptInfo = buildBiometricPromptInfo();
                promptInfo.setDeviceCredentialAllowed(true);
                biometricPrompt.authenticate(promptInfo.build());
            }
        });
    }

    // ...

    private BiometricPrompt.PromptInfo.Builder buildBiometricPromptInfo() {
        // Show prompt dialog
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Log in using your biometric credential");
    }


    private void handleAuthenticationError(int errorCode, CharSequence errString) {
        String errorMessage = "Login error: " + errString;
        // Display an error message to the user.
        showToast(errorMessage);
    }

    private void handleAuthenticationSuccess() {
        if (RetrofitClient.getStringUserPreference(FingerprintPinLoginActivity.this, Constants.VEARIFICATION_STATUS) == null) {
            startActivity(new Intent(FingerprintPinLoginActivity.this, EmployeeStatusCheckActivity.class));
        } else {
            startActivity(new Intent(FingerprintPinLoginActivity.this, MainActivity.class).putExtra("me", "No"));
        }
        showToast("Login Success!");
        finish();
    }

    private void handleAuthenticationFailed() {
        // Handle the case when authentication fails (e.g., not recognized fingerprint).
        String errorMessage = "Login failed";
        showToast(errorMessage);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // ...

    private void checkBiometricSupported() {
        BiometricManager manager = BiometricManager.from(this);
        int biometricSupport = manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.BIOMETRIC_STRONG);

        // Handle the different cases and display user-friendly information.
        String info = "";
        switch (biometricSupport) {
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
                info = "You need to register at least one fingerprint.";
                break;
            default:
                info = "Unknown cause";
        }

        TextView txinfo = findViewById(R.id.tx_info);
        txinfo.setText(info);
    }

    public void loginwithPassword(View view) {
        startActivity(new Intent(FingerprintPinLoginActivity.this, LoginActivity.class));
        //  overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        finish();
    }


}
