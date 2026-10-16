package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BranchListResponse {

    @SerializedName("branchList")
    private List<BranchList> branchList;
    @SerializedName("ResponseCode")
    private String responseCode;
    @SerializedName("message")
    private String message;

    public List<BranchList> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<BranchList> branchList) {
        this.branchList = branchList;
    }

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

    public static class BranchList {
        @SerializedName("branch_id")
        private String branchId;
        @SerializedName("branch_name")
        private String branchName;
        @SerializedName("branch_code")
        private String branchCode;
        @SerializedName("region_id")
        private String regionId;
        @SerializedName("region_name")
        private String regionName;
        @SerializedName("division_id")
        private String divisionId;
        @SerializedName("division_name")
        private String divisionName;
        @SerializedName("ac_branch")
        private String acBranch;
        @SerializedName("branch_address")
        private String branchAddress;
        @SerializedName("branch_mngr_mob")
        private String branchMngrMob;
        @SerializedName("std_code")
        private String stdCode;
        @SerializedName("phone_number")
        private String phoneNumber;
        @SerializedName("branch_mobileno")
        private String branchMobileno;
        @SerializedName("email_id")
        private String emailId;
        @SerializedName("updated_date")
        private String updatedDate;

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getBranchCode() {
            return branchCode;
        }

        public void setBranchCode(String branchCode) {
            this.branchCode = branchCode;
        }

        public String getRegionId() {
            return regionId;
        }

        public void setRegionId(String regionId) {
            this.regionId = regionId;
        }

        public String getRegionName() {
            return regionName;
        }

        public void setRegionName(String regionName) {
            this.regionName = regionName;
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

        public String getAcBranch() {
            return acBranch;
        }

        public void setAcBranch(String acBranch) {
            this.acBranch = acBranch;
        }

        public String getBranchAddress() {
            return branchAddress;
        }

        public void setBranchAddress(String branchAddress) {
            this.branchAddress = branchAddress;
        }

        public String getBranchMngrMob() {
            return branchMngrMob;
        }

        public void setBranchMngrMob(String branchMngrMob) {
            this.branchMngrMob = branchMngrMob;
        }

        public String getStdCode() {
            return stdCode;
        }

        public void setStdCode(String stdCode) {
            this.stdCode = stdCode;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getBranchMobileno() {
            return branchMobileno;
        }

        public void setBranchMobileno(String branchMobileno) {
            this.branchMobileno = branchMobileno;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }
    }
}
