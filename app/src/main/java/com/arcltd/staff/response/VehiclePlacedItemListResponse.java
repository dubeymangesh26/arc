package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehiclePlacedItemListResponse {

    private List<PlaceNo> place_no;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<PlaceNo> getPlace_no() {
        return place_no;
    }

    public void setPlace_no(List<PlaceNo> place_no) {
        this.place_no = place_no;
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

    public static class PlaceNo {
        private String placeVehicle_id;
        private String region_Name;
        private String division_Name;
        private String branch_code;
        private String branch_name;
        private String emp_code;
        private String emp_name;
        private String vehicle_type;
        private String date;
        private String noOfVehicle;
        private String etDestination;
        private String month;
        private String year;
        private String created_date;

        public String getPlaceVehicle_id() {
            return placeVehicle_id;
        }

        public void setPlaceVehicle_id(String placeVehicle_id) {
            this.placeVehicle_id = placeVehicle_id;
        }

        public String getRegion_Name() {
            return region_Name;
        }

        public void setRegion_Name(String region_Name) {
            this.region_Name = region_Name;
        }

        public String getDivision_Name() {
            return division_Name;
        }

        public void setDivision_Name(String division_Name) {
            this.division_Name = division_Name;
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

        public String getNoOfVehicle() {
            return noOfVehicle;
        }

        public void setNoOfVehicle(String noOfVehicle) {
            this.noOfVehicle = noOfVehicle;
        }

        public String getEtDestination() {
            return etDestination;
        }

        public void setEtDestination(String etDestination) {
            this.etDestination = etDestination;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }
    }
}
