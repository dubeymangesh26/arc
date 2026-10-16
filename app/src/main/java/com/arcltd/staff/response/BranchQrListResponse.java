package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BranchQrListResponse {

    @SerializedName("ResponseCode")
    private String responseCode;

    @SerializedName("message")
    private String message;

    @SerializedName("branch_list")
    private List<BranchQrItem> branchList;

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

    public List<BranchQrItem> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<BranchQrItem> branchList) {
        this.branchList = branchList;
    }

    public static class BranchQrItem {

        @SerializedName("branch_code")
        private String branchCode;

        @SerializedName("branch_name")
        private String branchName;

        @SerializedName("latitude")
        private double latitude;

        @SerializedName("longitude")
        private double longitude;

        @SerializedName("radius")
        private int radius;

        @SerializedName("qr_key")
        private String qrKey;

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

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        public String getQrKey() {
            return qrKey;
        }

        public void setQrKey(String qrKey) {
            this.qrKey = qrKey;
        }
    }
}
