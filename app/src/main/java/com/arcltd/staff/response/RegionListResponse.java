package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegionListResponse {


    private List<RegionList> region_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<RegionList> getRegion_list() {
        return region_list;
    }

    public void setRegion_list(List<RegionList> region_list) {
        this.region_list = region_list;
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

    public static class RegionList {
        private String region_id;
        private String region_name;

        public String getRegion_id() {
            return region_id;
        }

        public void setRegion_id(String region_id) {
            this.region_id = region_id;
        }

        public String getRegion_name() {
            return region_name;
        }

        public void setRegion_name(String region_name) {
            this.region_name = region_name;
        }
    }
}
