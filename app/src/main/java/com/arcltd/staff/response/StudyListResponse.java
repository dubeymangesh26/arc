package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudyListResponse {


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
        private String topic_name;
        private String topic_link;
        @SerializedName("created date")
        private String _$CreatedDate147;// FIXME check this code

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTopic_name() {
            return topic_name;
        }

        public void setTopic_name(String topic_name) {
            this.topic_name = topic_name;
        }

        public String getTopic_link() {
            return topic_link;
        }

        public void setTopic_link(String topic_link) {
            this.topic_link = topic_link;
        }

        public String get_$CreatedDate147() {
            return _$CreatedDate147;
        }

        public void set_$CreatedDate147(String _$CreatedDate147) {
            this._$CreatedDate147 = _$CreatedDate147;
        }
    }
}
