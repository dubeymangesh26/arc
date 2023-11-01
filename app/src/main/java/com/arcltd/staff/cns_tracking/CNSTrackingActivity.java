package com.arcltd.staff.cns_tracking;

import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.constraintlayout.widget.Constraints;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arcltd.staff.R;
import com.arcltd.staff.adapter.CNSMovementListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.response.ConsignmentMovementListResponse;
import com.arcltd.staff.response.ConsignmentTrackListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class CNSTrackingActivity extends BaseActivity {
    TextView cnsNO, dlyStatus, dlyDate, bkgDate, expDate, bkgFrom, bkgTo, cnsWeight, dlyType,
            cnsPackage, paymType, invocieNo, invDate, consignor, consignee, cnsPaymSide, notMoved, podView, branchCode;
    String data, cnsTrackNo,podIMAGE;
    ImageView podsm;
    RecyclerView list;
    ProgressBar searchProgressBar;

    ConsignmentTrackListResponse.ConsignmentList dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cnstracking);

        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("CNS_data");
            dataBean = new Gson().fromJson(data, ConsignmentTrackListResponse.ConsignmentList.class);
            Log.e("", "onCreate: " + new GsonBuilder().create().toJson(dataBean));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }

        list = findViewById(R.id.list);
        searchProgressBar = findViewById(R.id.searchProgressBar);
        notMoved = findViewById(R.id.notMoved);
        podView = findViewById(R.id.podView);
        cnsNO = findViewById(R.id.cnsNO);
        dlyStatus = findViewById(R.id.dlyStatus);
        dlyDate = findViewById(R.id.dlyDate);
        bkgDate = findViewById(R.id.bkgDate);
        expDate = findViewById(R.id.expDate);
        bkgFrom = findViewById(R.id.bkgFrom);
        bkgTo = findViewById(R.id.bkgTo);
        cnsWeight = findViewById(R.id.cnsWeight);
        dlyType = findViewById(R.id.dlyType);
        cnsPackage = findViewById(R.id.cnsPackage);
        paymType = findViewById(R.id.paymType);
        invocieNo = findViewById(R.id.invocieNo);
        invDate = findViewById(R.id.invDate);
        consignor = findViewById(R.id.consignor);
        consignee = findViewById(R.id.consignee);
        cnsPaymSide = findViewById(R.id.cnsPaymSide);
        branchCode = findViewById(R.id.branchCode);
        podsm = findViewById(R.id.podsm);

        try {
            cnsNO.setText(dataBean.getCns_no());
            bkgDate.setText(dataBean.getBooking_date());
            expDate.setText(dataBean.getExpected_date());
            bkgFrom.setText(dataBean.getCns_from());
            bkgTo.setText(dataBean.getCns_to());
            cnsWeight.setText(dataBean.getActual_weight());
            dlyType.setText(dataBean.getDelivery_type());
            cnsPackage.setText(dataBean.getNo_of_package());
            paymType.setText(dataBean.getPaymentType());
            invocieNo.setText(dataBean.getInvoice_no());
            invDate.setText(dataBean.getInvoice_date());
            consignor.setText(dataBean.getConsignor());
            consignee.setText(dataBean.getConsignee());
            cnsPaymSide.setText(dataBean.getPayment_cust_name());
            branchCode.setText(dataBean.getBranch_code());

            Glide.with(CNSTrackingActivity.this)
                    .load(dataBean.getPod_ilnk()) // image url
                    .placeholder(R.drawable.slide_one) // any placeholder to load at start
                    .error(R.drawable.avatar_10)  // any image in case of error
                    .override(200, 200) // resizing
                    .centerCrop()
                    .into(podsm);

            if (dataBean.getDelivery_status().equals("")) {
                dlyStatus.setText("In transit");
            }else {
                dlyStatus.setText(dataBean.getDelivery_status());
                dlyDate.setText(dataBean.getDelivery_date());
            }

            if (dataBean.getPod_ilnk().equals("")) {
                podView.setText("POD Not Available");
            } else {
                podIMAGE=dataBean.getPod_ilnk();
                podView.setText("Available");

                podsm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final BottomSheetDialog dialog = new BottomSheetDialog(CNSTrackingActivity.this);
                        dialog.setContentView(R.layout.item_podview);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.gravity = Gravity.CENTER;
                        dialog.getWindow()
                                .setLayout(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                );

                        ImageView podImageView,close;
                        podImageView = dialog.findViewById(R.id.podImageView);
                        close = dialog.findViewById(R.id.close);

                        Glide.with(CNSTrackingActivity.this)
                                .load(dataBean.getPod_ilnk()) // image url  // any image in case of error
                                .centerCrop()
                                .into(podImageView);

                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                            }
                        });

                        dialog.show();

                    }
                });


            }


            list.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            Log.e("", "ListingDetail error: " + e);
        }
        cnsTrackNo = dataBean.getCns_no();
        cnsTracking();

    }

    private void cnsTracking() {
        try {
            if (Infrastructure.isInternetPresent()) {
                searchProgressBar.setVisibility(View.VISIBLE);
                apiPresenter.movedCNS(disposable, Constants.ApiRequestCode.CNSTRACK, cnsTrackNo);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getConsignment_details", e);
        }

    }

    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult: " + object);
        searchProgressBar.setVisibility(View.GONE);
        if (callAPIId == Constants.ApiRequestCode.CNSTRACK) {
            ConsignmentMovementListResponse consignmentMovementListResponse = (ConsignmentMovementListResponse) object;
            getCNSTRACK(consignmentMovementListResponse);
        }
    }

    private void getCNSTRACK(ConsignmentMovementListResponse consignmentMovementListResponse) {

        try {
            Log.e(Constraints.TAG, "consignmentTrackListResponse: " + new GsonBuilder().create().toJson(consignmentMovementListResponse));
            if (consignmentMovementListResponse != null) {
                if (consignmentMovementListResponse.getConsignment_list() != null) {

                    searchProgressBar.setVisibility(View.GONE);

                    list.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, true));

                    CNSMovementListAdapter listAdapter = new CNSMovementListAdapter(this,
                            consignmentMovementListResponse.getConsignment_list(), list, consignmentMovementListResponse);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    notMoved.setVisibility(View.GONE);

                }
            } else {

                ELog.e(Constraints.TAG, "Exception in consignmentTrackListResponse" + consignmentMovementListResponse.getResponseCode());
                list.setVisibility(View.GONE);
                notMoved.setVisibility(View.VISIBLE);
                String message = consignmentMovementListResponse.getMessage();
                notMoved.setText(message);
                //Toast.makeText(this, consignmentMovementListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(Constraints.TAG, "Exception in appPermissionListResponse", e);
        }


    }

}