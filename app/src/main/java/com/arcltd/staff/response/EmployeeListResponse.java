package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmployeeListResponse {

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
        private String emp_id;
        private String region_name;
        private String region_id;
        private String division_name;
        private String division_id;
        private String branch_code;
        private String branch_name;
        private String emp_act_retire;
        private String emp_code;
        private String emp_name;
        private String emp_eqalification;
        private String emp_joining_date;
        private String emp_desig;
        private String ac_branch;
        private String pf_number;
        private String sal_basic;
        private String sal_hra;

        private String sal_tga;
        private String sal_fleet;
        private String emp_salary;
        private String status;
        private String dat_of_birth;
        private String retirement_date;
        private String uan_no;
        private String updated_date;
        private String created_date;
        private String esi_no;
        private String token;
        private String email;
        private String emp_mob;
        private String profilepic;


        private String gread;

        public String getPf_number() {
            return pf_number;
        }


        public String getSal_tga() {
            return sal_tga;
        }

        public void setSal_tga(String sal_tga) {
            this.sal_tga = sal_tga;
        }


        public void setPf_number(String pf_number) {
            this.pf_number = pf_number;
        }

        public String getGread() {
            return gread;
        }

        public void setGread(String gread) {
            this.gread = gread;
        }

        public String getAc_branch() {
            return ac_branch;
        }

        public void setAc_branch(String ac_branch) {
            this.ac_branch = ac_branch;
        }

        public String getSal_basic() {
            return sal_basic;
        }

        public void setSal_basic(String sal_basic) {
            this.sal_basic = sal_basic;
        }

        public String getSal_hra() {
            return sal_hra;
        }

        public void setSal_hra(String sal_hra) {
            this.sal_hra = sal_hra;
        }

        public String getSal_fleet() {
            return sal_fleet;
        }

        public void setSal_fleet(String sal_fleet) {
            this.sal_fleet = sal_fleet;
        }


        public String getProfilepic() {
            return profilepic;
        }

        public void setProfilepic(String profilepic) {
            this.profilepic = profilepic;
        }



        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmp_mob() {
            return emp_mob;
        }

        public void setEmp_mob(String emp_mob) {
            this.emp_mob = emp_mob;
        }



        public String getEmp_id() {
            return emp_id;
        }

        public void setEmp_id(String emp_id) {
            this.emp_id = emp_id;
        }

        public String getRegion_name() {
            return region_name;
        }

        public void setRegion_name(String region_name) {
            this.region_name = region_name;
        }

        public String getRegion_id() {
            return region_id;
        }

        public void setRegion_id(String region_id) {
            this.region_id = region_id;
        }

        public String getDivision_name() {
            return division_name;
        }

        public void setDivision_name(String division_name) {
            this.division_name = division_name;
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

        public String getBranch_name() {
            return branch_name;
        }

        public void setBranch_name(String branch_name) {
            this.branch_name = branch_name;
        }

        public String getEmp_act_retire() {
            return emp_act_retire;
        }

        public void setEmp_act_retire(String emp_act_retire) {
            this.emp_act_retire = emp_act_retire;
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

        public String getEmp_eqalification() {
            return emp_eqalification;
        }

        public void setEmp_eqalification(String emp_eqalification) {
            this.emp_eqalification = emp_eqalification;
        }

        public String getEmp_joining_date() {
            return emp_joining_date;
        }

        public void setEmp_joining_date(String emp_joining_date) {
            this.emp_joining_date = emp_joining_date;
        }

        public String getEmp_desig() {
            return emp_desig;
        }

        public void setEmp_desig(String emp_desig) {
            this.emp_desig = emp_desig;
        }

        public String getEmp_salary() {
            return emp_salary;
        }

        public void setEmp_salary(String emp_salary) {
            this.emp_salary = emp_salary;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDat_of_birth() {
            return dat_of_birth;
        }

        public void setDat_of_birth(String dat_of_birth) {
            this.dat_of_birth = dat_of_birth;
        }

        public String getRetirement_date() {
            return retirement_date;
        }

        public void setRetirement_date(String retirement_date) {
            this.retirement_date = retirement_date;
        }

        public String getUan_no() {
            return uan_no;
        }

        public void setUan_no(String uan_no) {
            this.uan_no = uan_no;
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

        public String getEsi_no() {
            return esi_no;
        }

        public void setEsi_no(String esi_no) {
            this.esi_no = esi_no;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
