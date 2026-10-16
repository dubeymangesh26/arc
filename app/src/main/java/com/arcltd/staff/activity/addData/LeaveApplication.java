package com.arcltd.staff.activity.addData;

import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.otherAndMain.MainActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.DivisionListResponse;
import com.arcltd.staff.response.LeaveApplicationResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class LeaveApplication extends BaseActivity {
    private static final String TAG = "LeaveApplication";

    EditText etToDate, etFromDate, etEmpCode, etEmpName, etMessage;
    TextInputLayout txFromDate;
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendarTo = Calendar.getInstance();
    TextView tvDays;
    String fromDate, toDate, wishMessage, region_id, division_id, division_name, branch_code, branch_name,
            lv_id = "", cnfFromDate = "", cnfToDate = "", cnfDays = "";

    Spinner spMessage;

    ArrayList<String> messageArray = new ArrayList<>(Arrays.asList(
            "Select Message",
            "Family Medical Emergency",
            "Personal Illness",
            "Personal Reason",
            "Visit Native Place",
            "Annual leave",
            "Casual leave",
            "Family Wedding",
            "Friend Wedding",
            "Unexpected event",
            "Personal circumstances",
            "Family member is not well",
            "Yearly Health Checkup",
            "Blood Donation",
            "Dental emergencies",
            "Death in family"
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initViews();
        populateUserData();
        setUpDatePickers();
        setUpMessageSpinner();
    }

    private void initViews() {
        etToDate = findViewById(R.id.etToDate);
        etFromDate = findViewById(R.id.etfromDate);
        tvDays = findViewById(R.id.tvDays);
        etEmpCode = findViewById(R.id.etEmpCode);
        etEmpName = findViewById(R.id.etEmpName);
        etMessage = findViewById(R.id.etMessage);
        spMessage = findViewById(R.id.spMessage);
    }

    private void populateUserData() {
        etEmpCode.setText(RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.EMP_CODE));
        etEmpName.setText(RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.FIRSTNAME));
        region_id = RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.REGION_ID);
        division_id = RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.DIVISION_ID);
        division_name = RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.DIVISION_NAME);
        branch_code = RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_CODE);
        branch_name = RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.BRANCH_NAME);
    }

    private void setUpDatePickers() {
        final DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateFromDate();
            }
        };

        final DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarTo.set(Calendar.YEAR, year);
                myCalendarTo.set(Calendar.MONTH, month);
                myCalendarTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateToDate();
            }
        };

        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(LeaveApplication.this, fromDateListener,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        etToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFromDate()) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(LeaveApplication.this, toDateListener,
                            myCalendarTo.get(Calendar.YEAR), myCalendarTo.get(Calendar.MONTH),
                            myCalendarTo.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                    datePickerDialog.show();
                }
            }
        });
    }

    private void setUpMessageSpinner() {
        ArrayAdapter<String> messageAdapter = new ArrayAdapter<>(LeaveApplication.this,
                android.R.layout.simple_spinner_item, messageArray);
        messageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMessage.setAdapter(messageAdapter);

        spMessage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    wishMessage = messageArray.get(position);
                    if (!wishMessage.equals("Select Message")) {
                        etMessage.setText(wishMessage);
                    } else {
                        etMessage.setText("");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onItemSelected: " + e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    private boolean validateFromDate() {
        if (etFromDate.getText().toString().isEmpty()) {
            Toast.makeText(LeaveApplication.this, "Please Select From Date", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void updateFromDate() {
        String myFormat = "dd/MM/yyyy";
        String myToFormatFrom = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        SimpleDateFormat sdff = new SimpleDateFormat(myToFormatFrom, Locale.ENGLISH);
        fromDate = sdff.format(myCalendar.getTime());
        etFromDate.setText(sdf.format(myCalendar.getTime()));

        if (myCalendarTo.getTimeInMillis() < myCalendar.getTimeInMillis()) {
            myCalendarTo.setTime(myCalendar.getTime());
        }
    }

    private void updateToDate() {
        String myToFormat = "dd/MM/yyyy";
        String myToFormatTo = "yyyy-MM-dd";
        SimpleDateFormat sdf_to = new SimpleDateFormat(myToFormat, Locale.ENGLISH);
        SimpleDateFormat sdf_tto = new SimpleDateFormat(myToFormatTo, Locale.ENGLISH);

        toDate = sdf_tto.format(myCalendarTo.getTime());
        etToDate.setText(sdf_to.format(myCalendarTo.getTime()));

        LocalDate startDate = LocalDate.parse(fromDate);
        LocalDate endDate = LocalDate.parse(toDate);

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        tvDays.setText(daysBetween + "\nDays");
    }

    public void submitLeave(View view) {
        if (validateLeaveForm()) {
            addLeaveApplication();
        }
    }

    private boolean validateLeaveForm() {
        if (etFromDate.getText().toString().isEmpty()) {
            Toast.makeText(LeaveApplication.this, "Please Select From Date", Toast.LENGTH_LONG).show();
            return false;
        }
        if (etToDate.getText().toString().isEmpty()) {
            Toast.makeText(LeaveApplication.this, "Please Select To Date", Toast.LENGTH_LONG).show();
            return false;
        }
        if (etMessage.getText().toString().isEmpty()) {
            Toast.makeText(LeaveApplication.this, "Please Select or Enter Leave Reason", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void addLeaveApplication() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(LeaveApplication.this);
                apiPresenter.addLeave(disposable, Constants.ApiRequestCode.LEAVEAPPLICATION,
                        lv_id, region_id, division_id, division_name, branch_code, branch_name,
                        etEmpCode.getText().toString(), etEmpName.getText().toString(), fromDate,
                        toDate, tvDays.getText().toString(), cnfFromDate, cnfToDate, cnfDays,
                        etMessage.getText().toString());

                Log.e("LeaveApplication_Rquest", "LeaveApplication_Rquest: " + apiPresenter);

            } else {
                new ErrorHandlerCode(LeaveApplication.this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in addLeaveApplication", e);
            Infrastructure.dismissProgressDialog();
        }
    }

    @Override
    public void showResult(Object object, int callAPIId) {
       /* ELog.d("TAG", "showResult: " + object);
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.LEAVEAPPLICATION) {
            LeaveApplicationResponse leaveApplicationResponse = (LeaveApplicationResponse) object;
            handleLeaveApplicationResponse(leaveApplicationResponse);
        }*/

        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.LEAVEAPPLICATION) {
            if (object instanceof LeaveApplicationResponse) {
                LeaveApplicationResponse leaveApplicationResponse = (LeaveApplicationResponse) object;
                handleLeaveApplicationResponse(leaveApplicationResponse);
            } else {
                Log.e(TAG, "Unexpected response type for LeaveApplication API");
            }
        }

    }

    private void handleLeaveApplicationResponse(LeaveApplicationResponse leaveApplicationResponse) {

            Log.e(TAG, "leaveApplicationResponse: " + new GsonBuilder().create().toJson(leaveApplicationResponse));
            if (leaveApplicationResponse.getResponseCode().equals(200)) {
                Toast.makeText(this, leaveApplicationResponse.getMessage(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(LeaveApplication.this, MainActivity.class));
                finish();
            } else {
                ELog.e(TAG, "Error in leaveApplicationResponse: " + leaveApplicationResponse.getResponseCode());
                Toast.makeText(this, leaveApplicationResponse.getMessage(), Toast.LENGTH_LONG).show();
            }

    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        super.onDisplayMessage(message, callAPIId, errorCode);
        Infrastructure.dismissProgressDialog();
    }
}
