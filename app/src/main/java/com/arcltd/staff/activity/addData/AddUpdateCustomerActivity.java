package com.arcltd.staff.activity.addData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.activity.listData.CustomerListActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.AddCustomerDeatilsResponse;
import com.arcltd.staff.response.CustomerListResponse;
import com.arcltd.staff.response.FeedbackResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class AddUpdateCustomerActivity extends BaseActivity implements View.OnClickListener {
    TextInputEditText editComName, editCustName, editTextEmail, editTextMobile, editCustDesig, feedback, etDate;
    Spinner spCustType;
    ArrayList<String> custTypeArray = new ArrayList<>(Arrays.asList("Select Visit Type", "NEW",
            "EXISTING", "LOST"));
    String custType;
    Integer cust_id;
    int custNo,repeated_cust;
    private int mYear, mMonth, mDay;
    String data;
    CustomerListResponse.CustomerList dataBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_add_update_customer);

        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("data");
            dataBean = new Gson().fromJson(data, CustomerListResponse.CustomerList.class);
            Log.e("", "onCreate: " + new GsonBuilder().create().toJson(dataBean));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        editComName = findViewById(R.id.editComName);
        editCustName = findViewById(R.id.editCustName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMobile = findViewById(R.id.editTextMobile);
        editCustDesig = findViewById(R.id.editCustDesig);
        feedback = findViewById(R.id.feedback);
        etDate = findViewById(R.id.etDate);
        spCustType = findViewById(R.id.spCustType);
        etDate.setOnClickListener(this);
        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("data");
            dataBean = new Gson().fromJson(data, CustomerListResponse.CustomerList.class);
            Log.e("", "onCreate: " + new GsonBuilder().create().toJson(dataBean));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }

        try {
            repeated_cust= Integer.parseInt(dataBean.getNoof_visit());
            Log.e(TAG, "repeated_cust: "+repeated_cust );

                custNo= repeated_cust+1;
            Log.e(TAG, "TOTAL VISIT: "+custNo );
            cust_id= Integer.valueOf(dataBean.getCust_id());
            editComName.setText(dataBean.getCompany_name());
            editCustName.setText(dataBean.getCust_name());
            editTextEmail.setText(dataBean.getCust_email());
            editTextMobile.setText(dataBean.getCust_mobile());
            editCustDesig.setText(dataBean.getCust_design());

            if (!dataBean.getCompany_name().equals("")){
                editComName.setEnabled(false);
            }else {
                editComName.setEnabled(true);
            }

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }

        ArrayAdapter<String> cast = new ArrayAdapter<String>(AddUpdateCustomerActivity.this,
                android.R.layout.simple_spinner_item, custTypeArray);
        cast.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spCustType.setAdapter(cast);

        spCustType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    custType = custTypeArray.get(position);
                    Log.e("", "onItemClick_custTypeArray: " + position);

                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    @Override
    public void onClick(View view) {
        if (view == etDate) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                     @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            etDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                                   }

                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        }

    }


    public void onClickSignUp(View view) {
        if (validate()) {
            addcustomer(
                    editComName.getText().toString().trim(),
                    editCustName.getText().toString().trim(),
                    editTextEmail.getText().toString().trim(),
                    editTextMobile.getText().toString().trim(),
                    editCustDesig.getText().toString().trim(),
                    feedback.getText().toString().trim(),
                    etDate.getText().toString().trim());
        }
    }

    private void addcustomer(String editComName, String editCustName, String editTextEmail,
                             String editTextMobile, String editCustDesig, String feedback,String etDate) {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.addCustomer(disposable, Constants.ApiRequestCode.ADD_CUSTOMER, String.valueOf(cust_id),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.DIVISION_ID),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE),
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.FIRSTNAME),
                        editComName, editCustName, editTextMobile,editCustDesig,editTextEmail,
                        String.valueOf(custNo), custType);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }

    }

    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("TAG", "showResult: " + object);
        if (callAPIId == Constants.ApiRequestCode.ADD_CUSTOMER) {
            AddCustomerDeatilsResponse addCustomerDeatilsResponse = (AddCustomerDeatilsResponse) object;
            getDivisionList(addCustomerDeatilsResponse);
        }
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.ADD_FEEDBACK) {
            FeedbackResponse feedbackResponse = (FeedbackResponse) object;
            getFeedbackResponse(feedbackResponse);
        }
    }

    private void getDivisionList(AddCustomerDeatilsResponse addCustomerDeatilsResponse) {
        try {
            Log.e(TAG, "AddCustomerDeatilsResponse: " + new GsonBuilder().create().toJson(addCustomerDeatilsResponse));
            if (addCustomerDeatilsResponse != null) {
                    if (cust_id==null) {
                        cust_id=addCustomerDeatilsResponse.getData().getCust_id();
                        getfeedback(cust_id);
                    }else {
                        getfeedback(cust_id);
                    }
                    Toast.makeText(this, addCustomerDeatilsResponse.getMessage(), Toast.LENGTH_LONG).show();


            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + addCustomerDeatilsResponse.getResponseCode());
                Toast.makeText(this, addCustomerDeatilsResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in checkLoginResponse", e);
        }
    }


    private void getFeedbackResponse(FeedbackResponse feedbackResponse) {
        try {
            Log.e(TAG, "AddCustomerDeatilsResponse: " + new GsonBuilder().create().toJson(feedbackResponse));
            if (feedbackResponse != null) {
                if (feedbackResponse.getResponseCode() != null) {
                    Infrastructure.dismissProgressDialog();
                    startActivity(new Intent(this, CustomerListActivity.class));
                    finish();

                } else {
                    Toast.makeText(this, feedbackResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + feedbackResponse.getResponseCode());
                Toast.makeText(this, feedbackResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in checkLoginResponse", e);
        }
    }


    private void getfeedback(Integer cust_id) {
        String customerID= String.valueOf(cust_id);
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.addCustFeedback(disposable, Constants.ApiRequestCode.ADD_FEEDBACK,customerID
                        ,etDate.getText().toString(),feedback.getText().toString());

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }

    }


    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        Infrastructure.dismissProgressDialog();
        Log.e("login", "login: " + errorCode);
        if (callAPIId == Constants.ApiRequestCode.USER_REGISTRATION) {
            new ErrorHandlerCode(AddUpdateCustomerActivity.this, errorCode, message);
        }
    }


    private boolean validate() {
        if (spCustType.getSelectedItem().toString().trim().equals("Select Visit Type")) {
            Toast.makeText(getApplicationContext(), "Please Select Visit Type!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(editComName.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Company name!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(editCustName.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Customer Name!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(editTextMobile.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter mobile number!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (editTextMobile.getText().length() < 10) {
            Toast.makeText(AddUpdateCustomerActivity.this, "Invalid Mobile Number.", Toast.LENGTH_LONG).show();
            return false;
        } else if (editTextMobile.getText().length() > 10) {
            Toast.makeText(AddUpdateCustomerActivity.this, "Invalid Mobile Number.", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(editTextEmail.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Email_ID!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(editCustDesig.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Designation!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(feedback.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Customer Feedback!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(etDate.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter Visit Date!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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