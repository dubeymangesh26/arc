package com.arcltd.staff.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenericResponse {

    @SerializedName("responsecode")
    @Expose
    private String responsecode;
    @SerializedName("message")
    @Expose
    private String message;

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}