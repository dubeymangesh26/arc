package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RemarkListResponse {


    private List<RemarkList> remark_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<RemarkList> getRemark_list() {
        return remark_list;
    }

    public void setRemark_list(List<RemarkList> remark_list) {
        this.remark_list = remark_list;
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

    public static class RemarkList {
        private String re_id;
        private String f_id;
        private String cust_id;
        private String branch_code;
        private String remark;
        private String created_date;

        public String getRe_id() {
            return re_id;
        }

        public void setRe_id(String re_id) {
            this.re_id = re_id;
        }

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

        public String getBranch_code() {
            return branch_code;
        }

        public void setBranch_code(String branch_code) {
            this.branch_code = branch_code;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }
    }
}
