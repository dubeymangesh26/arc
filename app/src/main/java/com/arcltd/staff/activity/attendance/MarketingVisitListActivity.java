package com.arcltd.staff.activity.attendance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arcltd.staff.R;
import com.arcltd.staff.adapter.MarketingVisitAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.BranchListResponse;
import com.arcltd.staff.response.DivisionListResponse;
import com.arcltd.staff.response.MarketingVisitListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.Infrastructure;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MarketingVisitListActivity extends BaseActivity {

    private TextView tvSelectedDate;
    private Button btnPickDate, btnAddVisit;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvVisitList;

    private LinearLayout llFilterContainer, llDivisionContainer, llBranchContainer;
    private Spinner spDivision, spBranch;

    private String loginType, myRegionId, myDivisionId, myBranchCode, myEmpCode;
    private String selectedDivisionId = "", selectedBranchCode = "";
    private String selectedDate;

    private final List<String> divisionNameList = new ArrayList<>();
    private final List<String> divisionIdList = new ArrayList<>();
    private final List<String> branchNameList = new ArrayList<>();
    private final List<String> branchCodeList = new ArrayList<>();

    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketing_visit_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Marketing Visits Log");
        }

        loginType = RetrofitClient.getStringUserPreference(this, Constants.LOGIN_TYPE);
        myRegionId = RetrofitClient.getStringUserPreference(this, Constants.REGION_ID);
        myDivisionId = RetrofitClient.getStringUserPreference(this, Constants.DIVISION_ID);
        myBranchCode = RetrofitClient.getStringUserPreference(this, Constants.BRANCH_CODE);
        myEmpCode = RetrofitClient.getStringUserPreference(this, Constants.EMP_CODE);

        selectedDate = apiDateFormat.format(new Date());

        initUI();
        setupRoleBasedAccess();
    }

    private void initUI() {
        tvSelectedDate = findViewById(R.id.tv_selected_date);
        btnPickDate = findViewById(R.id.btn_pick_date);
        btnAddVisit = findViewById(R.id.btn_add_visit);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        rvVisitList = findViewById(R.id.rv_visit_list);

        llFilterContainer = findViewById(R.id.ll_filter_container);
        llDivisionContainer = findViewById(R.id.ll_division_container);
        llBranchContainer = findViewById(R.id.ll_branch_container);
        spDivision = findViewById(R.id.sp_division);
        spBranch = findViewById(R.id.sp_branch);

        rvVisitList.setLayoutManager(new LinearLayoutManager(this));
        tvSelectedDate.setText(String.format("Date: %s", selectedDate));

        btnPickDate.setOnClickListener(v -> showDatePicker());
        btnAddVisit.setOnClickListener(v -> startActivity(new Intent(this, AddMarketingVisitActivity.class)));

        swipeRefresh.setOnRefreshListener(() -> {
            fetchMarketingVisits();
            swipeRefresh.setRefreshing(false);
        });

        // Set up spinner listeners
        spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < divisionIdList.size()) {
                    String newDivId = divisionIdList.get(position);
                    selectedDivisionId = newDivId;
                    selectedBranchCode = ""; // reset branch selection when division changes
                    fetchBranchesForDivision(selectedDivisionId);
                    fetchMarketingVisits();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < branchCodeList.size()) {
                    String newBranchCode = branchCodeList.get(position);
                    selectedBranchCode = newBranchCode;
                    fetchMarketingVisits();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupRoleBasedAccess() {
        if ("RG".equalsIgnoreCase(loginType)) {
            // RG: Show both Division and Branch lists for region
            llFilterContainer.setVisibility(View.VISIBLE);
            llDivisionContainer.setVisibility(View.VISIBLE);
            llBranchContainer.setVisibility(View.VISIBLE);
            fetchDivisionsForRegion();
            fetchBranchesForDivision("");
            fetchMarketingVisits();
        } else if ("DGM".equalsIgnoreCase(loginType)) {
            // DGM: Show only his division branches
            llFilterContainer.setVisibility(View.VISIBLE);
            llDivisionContainer.setVisibility(View.GONE);
            llBranchContainer.setVisibility(View.VISIBLE);
            selectedDivisionId = !TextUtils.isEmpty(myDivisionId) ? myDivisionId : "";
            fetchBranchesForDivision(selectedDivisionId);
            fetchMarketingVisits();
        } else if ("BM".equalsIgnoreCase(loginType)) {
            // BM: Show only his branch
            llFilterContainer.setVisibility(View.GONE);
            selectedDivisionId = !TextUtils.isEmpty(myDivisionId) ? myDivisionId : "";
            selectedBranchCode = !TextUtils.isEmpty(myBranchCode) ? myBranchCode : "";
            fetchMarketingVisits();
        } else {
            // Regular Employee / Other: Show only his own visits
            llFilterContainer.setVisibility(View.GONE);
            selectedDivisionId = !TextUtils.isEmpty(myDivisionId) ? myDivisionId : "";
            selectedBranchCode = !TextUtils.isEmpty(myBranchCode) ? myBranchCode : "";
            fetchMarketingVisits();
        }
    }

    private void fetchDivisionsForRegion() {
        if (Infrastructure.isInternetPresent()) {
            apiPresenter.list_division(disposable, Constants.ApiRequestCode.DIVISION_LIST, myRegionId);
        }
    }

    private void fetchBranchesForDivision(String divId) {
        if (Infrastructure.isInternetPresent()) {
            apiPresenter.list_branch(disposable, Constants.ApiRequestCode.BRANCH_LIST, "", divId);
        }
    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    selectedDate = apiDateFormat.format(calendar.getTime());
                    tvSelectedDate.setText(String.format("Date: %s", selectedDate));
                    fetchMarketingVisits();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    private void fetchMarketingVisits() {
        if (!Infrastructure.isInternetPresent()) {
            Toast.makeText(this, "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }

        Infrastructure.showProgressDialog(this);

        String queryBranch = selectedBranchCode;
        String queryEmp = "";

        if ("BM".equalsIgnoreCase(loginType)) {
            queryBranch = myBranchCode;
        } else if (!"RG".equalsIgnoreCase(loginType) && !"DGM".equalsIgnoreCase(loginType)) {
            // Regular Employee / Other: only his own visits
            queryBranch = myBranchCode;
            queryEmp = myEmpCode;
        }

        String queryDiv = ("RG".equalsIgnoreCase(loginType)) ? selectedDivisionId : myDivisionId;

        apiPresenter.getMarketingVisits(
                disposable,
                Constants.ApiRequestCode.MARKETING_VISIT_LIST,
                queryEmp,
                queryBranch,
                queryDiv,
                myRegionId,
                loginType,
                selectedDate
        );
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        Infrastructure.dismissProgressDialog();

        if (callAPIId == Constants.ApiRequestCode.DIVISION_LIST) {
            if (object instanceof DivisionListResponse) {
                DivisionListResponse divisionListResponse = (DivisionListResponse) object;
                populateDivisionSpinner(divisionListResponse);
            }
        } else if (callAPIId == Constants.ApiRequestCode.BRANCH_LIST) {
            if (object instanceof BranchListResponse) {
                BranchListResponse branchListResponse = (BranchListResponse) object;
                populateBranchSpinner(branchListResponse);
            }
        } else if (callAPIId == Constants.ApiRequestCode.MARKETING_VISIT_LIST) {
            if (object instanceof MarketingVisitListResponse) {
                MarketingVisitListResponse response = (MarketingVisitListResponse) object;
                if ("200".equals(response.getResponseCode()) && response.getVisitList() != null) {
                    List<MarketingVisitListResponse.MarketingVisitItem> list = response.getVisitList();

                    // Client-side filtering by selected branch if set
                    if (!TextUtils.isEmpty(selectedBranchCode)) {
                        List<MarketingVisitListResponse.MarketingVisitItem> branchFiltered = new ArrayList<>();
                        for (MarketingVisitListResponse.MarketingVisitItem item : list) {
                            if (item != null && !TextUtils.isEmpty(item.getBranchCode()) && item.getBranchCode().equalsIgnoreCase(selectedBranchCode)) {
                                branchFiltered.add(item);
                            }
                        }
                        list = branchFiltered;
                    } else if (!TextUtils.isEmpty(selectedDivisionId)) {
                        // Client-side filtering by selected division if set
                        List<MarketingVisitListResponse.MarketingVisitItem> divFiltered = new ArrayList<>();
                        for (MarketingVisitListResponse.MarketingVisitItem item : list) {
                            if (item != null && !TextUtils.isEmpty(item.getDivisionId()) && item.getDivisionId().equalsIgnoreCase(selectedDivisionId)) {
                                divFiltered.add(item);
                            }
                        }
                        if (!divFiltered.isEmpty()) {
                            list = divFiltered;
                        }
                    }

                    // Client-side filtering for regular employees (OTHER)
                    if (!"RG".equalsIgnoreCase(loginType) && !"DGM".equalsIgnoreCase(loginType) && !"BM".equalsIgnoreCase(loginType)) {
                        List<MarketingVisitListResponse.MarketingVisitItem> filteredList = new ArrayList<>();
                        for (MarketingVisitListResponse.MarketingVisitItem item : list) {
                            if (item != null && !TextUtils.isEmpty(item.getEmpCode()) && item.getEmpCode().equalsIgnoreCase(myEmpCode)) {
                                filteredList.add(item);
                            }
                        }
                        list = filteredList;
                    }

                    if (!list.isEmpty()) {
                        MarketingVisitAdapter adapter = new MarketingVisitAdapter(this, list);
                        rvVisitList.setAdapter(adapter);
                        rvVisitList.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(this, "No marketing visits recorded for this selection", Toast.LENGTH_SHORT).show();
                        rvVisitList.setAdapter(null);
                    }
                } else {
                    Toast.makeText(this, response.getMessage() != null ? response.getMessage() : "No visits found", Toast.LENGTH_SHORT).show();
                    rvVisitList.setAdapter(null);
                }
            }
        }
    }

    private void populateDivisionSpinner(DivisionListResponse response) {
        divisionNameList.clear();
        divisionIdList.clear();

        divisionNameList.add("All Divisions");
        divisionIdList.add("");

        if (response != null && response.getDivision_list() != null) {
            for (DivisionListResponse.DivisionList div : response.getDivision_list()) {
                if (div != null && !TextUtils.isEmpty(div.getDivision_name())) {
                    divisionNameList.add(div.getDivision_name());
                    divisionIdList.add(div.getDiv_id() != null ? div.getDiv_id() : "");
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, divisionNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDivision.setAdapter(adapter);
    }

    private void populateBranchSpinner(BranchListResponse response) {
        branchNameList.clear();
        branchCodeList.clear();

        branchNameList.add("All Branches");
        branchCodeList.add("");

        if (response != null && response.getBranchList() != null) {
            for (BranchListResponse.BranchList branch : response.getBranchList()) {
                if (branch != null && !TextUtils.isEmpty(branch.getBranchName())) {
                    // Show only branches that belong to the currently selected Division
                    if (TextUtils.isEmpty(selectedDivisionId)
                            || TextUtils.isEmpty(branch.getDivisionId())
                            || branch.getDivisionId().equalsIgnoreCase(selectedDivisionId)) {

                        String displayName = branch.getBranchName();
                        if (!TextUtils.isEmpty(branch.getBranchCode())) {
                            displayName += " (" + branch.getBranchCode() + ")";
                        }
                        branchNameList.add(displayName);
                        branchCodeList.add(branch.getBranchCode() != null ? branch.getBranchCode() : "");
                    }
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branchNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBranch.setAdapter(adapter);
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        Infrastructure.dismissProgressDialog();
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
