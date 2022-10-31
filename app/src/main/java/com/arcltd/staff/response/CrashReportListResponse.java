package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CrashReportListResponse {

    private List<CrashList> crash_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<CrashList> getCrash_list() {
        return crash_list;
    }

    public void setCrash_list(List<CrashList> crash_list) {
        this.crash_list = crash_list;
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

    public static class CrashList {
        private String crash_id;
        private String type;
        private String report;
        private String created_date;

        public String getCrash_id() {
            return crash_id;
        }

        public void setCrash_id(String crash_id) {
            this.crash_id = crash_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getReport() {
            return report;
        }

        public void setReport(String report) {
            this.report = report;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }
    }
}
