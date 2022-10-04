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
import com.arcltd.staff.activity.otherAndMain.MainActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.AddUpdatedAdminiExpResponse;
import com.arcltd.staff.response.AdvEXPResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class UpdateAdminExpencesActivity extends BaseActivity {
    TextInputEditText tvTelephone,tvPostageCourier,tvPrintStatinary,tvPetrolTwoWheeler,tvTwoWhMantatention,tvCarPetrol,
            tvCarMatatenance,tvConveyance,tvTravelling,tvStaffWelfare,tvMiscCharges,tvBisinessPramotion,tvOfficeMant,
            tvOffEqpMaint,tvrepaireBuliders,tvSalaryBoGarOther,tvCoContPFESI,tvRentPaid,tvElectricity,tvBookPeriodicals,
            tvAdvertisement,tvRecruPlaceConfr,tvInsurance,tvLegal,tvSubscription,tvCharityDonation,tvAuditorConsultcy,
            tvRateTaxes,tvBankCommission,tvEmpChildrenAssit,tvTotalBranchExpenses;


    String data,search,branch_code;
    AdvEXPResponse advEXPResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_update_admin_expences);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


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
            data = Objects.requireNonNull(getIntent().getExtras()).getString("data");
            advEXPResponse = new Gson().fromJson(data, AdvEXPResponse.class);
            Log.e("", "onCreate: " + new GsonBuilder().create().toJson(advEXPResponse));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }

        try {
        tvTelephone.setText(advEXPResponse.getBranch_administrativeexp().getTelephone());
        tvPostageCourier.setText(advEXPResponse.getBranch_administrativeexp().getPastge_courier());
        tvPrintStatinary.setText(advEXPResponse.getBranch_administrativeexp().getPrint_stationery());
        tvPetrolTwoWheeler.setText(advEXPResponse.getBranch_administrativeexp().getBike_petrol_exp());
        tvTwoWhMantatention.setText(advEXPResponse.getBranch_administrativeexp().getBike_maitainance());
        tvCarPetrol.setText(advEXPResponse.getBranch_administrativeexp().getCar_petrol_exp());
        tvCarMatatenance.setText(advEXPResponse.getBranch_administrativeexp().getCar_maitainance());
        tvConveyance.setText(advEXPResponse.getBranch_administrativeexp().getConveyance());
        tvTravelling.setText(advEXPResponse.getBranch_administrativeexp().getTravelling());
        tvStaffWelfare.setText(advEXPResponse.getBranch_administrativeexp().getStaff_welfare());
        tvMiscCharges.setText(advEXPResponse.getBranch_administrativeexp().getMisc_charges());
        tvBisinessPramotion.setText(advEXPResponse.getBranch_administrativeexp().getBusi_pramostion());
        tvOfficeMant.setText(advEXPResponse.getBranch_administrativeexp().getOffice_maitanance());
        tvOffEqpMaint.setText(advEXPResponse.getBranch_administrativeexp().getOffice_eqp_maintenance());
        tvrepaireBuliders.setText(advEXPResponse.getBranch_administrativeexp().getRepare_buildings());
        tvSalaryBoGarOther.setText(advEXPResponse.getBranch_administrativeexp().getSalary_bonas_gradu_other());
        tvCoContPFESI.setText(advEXPResponse.getBranch_administrativeexp().getCocontribution_pf_esi());
        tvRentPaid.setText(advEXPResponse.getBranch_administrativeexp().getRent_paid());
        tvElectricity.setText(advEXPResponse.getBranch_administrativeexp().getElectricity());
        tvBookPeriodicals.setText(advEXPResponse.getBranch_administrativeexp().getBooks_periodicals());
        tvAdvertisement.setText(advEXPResponse.getBranch_administrativeexp().getAdvertisement());
        tvRecruPlaceConfr.setText(advEXPResponse.getBranch_administrativeexp().getRecrument_place_conf());
        tvInsurance.setText(advEXPResponse.getBranch_administrativeexp().getInsurance());
        tvLegal.setText(advEXPResponse.getBranch_administrativeexp().getLegal());
        tvSubscription.setText(advEXPResponse.getBranch_administrativeexp().getSubscription());
        tvCharityDonation.setText(advEXPResponse.getBranch_administrativeexp().getCharity_donation());
        tvAuditorConsultcy.setText(advEXPResponse.getBranch_administrativeexp().getAuditor_consultancy());
        tvRateTaxes.setText(advEXPResponse.getBranch_administrativeexp().getRates_taxes());
        tvBankCommission.setText(advEXPResponse.getBranch_administrativeexp().getBank_commission());
        tvEmpChildrenAssit.setText(advEXPResponse.getBranch_administrativeexp().getEmp_children_education());
        tvTotalBranchExpenses.setText(advEXPResponse.getBranch_administrativeexp().getTelephone());
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
                apiPresenter.administrationExp(disposable, Constants.ApiRequestCode.ADMINI_EXP_UPDATE,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.REGION_ID),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.DIVISION_ID),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE),
                        tvTelephone.getText().toString(),tvPostageCourier.getText().toString(),
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
                        tvBankCommission.getText().toString(),tvEmpChildrenAssit.getText().toString(),"P");

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
    }

    private void getAdvRes(AddUpdatedAdminiExpResponse addUpdatedAdminiExpResponse) {
        try {
            Log.e(TAG, "addUpdatedAdminiExpResponse: " + new GsonBuilder().create().toJson(addUpdatedAdminiExpResponse));
            if (addUpdatedAdminiExpResponse != null) {
                if (addUpdatedAdminiExpResponse.getResponseCode().equals("200")) {
                    Infrastructure.dismissProgressDialog();
                    Toast.makeText(this, addUpdatedAdminiExpResponse.getMessage(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();

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