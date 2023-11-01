package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HolidaysResponse {

    @SerializedName("holidys_list")
    private List<HolidysList> holidysList;
    @SerializedName("ResponseCode")
    private String responseCode;
    @SerializedName("message")
    private String message;

    public List<HolidysList> getHolidysList() {
        return holidysList;
    }

    public void setHolidysList(List<HolidysList> holidysList) {
        this.holidysList = holidysList;
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

    public static class HolidysList {
        @SerializedName("h_id")
        private String hId;
        @SerializedName("year")
        private String year;
        @SerializedName("festval")
        private String festval;
        @SerializedName("fst_date")
        private String fstDate;
        @SerializedName("updated_date")
        private String updatedDate;
        @SerializedName("created_date")
        private String createdDate;

        public String getHId() {
            return hId;
        }

        public void setHId(String hId) {
            this.hId = hId;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getFestval() {
            return festval;
        }

        public void setFestval(String festval) {
            this.festval = festval;
        }

        public String getFstDate() {
            return fstDate;
        }

        public void setFstDate(String fstDate) {
            this.fstDate = fstDate;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }
    }
}
