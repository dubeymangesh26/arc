package com.arcltd.staff.authentication;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.arcltd.staff.R;
import com.arcltd.staff.activity.crashReport.CrashReportActivity;
import com.arcltd.staff.activity.crashReport.HandleAppCrashActivity;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.networkhandler.errors.ErrorHandlerCode;
import com.arcltd.staff.networkhandler.remote.RetrofitClient;
import com.arcltd.staff.response.EmployeeSignupDetailsResponse;
import com.arcltd.staff.response.RegisterRespponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.ELog;
import com.arcltd.staff.utility.Infrastructure;
import com.arcltd.staff.web_view.PrivacyPolicyWebViewActivity;
import com.arcltd.staff.web_view.TermAndConditionWebViewActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.GsonBuilder;


import java.util.List;
import java.util.UUID;

public class SignupActivity extends BaseActivity {
    TextInputEditText editTextName, editTextEmail, editTextMobile, editTextPassword, editTextCode;
    Button cirRegisterButton;
    private ProgressBar progressBar;
    ImageView image_logo;
    AutoCompleteTextView searchEmp;
    LinearLayout signData;
    TextView tvError;
    ProgressBar searchProgressBar;
    String email;
    String region_ids, region_names, division_ids, division_names, branch_codes,branch_name,
            emp_Codes, emp_names, emp_deisigns;


    private static final int STORAGE_PERMISSION_CODE = 4655;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filepath;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_signup);

        HandleAppCrashActivity.deploy(this, CrashReportActivity.class);


        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
// to make status bar transparent
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextPassword = findViewById(R.id.editTextPassword);
        searchProgressBar = (ProgressBar) findViewById(R.id.searchProgressBar);
        tvError = findViewById(R.id.tvError);
        editTextCode = findViewById(R.id.editTextCode);
        signData = findViewById(R.id.signData);
        searchEmp = findViewById(R.id.searchEmp);

        signData.setVisibility(View.GONE);

        //  image_logo = findViewById(R.id.image_logo);

        //   Glide.with(this).load(R.drawable.eagle).into(image_logo);

    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    public void onClickSignUp(View view) {
        if (validate()) {
            signup(
                    editTextName.getText().toString().trim(),
                    editTextEmail.getText().toString().trim(),
                    editTextPassword.getText().toString().trim(),
                    editTextMobile.getText().toString().trim());

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


    private void signup(String name, String email, String password, String contactno) {
        try {
            if (Infrastructure.isInternetPresent()) {
                searchProgressBar.setVisibility(View.VISIBLE);
                apiPresenter.userregistration(disposable, Constants.ApiRequestCode.USER_REGISTRATION,
                        name, email, password, contactno, "EM", region_ids,region_names, division_ids
                        , division_names, branch_codes,branch_name, emp_Codes, emp_deisigns, "H",
                        "","Not Verified");
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
                    branch_name = employeeListResponse.getEmployee_list().getBranch_name();
                    emp_Codes = employeeListResponse.getEmployee_list().getEmp_code();
                    emp_names = employeeListResponse.getEmployee_list().getEmp_name();
                    emp_deisigns = employeeListResponse.getEmployee_list().getEmp_desig();

                    editTextCode.setEnabled(false);
                    editTextName.setEnabled(false);

                    editTextCode.setText(employeeListResponse.getEmployee_list().getEmp_code());
                    editTextName.setText(employeeListResponse.getEmployee_list().getEmp_name());
                    signData.setVisibility(View.VISIBLE);
                    tvError.setVisibility(View.GONE);

                } else {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Please Enter Correct Employee Code");

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

                    email=registerRespponse.getData().getEmail();

                    startActivity(new Intent(SignupActivity.this, OTPActivity.class)
                            .putExtra("me",email));
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
        if (TextUtils.isEmpty(editTextName.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter  name!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(editTextEmail.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(editTextMobile.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter mobile number!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (editTextMobile.getText().length() < 10) {
            Toast.makeText(SignupActivity.this, "Invalid Mobile Number.", Toast.LENGTH_LONG).show();
            return false;
        } else if (editTextMobile.getText().length() > 12) {
            Toast.makeText(SignupActivity.this, "Invalid Mobile Number.", Toast.LENGTH_LONG).show();
            return false;
        } else if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
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

    /* *//* UpdateProfile ******************** Picture*//*

    private void requestStoragePermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void ShowFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {

            filepath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                profilePicture.setImageBitmap(bitmap);
                // Toast.makeText(getApplicationContext(),getPath(filepath),Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                Log.e(TAG, "onActivityResult: ",ex );
            }
        }
    }



    private String getPath(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + "=?", new String[]{document_id}, null
        );
        cursor.moveToFirst();
        @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }*/

/*    private void uploadImage() {

        String path = getPath(filepath);
        try {
            String uploadId = UUID.randomUUID().toString();
            MultipartUploadRequest multipartUploadRequest=
                    new MultipartUploadRequest(this, uploadId, UPLOAD_URL);

            multipartUploadRequest.addFileToUpload(path, "upload")
                    .addParameter("emp_code", emp_code)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(3)
                    .startUpload();


            Toast.makeText(this, "Upload Successfully", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.e(TAG, "uploadImage: ",ex );
            Toast.makeText(this, "Upload Failed", Toast.LENGTH_LONG).show();



        }

    }*/




}