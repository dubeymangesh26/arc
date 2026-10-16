package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetChatHistoryUsersResponse {

    @SerializedName("ResponseCode")
    private String responseCode;

    @SerializedName("message")
    private String message;

    @SerializedName("employee_list")
    private List<UserItem> employeeList;

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

    public List<UserItem> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<UserItem> employeeList) {
        this.employeeList = employeeList;
    }

    public static class UserItem {

        @SerializedName("emp_code")
        private String empCode;

        @SerializedName("emp_name")
        private String empName;

        @SerializedName("name")
        private String name;

        @SerializedName("profilepic")
        private String profilePic;

        @SerializedName("branch_code")
        private String branchCode;

        @SerializedName("branch_name")
        private String branchName;

        @SerializedName("division_name")
        private String divisionName;

        @SerializedName("region_name")
        private String regionName;

        @SerializedName("design")
        private String design;

        @SerializedName("email")
        private String email;

        @SerializedName("token")
        private String token;

        @SerializedName("last_message")
        private String lastMessage;

        @SerializedName("last_seen")
        private String lastSeen;

        @SerializedName("unread_count")
        private int unreadCount;

        public String getEmpCode() {
            return empCode;
        }

        public void setEmpCode(String empCode) {
            this.empCode = empCode;
        }

        public String getEmpName() {
            if (empName != null && !empName.trim().isEmpty()) {
                return empName;
            }
            return name != null ? name : "";
        }

        public void setEmpName(String empName) {
            this.empName = empName;
        }

        public String getName() {
            if (name != null && !name.trim().isEmpty()) {
                return name;
            }
            return empName != null ? empName : "";
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getBranchCode() {
            return branchCode;
        }

        public void setBranchCode(String branchCode) {
            this.branchCode = branchCode;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getDivisionName() {
            return divisionName;
        }

        public void setDivisionName(String divisionName) {
            this.divisionName = divisionName;
        }

        public String getRegionName() {
            return regionName;
        }

        public void setRegionName(String regionName) {
            this.regionName = regionName;
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

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getLastMessage() {
            return lastMessage;
        }

        public void setLastMessage(String lastMessage) {
            this.lastMessage = lastMessage;
        }

        public String getLastSeen() {
            return lastSeen;
        }

        public void setLastSeen(String lastSeen) {
            this.lastSeen = lastSeen;
        }

        public int getUnreadCount() {
            return unreadCount;
        }

        public void setUnreadCount(int unreadCount) {
            this.unreadCount = unreadCount;
        }
    }
}
