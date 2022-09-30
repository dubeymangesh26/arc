package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContacUsResponse {

    private List<BookingList> booking_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<BookingList> getBooking_list() {
        return booking_list;
    }

    public void setBooking_list(List<BookingList> booking_list) {
        this.booking_list = booking_list;
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

    public static class BookingList {
        private String id;
        private String contact_no;
        private String whatsapp_number;
        private String email;
        private String address;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContact_no() {
            return contact_no;
        }

        public void setContact_no(String contact_no) {
            this.contact_no = contact_no;
        }

        public String getWhatsapp_number() {
            return whatsapp_number;
        }

        public void setWhatsapp_number(String whatsapp_number) {
            this.whatsapp_number = whatsapp_number;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
