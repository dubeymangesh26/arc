package com.arcltd.staff.activity.addData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.activity.listData.AdminBranchListActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.AddTeeElectricityResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class AddBranchExpencessActivity extends BaseActivity {
    EditText etTeeAmt, etElectricity;
    CheckBox cbEdit,cbEditEle;
    String branch_code="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_add_branch_expencess);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        etTeeAmt=findViewById(R.id.etTeeAmt);
        etElectricity=findViewById(R.id.etElectricity);
        cbEdit=findViewById(R.id.cbEdit);
        cbEditEle=findViewById(R.id.cbEditEle);
        branch_code = Objects.requireNonNull(getIntent().getExtras()).getString("branch_code");
        if (!Objects.requireNonNull(getIntent().getExtras()).getString("branch_code").equals("")){
            branch_code = Objects.requireNonNull(getIntent().getExtras()).getString("branch_code");
        }else {
            branch_code = RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE);

        }

        etTeeAmt.setEnabled(false);
        etElectricity.setEnabled(false);

        cbEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    etTeeAmt.setEnabled(true);
                    //CODE TO MAKE THE EDITTEXT ENABLED
                } else
                    etTeeAmt.setEnabled(false);
                //CODE TO MAKE THE EDITTEXT DISABLED

            }
        });
        cbEditEle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    etElectricity.setEnabled(true);
                    //CODE TO MAKE THE EDITTEXT ENABLED
                }
                else
                    etElectricity.setEnabled(false);
                //CODE TO MAKE THE EDITTEXT DISABLED

            }
        });

    }

    public void submitBranchExp(View view) {
        if (validate()) {
            addTeaElectricity();
        }

    }

    private void addTeaElectricity() {

        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.addTeeElectricity(disposable, Constants.ApiRequestCode.TEE_ELE,
                        branch_code, etTeeAmt.getText().toString(),etElectricity.getText().toString());

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult addTeeElectricityResponse: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.TEE_ELE) {
            AddTeeElectricityResponse addTeeElectricityResponse = (AddTeeElectricityResponse) object;
            submitdata(addTeeElectricityResponse);
        }
    }

    private void submitdata(AddTeeElectricityResponse addTeeElectricityResponse) {
        try {
            Log.e(TAG, "addTeeElectricityResponse: " + new GsonBuilder().create().toJson(addTeeElectricityResponse));
            if (addTeeElectricityResponse != null) {
                    Infrastructure.dismissProgressDialog();
                Toast.makeText(this, addTeeElectricityResponse.getMessage(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, AdminBranchListActivity.class));
                    finish();

            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in addTeeElectricityResponse" + addTeeElectricityResponse.getResponseCode());
                Toast.makeText(this, addTeeElectricityResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in addTeeElectricityResponse", e);
        }
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
    }

    private boolean validate() {
        if (TextUtils.isEmpty(etTeeAmt.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Tee Amount!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etElectricity.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Electricity Bill Amount!", Toast.LENGTH_SHORT).show();
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