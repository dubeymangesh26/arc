package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BranchListResponse {

    private List<BranchList> branchList;
    @SerializedName("ResponseCode")
    private String responseCode;
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
        private String branch_id;
        private String branch_name;
        private String branch_code;
        private String region_id;
        private String region_name;
        private String division_id;
        private String division_name;
        private String ac_branch;
        private String branch_address;
        private String branch_mngr_mob;
        private String std_code;
        private String phone_number;
        private String branch_mobileno;
        private String email_id;
        private String updated_date;

        public String getBranch_id() {
            return branch_id;
        }

        public void setBranch_id(String branch_id) {
            this.branch_id = branch_id;
        }

        public String getBranch_name() {
            return branch_name;
        }

        public void setBranch_name(String branch_name) {
            this.branch_name = branch_name;
        }

        public String getBranch_code() {
            return branch_code;
        }

        public void setBranch_code(String branch_code) {
            this.branch_code = branch_code;
        }

        public String getRegion_id() {
            return region_id;
        }

        public void setRegion_id(String region_id) {
            this.region_id = region_id;
        }

        public String getRegion_name() {
            return region_name;
        }

        public void setRegion_name(String region_name) {
            this.region_name = region_name;
        }

        public String getDivision_id() {
            return division_id;
        }

        public void setDivision_id(String division_id) {
            this.division_id = division_id;
        }

        public String getDivision_name() {
            return division_name;
        }

        public void setDivision_name(String division_name) {
            this.division_name = division_name;
        }

        public String getAc_branch() {
            return ac_branch;
        }

        public void setAc_branch(String ac_branch) {
            this.ac_branch = ac_branch;
        }

        public String getBranch_address() {
            return branch_address;
        }

        public void setBranch_address(String branch_address) {
            this.branch_address = branch_address;
        }

        public String getBranch_mngr_mob() {
            return branch_mngr_mob;
        }

        public void setBranch_mngr_mob(String branch_mngr_mob) {
            this.branch_mngr_mob = branch_mngr_mob;
        }

        public String getStd_code() {
            return std_code;
        }

        public void setStd_code(String std_code) {
            this.std_code = std_code;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getBranch_mobileno() {
            return branch_mobileno;
        }

        public void setBranch_mobileno(String branch_mobileno) {
            this.branch_mobileno = branch_mobileno;
        }

        public String getEmail_id() {
            return email_id;
        }

        public void setEmail_id(String email_id) {
            this.email_id = email_id;
        }

        public String getUpdated_date() {
            return updated_date;
        }

        public void setUpdated_date(String updated_date) {
            this.updated_date = updated_date;
        }
    }
}
