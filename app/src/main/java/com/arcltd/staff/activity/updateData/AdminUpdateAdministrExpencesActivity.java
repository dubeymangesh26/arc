package com.arcltd.staff.activity.updateData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.activity.otherAndMain.MainActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.response.AddUpdatedAdminiExpResponse;
import com.arcltd.staff.response.AdministrativEXPAdminResponse;
import com.arcltd.staff.response.DeleteResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class AdminUpdateAdministrExpencesActivity extends BaseActivity {
    TextInputEditText tvTelephone,tvPostageCourier,tvPrintStatinary,tvPetrolTwoWheeler,tvTwoWhMantatention,tvCarPetrol,
            tvCarMatatenance,tvConveyance,tvTravelling,tvStaffWelfare,tvMiscCharges,tvBisinessPramotion,tvOfficeMant,
            tvOffEqpMaint,tvrepaireBuliders,tvSalaryBoGarOther,tvCoContPFESI,tvRentPaid,tvElectricity,tvBookPeriodicals,
            tvAdvertisement,tvRecruPlaceConfr,tvInsurance,tvLegal,tvSubscription,tvCharityDonation,tvAuditorConsultcy,
            tvRateTaxes,tvBankCommission,tvEmpChildrenAssit,tvTotalBranchExpenses;

    String data,branch_code,division_id,region_id;
    AdministrativEXPAdminResponse.BranchAdministrativeexpList databeen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_admin_update_aminist_expences);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);


        tvTelephone=findViewById(R.id.tvTelephone);
        tvPostageCourier=findViewById(R.id.tvPostageCourier);
        tvPrintStatinary=findViewById(R.id.tvPrintStatinary);
        tvPetrolTwoWheeler=findViewById(R.id.tvPetrolTwoWheeler);
        tvTwoWhMantatention=findViewById(R.id.tvTwoWhMantatention);
        tvCarPetrol=findViewById(R.id.tvCarPetrol);
        tvCarMatatenance=findViewById(R.id.tvCarMatatenance);
        tvConveyance=findViewById(R.id.tvConveyance);
        tvTravelling=findViewById(R.id.tvTravelling);
        tvStaffWelfare=findViewById(R.id.tvStaffWelfare);
        tvMiscCharges=findViewById(R.id.tvMiscCharges);
        tvBisinessPramotion=findViewById(R.id.tvBisinessPramotion);
        tvOfficeMant=findViewById(R.id.tvOfficeMant);
        tvOffEqpMaint=findViewById(R.id.tvOffEqpMaint);
        tvrepaireBuliders=findViewById(R.id.tvrepaireBuliders);
        tvSalaryBoGarOther=findViewById(R.id.tvSalaryBoGarOther);
        tvCoContPFESI=findViewById(R.id.tvCoContPFESI);
        tvRentPaid=findViewById(R.id.tvRentPaid);
        tvElectricity=findViewById(R.id.tvElectricity);
        tvBookPeriodicals=findViewById(R.id.tvBookPeriodicals);
        tvAdvertisement=findViewById(R.id.tvAdvertisement);
        tvRecruPlaceConfr=findViewById(R.id.tvRecruPlaceConfr);
        tvInsurance=findViewById(R.id.tvInsurance);
        tvLegal=findViewById(R.id.tvLegal);
        tvSubscription=findViewById(R.id.tvSubscription);
        tvCharityDonation=findViewById(R.id.tvCharityDonation);
        tvAuditorConsultcy=findViewById(R.id.tvAuditorConsultcy);
        tvRateTaxes=findViewById(R.id.tvRateTaxes);
        tvBankCommission=findViewById(R.id.tvBankCommission);
        tvEmpChildrenAssit=findViewById(R.id.tvEmpChildrenAssit);


        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("res");
            databeen = new Gson().fromJson(data,AdministrativEXPAdminResponse.BranchAdministrativeexpList.class);
            Log.e("", "onCreate: " + new GsonBuilder().create().toJson(databeen));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }

        try {
        branch_code=databeen.getBranch_code();
            division_id=databeen.getDivision_id();
            region_id=databeen.getRegion_id();
        tvTelephone.setText(databeen.getTelephone());
        tvPostageCourier.setText(databeen.getPastge_courier());
        tvPrintStatinary.setText(databeen.getPrint_stationery());
        tvPetrolTwoWheeler.setText(databeen.getBike_petrol_exp());
        tvTwoWhMantatention.setText(databeen.getBike_maitainance());
        tvCarPetrol.setText(databeen.getCar_petrol_exp());
        tvCarMatatenance.setText(databeen.getCar_maitainance());
        tvConveyance.setText(databeen.getConveyance());
        tvTravelling.setText(databeen.getTravelling());
        tvStaffWelfare.setText(databeen.getStaff_welfare());
        tvMiscCharges.setText(databeen.getMisc_charges());
        tvBisinessPramotion.setText(databeen.getBusi_pramostion());
        tvOfficeMant.setText(databeen.getOffice_maitanance());
        tvOffEqpMaint.setText(databeen.getOffice_eqp_maintenance());
        tvrepaireBuliders.setText(databeen.getRepare_buildings());
        tvSalaryBoGarOther.setText(databeen.getSalary_bonas_gradu_other());
        tvCoContPFESI.setText(databeen.getCocontribution_pf_esi());
        tvRentPaid.setText(databeen.getRent_paid());
        tvElectricity.setText(databeen.getElectricity());
        tvBookPeriodicals.setText(databeen.getBooks_periodicals());
        tvAdvertisement.setText(databeen.getAdvertisement());
        tvRecruPlaceConfr.setText(databeen.getRecrument_place_conf());
        tvInsurance.setText(databeen.getInsurance());
        tvLegal.setText(databeen.getLegal());
        tvSubscription.setText(databeen.getSubscription());
        tvCharityDonation.setText(databeen.getCharity_donation());
        tvAuditorConsultcy.setText(databeen.getAuditor_consultancy());
        tvRateTaxes.setText(databeen.getRates_taxes());
        tvBankCommission.setText(databeen.getBank_commission());
        tvEmpChildrenAssit.setText(databeen.getEmp_children_education());
        tvTotalBranchExpenses.setText(databeen.getTelephone());
    } catch (Exception e) {
        Log.e("", "ListingDetail error: " + e);
    }

    }

    public void onClickupdate(View view) {
        updateData();
    }

    private void updateData() {

        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.finaladministrationExp(disposable, Constants.ApiRequestCode.ADMINI_EXP_UPDATE,
                        region_id, division_id,branch_code, tvTelephone.getText().toString(),
                        tvPostageCourier.getText().toString(),
                        tvPrintStatinary.getText().toString(),tvPetrolTwoWheeler.getText().toString(),
                        tvTwoWhMantatention.getText().toString(),tvCarPetrol.getText().toString(),
                        tvCarMatatenance.getText().toString(),tvConveyance.getText().toString(),
                        tvTravelling.getText().toString(),tvStaffWelfare.getText().toString(),
                        tvMiscCharges.getText().toString(),tvBisinessPramotion.getText().toString(),
                        tvOfficeMant.getText().toString(),tvOffEqpMaint.getText().toString(),
                        tvrepaireBuliders.getText().toString(),tvSalaryBoGarOther.getText().toString(),
                        tvCoContPFESI.getText().toString(),tvRentPaid.getText().toString(),
                        tvElectricity.getText().toString(),tvBookPeriodicals.getText().toString(),
                        tvAdvertisement.getText().toString(),tvRecruPlaceConfr.getText().toString(),
                        tvInsurance.getText().toString(),tvLegal.getText().toString(),
                        tvSubscription.getText().toString(),tvCharityDonation.getText().toString(),
                        tvAuditorConsultcy.getText().toString(),tvRateTaxes.getText().toString(),
                        tvBankCommission.getText().toString(),tvEmpChildrenAssit.getText().toString(),"A");

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataADMINI_EXP", e);
        }
    }
    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.ADMINI_EXP_UPDATE) {
            AddUpdatedAdminiExpResponse addUpdatedAdminiExpResponse = (AddUpdatedAdminiExpResponse) object;
            getAdvRes(addUpdatedAdminiExpResponse);
        }
        if (callAPIId == Constants.ApiRequestCode.DELETE) {
            DeleteResponse deleteResponse = (DeleteResponse) object;
            getDelete(deleteResponse);
        }
    }



    private void getAdvRes(AddUpdatedAdminiExpResponse addUpdatedAdminiExpResponse) {
        try {
            Log.e(TAG, "addUpdatedAdminiExpResponse: " + new GsonBuilder().create().toJson(addUpdatedAdminiExpResponse));
            if (addUpdatedAdminiExpResponse != null) {
                if (addUpdatedAdminiExpResponse.getResponseCode().equals("200")) {
                    Infrastructure.dismissProgressDialog();
                    Toast.makeText(this, addUpdatedAdminiExpResponse.getMessage(), Toast.LENGTH_LONG).show();
                    deleade();
                }else {
                    Toast.makeText(this, addUpdatedAdminiExpResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + addUpdatedAdminiExpResponse.getResponseCode());
                Toast.makeText(this, addUpdatedAdminiExpResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in checkLoginResponse", e);
        }
    }


    private void getDelete(DeleteResponse deleteResponse) {

        try {
            Log.e(TAG, "deleteResponse: " + new GsonBuilder().create().toJson(deleteResponse));
            if (deleteResponse != null) {
                if (deleteResponse.getResponseCode().equals("200")) {
                    Infrastructure.dismissProgressDialog();
                    Toast.makeText(this, deleteResponse.getMessage(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finishAndRemoveTask();

                }else {
                    Toast.makeText(this, deleteResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in deleteResponse" + deleteResponse.getResponseCode());
                Toast.makeText(this, deleteResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in deleteResponse", e);
        }

    }

    private void deleade() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.delete(disposable, Constants.ApiRequestCode.DELETE,
                        branch_code);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in deleteResponse", e);
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