package com.arcltd.staff.activity.otherAndMain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.authentication.SignupActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class SendWhatsappMessageActivity extends AppCompatActivity {
    TextInputEditText etMobile,etMessage;
    String phonestr,messagestr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_whatsapp_message);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        etMobile=findViewById(R.id.etMobile);
        etMessage=findViewById(R.id.etMessage);

    }

    public void submitMessage(View view) {
        if (validate()){
            phonestr="+91"+etMobile.getText().toString();
            messagestr="Hi,"+etMessage.getText().toString();
            if (validate()){
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + phonestr +
                        "&text=" + messagestr));
                startActivity(i);

                if (getPackageManager() == null) {
                    Toast.makeText(this, "Error/n" + "Whatsapp Not Installed", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    private boolean validate() {
         if (TextUtils.isEmpty(etMobile.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter mobile number!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etMobile.getText().length() < 10) {
            Toast.makeText(this, "Invalid Mobile Number.", Toast.LENGTH_LONG).show();
            return false;
        } else if (etMobile.getText().length() > 12) {
            Toast.makeText(this, "Invalid Mobile Number.", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(etMessage.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Message!", Toast.LENGTH_SHORT).show();
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