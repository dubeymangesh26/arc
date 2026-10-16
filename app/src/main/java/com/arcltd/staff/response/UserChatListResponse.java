package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserChatListResponse {

    private List<EmployeeList> employee_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<EmployeeList> getEmployee_list() {
        return employee_list;
    }

    public void setEmployee_list(List<EmployeeList> employee_list) {
        this.employee_list = employee_list;
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

    public static class EmployeeList {
        private String region_name;
        private String division_name;
        private String branch_code;
        private String branch_name;
        private String emp_code;
        private String name;
        private String design;
        private String email;
        private String profilepic;
        private String last_seen;

        public String getRegion_name() {
            return region_name;
        }

        public void setRegion_name(String region_name) {
            this.region_name = region_name;
        }

        public String getDivision_name() {
            return division_name;
        }

        public void setDivision_name(String division_name) {
            this.division_name = division_name;
        }

        public String getBranch_code() {
            return branch_code;
        }

        public void setBranch_code(String branch_code) {
            this.branch_code = branch_code;
        }

        public String getBranch_name() {
            return branch_name;
        }

        public void setBranch_name(String branch_name) {
            this.branch_name = branch_name;
        }

        public String getEmp_code() {
            return emp_code;
        }

        public void setEmp_code(String emp_code) {
            this.emp_code = emp_code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesign() {
            return design;
        }

        public void setDesign(String design) {
            this.design = design;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getProfilepic() {
            return profilepic;
        }

        public void setProfilepic(String profilepic) {
            this.profilepic = profilepic;
        }

        public String getLast_seen() {
            return last_seen;
        }

        public void setLast_seen(String last_seen) {
            this.last_seen = last_seen;
        }
    }
}
