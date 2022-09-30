package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TargetResponseResponse {


    private List<BookingTarget> booking_target;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;
    private TeeElectricExp tee_electric_exp;
    private List<BookingDetails> booking_details;

    public List<BookingTarget> getBooking_target() {
        return booking_target;
    }

    public void setBooking_target(List<BookingTarget> booking_target) {
        this.booking_target = booking_target;
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

    public TeeElectricExp getTee_electric_exp() {
        return tee_electric_exp;
    }

    public void setTee_electric_exp(TeeElectricExp tee_electric_exp) {
        this.tee_electric_exp = tee_electric_exp;
    }

    public List<BookingDetails> getBooking_details() {
        return booking_details;
    }

    public void setBooking_details(List<BookingDetails> booking_details) {
        this.booking_details = booking_details;
    }

    public static class TeeElectricExp {
        private String t_id;
        private String branch_code;
        private String tea;
        private String electricity;
        private String updated_date;
        private String created_date;

        public String getT_id() {
            return t_id;
        }

        public void setT_id(String t_id) {
            this.t_id = t_id;
        }

        public String getBranch_code() {
            return branch_code;
        }

        public void setBranch_code(String branch_code) {
            this.branch_code = branch_code;
        }

        public String getTea() {
            return tea;
        }

        public void setTea(String tea) {
            this.tea = tea;
        }

        public String getElectricity() {
            return electricity;
        }

        public void setElectricity(String electricity) {
            this.electricity = electricity;
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

    public static class BookingTarget {
        private String id;
        private String region_id;
        private String division_id;
        private String branch_code;
        private String branch_name;
        private String parcel;
        private String part;
        private String ftl;
        private String total;
        private String updated_date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getParcel() {
            return parcel;
        }

        public void setParcel(String parcel) {
            this.parcel = parcel;
        }

        public String getPart() {
            return part;
        }

        public void setPart(String part) {
            this.part = part;
        }

        public String getFtl() {
            return ftl;
        }

        public void setFtl(String ftl) {
            this.ftl = ftl;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getUpdated_date() {
            return updated_date;
        }

        public void setUpdated_date(String updated_date) {
            this.updated_date = updated_date;
        }
    }

    public static class BookingDetails {
        private String b_id;
        private String branch_code;
        private String year;
        private String month;
        private String date;
        private String parcel;
        private String part;
        private String ftl;
        private String total;
        private String updated_date;
        private String created_date;

        public String getB_id() {
            return b_id;
        }

        public void setB_id(String b_id) {
            this.b_id = b_id;
        }

        public String getBranch_code() {
            return branch_code;
        }

        public void setBranch_code(String branch_code) {
            this.branch_code = branch_code;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getParcel() {
            return parcel;
        }

        public void setParcel(String parcel) {
            this.parcel = parcel;
        }

        public String getPart() {
            return part;
        }

        public void setPart(String part) {
            this.part = part;
        }

        public String getFtl() {
            return ftl;
        }

        public void setFtl(String ftl) {
            this.ftl = ftl;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
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
