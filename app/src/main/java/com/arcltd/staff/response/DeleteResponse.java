package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

public class DeleteResponse {


    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

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
}
