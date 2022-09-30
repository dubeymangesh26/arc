package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DivisionListResponse {


    private List<DivisionList> division_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<DivisionList> getDivision_list() {
        return division_list;
    }

    public void setDivision_list(List<DivisionList> division_list) {
        this.division_list = division_list;
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

    public static class DivisionList {
        private String div_id;
        private String region_id;
        private String division_name;

        public String getDiv_id() {
            return div_id;
        }

        public void setDiv_id(String div_id) {
            this.div_id = div_id;
        }

        public String getRegion_id() {
            return region_id;
        }

        public void setRegion_id(String region_id) {
            this.region_id = region_id;
        }

        public String getDivision_name() {
            return division_name;
        }

        public void setDivision_name(String division_name) {
            this.division_name = division_name;
        }
    }
}
