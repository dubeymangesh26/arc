package com.arcltd.staff.activity.otherAndMain;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.response.AppPermissionListResponse;
import com.arcltd.staff.response.UpdatePermissionResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Objects;

public class UserAppPermissionActivity extends BaseActivity {
    TextView empName,branchCode;
    SwitchCompat div_branches,div_mess,send_profile,emp_list,emp_birthday,retire_list,motercy_list,inciden_list,
            weightmc_list,conveyance,lanlord_details,admin_exp_list,sendEmp_message,add_mess,add_emp,add_convence_mobExp,
            add_sweeperPeon,transfer_emp,active_deactiveEmp;
    String data;
    String emp_code,sbranches,sMess,sSendProfile,sEmpList,sEmpBirthday,sRetireList,sMotercycList,sInscidetList,sWeightMcList,
    sConveyance,sLandlordList,sAdminExp,sSendEmpMessage,sAddMess,sAddEmp,sAddConveyance,sAddPeonSweeper,sTransfer,
            sActiveDectiveEmp;
    AppPermissionListResponse.UserList dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_user_app_permission);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        empName=findViewById(R.id.empName);
        branchCode=findViewById(R.id.branchCode);
        div_branches=findViewById(R.id.div_branches);
        div_mess=findViewById(R.id.div_mess);
        send_profile=findViewById(R.id.send_profile);
        emp_list=findViewById(R.id.emp_list);
        emp_birthday=findViewById(R.id.emp_birthday);
        retire_list=findViewById(R.id.retire_list);
        motercy_list=findViewById(R.id.motercy_list);
        inciden_list=findViewById(R.id.inciden_list);
        weightmc_list=findViewById(R.id.weightmc_list);
        conveyance=findViewById(R.id.conveyance);
        lanlord_details=findViewById(R.id.lanlord_details);
        admin_exp_list=findViewById(R.id.admin_exp_list);
        sendEmp_message=findViewById(R.id.sendEmp_message);
        add_mess=findViewById(R.id.add_mess);
        add_emp=findViewById(R.id.add_emp);
        add_convence_mobExp=findViewById(R.id.add_convence_mobExp);
        add_sweeperPeon=findViewById(R.id.add_sweeperPeon);
        transfer_emp=findViewById(R.id.transfer_emp);
        active_deactiveEmp=findViewById(R.id.active_deactiveEmp);

        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("data");
            dataBean = new Gson().fromJson(data, AppPermissionListResponse.UserList.class);
            Log.e("", "onCreate: " + new GsonBuilder().create().toJson(dataBean));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }

        try {
            emp_code=dataBean.getEmp_code();
            empName.setText(dataBean.getEmp_name());
            branchCode.setText(dataBean.getBranch_code());

            sbranches=dataBean.getDiv_branches();
            div_branches.setChecked(!sbranches.equals("N"));

            sMess=dataBean.getDiv_mess();
            div_mess.setChecked(!sMess.equals("N"));

            sSendProfile=dataBean.getSend_profile();
            send_profile.setChecked(!sSendProfile.equals("N"));

            sEmpList=dataBean.getEmp_list();
            emp_list.setChecked(!sEmpList.equals("N"));

            sEmpBirthday=dataBean.getEmp_birthday();
            emp_birthday.setChecked(!sEmpBirthday.equals("N"));

            sRetireList=dataBean.getRetire_list();
            retire_list.setChecked(!sRetireList.equals("N"));

            sMotercycList=dataBean.getMotercy_list();
            motercy_list.setChecked(!sMotercycList.equals("N"));

            sInscidetList=dataBean.getInciden_list();
            inciden_list.setChecked(!sInscidetList.equals("N"));

            sWeightMcList=dataBean.getWeightmc_list();
            weightmc_list.setChecked(!sWeightMcList.equals("N"));

            sConveyance=dataBean.getConveyance();
            conveyance.setChecked(!sConveyance.equals("N"));

            sLandlordList=dataBean.getLanlord_details();
            lanlord_details.setChecked(!sLandlordList.equals("N"));

            sAdminExp=dataBean.getAdmin_exp_list();
            admin_exp_list.setChecked(!sAdminExp.equals("N"));

            sSendEmpMessage=dataBean.getSendEmp_message();
            sendEmp_message.setChecked(!sSendEmpMessage.equals("N"));

            sAddMess=dataBean.getAdd_mess();
            add_mess.setChecked(!sAddMess.equals("N"));

            sAddEmp=dataBean.getAdd_emp();
            add_emp.setChecked(!sAddEmp.equals("N"));

            sAddConveyance=dataBean.getConveyance();
            add_convence_mobExp.setChecked(!sAddConveyance.equals("N"));

            sAddPeonSweeper=dataBean.getAdd_sweeperPeon();
            add_sweeperPeon.setChecked(!sAddPeonSweeper.equals("N"));

            sTransfer=dataBean.getTransfer_emp();
            transfer_emp.setChecked(!sTransfer.equals("N"));

            sActiveDectiveEmp=dataBean.getActive_deactiveEmp();
            active_deactiveEmp.setChecked(!sActiveDectiveEmp.equals("N"));


        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }

    }

    public void updatePermission(View view) {
        if (div_branches.isChecked()){
            sbranches="Y";
        }else {
            sbranches="N";
        }
        if (div_mess.isChecked()){
            sMess="Y";
        }else {
            sMess="N";
        }


        if (send_profile.isChecked()){
            sSendProfile="Y";
        }else {
            sSendProfile="N";
        }

        if (emp_list.isChecked()){
            sEmpList="Y";
        }else {
            sEmpList="N";
        }

        if (emp_birthday.isChecked()){
            sEmpBirthday="Y";
        }else {
            sEmpBirthday="N";
        }


        if (retire_list.isChecked()){
            sRetireList="Y";
        }else {
            sRetireList="N";
        }

        if (motercy_list.isChecked()){
            sMotercycList="Y";
        }else {
            sMotercycList="N";
        }

        if (inciden_list.isChecked()){
            sInscidetList="Y";
        }else {
            sInscidetList="N";
        }

        if (weightmc_list.isChecked()){
            sWeightMcList="Y";
        }else {
            sWeightMcList="N";
        }

        if (conveyance.isChecked()){
            sConveyance="Y";
        }else {
            sConveyance="N";
        }

        if (lanlord_details.isChecked()){
            sLandlordList="Y";
        }else {
            sLandlordList="N";
        }


        if (admin_exp_list.isChecked()){
            sAdminExp="Y";
        }else {
            sAdminExp="N";
        }

        if (sendEmp_message.isChecked()){
            sSendEmpMessage="Y";
        }else {
            sSendEmpMessage="N";
        }

        if (add_mess.isChecked()){
            sAddMess="Y";
        }else {
            sAddMess="N";
        }

        if (add_emp.isChecked()){
            sAddEmp="Y";
        }else {
            sAddEmp="N";
        }

        if (add_convence_mobExp.isChecked()){
            sAddConveyance="Y";
        }else {
            sAddConveyance="N";
        }

        if (add_sweeperPeon.isChecked()){
            sAddPeonSweeper="Y";
        }else {
            sAddPeonSweeper="N";
        }

        if (transfer_emp.isChecked()){
            sTransfer="Y";
        }else {
            sTransfer="N";
        }

        if (active_deactiveEmp.isChecked()){
            sActiveDectiveEmp="Y";
        }else {
            sActiveDectiveEmp="N";
        }

        updatePermission();
       // Log.e("TAG", "updatePermission:  "+ "sbranches "+  sbranches+ "  sMess  "+sMess  );
    }


    private void updatePermission() {
        try {
            if (Infrastructure.isInternetPresent()) {
               Infrastructure.showProgressDialog(this);
                apiPresenter.userAppPermission(disposable, Constants.ApiRequestCode.USER_REGISTRATION,
                        "",emp_code,sbranches, sMess, sSendProfile, sEmpList, sEmpBirthday, sRetireList,
                        sMotercycList, sInscidetList
                        , sWeightMcList, sConveyance, sLandlordList, sAdminExp,
                        sSendEmpMessage,sAddMess,sAddEmp,sAddConveyance,sAddPeonSweeper,sTransfer
                        ,sActiveDectiveEmp);
                Log.e("login_Rquest", "login_Rquest: " + apiPresenter);
            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("Exception", "Exception in getDataFromWebservice", e);
        }
    }


    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("showResult", "showResult: " + object);
       Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.USER_REGISTRATION) {
            // LoginResponse loginResponse = new Gson().fromJson(object, LoginResponse.class);
            UpdatePermissionResponse updatePermissionResponse = (UpdatePermissionResponse) object;
            addPermission(updatePermissionResponse);
        }
    }



    private void addPermission(UpdatePermissionResponse updatePermissionResponse) {
        try {
            Log.e(TAG, "updatePermissionResponse: " + new GsonBuilder().create().toJson(updatePermissionResponse));
            if (updatePermissionResponse != null) {
               Infrastructure.dismissProgressDialog();

                final Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                View custom_view = getLayoutInflater().inflate(R.layout.success_tost, null);
                TextView message=custom_view.findViewById(R.id.message);
                message.setText(updatePermissionResponse.getMessage());
                toast.setView(custom_view);
                toast.show();

                  //  Toast.makeText(this, updatePermissionResponse.getMessage(), Toast.LENGTH_LONG).show();
                    finish();

            } else {
                ELog.e(TAG, "Exception in updatePermissionResponse" + updatePermissionResponse.getResponseCode());
                final Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                View custom_viewError = getLayoutInflater().inflate(R.layout.error_tost, null);
                TextView message=custom_viewError.findViewById(R.id.message);
                message.setText(updatePermissionResponse.getMessage());
                toast.setView(custom_viewError);
                toast.show();
               // Toast.makeText(this, updatePermissionResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in updatePermissionResponse", e);
        }
    }



    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        Infrastructure.dismissProgressDialog();
        Log.e("login", "login: " + errorCode);
        if (callAPIId == Constants.ApiRequestCode.USER_REGISTRATION) {
            new ErrorHandlerCode(this, errorCode, message);
        }

    }


    @Override
    public void showArrayResult(List genericObj, int callAPIId) {
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