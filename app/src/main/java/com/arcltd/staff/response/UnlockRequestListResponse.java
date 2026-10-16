package com.arcltd.staff.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UnlockRequestListResponse {

    @SerializedName("responsecode")
    @Expose
    private String responsecode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<UnlockRequestModel> data = null;

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UnlockRequestModel> getData() {
        return data;
    }

    public void setData(List<UnlockRequestModel> data) {
        this.data = data;
    }
    
    public static class UnlockRequestModel {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("emp_code")
        @Expose
        private String emp_code;
        @SerializedName("emp_name")
        @Expose
        private String emp_name;
        @SerializedName("device_key")
        @Expose
        private String device_key;
        @SerializedName("request_date")
        @Expose
        private String request_date;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getEmp_code() { return emp_code; }
        public void setEmp_code(String emp_code) { this.emp_code = emp_code; }

        public String getEmp_name() { return emp_name; }
        public void setEmp_name(String emp_name) { this.emp_name = emp_name; }

        public String getDevice_key() { return device_key; }
        public void setDevice_key(String device_key) { this.device_key = device_key; }

        public String getRequest_date() { return request_date; }
        public void setRequest_date(String request_date) { this.request_date = request_date; }
    }
}