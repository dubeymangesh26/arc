package com.arcltd.staff.activity.ui;

import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.Constraints;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.addData.AddConveyanceMobileActivity;
import com.arcltd.staff.activity.addData.AddMessActivity;
import com.arcltd.staff.activity.addData.AddSweeperPeonActivity;
import com.arcltd.staff.activity.addData.AddUpdateEmployeeActivity;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.activity.employee.EmpMessageListActivity;
import com.arcltd.staff.activity.employee.EmployeeMessageActivity;
import com.arcltd.staff.activity.otherAndMain.SendWhatsappMessageActivity;
import com.arcltd.staff.activity.listData.UserPermissionListActivity;
import com.arcltd.staff.authentication.LoginActivity;
import com.arcltd.staff.authentication.ProfileDetailsActivity;
import com.arcltd.staff.authentication.ResetPassword;
import com.arcltd.staff.base.BaseFragment;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.AppPermissionListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.arcltd.staff.utility.UserSessionManager;
import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;

import java.security.KeyStore;

import javax.crypto.Cipher;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends BaseFragment {
    private SharedPreferences pref;
    private KeyStore keyStore;
    CircleImageView profilePicture;
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "androidHive";
    private Cipher cipher;

    TextView logout, name, mobile_no;
    UserSessionManager session;
    LinearLayout personalinfobtn,  liAddMess, regionManager,
            addConveyanceMobile, addSweeperPeon, profile, passwardUpdate,liAdd_Emp,
            listSendMessage,liNotification,appPermission,sendMsgWhatsapp;
    String fullName, LastName, custId, email, mobile;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        HandleAppCrashActivity.deploy(getActivity(), CrashReportActivity.class);
        userPermissionList();
        getdataCommon(view);
        fullName = RetrofitClient.getStringUserPreference(getActivity(), Constants.FIRSTNAME);
        mobile = RetrofitClient.getStringUserPreference(getActivity(), Constants.MOBILE_NO);
        name.setText(fullName);
        mobile_no.setText(mobile);
        if (RetrofitClient.getStringUserPreference(getActivity(), Constants.FIRSTNAME) != null || RetrofitClient.getStringUserPreference(getActivity(), Constants.LASTNAME) != null) {
            fullName = RetrofitClient.getStringUserPreference(getActivity(), Constants.FIRSTNAME) + " " + RetrofitClient.getStringUserPreference(getActivity(), Constants.LASTNAME);
        }



        if (!RetrofitClient.getStringUserPreference(requireActivity(),Constants.SENDEMP_MESSAGE_PERMISSION).equals("N")){
            listSendMessage.setVisibility(View.VISIBLE);
        }
        if (!RetrofitClient.getStringUserPreference(requireActivity(),Constants.ADD_MESS_PERMISSION).equals("N")){
            liAddMess.setVisibility(View.VISIBLE);
        }
        if (!RetrofitClient.getStringUserPreference(requireActivity(),Constants.ADD_EMP_PERMISSION).equals("N")){
            liAdd_Emp.setVisibility(View.VISIBLE);
        }
        if (!RetrofitClient.getStringUserPreference(requireActivity(),Constants.ADD_CONVEYANCE_PERMISSION).equals("N")){
            addConveyanceMobile.setVisibility(View.VISIBLE);
        }
        if (!RetrofitClient.getStringUserPreference(requireActivity(),Constants.ADD_SWEEPERPEON_PERMISSION).equals("N")){
            addSweeperPeon.setVisibility(View.VISIBLE);
        }

        if (RetrofitClient.getStringUserPreference(requireActivity(),Constants.LOGIN_TYPE).equals("RG")||
                RetrofitClient.getStringUserPreference(requireActivity(),Constants.EMP_CODE).equals("A8513")){
            appPermission.setVisibility(View.VISIBLE);
        }



        return view;
    }

    private void getdataCommon(View view) {

        liAddMess = view.findViewById(R.id.liAddMess);
        logout = view.findViewById(R.id.logout);
        name = view.findViewById(R.id.name);
        mobile_no = view.findViewById(R.id.mobile_no);
        personalinfobtn = view.findViewById(R.id.personalinfobtn);
        regionManager = view.findViewById(R.id.regionManager);
        addConveyanceMobile = view.findViewById(R.id.addConveyanceMobile);
        addSweeperPeon = view.findViewById(R.id.addSweeperPeon);
        profile = view.findViewById(R.id.profile);
        passwardUpdate = view.findViewById(R.id.passwardUpdate);
        liAdd_Emp = view.findViewById(R.id.liAdd_Emp);
        listSendMessage = view.findViewById(R.id.listSendMessage);
        liNotification = view.findViewById(R.id.liNotification);
        profilePicture = view.findViewById(R.id.profilePicture);
        appPermission = view.findViewById(R.id.appPermission);
        sendMsgWhatsapp = view.findViewById(R.id.sendMsgWhatsapp);
        view.findViewById(R.id.comStatus).setSelected(true);

        Glide.with(this)
                .load(RetrofitClient.getStringUserPreference(getActivity(),Constants.PROFILE_PICTURE)) // image url
                .placeholder(R.drawable.person_24) // any placeholder to load at start
                .error(R.drawable.avatar_10)  // any image in case of error
                .override(200, 200) // resizing
                .centerCrop()
                .into(profilePicture);


        liNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EmpMessageListActivity.class)
                        .putExtra("D", "D"));

            }
        });
        sendMsgWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SendWhatsappMessageActivity.class)
                        .putExtra("details", "Hi"));

            }
        });

        appPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UserPermissionListActivity.class)
                        .putExtra("D", "D"));

            }
        });

        listSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EmployeeMessageActivity.class));

            }
        });
               liAdd_Emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddUpdateEmployeeActivity.class));

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to Logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (Boolean.valueOf(RetrofitClient.getStringUserPreference(getActivity(), Constants.MEMBER_ID)) == true) {
                                    session.logoutUser();
                                } else {

                                }
                                RetrofitClient.clearAllPreference(getActivity());
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(Color.rgb(30, 144, 255));
                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setTextColor(Color.rgb(30, 144, 255));

            }
        });


        liAddMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddMessActivity.class));

            }
        });

        addConveyanceMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddConveyanceMobileActivity.class));

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileDetailsActivity.class));

            }
        });

        addSweeperPeon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddSweeperPeonActivity.class));

            }
        });
        passwardUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ResetPassword.class));
            }
        });

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

        Glide.with(this)
                .load(RetrofitClient.getStringUserPreference(getActivity(),Constants.PROFILE_PICTURE)) // image url
                .placeholder(R.drawable.person_24) // any placeholder to load at start
                .error(R.drawable.avatar_10)  // any image in case of error
                .override(200, 200) // resizing
                .centerCrop()
                .into(profilePicture);
        super.onResume();
    }


}