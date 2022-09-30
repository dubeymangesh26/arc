package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultListResponse {

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
        private String result_id;
        private String result_name;
        private String result_link;
        private String created_date;

        public String getResult_id() {
            return result_id;
        }

        public void setResult_id(String result_id) {
            this.result_id = result_id;
        }

        public String getResult_name() {
            return result_name;
        }

        public void setResult_name(String result_name) {
            this.result_name = result_name;
        }

        public String getResult_link() {
            return result_link;
        }

        public void setResult_link(String result_link) {
            this.result_link = result_link;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }
    }
}
