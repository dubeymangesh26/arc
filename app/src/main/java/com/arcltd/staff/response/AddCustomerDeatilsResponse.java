package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

public class AddCustomerDeatilsResponse {


    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String branch_code;
        private Integer cust_id;

        public String getBranch_code() {
            return branch_code;
        }

        public void setBranch_code(String branch_code) {
            this.branch_code = branch_code;
        }

        public Integer getCust_id() {
            return cust_id;
        }

        public void setCust_id(Integer cust_id) {
            this.cust_id = cust_id;
        }
    }
}
