package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InsCompanyResponse {


    private List<InsidentalList> insidental_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<InsidentalList> getInsidental_list() {
        return insidental_list;
    }

    public void setInsidental_list(List<InsidentalList> insidental_list) {
        this.insidental_list = insidental_list;
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

    public static class InsidentalList {
        private String ins_id;
        private String region_id;
        private String division_id;
        private String branch_code;
        private String company_code;
        private String company_name;
        private String created_date;

        public String getIns_id() {
            return ins_id;
        }

        public void setIns_id(String ins_id) {
            this.ins_id = ins_id;
        }

        public String getRegion_id() {
            return region_id;
        }

        public void setRegion_id(String region_id) {
            this.region_id = region_id;
        }

        public String getDivision_id() {
            return division_id;
        }

        public void setDivision_id(String division_id) {
            this.division_id = division_id;
        }

        public String getBranch_code() {
            return branch_code;
        }

        public void setBranch_code(String branch_code) {
            this.branch_code = branch_code;
        }

        public String getCompany_code() {
            return company_code;
        }

        public void setCompany_code(String company_code) {
            this.company_code = company_code;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }
    }
}
