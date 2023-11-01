package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

public class ConsignmentTrackListResponse {

    private ConsignmentList consignment_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public ConsignmentList getConsignment_list() {
        return consignment_list;
    }

    public void setConsignment_list(ConsignmentList consignment_list) {
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
        private String cns_id;
        private String branch_code;
        private String cns_no;
        private String booking_date;
        private String expected_date;
        private String consignor;
        private String consignee;
        private String payment_cust_name;
        private String cns_from;
        private String cns_to;
        private String delivery_status;
        private String delivery_date;
        private String actual_weight;
        private String delivery_type;
        private String no_of_package;
        private String paymentType;
        private String invoice_no;
        private String invoice_date;
        private String pod_ilnk;
        private String updated_date;
        private String created_date;

        public String getCns_id() {
            return cns_id;
        }

        public void setCns_id(String cns_id) {
            this.cns_id = cns_id;
        }

        public String getBranch_code() {
            return branch_code;
        }

        public void setBranch_code(String branch_code) {
            this.branch_code = branch_code;
        }

        public String getCns_no() {
            return cns_no;
        }

        public void setCns_no(String cns_no) {
            this.cns_no = cns_no;
        }

        public String getBooking_date() {
            return booking_date;
        }

        public void setBooking_date(String booking_date) {
            this.booking_date = booking_date;
        }

        public String getExpected_date() {
            return expected_date;
        }

        public void setExpected_date(String expected_date) {
            this.expected_date = expected_date;
        }

        public String getConsignor() {
            return consignor;
        }

        public void setConsignor(String consignor) {
            this.consignor = consignor;
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getPayment_cust_name() {
            return payment_cust_name;
        }

        public void setPayment_cust_name(String payment_cust_name) {
            this.payment_cust_name = payment_cust_name;
        }

        public String getCns_from() {
            return cns_from;
        }

        public void setCns_from(String cns_from) {
            this.cns_from = cns_from;
        }

        public String getCns_to() {
            return cns_to;
        }

        public void setCns_to(String cns_to) {
            this.cns_to = cns_to;
        }

        public String getDelivery_status() {
            return delivery_status;
        }

        public void setDelivery_status(String delivery_status) {
            this.delivery_status = delivery_status;
        }

        public String getDelivery_date() {
            return delivery_date;
        }

        public void setDelivery_date(String delivery_date) {
            this.delivery_date = delivery_date;
        }

        public String getActual_weight() {
            return actual_weight;
        }

        public void setActual_weight(String actual_weight) {
            this.actual_weight = actual_weight;
        }

        public String getDelivery_type() {
            return delivery_type;
        }

        public void setDelivery_type(String delivery_type) {
            this.delivery_type = delivery_type;
        }

        public String getNo_of_package() {
            return no_of_package;
        }

        public void setNo_of_package(String no_of_package) {
            this.no_of_package = no_of_package;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getInvoice_no() {
            return invoice_no;
        }

        public void setInvoice_no(String invoice_no) {
            this.invoice_no = invoice_no;
        }

        public String getInvoice_date() {
            return invoice_date;
        }

        public void setInvoice_date(String invoice_date) {
            this.invoice_date = invoice_date;
        }

        public String getPod_ilnk() {
            return pod_ilnk;
        }

        public void setPod_ilnk(String pod_ilnk) {
            this.pod_ilnk = pod_ilnk;
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
