package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingListResponse {

    private List<BookingList> booking_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<BookingList> getBooking_list() {
        return booking_list;
    }

    public void setBooking_list(List<BookingList> booking_list) {
        this.booking_list = booking_list;
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

    public static class BookingList {
        private String bkg_id;
        private String region_id;
        private String division_id;
        private String branch_code;
        private String date;
        private String previous_m_parcel;
        private String previous_m_total;
        private String current_m_parcel;
        private String current_m_total;
        private String diff_in_parcel;
        private String diff_in_total;
        private String create_date;

        public String getBkg_id() {
            return bkg_id;
        }

        public void setBkg_id(String bkg_id) {
            this.bkg_id = bkg_id;
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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPrevious_m_parcel() {
            return previous_m_parcel;
        }

        public void setPrevious_m_parcel(String previous_m_parcel) {
            this.previous_m_parcel = previous_m_parcel;
        }

        public String getPrevious_m_total() {
            return previous_m_total;
        }

        public void setPrevious_m_total(String previous_m_total) {
            this.previous_m_total = previous_m_total;
        }

        public String getCurrent_m_parcel() {
            return current_m_parcel;
        }

        public void setCurrent_m_parcel(String current_m_parcel) {
            this.current_m_parcel = current_m_parcel;
        }

        public String getCurrent_m_total() {
            return current_m_total;
        }

        public void setCurrent_m_total(String current_m_total) {
            this.current_m_total = current_m_total;
        }

        public String getDiff_in_parcel() {
            return diff_in_parcel;
        }

        public void setDiff_in_parcel(String diff_in_parcel) {
            this.diff_in_parcel = diff_in_parcel;
        }

        public String getDiff_in_total() {
            return diff_in_total;
        }

        public void setDiff_in_total(String diff_in_total) {
            this.diff_in_total = diff_in_total;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }
    }
}
