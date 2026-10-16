package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendanceReportResponse {

    @SerializedName("ResponseCode")
    private String responseCode;

    @SerializedName("message")
    private String message;

    @SerializedName("attendance_list")
    private List<AttendanceItem> attendanceList;

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

    public List<AttendanceItem> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<AttendanceItem> attendanceList) {
        this.attendanceList = attendanceList;
    }

    public static class AttendanceItem {

        @SerializedName("att_id")
        private String attId;

        @SerializedName("emp_code")
        private String empCode;

        @SerializedName("emp_name")
        private String empName;

        @SerializedName("branch_code")
        private String branchCode;

        @SerializedName("branch_name")
        private String branchName;

        @SerializedName("division_id")
        private String divisionId;

        @SerializedName("division_name")
        private String divisionName;

        @SerializedName("attendance_date")
        private String attendanceDate;

        @SerializedName("time_in")
        private String timeIn;

        @SerializedName("time_out")
        private String timeOut;

        @SerializedName("distance_meters")
        private double distanceMeters;

        @SerializedName("status")
        private String status;

        public String getAttId() {
            return attId;
        }

        public void setAttId(String attId) {
            this.attId = attId;
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

        public String getBranchCode() {
            return branchCode;
        }

        public void setBranchCode(String branchCode) {
            this.branchCode = branchCode;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getDivisionId() {
            return divisionId;
        }

        public void setDivisionId(String divisionId) {
            this.divisionId = divisionId;
        }

        public String getDivisionName() {
            return divisionName;
        }

        public void setDivisionName(String divisionName) {
            this.divisionName = divisionName;
        }

        public String getAttendanceDate() {
            return attendanceDate;
        }

        public void setAttendanceDate(String attendanceDate) {
            this.attendanceDate = attendanceDate;
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

        public double getDistanceMeters() {
            return distanceMeters;
        }

        public void setDistanceMeters(double distanceMeters) {
            this.distanceMeters = distanceMeters;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
