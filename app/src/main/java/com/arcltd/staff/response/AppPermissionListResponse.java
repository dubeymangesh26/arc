package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppPermissionListResponse {

    private List<UserList> user_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<UserList> getUser_list() {
        return user_list;
    }

    public void setUser_list(List<UserList> user_list) {
        this.user_list = user_list;
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

    public static class UserList {
        private String per_id;
        private String region_id;
        private String region_name;
        private String division_id;
        private String division_name;
        private String branch_code;
        private String emp_code;
        private String emp_name;
        private String div_branches;
        private String div_mess;
        private String send_profile;
        private String emp_list;
        private String emp_birthday;
        private String retire_list;
        private String motercy_list;
        private String inciden_list;
        private String weightmc_list;
        private String conveyance;
        private String lanlord_details;
        private String admin_exp_list;
        private String sendEmp_message;
        private String add_mess;
        private String add_emp;
        private String add_convence_mobExp;
        private String add_sweeperPeon;
        private String transfer_emp;
        private String active_deactiveEmp;
        private String profilepic;
        private String updated_date;
        private String created_date;

        public String getPer_id() {
            return per_id;
        }

        public void setPer_id(String per_id) {
            this.per_id = per_id;
        }

        public String getRegion_id() {
            return region_id;
        }

        public void setRegion_id(String region_id) {
            this.region_id = region_id;
        }

        public String getRegion_name() {
            return region_name;
        }

        public void setRegion_name(String region_name) {
            this.region_name = region_name;
        }

        public String getDivision_id() {
            return division_id;
        }

        public void setDivision_id(String division_id) {
            this.division_id = division_id;
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

        public String getDiv_branches() {
            return div_branches;
        }

        public void setDiv_branches(String div_branches) {
            this.div_branches = div_branches;
        }

        public String getDiv_mess() {
            return div_mess;
        }

        public void setDiv_mess(String div_mess) {
            this.div_mess = div_mess;
        }

        public String getSend_profile() {
            return send_profile;
        }

        public void setSend_profile(String send_profile) {
            this.send_profile = send_profile;
        }

        public String getEmp_list() {
            return emp_list;
        }

        public void setEmp_list(String emp_list) {
            this.emp_list = emp_list;
        }

        public String getEmp_birthday() {
            return emp_birthday;
        }

        public void setEmp_birthday(String emp_birthday) {
            this.emp_birthday = emp_birthday;
        }

        public String getRetire_list() {
            return retire_list;
        }

        public void setRetire_list(String retire_list) {
            this.retire_list = retire_list;
        }

        public String getMotercy_list() {
            return motercy_list;
        }

        public void setMotercy_list(String motercy_list) {
            this.motercy_list = motercy_list;
        }

        public String getInciden_list() {
            return inciden_list;
        }

        public void setInciden_list(String inciden_list) {
            this.inciden_list = inciden_list;
        }

        public String getWeightmc_list() {
            return weightmc_list;
        }

        public void setWeightmc_list(String weightmc_list) {
            this.weightmc_list = weightmc_list;
        }

        public String getConveyance() {
            return conveyance;
        }

        public void setConveyance(String conveyance) {
            this.conveyance = conveyance;
        }

        public String getLanlord_details() {
            return lanlord_details;
        }

        public void setLanlord_details(String lanlord_details) {
            this.lanlord_details = lanlord_details;
        }

        public String getAdmin_exp_list() {
            return admin_exp_list;
        }

        public void setAdmin_exp_list(String admin_exp_list) {
            this.admin_exp_list = admin_exp_list;
        }

        public String getSendEmp_message() {
            return sendEmp_message;
        }

        public void setSendEmp_message(String sendEmp_message) {
            this.sendEmp_message = sendEmp_message;
        }

        public String getAdd_mess() {
            return add_mess;
        }

        public void setAdd_mess(String add_mess) {
            this.add_mess = add_mess;
        }

        public String getAdd_emp() {
            return add_emp;
        }

        public void setAdd_emp(String add_emp) {
            this.add_emp = add_emp;
        }

        public String getAdd_convence_mobExp() {
            return add_convence_mobExp;
        }

        public void setAdd_convence_mobExp(String add_convence_mobExp) {
            this.add_convence_mobExp = add_convence_mobExp;
        }

        public String getAdd_sweeperPeon() {
            return add_sweeperPeon;
        }

        public void setAdd_sweeperPeon(String add_sweeperPeon) {
            this.add_sweeperPeon = add_sweeperPeon;
        }

        public String getTransfer_emp() {
            return transfer_emp;
        }

        public void setTransfer_emp(String transfer_emp) {
            this.transfer_emp = transfer_emp;
        }

        public String getActive_deactiveEmp() {
            return active_deactiveEmp;
        }

        public void setActive_deactiveEmp(String active_deactiveEmp) {
            this.active_deactiveEmp = active_deactiveEmp;
        }

        public String getProfilepic() {
            return profilepic;
        }

        public void setProfilepic(String profilepic) {
            this.profilepic = profilepic;
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
