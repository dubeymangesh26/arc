package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MarketingVisitListResponse {

    @SerializedName("ResponseCode")
    private String responseCode;

    @SerializedName("message")
    private String message;

    @SerializedName("visit_list")
    private List<MarketingVisitItem> visitList;

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

    public List<MarketingVisitItem> getVisitList() {
        return visitList;
    }

    public void setVisitList(List<MarketingVisitItem> visitList) {
        this.visitList = visitList;
    }

    public static class MarketingVisitItem {

        @SerializedName("id")
        private String id;

        @SerializedName("emp_code")
        private String empCode;

        @SerializedName("emp_name")
        private String empName;

        @SerializedName("branch_code")
        private String branchCode;

        @SerializedName("division_id")
        private String divisionId;

        @SerializedName("company_name")
        private String companyName;

        @SerializedName("purpose")
        private String purpose;

        @SerializedName("location_name")
        private String locationName;

        @SerializedName("latitude")
        private String latitude;

        @SerializedName("longitude")
        private String longitude;

        @SerializedName("image_url")
        private String imageUrl;

        @SerializedName("visit_date")
        private String visitDate;

        @SerializedName("visit_time")
        private String visitTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmpCode() {
            return empCode;
        }

        public void setEmpCode(String empCode) {
            this.empCode = empCode;
        }

        public String getEmpName() {
            return empName;
        }

        public void setEmpName(String empName) {
            this.empName = empName;
        }

        public String getBranchCode() {
            return branchCode;
        }

        public void setBranchCode(String branchCode) {
            this.branchCode = branchCode;
        }

        public String getDivisionId() {
            return divisionId;
        }

        public void setDivisionId(String divisionId) {
            this.divisionId = divisionId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getVisitDate() {
            return visitDate;
        }

        public void setVisitDate(String visitDate) {
            this.visitDate = visitDate;
        }

        public String getVisitTime() {
            return visitTime;
        }

        public void setVisitTime(String visitTime) {
            this.visitTime = visitTime;
        }
    }
}
