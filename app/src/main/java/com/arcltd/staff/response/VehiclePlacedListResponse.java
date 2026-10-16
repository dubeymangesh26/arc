package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehiclePlacedListResponse {

    private List<PlaceNo> place_no;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;
    private List<DivplaceList> divplace_list;

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

    public List<DivplaceList> getDivplace_list() {
        return divplace_list;
    }

    public void setDivplace_list(List<DivplaceList> divplace_list) {
        this.divplace_list = divplace_list;
    }

    public static class PlaceNo {
        private String division_Name;
        @SerializedName("No_of_Vehicle")
        private String no_of_Vehicle;

        public String getDivision_Name() {
            return division_Name;
        }

        public void setDivision_Name(String division_Name) {
            this.division_Name = division_Name;
        }

        public String getNo_of_Vehicle() {
            return no_of_Vehicle;
        }

        public void setNo_of_Vehicle(String no_of_Vehicle) {
            this.no_of_Vehicle = no_of_Vehicle;
        }
    }

    public static class DivplaceList {
        private String branch_code;
        private String branch_name;
        private String division_Name;
        @SerializedName("No_of_Vehicle")
        private String no_of_Vehicle;

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

        public String getDivision_Name() {
            return division_Name;
        }

        public void setDivision_Name(String division_Name) {
            this.division_Name = division_Name;
        }

        public String getNo_of_Vehicle() {
            return no_of_Vehicle;
        }

        public void setNo_of_Vehicle(String no_of_Vehicle) {
            this.no_of_Vehicle = no_of_Vehicle;
        }
    }
}
