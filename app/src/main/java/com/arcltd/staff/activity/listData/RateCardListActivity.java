package com.arcltd.staff.activity.listData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.adapter.RateCardListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.RateCardListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;

import java.util.Locale;
import java.util.Objects;

public class RateCardListActivity extends BaseActivity {
    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    AutoCompleteTextView atSearchTO,atSearchFrom;
    ProgressBar searchProgressBar;
    SpeechRecognizer speechRecognizer;
    String branch_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_card_list);

        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        list = findViewById(R.id.list);
        swiptoRefresh = findViewById(R.id.swiptoRefresh);
        searchProgressBar = findViewById(R.id.searchProgressBar);
        atSearchTO = findViewById(R.id.atSearchTO);
        atSearchFrom = findViewById(R.id.atSearchFrom);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        branch_code = RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE);

        atSearchFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                rateCardList();
            }
        });

        atSearchTO.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                rateCardList();
            }
        });

        rateCardList();

        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                rateCardList();
                swiptoRefresh.setRefreshing(false);
            }
        });


    }

    private void rateCardList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                searchProgressBar.setVisibility(View.VISIBLE);
                apiPresenter.list_RateCard(disposable, Constants.ApiRequestCode.DIVISION_LIST,
                        branch_code,atSearchTO.getText().toString());

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getRateCardFromWebservice", e);
        }
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult: " + object);
        searchProgressBar.setVisibility(View.GONE);
        if (callAPIId == Constants.ApiRequestCode.DIVISION_LIST) {
            RateCardListResponse rateCardListResponse = (RateCardListResponse) object;
            getEmpResp(rateCardListResponse);
        }
    }

    private void getEmpResp(RateCardListResponse rateCardListResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(rateCardListResponse));
            if (rateCardListResponse != null) {
                if (rateCardListResponse.getRatecard_list() != null) {
                    searchProgressBar.setVisibility(View.GONE);

                    list.setLayoutManager(new LinearLayoutManager(
                            this,  RecyclerView.VERTICAL, false));

                    RateCardListAdapter listAdapter = new RateCardListAdapter(this, rateCardListResponse.getRatecard_list()
                            , list, rateCardListResponse);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(this, rateCardListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                searchProgressBar.setVisibility(View.GONE);
                ELog.e(TAG, "Exception in checkLoginResponse" + rateCardListResponse.getResponseCode());
                Toast.makeText(this, rateCardListResponse.getMessage(), Toast.LENGTH_LONG).show();

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


}