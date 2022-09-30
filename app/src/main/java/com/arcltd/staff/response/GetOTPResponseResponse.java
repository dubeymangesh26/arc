package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

public class GetOTPResponseResponse {

    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;
    private Otp otp;

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

    public Otp getOtp() {
        return otp;
    }

    public void setOtp(Otp otp) {
        this.otp = otp;
    }

    public static class Otp {
        private Integer code;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }
    }
}
