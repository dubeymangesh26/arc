package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedbackListResponse {

    private List<FeedbackList> feedback_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<FeedbackList> getFeedback_list() {
        return feedback_list;
    }

    public void setFeedback_list(List<FeedbackList> feedback_list) {
        this.feedback_list = feedback_list;
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

    public static class FeedbackList {
        private String f_id;
        private String cust_id;
        private String fed_date;
        private String feedback;
        private String created_date;

        public String getF_id() {
            return f_id;
        }

        public void setF_id(String f_id) {
            this.f_id = f_id;
        }

        public String getCust_id() {
            return cust_id;
        }

        public void setCust_id(String cust_id) {
            this.cust_id = cust_id;
        }

        public String getFed_date() {
            return fed_date;
        }

        public void setFed_date(String fed_date) {
            this.fed_date = fed_date;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }
    }
}
