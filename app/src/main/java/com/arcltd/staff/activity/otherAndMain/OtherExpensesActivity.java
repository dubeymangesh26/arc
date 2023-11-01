package com.arcltd.staff.activity.otherAndMain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.addData.AddBranchExpencessActivity;
import com.arcltd.staff.activity.addData.AddWeightMachineActivity;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.activity.listData.MoterCycleListActivity;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.utility.Constants;

import java.util.Objects;

public class OtherExpensesActivity extends AppCompatActivity {
    CardView motercyle, weightMachine,cvTeeEle;
    String branch_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_other_expenses);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        motercyle = findViewById(R.id.motercyle);
        weightMachine = findViewById(R.id.weightMachine);
        cvTeeEle = findViewById(R.id.cvTeeEle);

        branch_code = RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE);

        motercyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtherExpensesActivity.this, MoterCycleListActivity.class)
                        .putExtra("branch_code", branch_code)
                        .putExtra("status", "B");
                startActivity(intent);
            }
        });
        weightMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(OtherExpensesActivity.this, AddWeightMachineActivity.class);
                startActivity(intent1);
            }
        });

        cvTeeEle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OtherExpensesActivity.this, AddBranchExpencessActivity.class)
                        .putExtra("branch_code", ""));
            }
        });


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