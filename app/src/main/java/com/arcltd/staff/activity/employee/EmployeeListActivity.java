package com.arcltd.staff.activity.employee;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.adapter.EmployeeListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.DeleteEmployeeResponse;
import com.arcltd.staff.response.EmployeeListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.arcltd.staff.web_view.WebEmpUploadActivity;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EmployeeListActivity extends BaseActivity {
    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    LinearLayout liDeleteUpdate;
    private EmployeeListAdapter.RerfreshWishList activeRerfreshWishList;
    String branch_code = "", delete, url = "";
    AutoCompleteTextView atSearch;
    ProgressBar searchProgressBar;
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions;
    SpeechRecognizer speechRecognizer;
    ImageView micButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_employee_list);

        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        list = findViewById(R.id.list);
        swiptoRefresh = findViewById(R.id.swiptoRefresh);
        searchProgressBar = findViewById(R.id.searchProgressBar);
        atSearch = findViewById(R.id.atSearch);
        liDeleteUpdate = findViewById(R.id.liDeleteUpdate);

        micButton = findViewById(R.id.ivEmpMic);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                atSearch.setText("");
                atSearch.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                micButton.setImageResource(R.drawable.ic_mic_24);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                atSearch.setText(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        micButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                speechRecognizer.stopListening();
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                // micButton.setImageResource(R.drawable.ic_mic_24);
                speechRecognizer.startListening(speechRecognizerIntent);
            }
            return false;
        });


        atSearch.addTextChangedListener(new TextWatcher() {
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


        delete = getIntent().getExtras().getString("D");
        branch_code = Objects.requireNonNull(getIntent().getExtras()).getString("branch_code");

        if (delete != null) {
            liDeleteUpdate.setVisibility(View.GONE);
        } else {
            liDeleteUpdate.setVisibility(View.GONE);

        }


        empList();

        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                empList();
                swiptoRefresh.setRefreshing(false);
            }
        });

        permissions = new String[]{
                Manifest.permission.CALL_PHONE,
                Manifest.permission.RECORD_AUDIO};

        checkPermissions();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(EmployeeListActivity.this,
                Arrays.toString(permissions)) != PackageManager.PERMISSION_GRANTED) {
            askPermissions();
        }

    }


    private void askPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            } else {
                //  askPermissions();
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()])
                    , MULTIPLE_PERMISSIONS);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[],
                                           int[] grantResults) {
        if (requestCode == MULTIPLE_PERMISSIONS) {
            if (grantResults.length > 0) {
                StringBuilder permissionsDenied = new StringBuilder();
                for (String per : permissionsList) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        permissionsDenied.append("\n").append(per);

                        //  Toast.makeText(SplashScreenActivity.this, "permission not granted", Toast.LENGTH_SHORT).show();
                        androidx.appcompat.app.AlertDialog.Builder builder = new
                                androidx.appcompat.app.AlertDialog.Builder(EmployeeListActivity.this);
                        builder.setMessage("Please Provide Permission Or Remaining Permission ")
                                .setCancelable(false)
                                .setPositiveButton("Allow Permission", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        recreate();
                                    }
                                })
                                .setNegativeButton("Exit From App", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        EmployeeListActivity.super.finish();
                                        dialog.cancel();
                                    }
                                });
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();
                        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                        pbutton.setTextColor(Color.rgb(30, 144, 255));
                        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                        nbutton.setTextColor(Color.rgb(30, 144, 255));

                    }
                }

            }
        }
    }

    private void empList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                searchProgressBar.setVisibility(View.VISIBLE);
                apiPresenter.list_employee(disposable, Constants.ApiRequestCode.DIVISION_LIST, branch_code,
                        atSearch.getText().toString());

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
        searchProgressBar.setVisibility(View.GONE);
        if (callAPIId == Constants.ApiRequestCode.DIVISION_LIST) {
            EmployeeListResponse employeeListResponse = (EmployeeListResponse) object;
            getEmpResp(employeeListResponse);
        }
        Infrastructure.dismissProgressDialog();
        if (callAPIId == Constants.ApiRequestCode.DELETE_EMP) {
            DeleteEmployeeResponse deleteEmployeeResponse = (DeleteEmployeeResponse) object;
            getEmpdelete(deleteEmployeeResponse);
        }
    }

    private void getEmpdelete(DeleteEmployeeResponse deleteEmployeeResponse) {
        try {
            Log.e(TAG, "deleteEmployeeResponse: " + new GsonBuilder().create().toJson(deleteEmployeeResponse));
            if (deleteEmployeeResponse != null) {
                Infrastructure.dismissProgressDialog();
                Toast.makeText(this, deleteEmployeeResponse.getMessage(), Toast.LENGTH_LONG).show();

            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in deleteEmployeeResponse" + deleteEmployeeResponse.getResponseCode());
                Toast.makeText(this, deleteEmployeeResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in deleteEmployeeResponse", e);
        }
    }

    private void getEmpResp(EmployeeListResponse employeeListResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(employeeListResponse));
            if (employeeListResponse != null) {
                if (employeeListResponse.getEmployee_list() != null) {
                    searchProgressBar.setVisibility(View.GONE);

                    list.setLayoutManager(new GridLayoutManager(
                            this, 2, RecyclerView.VERTICAL, false));

                    EmployeeListAdapter listAdapter = new EmployeeListAdapter(this, employeeListResponse.getEmployee_list()
                            , list, employeeListResponse);
                    list.setAdapter(listAdapter);
                    list.setVisibility(View.VISIBLE);
                    //   listAdapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(this, employeeListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                searchProgressBar.setVisibility(View.GONE);
                ELog.e(TAG, "Exception in checkLoginResponse" + employeeListResponse.getResponseCode());
                Toast.makeText(this, employeeListResponse.getMessage(), Toast.LENGTH_LONG).show();

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


    public void empUpdate(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeListActivity.this);
        builder.setTitle("Delete Alert")
                .setMessage("Are you sure, you want to Update All Employee Data ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(EmployeeListActivity.this, WebEmpUploadActivity.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        //Creating dialog box
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void dleteAllEmployee(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeListActivity.this);
        builder.setTitle("Delete Alert")
                .setMessage("Are you sure, you want to Delete All Employee Data ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getTruncateTaleEmployee();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        //Creating dialog box
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void getTruncateTaleEmployee() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(EmployeeListActivity.this);
                apiPresenter.deleteAllEmployee(disposable, Constants.ApiRequestCode.DELETE_EMP,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.REGION_ID));

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }


}
