package com.arcltd.staff.activity.without_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.listData.BranchListActivity;
import com.arcltd.staff.activity.listData.BranchListPincodeWiseActivity;
import com.arcltd.staff.adapter.The_Slide_items_Pager_Adapter;
import com.arcltd.staff.authentication.LoginActivity;
import com.arcltd.staff.response.The_Slide_Items_Model_Class;
import com.arcltd.staff.web_view.ArcWebViewActivity;
import com.arcltd.staff.web_view.WebViewActiviry;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public  class WithoutLoginActivity extends AppCompatActivity {
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


}