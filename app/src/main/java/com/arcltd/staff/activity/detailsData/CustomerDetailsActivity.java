package com.arcltd.staff.activity.detailsData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.addData.AddUpdateCustomerActivity;
import com.arcltd.staff.adapter.FeedbackListAdapter;
import com.arcltd.staff.adapter.RemarkListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.response.AddRemarkResponse;
import com.arcltd.staff.response.CustomerListResponse;
import com.arcltd.staff.response.FeedbackListResponse;
import com.arcltd.staff.response.RemarkListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class CustomerDetailsActivity extends BaseActivity implements FeedbackListAdapter.SearchAddressCallBack,
FeedbackListAdapter.AddRemark{
    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    private FeedbackListAdapter.RerfreshWishList activeRerfreshWishList;
    String data, cust_id,f_id,branch_code;
    CustomerListResponse.CustomerList dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_customer_details);

        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("data");
            dataBean = new Gson().fromJson(data, CustomerListResponse.CustomerList.class);
            Log.e("", "onCreate: " + new GsonBuilder().create().toJson(dataBean));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        list = findViewById(R.id.list);
        swiptoRefresh = findViewById(R.id.swiptoRefresh);


        try {

            cust_id = dataBean.getCust_id();
            branch_code = dataBean.getBranch_code();

        } catch (Exception e) {
            Log.e("", "Data  error: " + e);
        }

        feedbackList();

        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                feedbackList();
                swiptoRefresh.setRefreshing(false);
            }
        });

    }

    private void feedbackList() {

        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.feedbackList(disposable, Constants.ApiRequestCode.FEEDBACK_LIST, cust_id);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }

    private void addRemark(String remark) {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.addCustRemark(disposable, Constants.ApiRequestCode.ADD_REMARK,
                        f_id,cust_id,branch_code,remark);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }




    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult_ feedbackListResponse: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.FEEDBACK_LIST) {
            FeedbackListResponse feedbackListResponse = (FeedbackListResponse) object;
            getFeedbackList(feedbackListResponse);
        }

        if (callAPIId == Constants.ApiRequestCode.REMARK_LIST) {
            RemarkListResponse remarkListResponse = (RemarkListResponse) object;
            getRemarkListList(remarkListResponse);
        }

        if (callAPIId == Constants.ApiRequestCode.ADD_REMARK) {
            AddRemarkResponse addRemarkResponse = (AddRemarkResponse) object;
            addRemarkList(addRemarkResponse);
        }
    }

    private void addRemarkList(AddRemarkResponse addRemarkResponse) {
        try {
            Log.e(TAG, "addRemarkResponse: " + new GsonBuilder().create().toJson(addRemarkResponse));
            if (addRemarkResponse != null) {
                if (addRemarkResponse.getResponseCode() != null) {
                    Infrastructure.dismissProgressDialog();
                       finish();

                } else {
                    Toast.makeText(this, addRemarkResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in feedbackListResponse" + addRemarkResponse.getResponseCode());
                Toast.makeText(this, addRemarkResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in feedbackListResponse", e);
        }
    }


    private void getFeedbackList(FeedbackListResponse feedbackListResponse) {
        try {
            Log.e(TAG, "feedbackListResponse: " + new GsonBuilder().create().toJson(feedbackListResponse));
            if (feedbackListResponse != null) {
                if (feedbackListResponse.getFeedback_list() != null) {
                    Infrastructure.dismissProgressDialog();

                    list.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, false));
                    FeedbackListAdapter listAdapter = new FeedbackListAdapter(this,
                            feedbackListResponse.getFeedback_list(), list, feedbackListResponse,
                            this,this);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(this, feedbackListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in feedbackListResponse" + feedbackListResponse.getResponseCode());
                Toast.makeText(this, feedbackListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in feedbackListResponse", e);
        }
    }

    private void getRemarkListList(RemarkListResponse remarkListResponse) {
        try {
            Log.e(TAG, "remarkListResponse: " + new GsonBuilder().create().toJson(remarkListResponse));
            if (remarkListResponse != null) {
                if (remarkListResponse.getRemark_list() != null) {
                    Infrastructure.dismissProgressDialog();

                     Dialog dialog = new Dialog(this);
                    dialog.setContentView(R.layout.item_feedbacklist);
                    dialog.getWindow()
                            .setLayout(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );

                    RecyclerView reList=dialog.findViewById(R.id.list);

                    reList.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, false));
                    RemarkListAdapter listAdapter = new RemarkListAdapter(this,
                            remarkListResponse.getRemark_list(), list, remarkListResponse);
                    reList.setAdapter(listAdapter);
                    reList.setVisibility(View.VISIBLE);
                    listAdapter.notifyDataSetChanged();

                    ImageView close = (ImageView) dialog.findViewById(R.id.close);

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();


                } else {
                    Toast.makeText(this, remarkListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in remarkListResponse" + remarkListResponse.getResponseCode());
                Toast.makeText(this, remarkListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in remarkListResponse", e);
        }

    }


    private boolean validate(TextInputEditText etRemark) {
        if (TextUtils.isEmpty(etRemark.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Remark!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
    }

    public void searchbtn(View view) {
        startActivity(new Intent(this, AddUpdateCustomerActivity.class));
    }

    @Override
    public void searchAddressCallBack(FeedbackListResponse.FeedbackList feedbackList) {
         f_id = feedbackList.getF_id();
        getRemarkList(f_id);

    }

    private void getRemarkList(String f_id) {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.remarkList(disposable, Constants.ApiRequestCode.REMARK_LIST, f_id);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }

    }


    @Override
    public void addRemarksdd(FeedbackListResponse.FeedbackList feedbackList) {
        f_id=feedbackList.getF_id();
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_add_feedbacklist);
        dialog.getWindow()
                .setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        TextInputEditText etRemark=dialog.findViewById(R.id.etRemark);

        Button submit = dialog.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate(etRemark)) {
                    addRemark(etRemark.getText().toString().trim());
                     dialog.dismiss();
                }
            }
        });


        ImageView close = (ImageView) dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

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