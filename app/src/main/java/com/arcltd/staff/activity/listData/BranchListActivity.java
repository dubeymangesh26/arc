package com.arcltd.staff.activity.listData;

import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.adapter.BranchLocalListAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.BranchListResponse;
import com.arcltd.staff.response.BranchLocalListResponse;
import com.arcltd.staff.sqlliteDatabase.BranchLocalDBHelper;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BranchListActivity extends BaseActivity {
    private static final String TAG = "BranchListActivity";
    RecyclerView list;
    SwipeRefreshLayout swiptoRefresh;
    private BranchLocalListAdapter.RerfreshWishList chRerfreshWishList;
    AutoCompleteTextView atSearch;
    ProgressBar searchProgressBar;
    private ArrayList<BranchLocalListResponse> responselist;
    BranchLocalListAdapter listAdapter;
    BranchLocalDBHelper DB;
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions;
    SpeechRecognizer speechRecognizer;
    ImageView micButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_branch_list);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        list = findViewById(R.id.list);
        swiptoRefresh = findViewById(R.id.swiptoRefresh);
        searchProgressBar = findViewById(R.id.searchProgressBar);
        atSearch = findViewById(R.id.atSearch);
        responselist = new ArrayList<>();
        DB = new BranchLocalDBHelper(this);

                micButton = findViewById(R.id.ivMicAudio);
        try {
            if (SpeechRecognizer.isRecognitionAvailable(this)) {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in SpeechRecognizer.createSpeechRecognizer", e);
        }
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (speechRecognizer != null) {
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
                    if (data != null && !data.isEmpty()) {
                        atSearch.setText(data.get(0));
                    }
                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
        }

        micButton.setOnTouchListener((view, motionEvent) -> {
            if (speechRecognizer != null) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // micButton.setImageResource(R.drawable.ic_mic_24);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
            } else {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(this, "Speech recognition not available", Toast.LENGTH_SHORT).show();
                }
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
                if (listAdapter != null) {
                    listAdapter.getFilter().filter(editable.toString());
                }
            }
        });


        swiptoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshData(); // your code
                AlertDialog.Builder builder = new AlertDialog.Builder(BranchListActivity.this);
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



       /* if (RetrofitClient.getStringUserPreference(this, Constants.BRANCH_CHECK) != null) {
            showBranch();
        } else {
            branchList();
        }
*/
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstLaunch = preferences.getBoolean(Constants.FIRST_LAUNCH_KEY, true);

        if (isFirstLaunch) {
            // First time launch: Show progress dialog, update flag, and fetch data
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(Constants.FIRST_LAUNCH_KEY, false);
            editor.apply();

            // Show progress dialog
            Infrastructure.showProgressDialog(this);

            // Fetch and update data
            branchList();
        } else {
            // Not the first time launch: Fetch and update data
            showBranch();
            branchList();
        }


        permissions = new String[]{
                Manifest.permission.CALL_PHONE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE};

        checkPermissions();
    }

    private void checkPermissions() {
        boolean allGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }
        if (!allGranted) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissionsList,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissionsList, grantResults);
        if (requestCode == MULTIPLE_PERMISSIONS) {
            if (grantResults.length > 0) {
                boolean somePermissionDenied = false;
                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        somePermissionDenied = true;
                        break;
                    }
                }

                if (somePermissionDenied) {
                    //  Toast.makeText(SplashScreenActivity.this, "permission not granted", Toast.LENGTH_SHORT).show();
                    androidx.appcompat.app.AlertDialog.Builder builder = new
                            androidx.appcompat.app.AlertDialog.Builder(BranchListActivity.this);
                    builder.setMessage("Please Provide Permission Or Remaining Permission ")
                            .setCancelable(false)
                            .setPositiveButton("Allow Permission", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    recreate();
                                }
                            })
                            .setNegativeButton("Exit From App", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    BranchListActivity.super.finish();
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


    private void showBranch() {
        responselist = DB.readCourses();
        listAdapter = new BranchLocalListAdapter(BranchListActivity.this,
                responselist, list, chRerfreshWishList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BranchListActivity.this, RecyclerView.VERTICAL, false);
        list.setLayoutManager(linearLayoutManager);

        // setting our adapter to recycler view.
        list.setAdapter(listAdapter);
    }


    private void branchList() {
        try {
            if (Infrastructure.isInternetPresent()) {

                apiPresenter.list_branch(disposable, Constants.ApiRequestCode.BRANCH_LIST,
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
        if (callAPIId == Constants.ApiRequestCode.BRANCH_LIST) {
            BranchListResponse branchListResponse = (BranchListResponse) object;
            getbranchList(branchListResponse);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getbranchList(BranchListResponse branchListResponse) {
        try {
            Log.e(TAG, "branchListResponse: " + new GsonBuilder().create().toJson(branchListResponse));
            if (branchListResponse != null) {
                if (branchListResponse.getBranchList() != null) {
                  DB.clearData();
                    for (int i = 0; i < branchListResponse.getBranchList().size(); i++) {
                        DB.addNewList(
                                branchListResponse.getBranchList().get(i).getBranchId(),
                                branchListResponse.getBranchList().get(i).getBranchName(),
                                branchListResponse.getBranchList().get(i).getBranchCode(),
                                branchListResponse.getBranchList().get(i).getRegionId(),
                                branchListResponse.getBranchList().get(i).getRegionName(),
                                branchListResponse.getBranchList().get(i).getDivisionId(),
                                branchListResponse.getBranchList().get(i).getDivisionName(),
                                branchListResponse.getBranchList().get(i).getAcBranch(),
                                branchListResponse.getBranchList().get(i).getBranchAddress(),
                                branchListResponse.getBranchList().get(i).getBranchMngrMob(),
                                branchListResponse.getBranchList().get(i).getStdCode(),
                                branchListResponse.getBranchList().get(i).getPhoneNumber(),
                                branchListResponse.getBranchList().get(i).getBranchMobileno(),
                                branchListResponse.getBranchList().get(i).getEmailId(),
                                branchListResponse.getBranchList().get(i).getUpdatedDate());

                    }
                    RetrofitClient.saveUserPreference(BranchListActivity.this, Constants.BRANCH_CHECK, branchListResponse.getBranchList().get(0).getBranchCode());
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
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }


}