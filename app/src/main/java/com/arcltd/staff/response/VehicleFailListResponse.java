package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleFailListResponse {

    private List<PlaceFaildList> place_faild_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<PlaceFaildList> getPlace_faild_list() {
        return place_faild_list;
    }

    public void setPlace_faild_list(List<PlaceFaildList> place_faild_list) {
        this.place_faild_list = place_faild_list;
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

    public static class PlaceFaildList {
        private String faild_id;
        private String region_id;
        private String division_id;
        private String branch_code;
        private String branch_name;
        private String emp_code;
        private String emp_name;
        private String vehicle_type;
        private String date;
        private String remark;
        private String faild_by;
        private String created_date;

        public String getFaild_id() {
            return faild_id;
        }

        public void setFaild_id(String faild_id) {
            this.faild_id = faild_id;
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

        public String getBranch_name() {
            return branch_name;
        }

        public void setBranch_name(String branch_name) {
            this.branch_name = branch_name;
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

        public String getVehicle_type() {
            return vehicle_type;
        }

        public void setVehicle_type(String vehicle_type) {
            this.vehicle_type = vehicle_type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getFaild_by() {
            return faild_by;
        }

        public void setFaild_by(String faild_by) {
            this.faild_by = faild_by;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }
    }
}
