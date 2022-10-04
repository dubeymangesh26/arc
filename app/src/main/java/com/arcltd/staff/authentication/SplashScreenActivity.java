package com.arcltd.staff.authentication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.otherAndMain.CustomerOrEmployeeActivity;
import com.arcltd.staff.activity.without_login.WithoutLoginActivity;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.utility.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashScreenActivity extends AppCompatActivity {
    private static final boolean AUTO_HIDE = true;
    private static final int SPLASH_SCREEN_TIME_OUT = 3000;
    private ImageView logo, arc_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_splash_screen);

        logo = (ImageView) findViewById(R.id.logo);
        arc_name = (ImageView) findViewById(R.id.arc_name);
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

                        Log.d(TAG, token);
                        RetrofitClient.saveUserPreference(SplashScreenActivity.this, Constants.DEVICE_TOKEN, token);
                    }
                });

        goToMain();

    }

    private void goToMain() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.FIRSTNAME) != null) {
                    startActivity(new Intent(SplashScreenActivity.this, FingerprintPinLoginActivity.class));
                    //  overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                    finish();
                } else if (RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.CUSTOMER_LOGIN) != null) {
                    startActivity(new Intent(SplashScreenActivity.this, WithoutLoginActivity.class));
                    //  overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                    finish();
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, CustomerOrEmployeeActivity.class));
                }

            }
        }, SPLASH_SCREEN_TIME_OUT);
        /*Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mysplashanimation);
        logo.startAnimation(myanim);*/

    }
}