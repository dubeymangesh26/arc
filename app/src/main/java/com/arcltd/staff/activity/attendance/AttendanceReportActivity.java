package com.arcltd.staff.activity.attendance;

import android.app.DatePickerDialog;
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
import com.arcltd.staff.adapter.AttendanceAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.AttendanceReportResponse;
import com.arcltd.staff.response.BranchListResponse;
import com.arcltd.staff.response.DivisionListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.Infrastructure;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceReportActivity extends BaseActivity {

    private TextView tvSelectedDate;
    private Button btnPickDate;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvAttendanceList;

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
        setContentView(R.layout.activity_attendance_report);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Attendance Reports");
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
        swipeRefresh = findViewById(R.id.swipe_refresh);
        rvAttendanceList = findViewById(R.id.rv_attendance_list);

        llFilterContainer = findViewById(R.id.ll_filter_container);
        llDivisionContainer = findViewById(R.id.ll_division_container);
        llBranchContainer = findViewById(R.id.ll_branch_container);
        spDivision = findViewById(R.id.sp_division);
        spBranch = findViewById(R.id.sp_branch);

        rvAttendanceList.setLayoutManager(new LinearLayoutManager(this));
        tvSelectedDate.setText(String.format("Date: %s", selectedDate));

        btnPickDate.setOnClickListener(v -> showDatePicker());
        swipeRefresh.setOnRefreshListener(() -> {
            fetchAttendanceReport();
            swipeRefresh.setRefreshing(false);
        });

        // Set up spinner listeners ONCE
        spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < divisionIdList.size()) {
                    String newDivId = divisionIdList.get(position);
                    if (!newDivId.equals(selectedDivisionId)) {
                        selectedDivisionId = newDivId;
                        selectedBranchCode = ""; // reset branch selection when division changes
                        fetchBranchesForDivision(selectedDivisionId);
                        fetchAttendanceReport();
                    }
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
                    if (!newBranchCode.equals(selectedBranchCode)) {
                        selectedBranchCode = newBranchCode;
                        fetchAttendanceReport();
                    }
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
            fetchAttendanceReport();
        } else if ("DGM".equalsIgnoreCase(loginType)) {
            // DGM: Show only his division branches
            llFilterContainer.setVisibility(View.VISIBLE);
            llDivisionContainer.setVisibility(View.GONE);
            llBranchContainer.setVisibility(View.VISIBLE);
            selectedDivisionId = !TextUtils.isEmpty(myDivisionId) ? myDivisionId : "";
            fetchBranchesForDivision(selectedDivisionId);
            fetchAttendanceReport();
        } else if ("BM".equalsIgnoreCase(loginType)) {
            // BM: Show only his branch
            llFilterContainer.setVisibility(View.GONE);
            selectedDivisionId = !TextUtils.isEmpty(myDivisionId) ? myDivisionId : "";
            selectedBranchCode = !TextUtils.isEmpty(myBranchCode) ? myBranchCode : "";
            fetchAttendanceReport();
        } else {
            // Regular Employee / Other: Show only his own attendance
            llFilterContainer.setVisibility(View.GONE);
            selectedDivisionId = !TextUtils.isEmpty(myDivisionId) ? myDivisionId : "";
            selectedBranchCode = !TextUtils.isEmpty(myBranchCode) ? myBranchCode : "";
            fetchAttendanceReport();
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
                    fetchAttendanceReport();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    private void fetchAttendanceReport() {
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
            // Regular Employee / Other: only his own attendance
            queryBranch = myBranchCode;
            queryEmp = myEmpCode;
        }

        String queryDiv = ("RG".equalsIgnoreCase(loginType)) ? selectedDivisionId : myDivisionId;

        apiPresenter.getAttendanceReport(
                disposable,
                Constants.ApiRequestCode.ATTENDANCE_REPORT,
                queryBranch,
                queryEmp,
                selectedDate,
                loginType,
                queryDiv,
                myRegionId
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
        } else if (callAPIId == Constants.ApiRequestCode.ATTENDANCE_REPORT) {
            if (object instanceof AttendanceReportResponse) {
                AttendanceReportResponse response = (AttendanceReportResponse) object;
                if ("200".equals(response.getResponseCode()) && response.getAttendanceList() != null) {
                    List<AttendanceReportResponse.AttendanceItem> list = response.getAttendanceList();

                    // Filter for specific branch if selected
                    if (!TextUtils.isEmpty(selectedBranchCode)) {
                        List<AttendanceReportResponse.AttendanceItem> branchFiltered = new ArrayList<>();
                        for (AttendanceReportResponse.AttendanceItem item : list) {
                            if (item != null && !TextUtils.isEmpty(item.getBranchCode()) && item.getBranchCode().trim().equalsIgnoreCase(selectedBranchCode.trim())) {
                                branchFiltered.add(item);
                            }
                        }
                        if (!branchFiltered.isEmpty()) {
                            list = branchFiltered;
                        }
                    } else if (!TextUtils.isEmpty(selectedDivisionId)) {
                        // Filter for specific division if selected and divisionId present
                        List<AttendanceReportResponse.AttendanceItem> divFiltered = new ArrayList<>();
                        for (AttendanceReportResponse.AttendanceItem item : list) {
                            if (item != null && !TextUtils.isEmpty(item.getDivisionId()) && item.getDivisionId().trim().equalsIgnoreCase(selectedDivisionId.trim())) {
                                divFiltered.add(item);
                            }
                        }
                        if (!divFiltered.isEmpty()) {
                            list = divFiltered;
                        }
                    }

                    // Client-side filtering for regular employees (OTHER)
                    if (!"RG".equalsIgnoreCase(loginType) && !"DGM".equalsIgnoreCase(loginType) && !"BM".equalsIgnoreCase(loginType)) {
                        List<AttendanceReportResponse.AttendanceItem> filteredList = new ArrayList<>();
                        for (AttendanceReportResponse.AttendanceItem item : list) {
                            if (item != null && !TextUtils.isEmpty(item.getEmpCode()) && item.getEmpCode().trim().equalsIgnoreCase(myEmpCode.trim())) {
                                filteredList.add(item);
                            }
                        }
                        if (!filteredList.isEmpty()) {
                            list = filteredList;
                        }
                    }

                    if (!list.isEmpty()) {
                        // Sort list: PRESENT employees on top, ABSENT employees below
                        Collections.sort(list, (o1, o2) -> {
                            boolean isPresent1 = "PRESENT".equalsIgnoreCase(o1.getStatus())
                                    || (!TextUtils.isEmpty(o1.getTimeIn()) && !"-".equals(o1.getTimeIn().trim()));
                            boolean isPresent2 = "PRESENT".equalsIgnoreCase(o2.getStatus())
                                    || (!TextUtils.isEmpty(o2.getTimeIn()) && !"-".equals(o2.getTimeIn().trim()));

                            if (isPresent1 && !isPresent2) {
                                return -1; // PRESENT employees at the top
                            } else if (!isPresent1 && isPresent2) {
                                return 1;  // ABSENT employees below
                            } else {
                                String name1 = o1.getEmpName() != null ? o1.getEmpName() : "";
                                String name2 = o2.getEmpName() != null ? o2.getEmpName() : "";
                                return name1.compareToIgnoreCase(name2);
                            }
                        });

                        AttendanceAdapter adapter = new AttendanceAdapter(this, list);
                        rvAttendanceList.setAdapter(adapter);
                        rvAttendanceList.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(this, "No attendance records found", Toast.LENGTH_SHORT).show();
                        rvAttendanceList.setAdapter(null);
                    }
                } else {
                    Toast.makeText(this, response.getMessage() != null ? response.getMessage() : "No attendance records found", Toast.LENGTH_SHORT).show();
                    rvAttendanceList.setAdapter(null);
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
                    // Filter: Include ONLY branches belonging to the selected division (if a specific division is selected)
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
