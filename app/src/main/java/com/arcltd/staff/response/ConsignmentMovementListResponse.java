package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConsignmentMovementListResponse {

    private List<ConsignmentList> consignment_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<ConsignmentList> getConsignment_list() {
        return consignment_list;
    }

    public void setConsignment_list(List<ConsignmentList> consignment_list) {
        this.consignment_list = consignment_list;
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

    public static class ConsignmentList {
        private String traking_id;
        private String cns_no;
        private String move_From;
        private String move_To;
        private String move_date;
        private String created_date;

        public String getTraking_id() {
            return traking_id;
        }

        public void setTraking_id(String traking_id) {
            this.traking_id = traking_id;
        }

        public String getCns_no() {
            return cns_no;
        }

        public void setCns_no(String cns_no) {
            this.cns_no = cns_no;
        }

        public String getMove_From() {
            return move_From;
        }

        public void setMove_From(String move_From) {
            this.move_From = move_From;
        }

        public String getMove_To() {
            return move_To;
        }

        public void setMove_To(String move_To) {
            this.move_To = move_To;
        }

        public String getMove_date() {
            return move_date;
        }

        public void setMove_date(String move_date) {
            this.move_date = move_date;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }
    }
}
