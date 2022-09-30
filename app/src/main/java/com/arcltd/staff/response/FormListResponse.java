package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FormListResponse {


    private List<FormList> form_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<FormList> getForm_list() {
        return form_list;
    }

    public void setForm_list(List<FormList> form_list) {
        this.form_list = form_list;
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

    public static class FormList {
        private String form_id;
        private String form_name;
        private String form_type;
        private String last_date;
        private String age;
        private String gen_fees;
        private String obc_fees;
        private String st_sc_fees;
        private String service_charge;
        private String state;
        private String form_link;
        private String created_date;
        private String for_gender;
        private String female_fee;
        private String note;

        public String getForm_id() {
            return form_id;
        }

        public void setForm_id(String form_id) {
            this.form_id = form_id;
        }

        public String getForm_name() {
            return form_name;
        }

        public void setForm_name(String form_name) {
            this.form_name = form_name;
        }

        public String getForm_type() {
            return form_type;
        }

        public void setForm_type(String form_type) {
            this.form_type = form_type;
        }

        public String getLast_date() {
            return last_date;
        }

        public void setLast_date(String last_date) {
            this.last_date = last_date;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getGen_fees() {
            return gen_fees;
        }

        public void setGen_fees(String gen_fees) {
            this.gen_fees = gen_fees;
        }

        public String getObc_fees() {
            return obc_fees;
        }

        public void setObc_fees(String obc_fees) {
            this.obc_fees = obc_fees;
        }

        public String getSt_sc_fees() {
            return st_sc_fees;
        }

        public void setSt_sc_fees(String st_sc_fees) {
            this.st_sc_fees = st_sc_fees;
        }

        public String getService_charge() {
            return service_charge;
        }

        public void setService_charge(String service_charge) {
            this.service_charge = service_charge;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getForm_link() {
            return form_link;
        }

        public void setForm_link(String form_link) {
            this.form_link = form_link;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getFemale_fee() {
            return female_fee;
        }

        public void setFemale_fee(String female_fee) {
            this.female_fee = female_fee;
        }

        public String getFor_gender() {
            return for_gender;
        }

        public void setFor_gender(String for_gender) {
            this.for_gender = for_gender;
        }
    }
}
