package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RateCardListResponse {

    private List<RatecardList> ratecard_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<RatecardList> getRatecard_list() {
        return ratecard_list;
    }

    public void setRatecard_list(List<RatecardList> ratecard_list) {
        this.ratecard_list = ratecard_list;
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

    public static class RatecardList {
        private String rate_id;
        private String branch_code;
        private String main_DestCode;
        private String dest_code;
        private String dest_name;
        private String booking_rate;
        private String delivery_rate;
        private String transit_root;
        private String transit_days;
        private String updated_date;
        private String created_date;

        public String getRate_id() {
            return rate_id;
        }

        public void setRate_id(String rate_id) {
            this.rate_id = rate_id;
        }

        public String getBranch_code() {
            return branch_code;
        }

        public void setBranch_code(String branch_code) {
            this.branch_code = branch_code;
        }

        public String getMain_DestCode() {
            return main_DestCode;
        }

        public void setMain_DestCode(String main_DestCode) {
            this.main_DestCode = main_DestCode;
        }

        public String getDest_code() {
            return dest_code;
        }

        public void setDest_code(String dest_code) {
            this.dest_code = dest_code;
        }

        public String getDest_name() {
            return dest_name;
        }

        public void setDest_name(String dest_name) {
            this.dest_name = dest_name;
        }

        public String getBooking_rate() {
            return booking_rate;
        }

        public void setBooking_rate(String booking_rate) {
            this.booking_rate = booking_rate;
        }

        public String getDelivery_rate() {
            return delivery_rate;
        }

        public void setDelivery_rate(String delivery_rate) {
            this.delivery_rate = delivery_rate;
        }

        public String getTransit_root() {
            return transit_root;
        }

        public void setTransit_root(String transit_root) {
            this.transit_root = transit_root;
        }

        public String getTransit_days() {
            return transit_days;
        }

        public void setTransit_days(String transit_days) {
            this.transit_days = transit_days;
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
