package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmployeeTransferHistoryResponse {


    private List<EmpTrHistoryList> emp_tr_history_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<EmpTrHistoryList> getEmp_tr_history_list() {
        return emp_tr_history_list;
    }

    public void setEmp_tr_history_list(List<EmpTrHistoryList> emp_tr_history_list) {
        this.emp_tr_history_list = emp_tr_history_list;
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

    public static class EmpTrHistoryList {
        private String tr_id;
        private String emp_code;
        private String from_branch;
        private String to_branch;
        private String transfer_date;

        public String getTr_id() {
            return tr_id;
        }

        public void setTr_id(String tr_id) {
            this.tr_id = tr_id;
        }

        public String getEmp_code() {
            return emp_code;
        }

        public void setEmp_code(String emp_code) {
            this.emp_code = emp_code;
        }

        public String getFrom_branch() {
            return from_branch;
        }

        public void setFrom_branch(String from_branch) {
            this.from_branch = from_branch;
        }

        public String getTo_branch() {
            return to_branch;
        }

        public void setTo_branch(String to_branch) {
            this.to_branch = to_branch;
        }

        public String getTransfer_date() {
            return transfer_date;
        }

        public void setTransfer_date(String transfer_date) {
            this.transfer_date = transfer_date;
        }
    }
}
