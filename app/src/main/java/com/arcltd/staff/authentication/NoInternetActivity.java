package com.arcltd.staff.authentication;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.arcltd.staff.R;


public class NoInternetActivity extends AppCompatActivity {
    Button tryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_no_internet);

        tryAgain = (Button) findViewById(R.id.try_again);
        /*tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Config.isOnline(getApplicationContext()))
                {
                    finish();
                }

            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
/*
        if(Config.isOnline(getApplicationContext()))
        {
            finish();
        }*/

    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}