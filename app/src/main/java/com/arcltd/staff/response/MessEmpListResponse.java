package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessEmpListResponse {

    private List<MessEmpList> mess_Emp_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<MessEmpList> getMess_Emp_list() {
        return mess_Emp_list;
    }

    public void setMess_Emp_list(List<MessEmpList> mess_Emp_list) {
        this.mess_Emp_list = mess_Emp_list;
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

    public static class MessEmpList {
        private String me_id;
        private String mes_id;
        private String region_name;
        private String division_name;
        private String mess_name;
        private String emp_code;
        private String emp_name;
        private String emp_mob;
        private String emp_branch;
        private String room_no;
        private String updated_date;
        private String created_date;

        public String getMe_id() {
            return me_id;
        }

        public void setMe_id(String me_id) {
            this.me_id = me_id;
        }

        public String getMes_id() {
            return mes_id;
        }

        public void setMes_id(String mes_id) {
            this.mes_id = mes_id;
        }

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

        public String getMess_name() {
            return mess_name;
        }

        public void setMess_name(String mess_name) {
            this.mess_name = mess_name;
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

        public String getEmp_mob() {
            return emp_mob;
        }

        public void setEmp_mob(String emp_mob) {
            this.emp_mob = emp_mob;
        }

        public String getEmp_branch() {
            return emp_branch;
        }

        public void setEmp_branch(String emp_branch) {
            this.emp_branch = emp_branch;
        }

        public String getRoom_no() {
            return room_no;
        }

        public void setRoom_no(String room_no) {
            this.room_no = room_no;
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
