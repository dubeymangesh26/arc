package com.arcltd.staff.response;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetChatResponse {

    @SerializedName("responseCode")
    private String responseCode;

    @SerializedName("ResponseCode")
    private String altResponseCode;

    @SerializedName("message")
    private String message;

    @SerializedName("employee_message")
    private List<ChatItem> chat;

    @SerializedName("chat_list")
    private List<ChatItem> chatList;

    public String getResponseCode() {
        if (!TextUtils.isEmpty(responseCode)) return responseCode;
        return altResponseCode != null ? altResponseCode : "";
    }

    public String getMessage() {
        return message != null ? message : "";
    }

    public List<ChatItem> getChat() {
        if (chat != null) return chat;
        return chatList;
    }

    public static class ChatItem {

        @SerializedName("m_id")
        private String mId;

        @SerializedName("id")
        private String id;

        @SerializedName("message_id")
        private String messageId;

        @SerializedName("sender_emp_code")
        private String senderEmpCode;

        @SerializedName("sender_code")
        private String senderCode;

        @SerializedName("sender")
        private String sender;

        @SerializedName("receiver_emp_code")
        private String receiverEmpCode;

        @SerializedName("receiver_code")
        private String receiverCode;

        @SerializedName("receiver")
        private String receiver;

        @SerializedName("message")
        private String message;

        @SerializedName("read_unread")
        private String readUnread;

        @SerializedName("message_status")
        private String messageStatus;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("seen_at")
        private String seenAt;

        @SerializedName("image_url")
        private String imageUrl;

        public String getId() {
            if (!TextUtils.isEmpty(mId)) return mId;
            if (!TextUtils.isEmpty(id)) return id;
            return messageId != null ? messageId : "";
        }

        public String getSenderEmpCode() {
            if (!TextUtils.isEmpty(senderEmpCode)) return senderEmpCode;
            if (!TextUtils.isEmpty(senderCode)) return senderCode;
            return sender != null ? sender : "";
        }

        public String getReceiverEmpCode() {
            if (!TextUtils.isEmpty(receiverEmpCode)) return receiverEmpCode;
            if (!TextUtils.isEmpty(receiverCode)) return receiverCode;
            return receiver != null ? receiver : "";
        }

        public String getMessage() {
            return message != null ? message : "";
        }

        public String getReadUnread() {
            return readUnread != null ? readUnread : "0";
        }

        public String getMessageStatus() {
            return messageStatus != null ? messageStatus : "0";
        }

        public String getCreatedAt() {
            return createdAt != null ? createdAt : "";
        }

        public String getSeenAt() {
            return seenAt != null ? seenAt : "";
        }

        public String getImageUrl() {
            return imageUrl != null ? imageUrl : "";
        }
    }
}
