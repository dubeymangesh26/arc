package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessListResponse {

    private List<MessList> mess_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<MessList> getMess_list() {
        return mess_list;
    }

    public void setMess_list(List<MessList> mess_list) {
        this.mess_list = mess_list;
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

    public static class MessList {
        private String mes_id;
        private String div_name;
        private String reg_id;
        private String under_branch;
        private String mess_name;
        private String mess_address;
        private String own_rented;
        private String rent_amount;
        private String cook_avl;
        private String cook_salary;
        private String no_of_room;
        private String current_bed;
        private String capacity_bed;
        private String refrigerator;
        private String tv;
        private String description;
        private String no_emp;
        private String update_time;

        public String getMes_id() {
            return mes_id;
        }

        public void setMes_id(String mes_id) {
            this.mes_id = mes_id;
        }

        public String getDiv_name() {
            return div_name;
        }

        public void setDiv_name(String div_name) {
            this.div_name = div_name;
        }

        public String getReg_id() {
            return reg_id;
        }

        public void setReg_id(String reg_id) {
            this.reg_id = reg_id;
        }

        public String getUnder_branch() {
            return under_branch;
        }

        public void setUnder_branch(String under_branch) {
            this.under_branch = under_branch;
        }

        public String getMess_name() {
            return mess_name;
        }

        public void setMess_name(String mess_name) {
            this.mess_name = mess_name;
        }

        public String getMess_address() {
            return mess_address;
        }

        public void setMess_address(String mess_address) {
            this.mess_address = mess_address;
        }

        public String getOwn_rented() {
            return own_rented;
        }

        public void setOwn_rented(String own_rented) {
            this.own_rented = own_rented;
        }

        public String getRent_amount() {
            return rent_amount;
        }

        public void setRent_amount(String rent_amount) {
            this.rent_amount = rent_amount;
        }

        public String getCook_avl() {
            return cook_avl;
        }

        public void setCook_avl(String cook_avl) {
            this.cook_avl = cook_avl;
        }

        public String getCook_salary() {
            return cook_salary;
        }

        public void setCook_salary(String cook_salary) {
            this.cook_salary = cook_salary;
        }

        public String getNo_of_room() {
            return no_of_room;
        }

        public void setNo_of_room(String no_of_room) {
            this.no_of_room = no_of_room;
        }

        public String getCurrent_bed() {
            return current_bed;
        }

        public void setCurrent_bed(String current_bed) {
            this.current_bed = current_bed;
        }

        public String getCapacity_bed() {
            return capacity_bed;
        }

        public void setCapacity_bed(String capacity_bed) {
            this.capacity_bed = capacity_bed;
        }

        public String getRefrigerator() {
            return refrigerator;
        }

        public void setRefrigerator(String refrigerator) {
            this.refrigerator = refrigerator;
        }

        public String getTv() {
            return tv;
        }

        public void setTv(String tv) {
            this.tv = tv;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getNo_emp() {
            return no_emp;
        }

        public void setNo_emp(String no_emp) {
            this.no_emp = no_emp;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }
    }
}
