package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoterCycleListResponse {


    private List<MoterCycle> moter_cycle;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<MoterCycle> getMoter_cycle() {
        return moter_cycle;
    }

    public void setMoter_cycle(List<MoterCycle> moter_cycle) {
        this.moter_cycle = moter_cycle;
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

    public static class MoterCycle {
        private String v_id;
        private String region_id;
        private String division_id;
        private String division_name;
        private String branch_code;
        private String branch_name;
        private String vehicle_no;
        private String regist_no;
        private String sold_status;
        private String reg_last_Date;
        private String standerd_km;
        private String insurance_no;
        private String make;
        private String model;
        private String ins_upto;
        private String puc_upto;
        private String user_person;
        private String updated_date;
        private String created_date;

        public String getV_id() {
            return v_id;
        }

        public void setV_id(String v_id) {
            this.v_id = v_id;
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

        public String getDivision_name() {
            return division_name;
        }

        public void setDivision_name(String division_name) {
            this.division_name = division_name;
        }

        public String getBranch_code() {
            return branch_code;
        }

        public void setBranch_code(String branch_code) {
            this.branch_code = branch_code;
        }

        public String getBranch_name() {
            return branch_name;
        }

        public void setBranch_name(String branch_name) {
            this.branch_name = branch_name;
        }

        public String getVehicle_no() {
            return vehicle_no;
        }

        public void setVehicle_no(String vehicle_no) {
            this.vehicle_no = vehicle_no;
        }

        public String getRegist_no() {
            return regist_no;
        }

        public void setRegist_no(String regist_no) {
            this.regist_no = regist_no;
        }

        public String getSold_status() {
            return sold_status;
        }

        public void setSold_status(String sold_status) {
            this.sold_status = sold_status;
        }

        public String getReg_last_Date() {
            return reg_last_Date;
        }

        public void setReg_last_Date(String reg_last_Date) {
            this.reg_last_Date = reg_last_Date;
        }

        public String getStanderd_km() {
            return standerd_km;
        }

        public void setStanderd_km(String standerd_km) {
            this.standerd_km = standerd_km;
        }

        public String getInsurance_no() {
            return insurance_no;
        }

        public void setInsurance_no(String insurance_no) {
            this.insurance_no = insurance_no;
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

        public String getIns_upto() {
            return ins_upto;
        }

        public void setIns_upto(String ins_upto) {
            this.ins_upto = ins_upto;
        }

        public String getPuc_upto() {
            return puc_upto;
        }

        public void setPuc_upto(String puc_upto) {
            this.puc_upto = puc_upto;
        }

        public String getUser_person() {
            return user_person;
        }

        public void setUser_person(String user_person) {
            this.user_person = user_person;
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
