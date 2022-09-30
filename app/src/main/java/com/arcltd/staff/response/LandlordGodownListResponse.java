package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LandlordGodownListResponse {


    private List<LandlordGodownList> landlord_godown_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<LandlordGodownList> getLandlord_godown_list() {
        return landlord_godown_list;
    }

    public void setLandlord_godown_list(List<LandlordGodownList> landlord_godown_list) {
        this.landlord_godown_list = landlord_godown_list;
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

    public static class LandlordGodownList {
        private String landlord_id;
        private String branch_code;
        private String cost_code;
        private String location_address;
        private String landlord_name;
        private String landlord_address;
        private String pan_no;
        private String off_gdn_residence;
        private String period_from;
        private String period_to;
        private String rent_amount;
        private String ofgdn_area;
        private String open_area;
        private String updated_date;

        public String getLandlord_id() {
            return landlord_id;
        }

        public void setLandlord_id(String landlord_id) {
            this.landlord_id = landlord_id;
        }

        public String getBranch_code() {
            return branch_code;
        }

        public void setBranch_code(String branch_code) {
            this.branch_code = branch_code;
        }

        public String getCost_code() {
            return cost_code;
        }

        public void setCost_code(String cost_code) {
            this.cost_code = cost_code;
        }

        public String getLocation_address() {
            return location_address;
        }

        public void setLocation_address(String location_address) {
            this.location_address = location_address;
        }

        public String getLandlord_name() {
            return landlord_name;
        }

        public void setLandlord_name(String landlord_name) {
            this.landlord_name = landlord_name;
        }

        public String getLandlord_address() {
            return landlord_address;
        }

        public void setLandlord_address(String landlord_address) {
            this.landlord_address = landlord_address;
        }

        public String getPan_no() {
            return pan_no;
        }

        public void setPan_no(String pan_no) {
            this.pan_no = pan_no;
        }

        public String getOff_gdn_residence() {
            return off_gdn_residence;
        }

        public void setOff_gdn_residence(String off_gdn_residence) {
            this.off_gdn_residence = off_gdn_residence;
        }

        public String getPeriod_from() {
            return period_from;
        }

        public void setPeriod_from(String period_from) {
            this.period_from = period_from;
        }

        public String getPeriod_to() {
            return period_to;
        }

        public void setPeriod_to(String period_to) {
            this.period_to = period_to;
        }

        public String getRent_amount() {
            return rent_amount;
        }

        public void setRent_amount(String rent_amount) {
            this.rent_amount = rent_amount;
        }

        public String getOfgdn_area() {
            return ofgdn_area;
        }

        public void setOfgdn_area(String ofgdn_area) {
            this.ofgdn_area = ofgdn_area;
        }

        public String getOpen_area() {
            return open_area;
        }

        public void setOpen_area(String open_area) {
            this.open_area = open_area;
        }

        public String getUpdated_date() {
            return updated_date;
        }

        public void setUpdated_date(String updated_date) {
            this.updated_date = updated_date;
        }
    }
}
