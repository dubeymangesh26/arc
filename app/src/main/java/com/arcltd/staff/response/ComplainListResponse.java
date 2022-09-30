package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ComplainListResponse {

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
        private String cmp_id;
        private String user_id;
        private String subject;
        private String complain;
        private String reply;
        private String status;
        private String created_time;

        public String getCmp_id() {
            return cmp_id;
        }

        public void setCmp_id(String cmp_id) {
            this.cmp_id = cmp_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getComplain() {
            return complain;
        }

        public void setComplain(String complain) {
            this.complain = complain;
        }

        public String getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreated_time() {
            return created_time;
        }

        public void setCreated_time(String created_time) {
            this.created_time = created_time;
        }
    }
}
