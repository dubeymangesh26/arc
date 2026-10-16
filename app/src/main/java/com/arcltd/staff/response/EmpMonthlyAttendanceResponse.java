package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmpMonthlyAttendanceResponse {

    @SerializedName("ResponseCode")
    private String responseCode;

    @SerializedName("message")
    private String message;

    @SerializedName("emp_code")
    private String empCode;

    @SerializedName("emp_name")
    private String empName;

    @SerializedName("branch_name")
    private String branchName;

    @SerializedName("division_name")
    private String divisionName;

    @SerializedName("designation")
    private String designation;

    @SerializedName("emp_salary")
    private double empSalary;

    @SerializedName("month_year")
    private String monthYear;

    @SerializedName("total_days")
    private int totalDays;

    @SerializedName("present_days")
    private int presentDays;

    @SerializedName("absent_days")
    private int absentDays;

    @SerializedName("earned_salary")
    private double earnedSalary;

    @SerializedName("daily_attendance_list")
    private List<DailyItem> dailyAttendanceList;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getEmpSalary() {
        return empSalary;
    }

    public void setEmpSalary(double empSalary) {
        this.empSalary = empSalary;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public int getPresentDays() {
        return presentDays;
    }

    public void setPresentDays(int presentDays) {
        this.presentDays = presentDays;
    }

    public int getAbsentDays() {
        return absentDays;
    }

    public void setAbsentDays(int absentDays) {
        this.absentDays = absentDays;
    }

    public double getEarnedSalary() {
        return earnedSalary;
    }

    public void setEarnedSalary(double earnedSalary) {
        this.earnedSalary = earnedSalary;
    }

    public List<DailyItem> getDailyAttendanceList() {
        return dailyAttendanceList;
    }

    public void setDailyAttendanceList(List<DailyItem> dailyAttendanceList) {
        this.dailyAttendanceList = dailyAttendanceList;
    }

    public static class DailyItem {

        @SerializedName("date")
        private String date;

        @SerializedName("time_in")
        private String timeIn;

        @SerializedName("time_out")
        private String timeOut;

        @SerializedName("status")
        private String status;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTimeIn() {
            return timeIn;
        }

        public void setTimeIn(String timeIn) {
            this.timeIn = timeIn;
        }

        public String getTimeOut() {
            return timeOut;
        }

        public void setTimeOut(String timeOut) {
            this.timeOut = timeOut;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
