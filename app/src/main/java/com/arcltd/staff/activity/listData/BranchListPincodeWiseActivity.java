package com.arcltd.staff.activity.listData;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.sqlliteDatabase.PincodeLocalDBHelper;
import com.arcltd.staff.adapter.BranchLocalPincodeListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.BranchListPiccodeResponse;
import com.arcltd.staff.response.PincodeBranchListResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BranchListPincodeWiseActivity extends BaseActivity {
    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    BranchLocalPincodeListAdapter.RerfreshWishList chRerfreshWishList;
    AutoCompleteTextView atSearch;
    ProgressBar searchProgressBar;
    private ArrayList<PincodeBranchListResponse> responselist;
    BranchLocalPincodeListAdapter listAdapter;
    PincodeLocalDBHelper DB;
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions;
    SpeechRecognizer speechRecognizer;
    ImageView micButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_branch_list_pincode);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        list = findViewById(R.id.list);
        micButton = findViewById(R.id.ivPincodeMic);
        swiptoRefresh = findViewById(R.id.swiptoRefresh);
        searchProgressBar = findViewById(R.id.searchProgressBar);
        atSearch = findViewById(R.id.atSearch);
        //getSavebranchList();
        responselist = new ArrayList<>();
        DB = new PincodeLocalDBHelper(this);

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

        micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // micButton.setImageResource(R.drawable.ic_mic_24);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });

        atSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // getSavebranchList();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                listAdapter.getFilter().filter(editable.toString());
            }
        });


        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AlertDialog.Builder builder = new AlertDialog.Builder(BranchListPincodeWiseActivity.this);
                builder.setTitle("Refresh List")
                        .setMessage("Take Some Time for refreshing data")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                branchList();
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
                swiptoRefresh.setRefreshing(false);
            }
        });


        if (RetrofitClient.getStringUserPreference(this, Constants.PINCODE_CHECK) != null) {
            showBranch();
        } else {
            branchList();
        }


        permissions = new String[]{
                Manifest.permission.CALL_PHONE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE};
        checkPermissions();

    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(BranchListPincodeWiseActivity.this,
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
                                androidx.appcompat.app.AlertDialog.Builder(BranchListPincodeWiseActivity.this);
                        builder.setMessage("Please Provide Permission Or Remaining Permission ")
                                .setCancelable(false)
                                .setPositiveButton("Allow Permission", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        recreate();
                                    }
                                })
                                .setNegativeButton("Exit From App", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        BranchListPincodeWiseActivity.super.finish();
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


    private void showBranch() {
        responselist = DB.readCourses();
        listAdapter = new BranchLocalPincodeListAdapter(BranchListPincodeWiseActivity.this,
                responselist, list, chRerfreshWishList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BranchListPincodeWiseActivity.this, RecyclerView.VERTICAL, false);
        list.setLayoutManager(linearLayoutManager);

        // setting our adapter to recycler view.
        list.setAdapter(listAdapter);
    }


    private void branchList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.list_branchPincode(disposable, Constants.ApiRequestCode.BRANCH_LIST_PINCODE,
                        atSearch.getText().toString(), "");

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
        if (callAPIId == Constants.ApiRequestCode.BRANCH_LIST_PINCODE) {
            BranchListPiccodeResponse branchListResponse = (BranchListPiccodeResponse) object;
            getbranchList(branchListResponse);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getbranchList(BranchListPiccodeResponse branchListResponse) {
        try {
            Log.e(TAG, "branchListResponse: " + new GsonBuilder().create().toJson(branchListResponse));
            if (branchListResponse != null) {
                if (branchListResponse.getBranchList() != null) {

                    for (int i = 0; i < branchListResponse.getBranchList().size(); i++) {
                        DB.addNewList(
                                branchListResponse.getBranchList().get(i).getBranch_id(),
                                branchListResponse.getBranchList().get(i).getBranch_name(),
                                branchListResponse.getBranchList().get(i).getBranch_code(),
                                branchListResponse.getBranchList().get(i).getRegion_id(),
                                branchListResponse.getBranchList().get(i).getRegion_name(),
                                branchListResponse.getBranchList().get(i).getDivision_id(),
                                branchListResponse.getBranchList().get(i).getDivision_name(),
                                branchListResponse.getBranchList().get(i).getAc_branch(),
                                branchListResponse.getBranchList().get(i).getBranch_address(),
                                branchListResponse.getBranchList().get(i).getBranch_mngr_mob(),
                                branchListResponse.getBranchList().get(i).getStd_code(),
                                branchListResponse.getBranchList().get(i).getPhone_number(),
                                branchListResponse.getBranchList().get(i).getBranch_mobileno(),
                                branchListResponse.getBranchList().get(i).getEmail_id(),
                                branchListResponse.getBranchList().get(i).getUpdated_date(),
                                branchListResponse.getBranchList().get(i).getBranch_pincode(),
                                branchListResponse.getBranchList().get(i).getService_pincode()

                        );

                    }

                    RetrofitClient.saveUserPreference(BranchListPincodeWiseActivity.this, Constants.PINCODE_CHECK, branchListResponse.getBranchList().get(0).getBranch_code());
                    Infrastructure.dismissProgressDialog();
                    showBranch();
                } else {
                    Toast.makeText(this, branchListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + branchListResponse.getResponseCode());
                Toast.makeText(this, branchListResponse.getMessage(), Toast.LENGTH_LONG).show();

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

    public void searchbtn(View view) {
        branchList();
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