package com.arcltd.staff.authentication;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.arcltd.staff.BuildConfig;
import com.arcltd.staff.R;
import com.arcltd.staff.Server.CameraUtils;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.WebConstants;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.remote.WebService;
import com.arcltd.staff.response.BranchListResponse;
import com.arcltd.staff.response.DivisionListResponse;
import com.arcltd.staff.response.EmployeeSignupDetailsResponse;
import com.arcltd.staff.response.RegisterRespponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.arcltd.staff.web_view.PrivacyPolicyWebViewActivity;
import com.arcltd.staff.web_view.TermAndConditionWebViewActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.GsonBuilder;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SignupActivity extends BaseActivity {
    TextInputEditText editTextName,editBranchcode, editTextEmail, editTextMobile, editTextPassword, editTextCode;
    Button cirRegisterButton;
    private ProgressBar progressBar;
    ImageView image_logo;
    AutoCompleteTextView searchEmp;
    LinearLayout signData;
    TextView tvError;
    Spinner spdivision,spBranch;
    String branch_code,branchName,divisionselected_id,divisionselected_Name,activeRetireStaus;
    List<String> divisionName=new ArrayList<>();
    List<String> divisionID=new ArrayList<>();
    List<String> branch_code_ID=new ArrayList<>();
    List<String> branch_name=new ArrayList<>();
    AutoCompleteTextView acType;

    ProgressBar searchProgressBar;
    CircleImageView profilePic;
    String email_id,status="H",Mess_name="Pending",notvarify="Not Verified",loginType="EM",token;
    String region_ids, region_names, division_ids, division_names, branch_codes,
            emp_Codes, emp_names, emp_deisigns;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 101;


    private static final int STORAGE_PERMISSION_CODE = 4655;
    private final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImage;
    RequestBody glBody;
    MultipartBody.Part glMulPart;
    private File file;
    int REQUEST_CODE;
    String cPath;
    private Bitmap bitmap;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextPassword = findViewById(R.id.editTextPassword);
        searchProgressBar = (ProgressBar) findViewById(R.id.searchProgressBar2);
        spdivision=findViewById(R.id.spldivision);
        spBranch=findViewById(R.id.splBranch);
        acType=findViewById(R.id.acbranch);

        tvError = findViewById(R.id.tvError);
        editTextCode = findViewById(R.id.editTextCode);
        signData = findViewById(R.id.signData);
        searchEmp = findViewById(R.id.searchEmp);
        profilePic = findViewById(R.id.profilePicture);



        signData.setVisibility(View.GONE);

        //  image_logo = findViewById(R.id.image_logo);

        //   Glide.with(this).load(R.drawable.eagle).into(image_logo);





        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        // Log and toast

                        Log.d(ContentValues.TAG, token);
                        RetrofitClient.saveUserPreference(SignupActivity.this, Constants.DEVICE_TOKEN, token);

                    }
                });





    }
    private void divisionList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                Infrastructure.showProgressDialog(this);
                apiPresenter.list_division(disposable, Constants.ApiRequestCode.DIVISION_LIST,
                        RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.REGION_ID));

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }

    private void branchList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                apiPresenter.list_branch(disposable, Constants.ApiRequestCode.BRANCH_LIST,
                        "",division_ids);

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }


    private void getDivisionList(DivisionListResponse divisionListResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(divisionListResponse));
            if (divisionListResponse != null) {
                if (divisionListResponse.getDivision_list()!=null) {
                    Infrastructure.dismissProgressDialog();
                    divisionID.clear();
                    divisionName.clear();
                    divisionID.add("Select Division");
                    divisionName.add("Select Division");
                    for (int i = 0; i < divisionListResponse.getDivision_list().size(); i++) {
                        divisionName.add(divisionListResponse.getDivision_list().get(i).getDivision_name());
                        divisionID.add(divisionListResponse.getDivision_list().get(i).getDiv_id());
                    }

                    ArrayAdapter<String> div = new ArrayAdapter<String>(this,R.layout.spinner_textview, divisionName);
                    div.setDropDownViewResource(R.layout.spinner_textview);
                    spdivision.setAdapter(div);
                    div.notifyDataSetChanged();
                    spdivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                divisionselected_id = divisionID.get(position);
                                divisionselected_Name = divisionName.get(position);
                                Log.e("", "onItemClick_divisionName: " + position);
                                Log.e("", "onItemClick_divisionID: " + position);
                                Log.e("","Brnch divisionselected_id="+divisionselected_id+"  " +
                                        "Branch divisionselected_Name="+divisionselected_Name);
                                branchList();

                            } catch (Exception e) {
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });


                }else {
                    //     Toast.makeText(this, divisionListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Infrastructure.dismissProgressDialog();
                ELog.e(TAG, "Exception in checkLoginResponse" + divisionListResponse.getResponseCode());
                Toast.makeText(this, divisionListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in checkLoginResponse", e);
        }
    }

    private void getbranchList(BranchListResponse branchListResponse) {
        try {
            Log.e(TAG, "branchListResponse: " + new GsonBuilder().create().toJson(branchListResponse));
            if (branchListResponse != null) {
                if (branchListResponse.getBranchList()!=null) {

                    Infrastructure.dismissProgressDialog();

                    if (!branchListResponse.getBranchList().isEmpty()) {
                        branch_code_ID.clear();
                        branch_name.clear();
                        branch_code_ID.add("Select Branch");
                        branch_name.add("Select Branch");
                        for (int i = 0; i < branchListResponse.getBranchList().size(); i++) {
                            branch_name.add(branchListResponse.getBranchList().get(i).getBranchName());
                            branch_code_ID.add(branchListResponse.getBranchList().get(i).getBranchCode());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, branch_name);
                        adapter.setDropDownViewResource(R.layout.spinner_textview);
                        spBranch.setAdapter(adapter);
                        spBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    branch_code = branch_code_ID.get(position);
                                    branchName = branch_name.get(position);

                                    String  branch_co = branch_code_ID.get(position);
                                    Log.e("", "onItemClick_branch_codearray: " + position);
                                    Log.e("","Brnch Code="+branch_code+"  Branch Name="+branchName);
                                    acType.setText(branch_co);

                                } catch (Exception e) {
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });


                    }


                }else {
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










    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    public void onClickSignUp(View view) {

        if (validate()) {

            signup(

                    Objects.requireNonNull(editTextName.getText()).toString().trim(),
                    Objects.requireNonNull(editTextEmail.getText()).toString().trim(),
                    Objects.requireNonNull(editTextPassword.getText()).toString().trim(),
                    Objects.requireNonNull(editTextMobile.getText()).toString().trim());

        }
    }
    public void sechEmp(View view) {
        empList();
    }
    private void empList() {
        try {
            if (Infrastructure.isInternetPresent()) {
                searchProgressBar.setVisibility(View.VISIBLE);
                apiPresenter.getEmpDetails(disposable, Constants.ApiRequestCode.EMPLOYEE_SIGNUP, searchEmp.getText().toString());

            } else {
                new ErrorHandlerCode(this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("TAG", "Exception in getDataFromWebservice", e);
        }
    }


    private void signup(String name,String email, String password, String contactno) {
        try {
            if (Infrastructure.isInternetPresent()) {

                searchProgressBar.setVisibility(View.VISIBLE);
                
                String deviceKey = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                apiPresenter.userregistration(disposable, Constants.ApiRequestCode.USER_REGISTRATION,
                        name, email, password, contactno, "EM", region_ids,region_names, division_ids
                        , division_names, branch_codes,branchName, emp_Codes, emp_deisigns, "H",
                        "","Not Verified", deviceKey);
                Log.e("login_Rquest", "login_Rquest: " + apiPresenter);




            } else {
                new ErrorHandlerCode(SignupActivity.this, NO_INTERNET, getString(R.string.no_internet_connection_message));
            }
        } catch (Exception e) {
            ELog.e("Exception", "Exception in getDataFromWebservice", e);
        }
    }


    @Override
    public void showResult(Object object, int callAPIId) {
        ELog.d("showResult", "showResult: " + object);
        searchProgressBar.setVisibility(View.GONE);


        if (callAPIId == Constants.ApiRequestCode.DIVISION_LIST) {
            DivisionListResponse divisionListResponse = (DivisionListResponse) object;
            getDivisionList(divisionListResponse);
        }
        if (callAPIId == Constants.ApiRequestCode.BRANCH_LIST) {
            BranchListResponse branchListResponse = (BranchListResponse) object;
            getbranchList(branchListResponse);
        }

        if (callAPIId == Constants.ApiRequestCode.EMPLOYEE_SIGNUP) {
            EmployeeSignupDetailsResponse employeeListResponse = (EmployeeSignupDetailsResponse) object;
            getEmpResp(employeeListResponse);
        }

        if (callAPIId == Constants.ApiRequestCode.USER_REGISTRATION) {
            // LoginResponse loginResponse = new Gson().fromJson(object, LoginResponse.class);
            RegisterRespponse registerRespponse = (RegisterRespponse) object;
            finallogin(registerRespponse);
        }
    }



    private void getEmpResp(EmployeeSignupDetailsResponse employeeListResponse) {
        try {
            Log.e(TAG, "activeOrderResponse: " + new GsonBuilder().create().toJson(employeeListResponse));
            if (employeeListResponse != null) {
                searchProgressBar.setVisibility(View.GONE);
                if (employeeListResponse.getEmployee_list() != null) {
                    region_ids = employeeListResponse.getEmployee_list().getRegion_id();
                    region_names = employeeListResponse.getEmployee_list().getRegion_name();
                    division_ids = employeeListResponse.getEmployee_list().getDivision_id();
                    division_names = employeeListResponse.getEmployee_list().getDivision_name();
                    branch_codes = employeeListResponse.getEmployee_list().getBranch_code();
                    emp_Codes = employeeListResponse.getEmployee_list().getEmp_code();
                    emp_names = employeeListResponse.getEmployee_list().getEmp_name();
                    emp_deisigns = employeeListResponse.getEmployee_list().getEmp_desig();

                    editTextCode.setEnabled(false);
                    editTextName.setEnabled(false);

                    editTextCode.setText(employeeListResponse.getEmployee_list().getEmp_code());
                    editTextName.setText(employeeListResponse.getEmployee_list().getEmp_name());
                    signData.setVisibility(View.VISIBLE);
                    tvError.setVisibility(View.GONE);
                    branchList();

                } else {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Employee Code Not Found! Please contact your region.");

                    Toast.makeText(this, employeeListResponse.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                searchProgressBar.setVisibility(View.GONE);
                tvError.setVisibility(View.VISIBLE);
                tvError.setText(employeeListResponse.getMessage());
                ELog.e(TAG, "Exception in checkLoginResponse" + employeeListResponse.getResponseCode());
                Toast.makeText(this, employeeListResponse.getMessage(), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            ELog.e(TAG, "Exception in checkLoginResponse", e);
        }
    }


    private void finallogin(RegisterRespponse registerRespponse) {
        try {
            Log.e("login", "login: " + new GsonBuilder().create().toJson(registerRespponse));
            switch (registerRespponse.getResponseCode()) {
                case "200":

                    email_id=registerRespponse.getData().getEmail();

                    startActivity(new Intent(SignupActivity.this, OTPActivity.class)
                            .putExtra("me",email_id));
                    finish();

                    //RetrofitClient.saveUserPreference(SignupActivity.this, Constants.MEMBER_ID, registerRespponse.getData().getEmp_id());
                    RetrofitClient.saveUserPreference(SignupActivity.this, Constants.FIRSTNAME, registerRespponse.getData().getName());
                    RetrofitClient.saveUserPreference(SignupActivity.this, Constants.EMAIL_ID, registerRespponse.getData().getEmail());
                    RetrofitClient.saveUserPreference(SignupActivity.this, Constants.MOBILE_NO, registerRespponse.getData().getContactno());
                    RetrofitClient.saveUserPreference(SignupActivity.this, Constants.LOGIN_TYPE, registerRespponse.getData().getLogin_type());
                    RetrofitClient.saveUserPreference(SignupActivity.this, Constants.REGION_ID, registerRespponse.getData().getRegion_id());
                    RetrofitClient.saveUserPreference(SignupActivity.this, Constants.REGION_NAME, registerRespponse.getData().getRegion_name());
                    RetrofitClient.saveUserPreference(SignupActivity.this, Constants.DIVISION_ID, registerRespponse.getData().getDivision_id());
                    RetrofitClient.saveUserPreference(SignupActivity.this, Constants.DIVISION_NAME, registerRespponse.getData().getDivision_name());
                    RetrofitClient.saveUserPreference(SignupActivity.this, Constants.BRANCH_CODE, registerRespponse.getData().getBranch_code());
                    RetrofitClient.saveUserPreference(SignupActivity.this, Constants.BRANCH_NAME, registerRespponse.getData().getBranch_name());
                    RetrofitClient.saveUserPreference(SignupActivity.this, Constants.EMP_CODE, registerRespponse.getData().getEmp_code());
                    RetrofitClient.saveUserPreference(SignupActivity.this, Constants.DESIGN, registerRespponse.getData().getDesign());
                    RetrofitClient.saveUserPreference(SignupActivity.this, Constants.STATUS, registerRespponse.getData().getStatus());
                    RetrofitClient.saveUserPreference(SignupActivity.this, Constants.PROFILE_PICTURE, registerRespponse.getData().getProfilepic());

                    final Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    View custom_view = getLayoutInflater().inflate(R.layout.success_tost, null);
                    TextView message=custom_view.findViewById(R.id.message);
                    message.setText(registerRespponse.getMessage());
                    toast.setView(custom_view);
                    toast.show();

                    break;
                case "404":
                    final Toast toast1 = new Toast(getApplicationContext());
                    toast1.setDuration(Toast.LENGTH_LONG);
                    View custom_viewError = getLayoutInflater().inflate(R.layout.error_tost, null);
                    TextView message1=custom_viewError.findViewById(R.id.message);
                    message1.setText(registerRespponse.getMessage());
                    toast1.setView(custom_viewError);
                    toast1.show();
                    break;
                case "304":
                    final Toast toast2 = new Toast(getApplicationContext());
                    toast2.setDuration(Toast.LENGTH_LONG);
                    View custom_viewError1 = getLayoutInflater().inflate(R.layout.error_tost, null);
                    TextView message2=custom_viewError1.findViewById(R.id.message);
                    message2.setText(registerRespponse.getMessage());
                    toast2.setView(custom_viewError1);
                    toast2.show();
                    break;
                case "400":
                    final Toast toast3 = new Toast(getApplicationContext());
                    toast3.setDuration(Toast.LENGTH_LONG);
                    View custom_viewError2 = getLayoutInflater().inflate(R.layout.error_tost, null);
                    TextView message3=custom_viewError2.findViewById(R.id.message);
                    message3.setText(registerRespponse.getMessage());
                    toast3.setView(custom_viewError2);
                    toast3.show();
                    break;
                case "401":
                    final Toast toast4 = new Toast(getApplicationContext());
                    toast4.setDuration(Toast.LENGTH_LONG);
                    View custom_viewError3 = getLayoutInflater().inflate(R.layout.error_tost, null);
                    TextView message4=custom_viewError3.findViewById(R.id.message);
                    message4.setText(registerRespponse.getMessage());
                    toast4.setView(custom_viewError3);
                    toast4.show();
                    break;
                case "403":
                    final Toast toast5 = new Toast(getApplicationContext());
                    toast5.setDuration(Toast.LENGTH_LONG);
                    View custom_viewError4 = getLayoutInflater().inflate(R.layout.error_tost, null);
                    TextView message5=custom_viewError4.findViewById(R.id.message);
                    message5.setText(registerRespponse.getMessage());
                    toast5.setView(custom_viewError4);
                    toast5.show();
                    break;
                case "405":
                    final Toast toast6 = new Toast(getApplicationContext());
                    toast6.setDuration(Toast.LENGTH_LONG);
                    View custom_viewError5 = getLayoutInflater().inflate(R.layout.error_tost, null);
                    TextView message6=custom_viewError5.findViewById(R.id.message);
                    message6.setText(registerRespponse.getMessage());
                    toast6.setView(custom_viewError5);
                    toast6.show();
                    break;
                default:
                    Toast.makeText(SignupActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                    break;
            }
            //  new ErrorHandlerCode(LoginActivity.this, loginResponse.getResponse().getCode(), loginResponse.getResponse().getMsg());

        } catch (Exception e) {
            ELog.e("Exception", "Exception in checkLoginResponse", e);
        }

    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        Infrastructure.dismissProgressDialog();
        Log.e("login", "login: " + errorCode);
        if (callAPIId == Constants.ApiRequestCode.EMPLOYEE_SIGNUP) {
            new ErrorHandlerCode(SignupActivity.this, errorCode, message);
        }
        if (callAPIId == Constants.ApiRequestCode.USER_REGISTRATION) {
            new ErrorHandlerCode(SignupActivity.this, errorCode, message);
        }
    }


    @Override
    public void showArrayResult(List genericObj, int callAPIId) {
    }


    private boolean validate() {
        if (TextUtils.isEmpty(Objects.requireNonNull(editTextName.getText()).toString())) {
            Toast.makeText(getApplicationContext(), "Enter  name!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (spBranch.getSelectedItem().toString().trim().equals("Select Branch")) {
            Toast.makeText(getApplicationContext(), "Please Select Branch!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(Objects.requireNonNull(editTextEmail.getText()).toString())) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(Objects.requireNonNull(editTextMobile.getText()).toString())) {
            Toast.makeText(getApplicationContext(), "Enter mobile number!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (editTextMobile.getText().length() < 10) {
            Toast.makeText(SignupActivity.this, "Invalid Mobile Number.", Toast.LENGTH_LONG).show();
            return false;
        } else if (editTextMobile.getText().length() > 12) {
            Toast.makeText(SignupActivity.this, "Invalid Mobile Number.", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(Objects.requireNonNull(editTextPassword.getText()).toString())) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (editTextPassword.getText().toString().length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //progressBar.setVisibility(View.GONE);
    }

    public void onImageClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    public void onReadPrivacyPolicyClick(View view) {
        startActivity(new Intent(SignupActivity.this, PrivacyPolicyWebViewActivity.class));
    }

    public void onTermandConditionsClick(View view) {
        startActivity(new Intent(SignupActivity.this, TermAndConditionWebViewActivity.class));
    }






}