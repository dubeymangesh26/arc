package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

public class BranchLocationResponse {

    @SerializedName("ResponseCode")
    private String responseCode;

    @SerializedName("message")
    private String message;

    @SerializedName("branch_code")
    private String branchCode;

    @SerializedName("branch_name")
    private String branchName;

    @SerializedName("lat")
    private String lat;

    @SerializedName("lng")
    private String lng;

    @SerializedName("allowed_radius_meters")
    private int allowedRadiusMeters;

    @SerializedName("branch_qr_code")
    private String branchQrCode;

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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public int getAllowedRadiusMeters() {
        return allowedRadiusMeters;
    }

    public void setAllowedRadiusMeters(int allowedRadiusMeters) {
        this.allowedRadiusMeters = allowedRadiusMeters;
    }

    public String getBranchQrCode() {
        return branchQrCode;
    }

    public void setBranchQrCode(String branchQrCode) {
        this.branchQrCode = branchQrCode;
    }
}
