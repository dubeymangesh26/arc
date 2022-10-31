package com.arcltd.staff.activity.detailsData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.addData.AddBranchExpencessActivity;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.activity.employee.EmployeeConvenceListActivity;
import com.arcltd.staff.activity.employee.EmployeeListActivity;
import com.arcltd.staff.activity.listData.LandLordListActivity;
import com.arcltd.staff.activity.listData.MoterCycleListActivity;
import com.arcltd.staff.activity.listData.PeonSweeperListActivity;
import com.arcltd.staff.activity.listData.WeightMCListActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.response.BranchListResponse;
import com.arcltd.staff.response.OfficeGodownMessListResponse;
import com.arcltd.staff.response.TargetResponseResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class DetailsActivity extends BaseActivity {
    TextView tvRegion,tvDivision,tvAccountBr,tvSubBranch,tvBranchAddress,tvLandlordName,
            tvLndPan,tvLandLordAddress,tvCostCode,tvgdnofficeResd,tvPeriodFrom,tvPeriodTo,tvRentAmt,tvGdnOffResArea
            ,tvOpenAres,tvmiscellaneous,tvMoterBike,tvWeightMachine,tvNoEmp,tvSweeper,
            tvPeon,tvTea,tvElectricity,tvPartTarget,tvTotalTarget,tvParcelTarget,tvMessAvailable;
    String data,search,branch_code,AdBranch_Code,div_id;
    BranchListResponse.BranchList dataBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_details);

        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("data");
            dataBean = new Gson().fromJson(data, BranchListResponse.BranchList.class);
            Log.e("", "onCreate: " + new GsonBuilder().create().toJson(dataBean));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        tvRegion = findViewById(R.id.tvRegion);
        tvDivision = findViewById(R.id.tvDivision);
        tvAccountBr = findViewById(R.id.tvAccountBr);
        tvSubBranch = findViewById(R.id.tvSubBranch);
        tvBranchAddress = findViewById(R.id.tvBranchAddress);
        tvLandlordName = findViewById(R.id.tvLandlordName);
        tvLndPan = findViewById(R.id.tvLndPan);
        tvLandLordAddress = findViewById(R.id.tvLandLordAddress);
        tvCostCode = findViewById(R.id.tvCostCode);
        tvgdnofficeResd = findViewById(R.id.tvgdnofficeResd);
        tvPeriodFrom = findViewById(R.id.tvPeriodFrom);
        tvPeriodTo = findViewById(R.id.tvPeriodTo);
        tvRentAmt = findViewById(R.id.tvRentAmt);
        tvGdnOffResArea = findViewById(R.id.tvGdnOffResArea);
        tvOpenAres = findViewById(R.id.tvOpenAres);
        tvMoterBike = findViewById(R.id.tvMoterBike);
        tvmiscellaneous = findViewById(R.id.tvmiscellaneous);
        tvWeightMachine = findViewById(R.id.tvWeightMachine);
        tvWeightMachine = findViewById(R.id.tvWeightMachine);
        tvNoEmp = findViewById(R.id.tvNoEmp);
        tvSweeper = findViewById(R.id.tvSweeper);
        tvPeon = findViewById(R.id.tvPeon);
        tvTea = findViewById(R.id.tvTea);
        tvElectricity = findViewById(R.id.tvElectricity);
        tvPartTarget = findViewById(R.id.tvPartTarget);
        tvTotalTarget = findViewById(R.id.tvTotalTarget);
        tvParcelTarget = findViewById(R.id.tvParcelTarget);
        tvMessAvailable = findViewById(R.id.tvMessAvailable);

        try {
            AdBranch_Code=dataBean.getBranch_code();
            branch_code=dataBean.getBranch_code();
            div_id=dataBean.getDivision_id();
            tvBranchAddress.setText(dataBean.getBranch_address());


        } catch (Exception e) {
            Log.e("", "Data  error: " + e);
        }

        target();
        messOfficeGdnList();

    }
    private void target() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.target(disposable, Constants.ApiRequestCode.TARGET,
                        branch_code,search);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }
    private void messOfficeGdnList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                 Infrastructure.showProgressDialog(this);
                apiPresenter.list_ofiicemessgdn(disposable, Constants.ApiRequestCode.OFFICEMESSGDN,
                        div_id,AdBranch_Code);

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
        if (callAPIId == Constants.ApiRequestCode.TARGET) {
            TargetResponseResponse targetResponseResponse = (TargetResponseResponse) object;
            getDivisionList(targetResponseResponse);
        }
        if (callAPIId == Constants.ApiRequestCode.OFFICEMESSGDN) {
            OfficeGodownMessListResponse officeGodownMessListResponse = (OfficeGodownMessListResponse) object;
            getMessOfficeGodown(officeGodownMessListResponse);
        }
    }

    private void getDivisionList(TargetResponseResponse targetResponseResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(targetResponseResponse));
            if (targetResponseResponse != null) {
                if (targetResponseResponse.getBooking_target()!=null) {
                    Infrastructure.dismissProgressDialog();

                    tvParcelTarget.setText(targetResponseResponse.getBooking_target().get(0).getParcel());
                    tvPartTarget.setText(targetResponseResponse.getBooking_target().get(0).getPart());
                    tvTotalTarget.setText(targetResponseResponse.getBooking_target().get(0).getTotal());
                    tvTea.setText(targetResponseResponse.getTee_electric_exp().getTea());
                    tvElectricity.setText(targetResponseResponse.getTee_electric_exp().getElectricity());



                }else {
                    //Toast.makeText(this, targetResponseResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + targetResponseResponse.getResponseCode());
                Toast.makeText(this, targetResponseResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in checkLoginResponse", e);
        }
    }

    private void getMessOfficeGodown(OfficeGodownMessListResponse officeGodownMessListResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(officeGodownMessListResponse));
            if (officeGodownMessListResponse != null) {
                if (officeGodownMessListResponse.getOffice_mess_rent_list()!=null) {
                      Infrastructure.dismissProgressDialog();

                    tvRegion.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getRegion_name());
                    tvDivision.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getDivision_name());
                    tvAccountBr.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getAc_branch());
                    tvSubBranch.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getSub_branch());
                    tvLandlordName.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getLandlord_name());
                    tvLndPan.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getPan_no());
                    tvLandLordAddress.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getLandlord_address());
                    tvCostCode.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getCost_code());
                    tvgdnofficeResd.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getOff_gdn_residence());
                    tvPeriodFrom.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getPeriod_from());
                    tvPeriodTo.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getPeriod_to());
                    tvRentAmt.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getRent_amount());
                    tvGdnOffResArea.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getOfgdn_area());
                    tvOpenAres.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getOpen_area());
                    tvMoterBike.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getMoter_bike());
                    tvmiscellaneous.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getMiscellaneous());
                    tvWeightMachine.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getWeight_machine());
                    tvNoEmp.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getEmp());
                    tvSweeper.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getSweeper());
                    tvPeon.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getPeon());
                    tvMessAvailable.setText(officeGodownMessListResponse.getOffice_mess_rent_list().get(0).getParcel());

                }else {
                    Toast.makeText(this, officeGodownMessListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + officeGodownMessListResponse.getResponseCode());
                Toast.makeText(this, officeGodownMessListResponse.getMessage(), Toast.LENGTH_LONG).show();

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

    public void btnAdministrati(View view) {
        startActivity(new Intent(this, AdministativeExpensesDetailsActivity.class)
                .putExtra("branch_code", dataBean.getBranch_code()));
    }

    public void editOnClick(View view) {
        startActivity(new Intent(this, LandLordListActivity.class)
                .putExtra("branch_code", dataBean.getBranch_code()));

    }

    public void WeighrMachineView(View view) {
        startActivity(new Intent(this, WeightMCListActivity.class)
                .putExtra("branch_code", dataBean.getBranch_code()));
    }

    public void MotercycleView(View view) {
        startActivity(new Intent(this, MoterCycleListActivity.class)
                .putExtra("branch_code", dataBean.getBranch_code())
                .putExtra("status", "B"));
    }

    public void onViewPeon(View view) {
        startActivity(new Intent(this, PeonSweeperListActivity.class)
                .putExtra("branch_code", dataBean.getBranch_code())
                .putExtra("type", "P"));

    }

    public void onViewSweeper(View view) {
        startActivity(new Intent(this, PeonSweeperListActivity.class)
                .putExtra("branch_code", dataBean.getBranch_code())
                .putExtra("type", "S"));

    }

    public void onViewEmp(View view) {
        startActivity(new Intent(this, EmployeeListActivity.class)
                .putExtra("branch_code", dataBean.getBranch_code()));


    }


    public void onViewConveyance(View view) {
        startActivity(new Intent(this, EmployeeConvenceListActivity.class)
                .putExtra("branch_code", dataBean.getBranch_code()));

    }

    public void teeeditOnClick(View view) {
        startActivity(new Intent(this, AddBranchExpencessActivity.class)
                .putExtra("branch_code", dataBean.getBranch_code()));

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