package com.arcltd.staff.activity.updateData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.ProfileResponseResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends BaseActivity {
    CircleImageView profilePicture;
    TextInputEditText etBranchCode,etEmployeeId,etEmpName,etDisign,etEmailId,etMobile,etEduction,
            etJoiningDate,etSalary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_update_profile);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        etBranchCode=findViewById(R.id.etBranchCode);
        etEmployeeId=findViewById(R.id.etEmployeeId);
        etEmpName=findViewById(R.id.etEmpName);
        etDisign=findViewById(R.id.etDisign);
        etEmailId=findViewById(R.id.etEmailId);
        etMobile=findViewById(R.id.etMobile);
        etEduction=findViewById(R.id.etEduction);
        etJoiningDate=findViewById(R.id.etJoiningDate);
        etSalary=findViewById(R.id.etSalary);
        profilePicture=findViewById(R.id.profilePicture);
        profileDetails();

    }

    public void onClickProfileupdate(View view) {

    }

    private void profileDetails() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.profile(disposable, Constants.ApiRequestCode.OFFICEMESSGDN,
                        RetrofitClient.getStringUserPreference(getApplicationContext(),Constants.EMP_CODE));

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult: " + object);
        Infrastructure.dismissProgressDialog();
       /* if (callAPIId == Constants.ApiRequestCode.TARGET) {
            TargetResponseResponse targetResponseResponse = (TargetResponseResponse) object;
            getDivisionList(targetResponseResponse);
        }*/
        if (callAPIId == Constants.ApiRequestCode.OFFICEMESSGDN) {
            ProfileResponseResponse profileResponseResponse = (ProfileResponseResponse) object;
            getprofile(profileResponseResponse);
        }
    }

    private void getprofile(ProfileResponseResponse profileResponseResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(profileResponseResponse));
            if (profileResponseResponse != null) {

                    Infrastructure.dismissProgressDialog();
                etBranchCode.setText(profileResponseResponse.getUser().getBranch_code());
                etEmployeeId.setText(profileResponseResponse.getUser().getEmp_code());
                etEmpName.setText(profileResponseResponse.getUser().getName());
                etDisign.setText(profileResponseResponse.getUser().getDesign());
                etEmailId.setText(profileResponseResponse.getUser().getEmail());
                etMobile.setText(profileResponseResponse.getUser().getContactno());
                etEduction.setText(profileResponseResponse.getEmployee_details().getEmp_eqalification());
                etJoiningDate.setText(profileResponseResponse.getEmployee_details().getEmp_joining_date());
                etSalary.setText(profileResponseResponse.getEmployee_details().getEmp_salary());

            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + profileResponseResponse.getResponseCode());
                Toast.makeText(this, profileResponseResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in checkLoginResponse", e);
        }
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
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