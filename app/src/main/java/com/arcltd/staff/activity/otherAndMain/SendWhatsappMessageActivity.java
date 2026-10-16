package com.arcltd.staff.activity.otherAndMain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.google.android.material.textfield.TextInputEditText;

public class SendWhatsappMessageActivity extends AppCompatActivity {
    TextInputEditText etMobile,etMessage;
    String phonestr,messagestr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_send_whatsapp_message);
        
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        etMobile=findViewById(R.id.etMobile);
        etMessage=findViewById(R.id.etMessage);

        if (getIntent().getExtras() != null) {
            messagestr = getIntent().getExtras().getString("details");
        }
        if (messagestr != null && !messagestr.isEmpty()) {
            etMessage.setText(messagestr);
        }

    }

    public void submitMessage(View view) {
        if (validate()){
            phonestr = "+91" + (etMobile.getText() != null ? etMobile.getText().toString() : "");
            messagestr = etMessage.getText() != null ? etMessage.getText().toString() : "";
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + phonestr +
                    "&text=" + messagestr));
            startActivity(i);

            if (getPackageManager() == null) {
                Toast.makeText(this, "Error/n" + "Whatsapp Not Installed", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean validate() {
         String mobile = etMobile.getText() != null ? etMobile.getText().toString() : "";
         String message = etMessage.getText() != null ? etMessage.getText().toString() : "";

         if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(getApplicationContext(), "Enter mobile number!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mobile.length() < 10) {
            Toast.makeText(this, "Invalid Mobile Number.", Toast.LENGTH_LONG).show();
            return false;
        } else if (mobile.length() > 12) {
            Toast.makeText(this, "Invalid Mobile Number.", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(message)) {
            Toast.makeText(getApplicationContext(), "Enter Message!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}