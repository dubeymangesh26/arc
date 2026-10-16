package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

public class AddMessageResponse {

    @SerializedName(value = "responseCode", alternate = {"ResponseCode", "responsecode", "code", "status"})
    private String responseCode;
    
    @SerializedName(value = "message", alternate = {"msg", "error"})
    private String message;

    @SerializedName(value = "m_id", alternate = {"id", "message_id"})
    private String messageId;

    @SerializedName(value = "sender_emp_code", alternate = {"emp_code"})
    private String senderEmpCode;

    @SerializedName(value = "receiver_emp_code", alternate = {"target_emp_code"})
    private String receiverEmpCode;

    @SerializedName(value = "created_at", alternate = {"date", "timestamp"})
    private String createdAt;

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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderEmpCode() {
        return senderEmpCode;
    }

    public void setSenderEmpCode(String senderEmpCode) {
        this.senderEmpCode = senderEmpCode;
    }

    public String getReceiverEmpCode() {
        return receiverEmpCode;
    }

    public void setReceiverEmpCode(String receiverEmpCode) {
        this.receiverEmpCode = receiverEmpCode;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
