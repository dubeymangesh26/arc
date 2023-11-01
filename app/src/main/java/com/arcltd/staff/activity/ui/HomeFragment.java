package com.arcltd.staff.activity.ui;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.ContentValues.TAG;

import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.addData.AddVehiclePlaceFaildActivity;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.activity.detailsData.AdministativeExpensesDetailsActivity;
import com.arcltd.staff.activity.detailsData.ViewExcelActivity;
import com.arcltd.staff.activity.listData.BranchListActivity;
import com.arcltd.staff.activity.listData.BranchListPincodeWiseActivity;
import com.arcltd.staff.activity.listData.CustomerListActivity;
import com.arcltd.staff.activity.listData.DivisionListActivity;
import com.arcltd.staff.activity.listData.RateCardListActivity;
import com.arcltd.staff.activity.listData.VehiclePlaceFaildListActivity;
import com.arcltd.staff.activity.messDetails.MessDivisionListActivity;
import com.arcltd.staff.activity.otherAndMain.OtherExpensesActivity;
import com.arcltd.staff.adapter.The_Slide_items_Pager_Adapter;
import com.arcltd.staff.authentication.LoginActivity;
import com.arcltd.staff.base.BaseFragment;
import com.arcltd.staff.cns_tracking.CNSTrackingActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.AppPermissionListResponse;
import com.arcltd.staff.response.CheckStatusResponse;
import com.arcltd.staff.response.ConsignmentTrackListResponse;
import com.arcltd.staff.response.SendDiviceTokenResponse;
import com.arcltd.staff.response.The_Slide_Items_Model_Class;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.arcltd.staff.utility.UserSessionManager;
import com.arcltd.staff.web_view.WebViewActiviry;
import com.arcltd.staff.web_view.WebViewpoppupActivity;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;


public class HomeFragment extends BaseFragment {
    CardView cvBranchList, cvTrackBNS;
    AutoCompleteTextView atCnsSearch;
    RelativeLayout rlBranchList,rkTrackCNS, divMess, div, cvAdvExpences, cvOtherExpences, cvSendProfiel,
            cvARC, cvAdvCustomer, cvAddFaieldList, cvAddFaield, incharge,rkRateCard;
    ProgressBar searchProgressBar;
    ImageView ivCnsSearch;
    LinearLayout liHR, liBM;
    private List<The_Slide_Items_Model_Class> listItems;
    private ViewPager page;
    TabLayout tabLayout;
    String corp_profile = "https://arclimited.com/corporate-profile.php";
    String cnsTrack = "https://online.arclimited.com/cnstrk/cnstrk.aspx";
    // String cnsTrack="http://onlineformsolution.in/arcMessApi/update_employee.php";
    UserSessionManager session;
    String uriString, path, folderPath;
    Uri bmpUri;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        HandleAppCrashActivity.deploy(getActivity(), CrashReportActivity.class);
        ativeStatus();
        diviceSendtoken();
        userPermissionList();
        getview(view);


        try {
            if (RetrofitClient.getStringUserPreference(requireActivity(), Constants.DIVISION_BRANCHES_PERMISSION) != null) {
                if (!RetrofitClient.getStringUserPreference(requireActivity(), Constants.DIVISION_BRANCHES_PERMISSION).equals("N")) {
                    div.setVisibility(View.VISIBLE);
                }
                if (!RetrofitClient.getStringUserPreference(requireActivity(), Constants.DIVISION_MESS_PERMISSION).equals("N")) {
                    divMess.setVisibility(View.VISIBLE);
                }
                if (!RetrofitClient.getStringUserPreference(requireActivity(), Constants.SEND_PROFILE_PERMISSION).equals("N")) {
                    cvSendProfiel.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: ", e);
        }


        return view;
    }


    private void getview(View view) {
        div = view.findViewById(R.id.div);
        divMess = view.findViewById(R.id.divMess);
        cvAdvExpences = view.findViewById(R.id.cvAdvExpences);
        cvOtherExpences = view.findViewById(R.id.cvOtherExpences);
        cvSendProfiel = view.findViewById(R.id.cvSendProfiel);
        cvBranchList = view.findViewById(R.id.cvBranchList);
        rlBranchList = view.findViewById(R.id.rlBranchList);
        liHR = view.findViewById(R.id.liHR);
        liBM = view.findViewById(R.id.liBM);
        cvTrackBNS = view.findViewById(R.id.cvTrackBNS);
        cvARC = view.findViewById(R.id.cvARC);
        cvAdvCustomer = view.findViewById(R.id.cvAdvCustomer);
        cvAddFaieldList = view.findViewById(R.id.cvAddFaieldList);
        cvAddFaield = view.findViewById(R.id.cvAddFaield);
        incharge = view.findViewById(R.id.incharge);
        view.findViewById(R.id.comStatus).setSelected(true);
        page = view.findViewById(R.id.my_pager);
        tabLayout = view.findViewById(R.id.my_tablayout);
        rkTrackCNS = view.findViewById(R.id.rkTrackCNS);
        ivCnsSearch = view.findViewById(R.id.ivCnsSearch);
        atCnsSearch = view.findViewById(R.id.atCnsSearch);
        rkRateCard = view.findViewById(R.id.rkRateCard);
        searchProgressBar = view.findViewById(R.id.searchProgressBar);


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


        ActivityCompat.requestPermissions(getActivity(),
                new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);


        if (RetrofitClient.getStringUserPreference(getContext(), Constants.LOGIN_TYPE).equals("DGM")) {
            cvAdvCustomer.setVisibility(View.VISIBLE);
            cvAddFaieldList.setVisibility(View.GONE);
        } else if (RetrofitClient.getStringUserPreference(getContext(), Constants.LOGIN_TYPE).equals("BM")) {
            cvAdvCustomer.setVisibility(View.VISIBLE);
        } else {
            cvAdvCustomer.setVisibility(View.GONE);
        }

        if (RetrofitClient.getStringUserPreference(getContext(), Constants.LOGIN_TYPE).equals("BM")) {
            liBM.setVisibility(View.VISIBLE);
        } else {
            liBM.setVisibility(View.GONE);
        }

        ivCnsSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!atCnsSearch.getText().toString().equals("")) {
                    cnsTracking();
                   /* Intent intent = new Intent(getActivity(), CNSTrackingActivity.class)
                            .putExtra("CNS", atCnsSearch.getText().toString());
                    startActivity(intent);*/
                }else {
                    Toast.makeText(getActivity(), "Please Enter Consignment Number", Toast.LENGTH_LONG).show();
                }

            }
        });


        rkRateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RateCardListActivity.class);
                startActivity(intent);
            }
        });

         cvAddFaield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddVehiclePlaceFaildActivity.class);
                startActivity(intent);
            }
        });

        incharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewExcelActivity.class);
                startActivity(intent);
            }
        });
        cvAddFaieldList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VehiclePlaceFaildListActivity.class);
                startActivity(intent);
            }
        });
        div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DivisionListActivity.class);
                startActivity(intent);
            }
        });
        cvAdvCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CustomerListActivity.class);
                startActivity(intent);
            }
        });

        cvTrackBNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BranchListPincodeWiseActivity.class);
                startActivity(intent);
            }
        });
        rkTrackCNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewpoppupActivity.class)
                        .putExtra("url", cnsTrack);
                startActivity(intent);
            }
        });
        cvARC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActiviry.class)
                        .putExtra("url", corp_profile);
                startActivity(intent);
            }
        });
        divMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessDivisionListActivity.class);
                startActivity(intent);
            }
        });


        cvAdvExpences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdministativeExpensesDetailsActivity.class)
                        .putExtra("OFFICETYPE", "OFFICE");
                startActivity(intent);
            }
        });

        cvOtherExpences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OtherExpensesActivity.class)
                        .putExtra("OFFICETYPE", "STAFF RESIDENCE");
                startActivity(intent);
            }
        });

        cvSendProfiel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/html");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
                i.putExtra(Intent.EXTRA_SUBJECT, "Our Proposal From M/s Associated Road Carriers Limited");
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                i.putExtra(Intent.EXTRA_TEXT, "Dear Sir,\n" +
                        "\n" +
                        "Greetings of the Day !!!!\n" +
                        "\n" +
                        "Greetings from Associated Road Carriers Limited!\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "    First up all like to take the opportunity of thanking you for providing Associated Road Carriers Limited. to get related with your Esteem Organization\n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "Today we take pride in the fact that ARC is one of India’s Largest ISO 9001-2015 accredited Surface Transport Organization and has evolved into a huge corporate conglomerate.ARC is built on the strong pillars of Integrity, Massive Infrastructure, Operational Expertise, Financial soundness and a professional team with a Strong Will to Serve by adhering to enduring traditions and lasting values.\n" +
                        "\n" +
                        "Associated Road Carriers Limited\n" +
                        "\n" +
                        "Our Beginning\n" +
                        "\n" +
                        "Established on 30/04/1972 as Associated Road Carriers Limited.  50 years of experience and expertise in the field of road transportation.\n" +
                        "\n" +
                        "Our Vision\n" +
                        "\n" +
                        "We will give our customers the best value because we will offer them the highest standards of quality and efficiency, in ways that are most useful to them. To provide them comprehensive and “can-do” solutions, we will form a network of complementary allies who share our values and standards.\n" +
                        "\n" +
                        "Network,Infrastructure & Service :-\n" +
                        "\n" +
                        "As from little acorns grow mighty oaks, from one truck, five branches and a staff of fifteen, the size and magnitude of ARC have grown to achieve a massive turnover with over 2400 dedicated professionals, 585+ owned outlets in 400+ cities, spanning the length and breadth of the nation. At any given point of time, over 3500 vehicles are traversing the roads, covering about 3,00,000 km. everyday, delivering consignments to 5000+ destinations. More than 3 million tons of cargo is transported annually, adding up to a whopping 10,000 Crores worth of material.\n" +
                        "\n" +
                        "Our Mission\n" +
                        "\n" +
                        "To keep pace with the latest technological demand of the market, our mission is to provide point-to-point transport service through efficient fleet management and Internet utilities thereby monitoring the movements of the goods.\n" +
                        "\n" +
                        "Our Infrastructure\n" +
                        "\n" +
                        "Pan India Network of 585+ outlets in 400+ cities serving 5000+ destinations & spread out in 24 States.\n" +
                        "\n" +
                        "Efficient fleet of over 3500+ vehicles operating round the clock covering over a million kilometers every day across India.\n" +
                        "\n" +
                        "Extensive network in West, South, North and presence in East.\n" +
                        "\n" +
                        "Unmatched Infrastructural Capabilities with State of the art facilities.\n" +
                        "\n" +
                        "Truck platform level warehouses at all transshipment and major branches for safe handling of heavy cargo.   \n" +
                        "\n" +
                        "Dedicated team to handle all kinds of cargo safely as per customer requirements.    \n" +
                        "\n" +
                        "Majority of offices are computerized.\n" +
                        "\n" +
                        "Secured CCTV Surveillance available across all Major Transhipment Centers.\n" +
                        "\n" +
                        "24 x 7 Online Track & Trace facility. www.arclimited.com   \n" +
                        "\n" +
                        " \n" +
                        "\n" +
                        "ARC : An internally developed comprehensive online operations software tailor-made, which captures the complete operation at all stages of transportation, right from booking till final delivery of consignments.\n" +
                        "\n" +
                        "Our Workforce\n" +
                        "\n" +
                        "ARC: Efficient, reliable and secure transportation of your consignments. Approved by the Indian Banks Association.IBA Code: CAA – 273.  Certified by ISO 9001-2015.\n" +
                        "ARC: Comprehensive warehousing and Inventory Management solutions.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "GST Number : 27AACCA4861C2ZY\n" +
                        "\n" +
                        "Download Company profile\n" + "https://arclimited.com/uploads/Corporate_Profile.pdf"
                        +
                        "\n");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rlBranchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BranchListActivity.class)
                        .putExtra("OFFICETYPE", "GODOWN"));
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

    private void ativeStatus() {
        try {
            if (Infrastructure.isInternetPresent()) {
                apiPresenter.checkStaus(disposable, Constants.ApiRequestCode.ACTIV_STATUS,
                        RetrofitClient.getStringUserPreference(requireActivity(), Constants.EMP_CODE));

            } else {
                new ErrorHandlerCode(getActivity(), NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getUser_listFromWebservice", e);
        }
    }

    private void cnsTracking() {
        try {
            if (Infrastructure.isInternetPresent()) {
                searchProgressBar.setVisibility(View.VISIBLE);
                ivCnsSearch.setVisibility(View.GONE);
                apiPresenter.sendCNSNO(disposable, Constants.ApiRequestCode.CNSTRACK,
                        atCnsSearch.getText().toString());

            } else {
                new ErrorHandlerCode(getActivity(), NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getConsignment_details", e);
        }

    }

    private void diviceSendtoken() {
        try {
            if (Infrastructure.isInternetPresent()) {

                apiPresenter.sendToken(disposable, Constants.ApiRequestCode.TOKEN,
                        RetrofitClient.getStringUserPreference(getActivity(), Constants.EMP_CODE),
                        RetrofitClient.getStringUserPreference(getActivity(), Constants.DEVICE_TOKEN)
                );

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
        searchProgressBar.setVisibility(View.GONE);
        ivCnsSearch.setVisibility(View.VISIBLE);
        if (callAPIId == Constants.ApiRequestCode.CNSTRACK) {
            ConsignmentTrackListResponse consignmentTrackListResponse = (ConsignmentTrackListResponse) object;
            getCNSTRACK(consignmentTrackListResponse);
        }
        if (callAPIId == Constants.ApiRequestCode.ACTIV_STATUS) {
            CheckStatusResponse checkStatusResponse = (CheckStatusResponse) object;
            getStatus(checkStatusResponse);
        }

        if (callAPIId == Constants.ApiRequestCode.TOKEN) {
            SendDiviceTokenResponse sendDiviceTokenResponse = (SendDiviceTokenResponse) object;
            deviceToken(sendDiviceTokenResponse);
        }

        if (callAPIId == Constants.ApiRequestCode.PERMISSION_LIST) {
            AppPermissionListResponse appPermissionListResponse = (AppPermissionListResponse) object;
            getEmpResp(appPermissionListResponse);
        }
    }

    private void getCNSTRACK(ConsignmentTrackListResponse consignmentTrackListResponse) {

        try {
            Log.e(Constraints.TAG, "consignmentTrackListResponse: " + new GsonBuilder().create().toJson(consignmentTrackListResponse));
            if (consignmentTrackListResponse != null) {
                searchProgressBar.setVisibility(View.GONE);
                if (consignmentTrackListResponse.getConsignment_list() != null) {
                    String data = new Gson().toJson(consignmentTrackListResponse.getConsignment_list());
                    Log.e("TAG", "saddLayoutCallBack: "+data );
                    Intent intent = new Intent(getActivity(), CNSTrackingActivity.class)
                            .putExtra("CNS_data", data);
                    startActivity(intent);
                }
            } else {

                ELog.e(Constraints.TAG, "Exception in consignmentTrackListResponse" + consignmentTrackListResponse.getResponseCode());
                Toast.makeText(getActivity(), consignmentTrackListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(Constraints.TAG, "Exception in appPermissionListResponse", e);
        }

    }

    private void deviceToken(SendDiviceTokenResponse sendDiviceTokenResponse) {
        try {
            Log.e(Constraints.TAG, "sendDiviceTokenResponse: " + new GsonBuilder().create().toJson(sendDiviceTokenResponse));
            if (sendDiviceTokenResponse != null) {


            } else {
                ELog.e(Constraints.TAG, "Exception in sendDiviceTokenResponse" + sendDiviceTokenResponse.getResponseCode());
                // Toast.makeText(getActivity(), employeeActive_inctiveResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(Constraints.TAG, "Exception in sendDiviceTokenResponse", e);
        }
    }


    private void getStatus(CheckStatusResponse checkStatusResponse) {
        try {
            // Log.e(Constraints.TAG, "branchListResponse: " + new GsonBuilder().create().toJson(checkStatusResponse));
            if (checkStatusResponse != null) {
                if (checkStatusResponse.getEmployee() != null) {
                    int f = checkStatusResponse.getEmployee().size();
                    // Log.e("ListSize ", "ArrySize" + f);
                    RetrofitClient.saveUserPreference(getActivity(),
                            Constants.NOOF_EMPLOYEE, String.valueOf(checkStatusResponse.getEmployee().size()));
                    ;

                }
                if (!checkStatusResponse.getUserName().getStatus().equals("A")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Your ID Is Block, Please Contact ADMIN ?")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (Boolean.valueOf(RetrofitClient.getStringUserPreference(
                                            getActivity(), Constants.MEMBER_ID)) == true) {
                                        session.logoutUser();
                                    } else {

                                    }
                                    RetrofitClient.clearAllPreference(getActivity());
                                    SharedPreferences sharedPreferences =
                                            PreferenceManager.getDefaultSharedPreferences(getActivity());
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();
                                    sharedPreferences.edit().clear().apply();
                                    sharedPreferences.edit().remove("COU").commit();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                    /*    HomeActivity.this.finish();*/
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setTextColor(Color.rgb(30, 144, 255));
                    Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                    nbutton.setTextColor(Color.rgb(30, 144, 255));
                }

            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(Constraints.TAG, "Exception in checkLoginResponse" + checkStatusResponse.getResponseCode());
                Toast.makeText(getActivity(), checkStatusResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(Constraints.TAG, "Exception in checkLoginResponse", e);
        }
    }


    private void userPermissionList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                apiPresenter.getUserPermissionList(disposable, Constants.ApiRequestCode.PERMISSION_LIST,
                        RetrofitClient.getStringUserPreference(requireActivity(), Constants.REGION_ID),
                        RetrofitClient.getStringUserPreference(requireActivity(), Constants.EMP_CODE));
            } else {
                new ErrorHandlerCode(getActivity(), NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getUser_listFromWebservice", e);
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

                    if (!RetrofitClient.getStringUserPreference(requireActivity(), Constants.DIVISION_BRANCHES_PERMISSION).equals("N")) {
                        div.setVisibility(View.VISIBLE);
                    }
                    if (!RetrofitClient.getStringUserPreference(requireActivity(), Constants.DIVISION_MESS_PERMISSION).equals("N")) {
                        divMess.setVisibility(View.VISIBLE);
                    }
                    if (!RetrofitClient.getStringUserPreference(requireActivity(), Constants.SEND_PROFILE_PERMISSION).equals("N")) {
                        cvSendProfiel.setVisibility(View.VISIBLE);
                    }
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