package com.arcltd.staff.activity.without_login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.listData.BranchListActivity;
import com.arcltd.staff.activity.listData.BranchListPincodeWiseActivity;
import com.arcltd.staff.activity.otherAndMain.MainActivity;
import com.arcltd.staff.adapter.The_Slide_items_Pager_Adapter;
import com.arcltd.staff.authentication.LoginActivity;
import com.arcltd.staff.response.The_Slide_Items_Model_Class;
import com.arcltd.staff.web_view.ArcWebViewActivity;
import com.arcltd.staff.web_view.WebViewActiviry;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public  class WithoutLoginActivity extends AppCompatActivity {
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    private static final int FLEXIBLE_APP_UPDATE_REQ_CODE = 123;

    CardView cvBranchList, cvPincode,cvEmpLogin;
    RelativeLayout rkTrackCNS, cvARC;
    private List<The_Slide_Items_Model_Class> listItems;
    private ViewPager page;
    TabLayout tabLayout;
    TextView comStatus;
    String corp_profile = "https://arclimited.com/corporate-profile.php";
    String cnsTrack = "https://online.arclimited.com/cnstrk/cnstrk.aspx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_without_login);

        cvBranchList = findViewById(R.id.cvBranchList);
        cvPincode = findViewById(R.id.cvPincode);
        cvEmpLogin = findViewById(R.id.cvEmpLogin);
        rkTrackCNS = findViewById(R.id.rkTrackCNS);
        cvARC = findViewById(R.id.cvARC);
        page = findViewById(R.id.my_pager);
        tabLayout = findViewById(R.id.my_tablayout);
        comStatus=findViewById(R.id.comStatus);
        comStatus.setSelected(true);


        listItems = new ArrayList<>();
        listItems.add(new The_Slide_Items_Model_Class(R.drawable.slide_zero, "Slider 1 Title"));
        listItems.add(new The_Slide_Items_Model_Class(R.drawable.slide_one, "Slider 1 Title"));
        listItems.add(new The_Slide_Items_Model_Class(R.drawable.slide_two, "Slider 2 Title"));
        listItems.add(new The_Slide_Items_Model_Class(R.drawable.slide_three, "Slider 3 Title"));
        listItems.add(new The_Slide_Items_Model_Class(R.drawable.slide_four, "Slider 4 Title"));

        The_Slide_items_Pager_Adapter itemsPager_adapter = new The_Slide_items_Pager_Adapter(this, listItems);
        page.setAdapter(itemsPager_adapter);

        // The_slide_timer
        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new The_slide_timer(), 2000, 3000);
        tabLayout.setupWithViewPager(page, true);

        rkTrackCNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WithoutLoginActivity.this, ArcWebViewActivity.class)
                        .putExtra("url", cnsTrack);
                startActivity(intent);
            }
        });
        cvARC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WithoutLoginActivity.this, WebViewActiviry.class)
                        .putExtra("url", corp_profile);
                startActivity(intent);
            }
        });
        cvEmpLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WithoutLoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        cvPincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WithoutLoginActivity.this,
                        BranchListPincodeWiseActivity.class);
                startActivity(intent);
            }
        });


        cvBranchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WithoutLoginActivity.this, BranchListActivity.class)
                        .putExtra("OFFICETYPE", "GODOWN"));
            }
        });


        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        installStateUpdatedListener = state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                removeInstallStateUpdateListener();
            } else {
                Toast.makeText(getApplicationContext(), "InstallStateUpdatedListener: state: " + state.installStatus(), Toast.LENGTH_LONG).show();
            }
        };
        appUpdateManager.registerListener(installStateUpdatedListener);
        checkUpdate();

    }


    private void checkUpdate() {

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                startUpdateFlow(appUpdateInfo);
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            }
        });
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, WithoutLoginActivity.FLEXIBLE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLEXIBLE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),"Update success! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
                checkUpdate();
            }
        }
    }

    private void popupSnackBarForCompleteUpdate() {
        Snackbar.make(findViewById(android.R.id.content).getRootView(), "New app is ready!", Snackbar.LENGTH_INDEFINITE)

                .setAction("Install", view -> {
                    if (appUpdateManager != null) {
                        appUpdateManager.completeUpdate();
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.purple_500))
                .show();
    }

    private void removeInstallStateUpdateListener() {
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeInstallStateUpdateListener();
    }



    private class The_slide_timer extends TimerTask {
        @Override
        public void run() {

            try {
                runOnUiThread(new Runnable() {
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WithoutLoginActivity.this);
        builder.setMessage("Really Exit ??");
        builder.setTitle(R.string.app_name);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new WithoutLoginActivity.MyListener());
        builder.setNegativeButton("Cancel", null);
        builder.show();

    }

    private class MyListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }


}