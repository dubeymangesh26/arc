package com.arcltd.staff.activity.employee;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.adapter.EmployeeMSGListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.firebase.FcmNotificationsSender;
import com.arcltd.staff.firebase.MyMessagingService;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.response.AddMessageResponse;
import com.arcltd.staff.response.EmployeeListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class EmployeeMessageActivity extends BaseActivity implements
        EmployeeMSGListAdapter.SearchAddressCallBack {
    LinearLayout liSendEmp;
    CheckBox cbALL;
    TextInputEditText etEmpName, etEmpBranch, etMessage, etEmpCode;
    AutoCompleteTextView searchEmp;
    RecyclerView list;
    ProgressBar searchProgressBar;
    String DeviceToken;
    MyMessagingService myMessagingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_message);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        liSendEmp = findViewById(R.id.liSendEmp);
        cbALL = findViewById(R.id.cbALL);
        etEmpName = findViewById(R.id.etEmpName);
        etEmpBranch = findViewById(R.id.etEmpBranch);
        etMessage = findViewById(R.id.etMessage);
        searchEmp = findViewById(R.id.searchEmp);
        list = findViewById(R.id.list);
        etEmpCode = findViewById(R.id.etEmpCode);
        searchProgressBar = findViewById(R.id.searchProgressBar);


        FirebaseMessaging.getInstance().subscribeToTopic("all");

        cbALL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked) {
                    liSendEmp.setVisibility(View.GONE);
                } else {
                    liSendEmp.setVisibility(View.VISIBLE);
                }
            }
        });
        searchEmp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                empList();
            }
        });


    }


    public void submitMessage(View view) {
        sendmsg();
    }

    private void sendmsg() {
        if (cbALL.isChecked()) {
            if (validate()) {
                etEmpCode.setText("ALL");
                addMessage();
                sendNotificationALL();
            }
        } else {
            if (validate2()) {
                addMessage();
                sendNotificationUser();
            }
        }
    }

    private void sendNotificationALL() {
        FcmNotificationsSender notificationsSender = new
                FcmNotificationsSender("/topics/all", "Common Message", etMessage.getText().toString(),
                getApplicationContext(), this);
        notificationsSender.SendNotifications();
    }

    private void sendNotificationUser() {
        if (!DeviceToken.equals("")) {
            FcmNotificationsSender notificationsSender = new
                    FcmNotificationsSender(DeviceToken, etEmpCode.getText().toString(), etMessage.getText().toString(),
                    getApplicationContext(), this);
            notificationsSender.SendNotifications();
        } else {
            Toast.makeText(getApplicationContext(), "Employee Not Register!", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean validate2() {
        if (TextUtils.isEmpty(etEmpCode.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please Check Employee Code!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(etEmpName.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please Check Employee Name!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(etEmpBranch.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please Check Branch Name!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(etMessage.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Message!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validate() {
        if (TextUtils.isEmpty(etMessage.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Message!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void empList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                searchProgressBar.setVisibility(View.VISIBLE);
                apiPresenter.list_employee(disposable, Constants.ApiRequestCode.EMPLOYEE,
                        "", searchEmp.getText().toString());

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }

    private void addMessage() {
        try {
            if (Infrastructure.isInternetPresent()) {
                searchProgressBar.setVisibility(View.VISIBLE);
                apiPresenter.addMessage(disposable, Constants.ApiRequestCode.MESSAGE,
                        etEmpCode.getText().toString(), etEmpName.getText().toString(),
                        etEmpBranch.getText().toString(),
                        etMessage.getText().toString());

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("showResult", "showResult: " + object);
        searchProgressBar.setVisibility(View.GONE);

        if (callAPIId == Constants.ApiRequestCode.EMPLOYEE) {
            EmployeeListResponse employeeListResponse = (EmployeeListResponse) object;
            getEmpResp(employeeListResponse);
        }

        if (callAPIId == Constants.ApiRequestCode.MESSAGE) {
            AddMessageResponse addMessageResponse = (AddMessageResponse) object;
            sendMessage(addMessageResponse);
        }
    }

    private void sendMessage(AddMessageResponse addMessageResponse) {
        try {
            Log.e(TAG, "addMessageResponse: " + new GsonBuilder().create().toJson(addMessageResponse));
            if (addMessageResponse != null) {

                searchProgressBar.setVisibility(View.GONE);
                if (addMessageResponse.getMessage() != null) {
                    Toast.makeText(this, addMessageResponse.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, addMessageResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
                //   listAdapter.notifyDataSetChanged();
            } else {
                searchProgressBar.setVisibility(View.GONE);
                list.setVisibility(View.GONE);
                ELog.e(TAG, "Exception in addMessageResponse" + addMessageResponse.getResponseCode());
                Toast.makeText(this, addMessageResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in addMessageResponse", e);
        }

    }

    private void getEmpResp(EmployeeListResponse employeeListResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(employeeListResponse));
            if (employeeListResponse != null) {

                searchProgressBar.setVisibility(View.GONE);
                if (employeeListResponse.getEmployee_list() != null) {
                    list.setLayoutManager(new LinearLayoutManager(
                            this, RecyclerView.VERTICAL, false));
                    EmployeeMSGListAdapter listAdapter = new EmployeeMSGListAdapter(this, employeeListResponse.getEmployee_list()
                            , list, employeeListResponse, this);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this, employeeListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
                //   listAdapter.notifyDataSetChanged();
            } else {
                searchProgressBar.setVisibility(View.GONE);
                list.setVisibility(View.GONE);
                ELog.e(TAG, "Exception in checkLoginResponse" + employeeListResponse.getResponseCode());
                Toast.makeText(this, employeeListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in checkLoginResponse", e);
        }
    }


    @Override
    public void searchdAddressCallBack(String empName, String empCode, String branchCode, String region_id, String region_name,
                                       String division_id, String division_name, String design, String token) {
        etEmpName.setText(empName);
        etEmpCode.setText(empCode);
        etEmpBranch.setText(branchCode);
        list.setVisibility(View.GONE);
        DeviceToken = token;

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