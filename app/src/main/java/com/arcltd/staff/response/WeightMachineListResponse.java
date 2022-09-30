package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeightMachineListResponse {


    private List<WeightMachine> weight_machine;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<WeightMachine> getWeight_machine() {
        return weight_machine;
    }

    public void setWeight_machine(List<WeightMachine> weight_machine) {
        this.weight_machine = weight_machine;
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

    public static class WeightMachine {
        private String w_id;
        private String branch_code;
        private String weightMc_No;
        private String make;
        private String model;
        private String renew_upto;
        private String updated_date;
        private String created_date;

        public String getW_id() {
            return w_id;
        }

        public void setW_id(String w_id) {
            this.w_id = w_id;
        }

        public String getBranch_code() {
            return branch_code;
        }

        public void setBranch_code(String branch_code) {
            this.branch_code = branch_code;
        }

        public String getWeightMc_No() {
            return weightMc_No;
        }

        public void setWeightMc_No(String weightMc_No) {
            this.weightMc_No = weightMc_No;
        }

        public String getMake() {
            return make;
        }

        public void setMake(String make) {
            this.make = make;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getRenew_upto() {
            return renew_upto;
        }

        public void setRenew_upto(String renew_upto) {
            this.renew_upto = renew_upto;
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
