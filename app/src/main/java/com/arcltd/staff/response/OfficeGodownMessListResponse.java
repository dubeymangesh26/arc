package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OfficeGodownMessListResponse {


    private List<OfficeMessRentList> office_mess_rent_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<OfficeMessRentList> getOffice_mess_rent_list() {
        return office_mess_rent_list;
    }

    public void setOffice_mess_rent_list(List<OfficeMessRentList> office_mess_rent_list) {
        this.office_mess_rent_list = office_mess_rent_list;
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

    public static class OfficeMessRentList {
        private String mor_id;
        private String region_id;
        private String region_name;
        private String division_id;
        private String division_name;
        private String ac_branch;
        private String sub_branch;
        private String cost_code;
        private String location_address;
        private String landlord_name;
        private String landlord_address;
        private String pan_no;
        private String off_gdn_residence;
        private String period_from;
        private String period_to;
        private String rent_amount;
        private String ofgdn_area;
        private String open_area;
        private String miscellaneous;
        private String moter_bike;
        private String weight_machine;
        private String mobile_exp;
        private String electricity;
        private String emp;
        private String sweeper;
        private String peon;
        private String tea;
        private String updated_date;
        private String id;
        private String branch_code;
        private String branch_name;
        private String parcel;
        private String part;
        private String total;

        public String getMor_id() {
            return mor_id;
        }

        public void setMor_id(String mor_id) {
            this.mor_id = mor_id;
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

        public String getAc_branch() {
            return ac_branch;
        }

        public void setAc_branch(String ac_branch) {
            this.ac_branch = ac_branch;
        }

        public String getSub_branch() {
            return sub_branch;
        }

        public void setSub_branch(String sub_branch) {
            this.sub_branch = sub_branch;
        }

        public String getCost_code() {
            return cost_code;
        }

        public void setCost_code(String cost_code) {
            this.cost_code = cost_code;
        }

        public String getLocation_address() {
            return location_address;
        }

        public void setLocation_address(String location_address) {
            this.location_address = location_address;
        }

        public String getLandlord_name() {
            return landlord_name;
        }

        public void setLandlord_name(String landlord_name) {
            this.landlord_name = landlord_name;
        }

        public String getLandlord_address() {
            return landlord_address;
        }

        public void setLandlord_address(String landlord_address) {
            this.landlord_address = landlord_address;
        }

        public String getPan_no() {
            return pan_no;
        }

        public void setPan_no(String pan_no) {
            this.pan_no = pan_no;
        }

        public String getOff_gdn_residence() {
            return off_gdn_residence;
        }

        public void setOff_gdn_residence(String off_gdn_residence) {
            this.off_gdn_residence = off_gdn_residence;
        }

        public String getPeriod_from() {
            return period_from;
        }

        public void setPeriod_from(String period_from) {
            this.period_from = period_from;
        }

        public String getPeriod_to() {
            return period_to;
        }

        public void setPeriod_to(String period_to) {
            this.period_to = period_to;
        }

        public String getRent_amount() {
            return rent_amount;
        }

        public void setRent_amount(String rent_amount) {
            this.rent_amount = rent_amount;
        }

        public String getOfgdn_area() {
            return ofgdn_area;
        }

        public void setOfgdn_area(String ofgdn_area) {
            this.ofgdn_area = ofgdn_area;
        }

        public String getOpen_area() {
            return open_area;
        }

        public void setOpen_area(String open_area) {
            this.open_area = open_area;
        }

        public String getMiscellaneous() {
            return miscellaneous;
        }

        public void setMiscellaneous(String miscellaneous) {
            this.miscellaneous = miscellaneous;
        }

        public String getMoter_bike() {
            return moter_bike;
        }

        public void setMoter_bike(String moter_bike) {
            this.moter_bike = moter_bike;
        }

        public String getWeight_machine() {
            return weight_machine;
        }

        public void setWeight_machine(String weight_machine) {
            this.weight_machine = weight_machine;
        }

        public String getMobile_exp() {
            return mobile_exp;
        }

        public void setMobile_exp(String mobile_exp) {
            this.mobile_exp = mobile_exp;
        }

        public String getElectricity() {
            return electricity;
        }

        public void setElectricity(String electricity) {
            this.electricity = electricity;
        }

        public String getEmp() {
            return emp;
        }

        public void setEmp(String emp) {
            this.emp = emp;
        }

        public String getSweeper() {
            return sweeper;
        }

        public void setSweeper(String sweeper) {
            this.sweeper = sweeper;
        }

        public String getPeon() {
            return peon;
        }

        public void setPeon(String peon) {
            this.peon = peon;
        }

        public String getTea() {
            return tea;
        }

        public void setTea(String tea) {
            this.tea = tea;
        }

        public String getUpdated_date() {
            return updated_date;
        }

        public void setUpdated_date(String updated_date) {
            this.updated_date = updated_date;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getParcel() {
            return parcel;
        }

        public void setParcel(String parcel) {
            this.parcel = parcel;
        }

        public String getPart() {
            return part;
        }

        public void setPart(String part) {
            this.part = part;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }
}
