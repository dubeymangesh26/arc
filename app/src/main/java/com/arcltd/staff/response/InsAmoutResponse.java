package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InsAmoutResponse {

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
        private String insm_id;
        private String branch_code;
        private String company_code;
        private String month;
        private String fright_amount;
        private String incidental_amount;
        private String amount_paid;
        private String paid_to;
        private String paid_tomobile_no;
        private String paid_by;
        private String created_date;

        public String getInsm_id() {
            return insm_id;
        }

        public void setInsm_id(String insm_id) {
            this.insm_id = insm_id;
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

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getFright_amount() {
            return fright_amount;
        }

        public void setFright_amount(String fright_amount) {
            this.fright_amount = fright_amount;
        }

        public String getIncidental_amount() {
            return incidental_amount;
        }

        public void setIncidental_amount(String incidental_amount) {
            this.incidental_amount = incidental_amount;
        }

        public String getAmount_paid() {
            return amount_paid;
        }

        public void setAmount_paid(String amount_paid) {
            this.amount_paid = amount_paid;
        }

        public String getPaid_to() {
            return paid_to;
        }

        public void setPaid_to(String paid_to) {
            this.paid_to = paid_to;
        }

        public String getPaid_tomobile_no() {
            return paid_tomobile_no;
        }

        public void setPaid_tomobile_no(String paid_tomobile_no) {
            this.paid_tomobile_no = paid_tomobile_no;
        }

        public String getPaid_by() {
            return paid_by;
        }

        public void setPaid_by(String paid_by) {
            this.paid_by = paid_by;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }
    }
}
