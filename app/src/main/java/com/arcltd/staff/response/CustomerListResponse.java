package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerListResponse {


    @SerializedName("Customer_list")
    private List<CustomerList> customer_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<CustomerList> getCustomer_list() {
        return customer_list;
    }

    public void setCustomer_list(List<CustomerList> customer_list) {
        this.customer_list = customer_list;
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

    public static class CustomerList {
        private String cust_id;
        private String division_id;
        private String branch_code;
        private String m_name;
        private String company_name;
        private String cust_name;
        private String cust_mobile;
        private String cust_design;
        private String cust_email;
        private String noof_visit;
        private String cust_type;
        private String updated_date;
        private String created_date;

        public String getCust_id() {
            return cust_id;
        }

        public void setCust_id(String cust_id) {
            this.cust_id = cust_id;
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

        public String getM_name() {
            return m_name;
        }

        public void setM_name(String m_name) {
            this.m_name = m_name;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getCust_name() {
            return cust_name;
        }

        public void setCust_name(String cust_name) {
            this.cust_name = cust_name;
        }

        public String getCust_mobile() {
            return cust_mobile;
        }

        public void setCust_mobile(String cust_mobile) {
            this.cust_mobile = cust_mobile;
        }

        public String getCust_design() {
            return cust_design;
        }

        public void setCust_design(String cust_design) {
            this.cust_design = cust_design;
        }

        public String getCust_email() {
            return cust_email;
        }

        public void setCust_email(String cust_email) {
            this.cust_email = cust_email;
        }

        public String getNoof_visit() {
            return noof_visit;
        }

        public void setNoof_visit(String noof_visit) {
            this.noof_visit = noof_visit;
        }

        public String getCust_type() {
            return cust_type;
        }

        public void setCust_type(String cust_type) {
            this.cust_type = cust_type;
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
