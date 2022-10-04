package com.arcltd.staff.activity.otherAndMain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arcltd.staff.BuildConfig;
import com.arcltd.staff.R;
import com.arcltd.staff.response.EmployeeListResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class BirthdayWisheshActivity extends AppCompatActivity {
    TextInputEditText etEmpCode,etEmpName,etMessage,etEmpMobile;
    Spinner spMessage;
    String data,wishMessage;
    EmployeeListResponse.EmployeeList dataBean;
    String empNamedilog,path,folderPath;
    String phonestr,messagestr;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    Uri bmpUri;
    ImageView imageView;
    ArrayList<String> messageArrary = new ArrayList<>(Arrays.asList("Select Message",
            "! HAPPY BIRTHDAY! \uD83D\uDC90\uD83D\uDC90\n" +
                    " Many Many Happy Returns of the Day. Have a " +
                    "Blessed Day filled with God's Love and Peace. \n" +
                    "Stay Blessed.\uD83C\uDF8A\uD83C\uDF8A",
            "Happy birthday! I hope all your birthday wishes and dreams come true.\n \uD83C\uDF82\uD83C\uDF82\uD83C\uDF82"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_birthday_wishesh);

        etEmpCode=findViewById(R.id.etEmpCode);
        etEmpName=findViewById(R.id.etEmpName);
        etMessage=findViewById(R.id.etMessage);
        spMessage=findViewById(R.id.spMessage);
        imageView=findViewById(R.id.imageView);
        etEmpMobile=findViewById(R.id.etEmpMobile);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        try {
            data = Objects.requireNonNull(getIntent().getExtras()).getString("data");
            dataBean = new Gson().fromJson(data, EmployeeListResponse.EmployeeList.class);
            Log.e("", "onCreate: " + new GsonBuilder().create().toJson(dataBean));

        } catch (Exception e) {
            Log.e("", "Data Listing error: " + e);
        }

        try {
            empNamedilog=dataBean.getEmp_name();
            etEmpCode.setText(dataBean.getEmp_code());
            etEmpName.setText(dataBean.getEmp_name());
            etEmpMobile.setText(dataBean.getEmp_mob());

        } catch (Exception e) {
            Log.e("", "ListingDetail error: " + e);
        }


        ArrayAdapter<String> cast = new ArrayAdapter<String>(BirthdayWisheshActivity.this,
                android.R.layout.simple_spinner_item, messageArrary);
        cast.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spMessage.setAdapter(cast);

        spMessage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    wishMessage = messageArrary.get(position);
                    if (!wishMessage.equals("Select Message")) {
                        etMessage.setText(wishMessage);
                    }else {
                        etMessage.setText("");
                    }
                    Log.e("", "onItemClick: " + position);

                } catch (Exception e) {
                    Log.e("TAG", "onItemSelected: "+e );
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



    }

    public void submitSharen(View view) {
        phonestr="+91"+etEmpMobile.getText().toString();
        messagestr="Hi,"+"\n"+dataBean.getEmp_name()+"\n"+"\uD83C\uDF82Happy Birthday!\uD83C\uDF82"+"\n\n"
                +etMessage.getText().toString() +"\n\n"+
                "From "+"\n"+"ARC Family"+
                "\n"+"Mumbai ";
        if (validate()){
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + phonestr +
                    "&text=" + messagestr));
            startActivity(i);

            if (getPackageManager() == null) {
                Toast.makeText(BirthdayWisheshActivity.this, "Error/n" + "Whatsapp Not Installed", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public void submitShareMessage(View view) {
        phonestr="+91"+etEmpMobile.getText().toString();
        messagestr="Hi,"+"\n"+dataBean.getEmp_name()+"\n"+"\uD83C\uDF82Happy Birthday!\uD83C\uDF82"+"\n\n"
                +etMessage.getText().toString() +"\n\n"+
                "From"+"\n"+ "ARC Family"+
                "\n"+"Mumbai ";
       // sendSms(phonestr, messagestr);
        if (validate()) {
            Uri sms_uri = Uri.parse("smsto:" +phonestr);
            Intent sms_intent = new Intent(Intent.ACTION_VIEW, sms_uri);
            sms_intent.setData(sms_uri);
            sms_intent.putExtra("sms_body", messagestr);
            startActivity(sms_intent);


           /* try {
                SmsManager sms = SmsManager.getDefault();
                ArrayList<String> parts = sms.divideMessage(messagestr);
                sms.sendMultipartTextMessage(phonestr, null, parts, null, null);
                Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Some fiedls is Empty", Toast.LENGTH_LONG).show();
            }*/
        }
    }

    private boolean validate() {

        if (TextUtils.isEmpty(etMessage.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please Select  Message!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etMessage.getText().toString().equals("Select Message")) {
            Toast.makeText(getApplicationContext(), " Please Select Message!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etEmpMobile.getText().toString())) {
            Toast.makeText(getApplicationContext(), " Please Enter Mobile No.!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (etEmpMobile.getText().length() < 10) {
            Toast.makeText(getApplicationContext(), "Invalid Mobile Number.", Toast.LENGTH_LONG).show();
            return false;
        } else if (etEmpMobile.getText().length() > 12) {
            Toast.makeText(getApplicationContext(), "Invalid Mobile Number.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }



    public void submitShareContact(View view) {
        viewWishingCard();

    }


    public void viewCard(View view) {
        viewWishingCard();
    }

    private void viewWishingCard() {
        final Dialog dialog = new Dialog(BirthdayWisheshActivity.this);
        dialog.setContentView(R.layout.item_birthdaywish);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow()
                .setLayout(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        try {
            TextView tvCardName;
            Button shareCard;
            RelativeLayout relativelaout;


            tvCardName = dialog.findViewById(R.id.tvCardName);
            shareCard = dialog.findViewById(R.id.shareCard);
            relativelaout = dialog.findViewById(R.id.relativelaout);

            tvCardName.setText(empNamedilog);

            shareCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    Bitmap bitmap = loadBitmapFromView(relativelaout);
                   // File dir = new File(getApplicationContext().getExternalCacheDir(), "happybirthday.png");

                        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/happybirthday.png";
                        folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "";
                        OutputStream out = null;
                        File file = new File(getApplicationContext().getExternalCacheDir(), "happybirthday.png");
// if path not found create folder and get path
                        if (!file.exists()) {
                            File folder_path = new File(folderPath);
                            folder_path.mkdirs();
                            file = new File(path);
                        }
                        try {
                            out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        path = file.getPath();
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                            bmpUri = FileProvider.getUriForFile(BirthdayWisheshActivity.this,
                                    BuildConfig.APPLICATION_ID + ".provider", file);
                        } else {
                            bmpUri = Uri.parse("file://" + path);
                        }

                        FileOutputStream fos = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();
                        fos.close();
                        file.setReadable(true,false);
                        Intent shareIntent;
                        shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        shareIntent.setType("image/png");
                        startActivity(Intent.createChooser(shareIntent, "Share Using"));
                    } catch (FileNotFoundException e) {
                        Log.e("ExpressionEditImageActivity", "Error, " + e);
                    }catch (IOException e){
                        Log.e("TAG", "onClick: "+"Erorrr"+e );
                    }
                }
            });


        } catch (Exception e) {
            Log.e("TAG", "onClick: ", e);
        }

        ImageView close = (ImageView) dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public Bitmap loadBitmapFromView(RelativeLayout v) {
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Drawable drawable=v.getBackground();
        if (drawable!=null){
            drawable.draw(c);
        }else {
            c.drawColor(Color.WHITE);
        }
      //  v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
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