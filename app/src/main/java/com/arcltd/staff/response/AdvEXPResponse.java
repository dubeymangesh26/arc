package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

public class AdvEXPResponse {

    private BranchAdministrativeexp branch_administrativeexp;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public BranchAdministrativeexp getBranch_administrativeexp() {
        return branch_administrativeexp;
    }

    public void setBranch_administrativeexp(BranchAdministrativeexp branch_administrativeexp) {
        this.branch_administrativeexp = branch_administrativeexp;
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

    public static class BranchAdministrativeexp {
        private String ad_id;
        private String region_id;
        private String division_id;
        private String branch_code;
        private String telephone;
        private String pastge_courier;
        private String print_stationery;
        private String bike_petrol_exp;
        private String bike_maitainance;
        private String car_petrol_exp;
        private String car_maitainance;
        private String conveyance;
        private String travelling;
        private String staff_welfare;
        private String misc_charges;
        private String busi_pramostion;
        private String office_maitanance;
        private String office_eqp_maintenance;
        private String repare_buildings;
        private String salary_bonas_gradu_other;
        private String cocontribution_pf_esi;
        private String rent_paid;
        private String electricity;
        private String books_periodicals;
        private String advertisement;
        private String recrument_place_conf;
        private String insurance;
        private String legal;
        private String subscription;
        private String charity_donation;
        private String auditor_consultancy;
        private String rates_taxes;
        private String bank_commission;
        private String emp_children_education;
        private String updated_date;
        private String created_date;

        public String getAd_id() {
            return ad_id;
        }

        public void setAd_id(String ad_id) {
            this.ad_id = ad_id;
        }

        public String getRegion_id() {
            return region_id;
        }

        public void setRegion_id(String region_id) {
            this.region_id = region_id;
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

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getPastge_courier() {
            return pastge_courier;
        }

        public void setPastge_courier(String pastge_courier) {
            this.pastge_courier = pastge_courier;
        }

        public String getPrint_stationery() {
            return print_stationery;
        }

        public void setPrint_stationery(String print_stationery) {
            this.print_stationery = print_stationery;
        }

        public String getBike_petrol_exp() {
            return bike_petrol_exp;
        }

        public void setBike_petrol_exp(String bike_petrol_exp) {
            this.bike_petrol_exp = bike_petrol_exp;
        }

        public String getBike_maitainance() {
            return bike_maitainance;
        }

        public void setBike_maitainance(String bike_maitainance) {
            this.bike_maitainance = bike_maitainance;
        }

        public String getCar_petrol_exp() {
            return car_petrol_exp;
        }

        public void setCar_petrol_exp(String car_petrol_exp) {
            this.car_petrol_exp = car_petrol_exp;
        }

        public String getCar_maitainance() {
            return car_maitainance;
        }

        public void setCar_maitainance(String car_maitainance) {
            this.car_maitainance = car_maitainance;
        }

        public String getConveyance() {
            return conveyance;
        }

        public void setConveyance(String conveyance) {
            this.conveyance = conveyance;
        }

        public String getTravelling() {
            return travelling;
        }

        public void setTravelling(String travelling) {
            this.travelling = travelling;
        }

        public String getStaff_welfare() {
            return staff_welfare;
        }

        public void setStaff_welfare(String staff_welfare) {
            this.staff_welfare = staff_welfare;
        }

        public String getMisc_charges() {
            return misc_charges;
        }

        public void setMisc_charges(String misc_charges) {
            this.misc_charges = misc_charges;
        }

        public String getBusi_pramostion() {
            return busi_pramostion;
        }

        public void setBusi_pramostion(String busi_pramostion) {
            this.busi_pramostion = busi_pramostion;
        }

        public String getOffice_maitanance() {
            return office_maitanance;
        }

        public void setOffice_maitanance(String office_maitanance) {
            this.office_maitanance = office_maitanance;
        }

        public String getOffice_eqp_maintenance() {
            return office_eqp_maintenance;
        }

        public void setOffice_eqp_maintenance(String office_eqp_maintenance) {
            this.office_eqp_maintenance = office_eqp_maintenance;
        }

        public String getRepare_buildings() {
            return repare_buildings;
        }

        public void setRepare_buildings(String repare_buildings) {
            this.repare_buildings = repare_buildings;
        }

        public String getSalary_bonas_gradu_other() {
            return salary_bonas_gradu_other;
        }

        public void setSalary_bonas_gradu_other(String salary_bonas_gradu_other) {
            this.salary_bonas_gradu_other = salary_bonas_gradu_other;
        }

        public String getCocontribution_pf_esi() {
            return cocontribution_pf_esi;
        }

        public void setCocontribution_pf_esi(String cocontribution_pf_esi) {
            this.cocontribution_pf_esi = cocontribution_pf_esi;
        }

        public String getRent_paid() {
            return rent_paid;
        }

        public void setRent_paid(String rent_paid) {
            this.rent_paid = rent_paid;
        }

        public String getElectricity() {
            return electricity;
        }

        public void setElectricity(String electricity) {
            this.electricity = electricity;
        }

        public String getBooks_periodicals() {
            return books_periodicals;
        }

        public void setBooks_periodicals(String books_periodicals) {
            this.books_periodicals = books_periodicals;
        }

        public String getAdvertisement() {
            return advertisement;
        }

        public void setAdvertisement(String advertisement) {
            this.advertisement = advertisement;
        }

        public String getRecrument_place_conf() {
            return recrument_place_conf;
        }

        public void setRecrument_place_conf(String recrument_place_conf) {
            this.recrument_place_conf = recrument_place_conf;
        }

        public String getInsurance() {
            return insurance;
        }

        public void setInsurance(String insurance) {
            this.insurance = insurance;
        }

        public String getLegal() {
            return legal;
        }

        public void setLegal(String legal) {
            this.legal = legal;
        }

        public String getSubscription() {
            return subscription;
        }

        public void setSubscription(String subscription) {
            this.subscription = subscription;
        }

        public String getCharity_donation() {
            return charity_donation;
        }

        public void setCharity_donation(String charity_donation) {
            this.charity_donation = charity_donation;
        }

        public String getAuditor_consultancy() {
            return auditor_consultancy;
        }

        public void setAuditor_consultancy(String auditor_consultancy) {
            this.auditor_consultancy = auditor_consultancy;
        }

        public String getRates_taxes() {
            return rates_taxes;
        }

        public void setRates_taxes(String rates_taxes) {
            this.rates_taxes = rates_taxes;
        }

        public String getBank_commission() {
            return bank_commission;
        }

        public void setBank_commission(String bank_commission) {
            this.bank_commission = bank_commission;
        }

        public String getEmp_children_education() {
            return emp_children_education;
        }

        public void setEmp_children_education(String emp_children_education) {
            this.emp_children_education = emp_children_education;
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
