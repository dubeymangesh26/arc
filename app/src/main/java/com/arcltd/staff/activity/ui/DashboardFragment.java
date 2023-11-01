package com.arcltd.staff.activity.ui;

import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.Constraints;
import androidx.viewpager.widget.ViewPager;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.activity.listData.AdministratiExpListActivity;
import com.arcltd.staff.activity.employee.EmployeeConvenceListActivity;
import com.arcltd.staff.activity.employee.EmployeeListActivity;
import com.arcltd.staff.activity.employee.EmployeeTodayBirthListActivity;
import com.arcltd.staff.activity.listData.ArcStructurePDFListActivity;
import com.arcltd.staff.activity.listData.BookingListActivity;
import com.arcltd.staff.activity.listData.InsCompanyListActivity;
import com.arcltd.staff.activity.listData.LandLordListActivity;
import com.arcltd.staff.activity.listData.MoterCycleListActivity;
import com.arcltd.staff.activity.otherAndMain.RetirementActivity;
import com.arcltd.staff.activity.listData.WeightMCListActivity;
import com.arcltd.staff.adapter.The_Slide_items_Pager_Adapter;
import com.arcltd.staff.base.BaseFragment;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.AppPermissionListResponse;
import com.arcltd.staff.response.The_Slide_Items_Model_Class;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimerTask;

public class DashboardFragment extends BaseFragment {
    TextView tvNoOfEmp;
    LinearLayout regionManager;
    RelativeLayout cvTarget, litrasaction, liAdministrati,
            listConveyance, listWeightMachine,
            listBike, listEmployee, listlandlord, listInsidental, listRetireEmp,cvStructure,listEmployeeDOB;
    private List<The_Slide_Items_Model_Class> listItems;
    private ViewPager page;
    private TabLayout tabLayout;
    String branchCode,loginType;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        HandleAppCrashActivity.deploy(getActivity(), CrashReportActivity.class);
        userPermissionList();
        getview(view);

        return view;
    }

    private void getview(View view) {
        cvTarget = view.findViewById(R.id.cvTarget);
        litrasaction = view.findViewById(R.id.litrasaction);
        regionManager = view.findViewById(R.id.regionManager);
        liAdministrati = view.findViewById(R.id.liAdministrati);
        listEmployee = view.findViewById(R.id.listEmployee);
        listBike = view.findViewById(R.id.listBike);
        listWeightMachine = view.findViewById(R.id.listWeightMachine);
        listConveyance = view.findViewById(R.id.listConveyance);
        listlandlord = view.findViewById(R.id.listlandlord);
        listInsidental = view.findViewById(R.id.listInsidental);
        listRetireEmp = view.findViewById(R.id.listRetireEmp);
        tvNoOfEmp = view.findViewById(R.id.tvNoOfEmp);
        cvStructure = view.findViewById(R.id.cvStructure);
        listEmployeeDOB = view.findViewById(R.id.listEmployeeDOB);
        page = view.findViewById(R.id.my_pager);
        tabLayout = view.findViewById(R.id.my_tablayout);

        branchCode=RetrofitClient.getStringUserPreference(requireActivity(),Constants.BRANCH_CODE);
        loginType=RetrofitClient.getStringUserPreference(requireActivity(),Constants.LOGIN_TYPE);


        listItems = new ArrayList<>();
        listItems.add(new The_Slide_Items_Model_Class(R.drawable.slide_zero, "Slider 1 Title"));
        listItems.add(new The_Slide_Items_Model_Class(R.drawable.slide_one, "Slider 1 Title"));
        listItems.add(new The_Slide_Items_Model_Class(R.drawable.slide_two, "Slider 2 Title"));
        listItems.add(new The_Slide_Items_Model_Class(R.drawable.slide_three, "Slider 3 Title"));
        listItems.add(new The_Slide_Items_Model_Class(R.drawable.slide_four, "Slider 4 Title"));

        The_Slide_items_Pager_Adapter itemsPager_adapter = new The_Slide_items_Pager_Adapter(getActivity(), listItems);
        page.setAdapter(itemsPager_adapter);

        // The_slide_timer
        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new The_slide_timer(), 2000, 3000);
        tabLayout.setupWithViewPager(page, true);


        if (RetrofitClient.getStringUserPreference(requireActivity(),Constants.LOGIN_TYPE).equals("RG")){
            listBike.setVisibility(View.VISIBLE);
        }else if (RetrofitClient.getStringUserPreference(requireActivity(),Constants.LOGIN_TYPE).equals("BM")){
            listBike.setVisibility(View.VISIBLE);
        }else {
            listBike.setVisibility(View.GONE);
        }

        if (RetrofitClient.getStringUserPreference(requireActivity(),Constants.LOGIN_TYPE).equals("RG")){
            listWeightMachine.setVisibility(View.VISIBLE);
        }else if (RetrofitClient.getStringUserPreference(requireActivity(),Constants.LOGIN_TYPE).equals("BM")){
            listWeightMachine.setVisibility(View.GONE);
        } else {
            listWeightMachine.setVisibility(View.GONE);
        }



        if (!RetrofitClient.getStringUserPreference(requireActivity(),Constants.EMP_DETAILS_PERMISSION).equals("N")){
            listEmployee.setVisibility(View.VISIBLE);
        }else {
            listEmployee.setVisibility(View.GONE);
        }

        if (!RetrofitClient.getStringUserPreference(requireActivity(),Constants.EMP_BIRTHDAY_DETAILS_PERMISSION).equals("N")){
            listEmployeeDOB.setVisibility(View.VISIBLE);
        }else {
            listEmployeeDOB.setVisibility(View.GONE);
        }

        if (!RetrofitClient.getStringUserPreference(requireActivity(),Constants.RETIREMENT_DETAILS_PERMISSION).equals("N")){
            listRetireEmp.setVisibility(View.VISIBLE);
        }else {
            listRetireEmp.setVisibility(View.GONE);
        }



        if (!RetrofitClient.getStringUserPreference(requireActivity(),Constants.INCIDENTAL_LIST_PERMISSION).equals("N")){
            listInsidental.setVisibility(View.VISIBLE);
        }else {
            listInsidental.setVisibility(View.GONE);
        }


        if (!RetrofitClient.getStringUserPreference(requireActivity(),Constants.EMP_CONVEYANCE_PERMISSION).equals("N")){
            listConveyance.setVisibility(View.VISIBLE);
        }else {
            listConveyance.setVisibility(View.GONE);
        }

        if (!RetrofitClient.getStringUserPreference(requireActivity(),Constants.LANDLORD_DETAILS_PERMISSION).equals("N")){
            listlandlord.setVisibility(View.VISIBLE);
        }else {
            listlandlord.setVisibility(View.GONE);
        }

        if (!RetrofitClient.getStringUserPreference(requireActivity(),Constants.ADMIN_EXPENCES_PERMISSION).equals("N")){
            liAdministrati.setVisibility(View.VISIBLE);
        }else {
            liAdministrati.setVisibility(View.GONE);
        }







        try {
            if (!RetrofitClient.getStringUserPreference(requireContext(), Constants.NOOF_EMPLOYEE).equals("")) {
                tvNoOfEmp.setText("Total Region Employee : " + RetrofitClient.getStringUserPreference(getContext(), Constants.NOOF_EMPLOYEE));
                tvNoOfEmp.setVisibility(View.VISIBLE);
            } else {
                tvNoOfEmp.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e(ContentValues.TAG, "getview: ", e);
        }


        cvTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(getActivity(), "Work in Progress", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getActivity(), BookingListActivity.class)
                        .putExtra("D", "D"));

            }
        });
        listEmployeeDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EmployeeTodayBirthListActivity.class)
                        .putExtra("D", "D"));

            }
        });

        cvStructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ArcStructurePDFListActivity.class));

            }
        });


        listEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EmployeeListActivity.class)
                        .putExtra("D", "D"));

            }
        });


        listRetireEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RetirementActivity.class)
                        .putExtra("D", "D"));

            }
        });
        listBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MoterCycleListActivity.class)
                        .putExtra("branch_code", branchCode)
                        .putExtra("loginType", loginType)
                        .putExtra("status", "A"));

            }
        });
        listInsidental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InsCompanyListActivity.class));

            }
        });

        listWeightMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WeightMCListActivity.class));

            }
        });
        listConveyance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EmployeeConvenceListActivity.class));

            }
        });
        listlandlord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LandLordListActivity.class));

            }
        });


        liAdministrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AdministratiExpListActivity.class));

            }
        });

    }

    private class The_slide_timer extends TimerTask {
        @Override
        public void run() {

            try {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (page.getCurrentItem() < listItems.size() - 1) {
                            page.setCurrentItem(page.getCurrentItem() + 1);
                        } else
                            page.setCurrentItem(0);
                    }
                });
            } catch (Exception e) {
              //  Log.e(TAG, "run: ", e);
            }
        }
    }
    private void userPermissionList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                apiPresenter.getUserPermissionList(disposable, Constants.ApiRequestCode.PERMISSION_LIST,
                        RetrofitClient.getStringUserPreference(requireActivity(),Constants.REGION_ID),
                        RetrofitClient.getStringUserPreference(getActivity(),Constants.EMP_CODE));
            } else {
                new ErrorHandlerCode(getActivity(), NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getUser_listFromWebservice", e);
        }
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult: " + object);

        if (callAPIId == Constants.ApiRequestCode.PERMISSION_LIST) {
            AppPermissionListResponse appPermissionListResponse = (AppPermissionListResponse) object;
            getEmpResp(appPermissionListResponse);
        }
    }

    private void getEmpResp(AppPermissionListResponse appPermissionListResponse) {
        try {
            Log.e(Constraints.TAG, "appPermissionListResponse: " + new GsonBuilder().create().toJson(appPermissionListResponse));
            if (appPermissionListResponse != null) {
                if (appPermissionListResponse.getUser_list() != null) {

                    RetrofitClient.saveUserPreference(requireActivity(), Constants.DIVISION_BRANCHES_PERMISSION, appPermissionListResponse.getUser_list().get(0).getDiv_branches());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.DIVISION_MESS_PERMISSION, appPermissionListResponse.getUser_list().get(0).getDiv_mess());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.SEND_PROFILE_PERMISSION, appPermissionListResponse.getUser_list().get(0).getSend_profile());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.EMP_DETAILS_PERMISSION, appPermissionListResponse.getUser_list().get(0).getEmp_list());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.EMP_BIRTHDAY_DETAILS_PERMISSION, appPermissionListResponse.getUser_list().get(0).getEmp_birthday());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.RETIREMENT_DETAILS_PERMISSION, appPermissionListResponse.getUser_list().get(0).getRetire_list());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.MOTERCYCLE_LIST_PERMISSION, appPermissionListResponse.getUser_list().get(0).getMotercy_list());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.INCIDENTAL_LIST_PERMISSION, appPermissionListResponse.getUser_list().get(0).getInciden_list());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.WEIGHT_MC_LIST_PERMISSION, appPermissionListResponse.getUser_list().get(0).getWeightmc_list());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.EMP_CONVEYANCE_PERMISSION, appPermissionListResponse.getUser_list().get(0).getConveyance());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.LANDLORD_DETAILS_PERMISSION, appPermissionListResponse.getUser_list().get(0).getLanlord_details());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.ADMIN_EXPENCES_PERMISSION, appPermissionListResponse.getUser_list().get(0).getAdmin_exp_list());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.SENDEMP_MESSAGE_PERMISSION, appPermissionListResponse.getUser_list().get(0).getSendEmp_message());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.ADD_MESS_PERMISSION, appPermissionListResponse.getUser_list().get(0).getAdd_mess());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.ADD_EMP_PERMISSION, appPermissionListResponse.getUser_list().get(0).getAdd_emp());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.ADD_CONVEYANCE_PERMISSION, appPermissionListResponse.getUser_list().get(0).getAdd_convence_mobExp());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.ADD_SWEEPERPEON_PERMISSION, appPermissionListResponse.getUser_list().get(0).getAdd_sweeperPeon());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.TRANSFER_EMP_PERMISSION, appPermissionListResponse.getUser_list().get(0).getTransfer_emp());
                    RetrofitClient.saveUserPreference(requireActivity(), Constants.ACTIVE_DEACTIVE_PERMISSION, appPermissionListResponse.getUser_list().get(0).getActive_deactiveEmp());

                }

            } else {

                ELog.e(Constraints.TAG, "Exception in appPermissionListResponse" + appPermissionListResponse.getResponseCode());
                // Toast.makeText(this, appPermissionListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(Constraints.TAG, "Exception in appPermissionListResponse", e);
        }
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
    }

    @Override
    public void onResume() {
        userPermissionList();
        super.onResume();
    }
}