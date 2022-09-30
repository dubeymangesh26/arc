package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmpMessageListResponse {

    private List<EmployeeMessage> employee_message;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<EmployeeMessage> getEmployee_message() {
        return employee_message;
    }

    public void setEmployee_message(List<EmployeeMessage> employee_message) {
        this.employee_message = employee_message;
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

    public static class EmployeeMessage {
        private String m_id;
        private String emp_code;
        private String emp_name;
        private String emp_branch;
        private String message;
        private String read_unread;
        private String read_date;
        private String created_date;

        public String getM_id() {
            return m_id;
        }

        public void setM_id(String m_id) {
            this.m_id = m_id;
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

        public String getEmp_branch() {
            return emp_branch;
        }

        public void setEmp_branch(String emp_branch) {
            this.emp_branch = emp_branch;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getRead_unread() {
            return read_unread;
        }

        public void setRead_unread(String read_unread) {
            this.read_unread = read_unread;
        }

        public String getRead_date() {
            return read_date;
        }

        public void setRead_date(String read_date) {
            this.read_date = read_date;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }
    }
}
