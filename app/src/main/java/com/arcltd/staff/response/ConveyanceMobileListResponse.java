package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConveyanceMobileListResponse {


    private List<ConveyanceMobileList> conveyance_mobile_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<ConveyanceMobileList> getConveyance_mobile_list() {
        return conveyance_mobile_list;
    }

    public void setConveyance_mobile_list(List<ConveyanceMobileList> conveyance_mobile_list) {
        this.conveyance_mobile_list = conveyance_mobile_list;
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

    public static class ConveyanceMobileList {
        private String ps_id;
        private String branch_code;
        private String emp_code;
        private String emp_name;
        private String conveyance;
        private String mobile;
        private String updated_date;
        private String created_date;

        public String getPs_id() {
            return ps_id;
        }

        public void setPs_id(String ps_id) {
            this.ps_id = ps_id;
        }

        public String getBranch_code() {
            return branch_code;
        }

        public void setBranch_code(String branch_code) {
            this.branch_code = branch_code;
        }

        public String getEmp_code() {
            return emp_code;
        }

        public void setEmp_code(String emp_code) {
            this.emp_code = emp_code;
        }

        public String getEmp_name() {
            return emp_name;
        }

        public void setEmp_name(String emp_name) {
            this.emp_name = emp_name;
        }

        public String getConveyance() {
            return conveyance;
        }

        public void setConveyance(String conveyance) {
            this.conveyance = conveyance;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getUpdated_date() {
            return updated_date;
        }

        public void setUpdated_date(String updated_date) {
            this.updated_date = updated_date;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }
    }
}
