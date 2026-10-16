package com.arcltd.staff.activity.attendance;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arcltd.staff.R;
import com.arcltd.staff.adapter.DailyAttendanceAdapter;
import com.arcltd.staff.base.BaseActivity;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.response.EmpMonthlyAttendanceResponse;
import com.arcltd.staff.utility.Constants;
import com.arcltd.staff.utility.Infrastructure;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EmpAttendanceMonthlyActivity extends BaseActivity {

    private static final String TAG = "EmpMonthlyAttendance";

    private Spinner spMonth, spYear;
    private TextView tvEmpInfo, tvBranchDesignation, tvTotalDays, tvPresentDays, tvAbsentDays, tvBasicSalary, tvEarnedSalary;
    private Button btnGeneratePdf;
    private RecyclerView rvDailyAttendance;

    private String targetEmpCode, targetEmpName, targetBranchName;
    private int selectedMonth, selectedYear;

    private EmpMonthlyAttendanceResponse currentResponseData;

    private final String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private final List<String> yearList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_attendance_monthly);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Monthly Attendance & Salary");
        }

        targetEmpCode = getIntent().getStringExtra("emp_code");
        targetEmpName = getIntent().getStringExtra("emp_name");
        targetBranchName = getIntent().getStringExtra("branch_name");

        Calendar cal = Calendar.getInstance();
        selectedMonth = cal.get(Calendar.MONTH) + 1; // 1-12
        selectedYear = cal.get(Calendar.YEAR);

        for (int y = selectedYear - 2; y <= selectedYear + 1; y++) {
            yearList.add(String.valueOf(y));
        }

        initUI();
        fetchMonthlyAttendance();
    }

    private void initUI() {
        spMonth = findViewById(R.id.sp_month);
        spYear = findViewById(R.id.sp_year);

        tvEmpInfo = findViewById(R.id.tv_emp_info);
        tvBranchDesignation = findViewById(R.id.tv_branch_designation);
        tvTotalDays = findViewById(R.id.tv_total_days);
        tvPresentDays = findViewById(R.id.tv_present_days);
        tvAbsentDays = findViewById(R.id.tv_absent_days);
        tvBasicSalary = findViewById(R.id.tv_basic_salary);
        tvEarnedSalary = findViewById(R.id.tv_earned_salary);
        btnGeneratePdf = findViewById(R.id.btn_generate_pdf);
        rvDailyAttendance = findViewById(R.id.rv_daily_attendance);

        rvDailyAttendance.setLayoutManager(new LinearLayoutManager(this));
        rvDailyAttendance.setNestedScrollingEnabled(false);

        // Check PDF Share Permissions: ONLY VISIBLE if BOTH login_type = "RG" AND emp_code = "A8513"
        String myLoginType = RetrofitClient.getStringUserPreference(this, Constants.LOGIN_TYPE);
        String myEmpCodePref = RetrofitClient.getStringUserPreference(this, Constants.EMP_CODE);

        boolean canShareSalarySlip = "RG".equalsIgnoreCase(myLoginType) && "A8513".equalsIgnoreCase(myEmpCodePref);
        if (canShareSalarySlip) {
            btnGeneratePdf.setVisibility(View.VISIBLE);
        } else {
            btnGeneratePdf.setVisibility(View.GONE);
        }

        // Setup Month Spinner
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthNames);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(monthAdapter);
        spMonth.setSelection(selectedMonth - 1);

        // Setup Year Spinner
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(yearAdapter);
        spYear.setSelection(yearList.indexOf(String.valueOf(selectedYear)));

        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int newMonth = position + 1;
                if (newMonth != selectedMonth) {
                    selectedMonth = newMonth;
                    fetchMonthlyAttendance();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int newYear = Integer.parseInt(yearList.get(position));
                if (newYear != selectedYear) {
                    selectedYear = newYear;
                    fetchMonthlyAttendance();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnGeneratePdf.setOnClickListener(v -> generateAndShareSalarySlipPdf());
    }

    private void fetchMonthlyAttendance() {
        if (TextUtils.isEmpty(targetEmpCode)) return;

        if (!Infrastructure.isInternetPresent()) {
            Toast.makeText(this, "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }

        Infrastructure.showProgressDialog(this);
        apiPresenter.getEmpMonthlyAttendance(
                disposable,
                Constants.ApiRequestCode.EMP_MONTHLY_ATTENDANCE,
                targetEmpCode,
                selectedYear,
                selectedMonth
        );
    }

    @Override
    public void showResult(Object object, int callAPIId) {
        Infrastructure.dismissProgressDialog();

        if (callAPIId == Constants.ApiRequestCode.EMP_MONTHLY_ATTENDANCE) {
            if (object instanceof EmpMonthlyAttendanceResponse) {
                EmpMonthlyAttendanceResponse response = (EmpMonthlyAttendanceResponse) object;
                if ("200".equals(response.getResponseCode())) {
                    currentResponseData = response;
                    displaySummary(response);
                } else {
                    Toast.makeText(this, response.getMessage() != null ? response.getMessage() : "Failed to load monthly attendance", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void displaySummary(EmpMonthlyAttendanceResponse data) {
        String empName = !TextUtils.isEmpty(data.getEmpName()) ? data.getEmpName() : targetEmpName;
        String empCode = !TextUtils.isEmpty(data.getEmpCode()) ? data.getEmpCode() : targetEmpCode;
        tvEmpInfo.setText(String.format("%s (%s)", empName, empCode));

        String branch = !TextUtils.isEmpty(data.getBranchName()) ? data.getBranchName() : targetBranchName;
        String desig = !TextUtils.isEmpty(data.getDesignation()) ? data.getDesignation() : "Staff";
        tvBranchDesignation.setText(String.format("Branch: %s | %s", branch, desig));

        // Re-calculate / verify present and absent days accurately from daily attendance list
        int totalDays = data.getTotalDays();
        int presentCount = 0;
        int absentCount = 0;

        if (data.getDailyAttendanceList() != null && !data.getDailyAttendanceList().isEmpty()) {
            totalDays = data.getDailyAttendanceList().size();
            for (EmpMonthlyAttendanceResponse.DailyItem item : data.getDailyAttendanceList()) {
                if (item != null) {
                    String status = item.getStatus();
                    String timeIn = item.getTimeIn();
                    boolean isPresent = "PRESENT".equalsIgnoreCase(status) || (!TextUtils.isEmpty(timeIn) && !"-".equals(timeIn.trim()));
                    if (isPresent) {
                        item.setStatus("PRESENT");
                        presentCount++;
                    } else {
                        item.setStatus("ABSENT");
                        absentCount++;
                    }
                }
            }
        } else {
            presentCount = data.getPresentDays();
            absentCount = data.getAbsentDays();
        }

        if (totalDays <= 0) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, selectedYear);
            cal.set(Calendar.MONTH, selectedMonth - 1);
            totalDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        double basicSalary = data.getEmpSalary();
        double dailyRate = totalDays > 0 ? (basicSalary / totalDays) : 0;
        double earnedSalary = Math.round(dailyRate * presentCount * 100.0) / 100.0;

        data.setTotalDays(totalDays);
        data.setPresentDays(presentCount);
        data.setAbsentDays(absentCount);
        data.setEarnedSalary(earnedSalary);

        tvTotalDays.setText(String.valueOf(totalDays));
        tvPresentDays.setText(String.valueOf(presentCount));
        tvAbsentDays.setText(String.valueOf(absentCount));

        tvBasicSalary.setText(String.format(Locale.getDefault(), "₹ %.2f", basicSalary));
        tvEarnedSalary.setText(String.format(Locale.getDefault(), "₹ %.2f", earnedSalary));

        if (data.getDailyAttendanceList() != null && !data.getDailyAttendanceList().isEmpty()) {
            DailyAttendanceAdapter adapter = new DailyAttendanceAdapter(this, data.getDailyAttendanceList());
            rvDailyAttendance.setAdapter(adapter);
            rvDailyAttendance.setVisibility(View.VISIBLE);
        } else {
            rvDailyAttendance.setAdapter(null);
        }
    }

    private void generateAndShareSalarySlipPdf() {
        String myLoginType = RetrofitClient.getStringUserPreference(this, Constants.LOGIN_TYPE);
        String myEmpCodePref = RetrofitClient.getStringUserPreference(this, Constants.EMP_CODE);

        if (!"RG".equalsIgnoreCase(myLoginType) || !"A8513".equalsIgnoreCase(myEmpCodePref)) {
            Toast.makeText(this, "Access restricted. Only RG users with employee code A8513 can generate and share salary slips.", Toast.LENGTH_LONG).show();
            return;
        }

        if (currentResponseData == null) {
            Toast.makeText(this, "Attendance data not loaded yet.", Toast.LENGTH_SHORT).show();
            return;
        }

        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Paint titlePaint = new Paint();
        titlePaint.setAntiAlias(true);

        // A4 page dimensions: 595 x 842 points
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // 1. Draw Clean White Page Canvas
        canvas.drawColor(Color.WHITE);

        // 2. Draw Light Eagle Logo Watermark in Center of Page
        try {
            Drawable watermarkDrawable = ContextCompat.getDrawable(this, R.drawable.ic_arc_header);
            if (watermarkDrawable != null) {
                // Centered watermark box in page
                watermarkDrawable.setBounds(100, 260, 495, 540);
                watermarkDrawable.setAlpha(25); // ~10% opacity light watermark
                watermarkDrawable.draw(canvas);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error drawing watermark", e);
        }

        // 3. Render Top Vector Header Logo & Address Lines
        try {
            Drawable headerDrawable = ContextCompat.getDrawable(this, R.drawable.ic_arc_header);
            if (headerDrawable != null) {
                headerDrawable.setBounds(15, 10, 580, 125);
                headerDrawable.draw(canvas);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error drawing vector header", e);
        }

        // Header Title Text & Subtitle
        Paint headerTitlePaint = new Paint();
        headerTitlePaint.setAntiAlias(true);
        headerTitlePaint.setColor(Color.parseColor("#360000")); // ARC Maroon
        headerTitlePaint.setTextSize(19);
        headerTitlePaint.setFakeBoldText(true);
        canvas.drawText("Associated Road Carriers Limited", 165, 62, headerTitlePaint);

        Paint headerSubPaint = new Paint();
        headerSubPaint.setAntiAlias(true);
        headerSubPaint.setColor(Color.parseColor("#1F0202"));
        headerSubPaint.setTextSize(7.5f);
        headerSubPaint.setFakeBoldText(true);
        headerSubPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Sagar Complex, Tower - B, 3rd Floor, Mumbai - Pune Road, Opp. Kasarwali Railway Station,", 350, 85, headerSubPaint);
        canvas.drawText("Nashik Phatta, Pune, Maharashtra - 411034  ★  Phone : 020 4676 4444", 350, 97, headerSubPaint);

        // Maroon Accent Separation Bar
        Paint barPaint = new Paint();
        barPaint.setColor(Color.parseColor("#360000"));
        barPaint.setStrokeWidth(2.5f);
        canvas.drawLine(20, 112, 575, 112, barPaint);

        // 4. Outer Frame for Salary Details in Middle Body Area (Y: 135 to 680)
        int boxLeft = 35;
        int boxRight = 560;
        int boxTop = 135;
        int boxBottom = 680;

        // Outer Border Outline
        paint.setColor(Color.parseColor("#360000")); // ARC Maroon
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1.5f);
        canvas.drawRect(boxLeft, boxTop, boxRight, boxBottom, paint);

        // Header Title directly inside Box
        paint.setStyle(Paint.Style.FILL);
        titlePaint.setColor(Color.parseColor("#360000"));
        titlePaint.setTextSize(14);
        titlePaint.setFakeBoldText(true);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("EMPLOYEE MONTHLY SALARY SLIP", 297, boxTop + 24, titlePaint);

        // Subtitle & Period
        int startX = 50;
        int startY = boxTop + 48;

        paint.setColor(Color.parseColor("#111B21"));
        paint.setTextSize(11);
        paint.setFakeBoldText(true);
        String periodStr = currentResponseData.getMonthYear() != null ? currentResponseData.getMonthYear() : (monthNames[selectedMonth - 1] + " " + selectedYear);
        canvas.drawText("Payslip Period: " + periodStr, startX, startY, paint);

        startY += 12;
        paint.setStrokeWidth(1.0f);
        canvas.drawLine(startX, startY, boxRight - 15, startY, paint);

        // --- EMPLOYEE DETAILS SECTION ---
        startY += 22;
        paint.setTextSize(11);
        paint.setFakeBoldText(true);
        canvas.drawText("Employee Name:", startX, startY, paint);
        paint.setFakeBoldText(false);
        canvas.drawText(currentResponseData.getEmpName() != null ? currentResponseData.getEmpName() : "", startX + 110, startY, paint);

        paint.setFakeBoldText(true);
        canvas.drawText("Emp Code:", 340, startY, paint);
        paint.setFakeBoldText(false);
        canvas.drawText(currentResponseData.getEmpCode() != null ? currentResponseData.getEmpCode() : "", 415, startY, paint);

        startY += 20;
        paint.setFakeBoldText(true);
        canvas.drawText("Branch Name:", startX, startY, paint);
        paint.setFakeBoldText(false);
        canvas.drawText(currentResponseData.getBranchName() != null ? currentResponseData.getBranchName() : "", startX + 110, startY, paint);

        paint.setFakeBoldText(true);
        canvas.drawText("Designation:", 340, startY, paint);
        paint.setFakeBoldText(false);
        canvas.drawText(currentResponseData.getDesignation() != null ? currentResponseData.getDesignation() : "Staff", 415, startY, paint);

        startY += 16;
        canvas.drawLine(startX, startY, boxRight - 15, startY, paint);

        // --- ATTENDANCE BREAKDOWN SECTION ---
        startY += 22;
        paint.setFakeBoldText(true);
        paint.setTextSize(12);
        paint.setColor(Color.parseColor("#360000"));
        canvas.drawText("ATTENDANCE BREAKDOWN", startX, startY, paint);

        startY += 20;
        paint.setColor(Color.parseColor("#111B21"));
        paint.setTextSize(11);
        paint.setFakeBoldText(false);
        canvas.drawText("Total Days in Month: " + currentResponseData.getTotalDays(), startX + 10, startY, paint);

        paint.setFakeBoldText(true);
        paint.setColor(Color.parseColor("#008000")); // Green
        canvas.drawText("Days Present: " + currentResponseData.getPresentDays(), startX + 180, startY, paint);

        paint.setColor(Color.parseColor("#CC0000")); // Red
        canvas.drawText("Days Absent: " + currentResponseData.getAbsentDays(), startX + 340, startY, paint);

        startY += 16;
        paint.setColor(Color.parseColor("#111B21"));
        canvas.drawLine(startX, startY, boxRight - 15, startY, paint);

        // --- SALARY EARNINGS & PAYABLE SECTION ---
        startY += 22;
        paint.setFakeBoldText(true);
        paint.setTextSize(12);
        paint.setColor(Color.parseColor("#360000"));
        canvas.drawText("SALARY EARNINGS & PAYABLE", startX, startY, paint);

        startY += 22;
        paint.setColor(Color.parseColor("#111B21"));
        paint.setTextSize(11);
        paint.setFakeBoldText(true);
        canvas.drawText("Basic Monthly Salary:", startX + 10, startY, paint);
        paint.setFakeBoldText(false);
        canvas.drawText(String.format(Locale.getDefault(), "Rs. %.2f", currentResponseData.getEmpSalary()), 360, startY, paint);

        startY += 20;
        double dailyRate = currentResponseData.getTotalDays() > 0 ? (currentResponseData.getEmpSalary() / currentResponseData.getTotalDays()) : 0;
        paint.setFakeBoldText(true);
        canvas.drawText("Daily Calculation Rate:", startX + 10, startY, paint);
        paint.setFakeBoldText(false);
        canvas.drawText(String.format(Locale.getDefault(), "Rs. %.2f / day", dailyRate), 360, startY, paint);

        startY += 20;
        paint.setFakeBoldText(true);
        canvas.drawText("Present Days Payable:", startX + 10, startY, paint);
        paint.setFakeBoldText(false);
        canvas.drawText(currentResponseData.getPresentDays() + " days", 360, startY, paint);

        startY += 18;
        paint.setStrokeWidth(1.5f);
        canvas.drawLine(startX, startY, boxRight - 15, startY, paint);

        startY += 25;
        paint.setFakeBoldText(true);
        paint.setTextSize(13);
        paint.setColor(Color.parseColor("#008000")); // Dark Green
        canvas.drawText("NET EARNED PAYABLE SALARY:", startX + 10, startY, paint);
        canvas.drawText(String.format(Locale.getDefault(), "Rs. %.2f", currentResponseData.getEarnedSalary()), 360, startY, paint);

        // --- ELECTRONIC GENERATION DISCLAIMER (No signature required) ---
        startY = boxBottom - 35;
        paint.setColor(Color.parseColor("#111B21"));
        paint.setStrokeWidth(1.0f);
        canvas.drawLine(startX, startY, boxRight - 15, startY, paint);

        startY += 20;
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setColor(Color.parseColor("#360000")); // ARC Maroon
        titlePaint.setTextSize(9.5f);
        titlePaint.setFakeBoldText(true);
        canvas.drawText("★ This is an electronically generated payslip, signature not required. ★", 297, startY, titlePaint);

        // Registered Office Footer at Bottom
        startY = 750;
        Paint footerPaint = new Paint();
        footerPaint.setAntiAlias(true);
        footerPaint.setColor(Color.parseColor("#1F0202"));
        footerPaint.setTextSize(7.5f);
        footerPaint.setFakeBoldText(true);
        footerPaint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText("Regd. Office: OM TOWER, 9th Floor, 32, Jawahar Lal Nehru Road, Kolkata - 700071 (West Bengal)", 297, startY, footerPaint);
        canvas.drawText("Corporate Office: Surya Towers, 3rd Floor, 105, Sardar Patel Road, Secunderabad - 500003 (Telangana)", 297, startY + 12, footerPaint);
        canvas.drawText("Website: www.arclimited.com  ★  CIN No.: U63090WB1972PLC028346", 297, startY + 24, footerPaint);

        pdfDocument.finishPage(page);

        // Save PDF File
        try {
            File cachePath = new File(getCacheDir(), "salary_slips");
            if (!cachePath.exists()) {
                cachePath.mkdirs();
            }
            File pdfFile = new File(cachePath, "SalarySlip_" + targetEmpCode + "_" + selectedMonth + "_" + selectedYear + ".pdf");
            FileOutputStream outputStream = new FileOutputStream(pdfFile);
            pdfDocument.writeTo(outputStream);
            outputStream.close();
            pdfDocument.close();

            // Share PDF Intent
            Uri pdfUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", pdfFile);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Salary Slip - " + targetEmpName + " (" + monthNames[selectedMonth - 1] + " " + selectedYear + ")");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Associated Road Carriers Limited - Monthly Salary Slip for " + targetEmpName + " (" + targetEmpCode + ")");
            shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share Salary Slip PDF via"));

            Toast.makeText(this, "Salary slip PDF generated successfully!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e(TAG, "Error generating salary slip PDF", e);
            Toast.makeText(this, "Failed to generate PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDisplayMessage(String message, int callAPIId, int errorCode) {
        Infrastructure.dismissProgressDialog();
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
