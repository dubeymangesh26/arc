package com.arcltd.staff.authentication;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.arcltd.staff.networkhandler.errors.ErrorStatus.NO_INTERNET;

import android.Manifest;
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
    TextInputEditText editTextName, editTextEmail, editTextMobile, editTextPassword, editTextCode;
    Button cirRegisterButton;
    private ProgressBar progressBar;
    ImageView image_logo;
    AutoCompleteTextView searchEmp;
    LinearLayout signData;
    TextView tvError;
    ProgressBar searchProgressBar;
    CircleImageView profilePic;
    String email_id,status="H",Mess_name="Pending",notvarify="Not Verified",loginType="EM",token;
    String region_ids, region_names, division_ids, division_names, branch_codes,branch_name,
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        profilePic = findViewById(R.id.profilePicture);

        signData.setVisibility(View.GONE);

        //  image_logo = findViewById(R.id.image_logo);

        //   Glide.with(this).load(R.drawable.eagle).into(image_logo);



      askPermissions();

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



            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectCameraOrGallery();
                }
            });


    }


    private void askPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(SignupActivity.this,

                    Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 2);
                // Permission is not granted
                // Should we show an explanation?
            } else if (ContextCompat.checkSelfPermission(SignupActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
                // Permission is not granted
                // Should we show an explanation?
            }
        }else {
            if (ContextCompat.checkSelfPermission(SignupActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                // Permission is not granted
                //
            } else if (ContextCompat.checkSelfPermission(SignupActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
                // Permission is not granted
                // Should we show an explanation?
            }

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the

                        askPermissions();

                } else {
                    // permission denied, boo! Disable the

                        askPermissions();


                    // functionality that depends on this permission.
                }
            }
        }
    }

    private void getProfileImageBody() {

        if (selectedImage == null) {
            glBody = RequestBody.create(MediaType.parse("image/*"), "");
            glMulPart = MultipartBody.Part.createFormData("profile_pic", "", glBody);
        } else {
            file = new File(getRealPathFromURI(selectedImage));
            //creating request body for file
            glBody = RequestBody.create(MediaType.parse("image/*"), file);
            glMulPart = MultipartBody.Part.createFormData("profile_pic", file.getName(), glBody);
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void selectCameraOrGallery() {
        String[] selectIntents = {"\uD83D\uDCF7" + "   Camera", "\uD83C\uDF07" + "   Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select image from")
                .setItems(selectIntents, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int position) {
                        if (position == 1) {
                            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(Intent.createChooser(gallery, "Select Profile Pic"), 9);
                        } else {
                            dispatchTakePictureIntent();
                        }
                    }
                });
        builder.create();
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//Start intent with Action_Image_Capture
        selectedImage = CameraUtils.getOutputMediaFileUri(getApplicationContext());//get fileUri from CameraUtils

        File photoFile = null;
        photoFile = CameraUtils.getOutputMediaFile(Objects.requireNonNull(getApplicationContext()));
        cPath = photoFile.getAbsolutePath();
        Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 101);
        Log.e(TAG, "dispatchTakePictureIntent: " + REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 9 && data != null) {
                selectedImage = data.getData();

            } else if (requestCode == 101) {
                Bitmap photo = BitmapFactory.decodeFile(cPath);
                selectedImage = getImageUri(this,photo);

            }
            Glide.with(getApplicationContext()).load(selectedImage).override(200, 200).into(profilePic);
        }
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }



    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    public void onClickSignUp(View view) {

        if (validate()) {
            getProfileImageBody();
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

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(WebConstants.baseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                WebService apiService = retrofit.create(WebService.class);

                File imageFile = new File(selectedImage.getPath());
                RequestBody imageRequestBody = glMulPart.body();
                // Create a multipart request body part
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("profile_image", imageFile.getName(), imageRequestBody);


                RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
                RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
                RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);
                RequestBody contactnoBody = RequestBody.create(MediaType.parse("text/plain"), contactno);
                RequestBody loginTypeBody = RequestBody.create(MediaType.parse("text/plain"), loginType);
                RequestBody region_idsBody = RequestBody.create(MediaType.parse("text/plain"), region_ids);
                RequestBody region_namesBody = RequestBody.create(MediaType.parse("text/plain"), region_names);
                RequestBody division_idsBody = RequestBody.create(MediaType.parse("text/plain"), division_ids);
                RequestBody division_namesBody = RequestBody.create(MediaType.parse("text/plain"), division_names);
                RequestBody branch_codesBody = RequestBody.create(MediaType.parse("text/plain"), branch_codes);
                RequestBody branch_nameBody = RequestBody.create(MediaType.parse("text/plain"), branch_name);
                RequestBody emp_CodesBody = RequestBody.create(MediaType.parse("text/plain"), emp_Codes);
                RequestBody emp_deisignsBody = RequestBody.create(MediaType.parse("text/plain"), emp_deisigns);
                RequestBody statusBody = RequestBody.create(MediaType.parse("text/plain"), status);
                RequestBody Mess_nameBody = RequestBody.create(MediaType.parse("text/plain"), Mess_name);
                RequestBody notvarifyBody = RequestBody.create(MediaType.parse("text/plain"), notvarify);
                RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);


                Call<RegisterRespponse> call = apiService.signup( nameBody, emailBody, passwordBody, contactnoBody, loginTypeBody, region_idsBody
                        ,region_namesBody, division_idsBody, division_namesBody, branch_codesBody,branch_nameBody, emp_CodesBody, emp_deisignsBody,
                        statusBody,Mess_nameBody,notvarifyBody,tokenBody, imagePart);
                call.enqueue(new Callback<RegisterRespponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RegisterRespponse> call, @NonNull Response<RegisterRespponse> registermlRespponse) {

                            searchProgressBar.setVisibility(View.GONE);

                        if (registermlRespponse.isSuccessful()) {
                            RegisterRespponse registerRespponse = registermlRespponse.body();
                            if (registerRespponse != null ) {

                                email_id = registerRespponse.getData().getEmail();

                                startActivity(new Intent(SignupActivity.this, OTPActivity.class)
                                        .putExtra("me", email_id));
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

                            } else {
                                // Signup failed, show an error message
                                Toast.makeText(SignupActivity.this, "Signup failed, show an error message", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            // Signup failed, show an error message
                            Toast.makeText(SignupActivity.this, "Something went Wrong", Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<RegisterRespponse> call, Throwable t) {
                        searchProgressBar.setVisibility(View.GONE);
                        // Handle failure
                        Toast.makeText(SignupActivity.this, "Error "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onFailure: "+t.getMessage().toString());
                    }
                });

               /* apiPresenter.userregistration(disposable, Constants.ApiRequestCode.USER_REGISTRATION,
                        name, email, password, contactno, "EM", region_ids,region_names, division_ids
                        , division_names, branch_codes,branch_name, emp_Codes, emp_deisigns, "H",
                        "","Not Verified", imagePart);
                Log.e("login_Rquest", "login_Rquest: " + apiPresenter);*/
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

       /* if (callAPIId == Constants.ApiRequestCode.USER_REGISTRATION) {
            // LoginResponse loginResponse = new Gson().fromJson(object, LoginResponse.class);
            RegisterRespponse registerRespponse = (RegisterRespponse) object;
            finallogin(registerRespponse);
        }*/
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


   /* private void finallogin(RegisterRespponse registerRespponse) {
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
*/
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
        } else if (TextUtils.isEmpty(Objects.requireNonNull(editTextEmail.getText()).toString())) {
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
        } else if (profilePic.getDrawable() == null) {
            Toast.makeText(getApplicationContext(), "Please Select Profile Image.", Toast.LENGTH_LONG).show();
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