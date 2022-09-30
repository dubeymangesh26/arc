package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmpRemarkListResponse {

    private List<EmployeeRemarkList> employee_remark_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<EmployeeRemarkList> getEmployee_remark_list() {
        return employee_remark_list;
    }

    public void setEmployee_remark_list(List<EmployeeRemarkList> employee_remark_list) {
        this.employee_remark_list = employee_remark_list;
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

    public static class EmployeeRemarkList {
        private String r_id;
        private String branch_code;
        private String emp_code;
        private String remark;
        private String created_date;

        public String getR_id() {
            return r_id;
        }

        public void setR_id(String r_id) {
            this.r_id = r_id;
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
