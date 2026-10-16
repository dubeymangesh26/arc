package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

public class MarkAttendanceResponse {

    @SerializedName("ResponseCode")
    private String responseCode;

    @SerializedName("message")
    private String message;

    @SerializedName("attendance_type")
    private String attendanceType; // "TIME_IN" or "TIME_OUT"

    @SerializedName("attendance_date")
    private String attendanceDate;

    @SerializedName("time_in")
    private String timeIn;

    @SerializedName("time_out")
    private String timeOut;

    @SerializedName("distance_meters")
    private double distanceMeters;

    @SerializedName("allowed_radius_meters")
    private double allowedRadiusMeters;

    @SerializedName("branch_code")
    private String branchCode;

    @SerializedName("branch_name")
    private String branchName;

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

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
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

    public double getAllowedRadiusMeters() {
        return allowedRadiusMeters;
    }

    public void setAllowedRadiusMeters(double allowedRadiusMeters) {
        this.allowedRadiusMeters = allowedRadiusMeters;
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
}
