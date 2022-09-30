package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InchargeListResponse {

    private List<InchargeList> incharge_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<InchargeList> getIncharge_list() {
        return incharge_list;
    }

    public void setIncharge_list(List<InchargeList> incharge_list) {
        this.incharge_list = incharge_list;
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

    public static class InchargeList {
        private String ins_id;
        private String name;
        private String link;
        private String created_date;

        public String getIns_id() {
            return ins_id;
        }

        public void setIns_id(String ins_id) {
            this.ins_id = ins_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }
    }
}
