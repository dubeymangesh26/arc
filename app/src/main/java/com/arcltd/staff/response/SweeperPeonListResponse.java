package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SweeperPeonListResponse {

    private List<SweeperPeonList> sweeper_peon_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<SweeperPeonList> getSweeper_peon_list() {
        return sweeper_peon_list;
    }

    public void setSweeper_peon_list(List<SweeperPeonList> sweeper_peon_list) {
        this.sweeper_peon_list = sweeper_peon_list;
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

    public static class SweeperPeonList {
        private String sp_id;
        private String branch_code;
        private String emp_code;
        private String emp_name;
        private String emp_salary;
        private String sweeper_peon;
        private String updated_date;
        private String created_date;

        public String getSp_id() {
            return sp_id;
        }

        public void setSp_id(String sp_id) {
            this.sp_id = sp_id;
        }

        public String getBranch_code() {
            return branch_code;
        }

        public void setBranch_code(String branch_code) {
            this.branch_code = branch_code;
        }

        public String getEmp_code() {
            return emp_code;
        }

        public void setEmp_code(String emp_code) {
            this.emp_code = emp_code;
        }

        public String getEmp_name() {
            return emp_name;
        }

        public void setEmp_name(String emp_name) {
            this.emp_name = emp_name;
        }

        public String getEmp_salary() {
            return emp_salary;
        }

        public void setEmp_salary(String emp_salary) {
            this.emp_salary = emp_salary;
        }

        public String getSweeper_peon() {
            return sweeper_peon;
        }

        public void setSweeper_peon(String sweeper_peon) {
            this.sweeper_peon = sweeper_peon;
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
