package com.arcltd.staff.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.arcltd.staff.authentication.SplashScreenActivity;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.sqlliteDatabase.ChatLocalDBHelper;
import com.arcltd.staff.utility.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyMessagingService
        extends FirebaseMessagingService {

    private static final String TAG =
            "MyMessagingService";

    public static final String CHAT_BROADCAST =
            "com.arcltd.staff.CHAT_MESSAGE_RECEIVED";

    public static final String EXTRA_SENDER_CODE =
            "sender_code";

    public static final String EXTRA_MESSAGE =
            "message";

    public static final String EXTRA_MESSAGE_ID =
            "id";

    private static volatile String activeChatEmployee;

    // =========================================================
    // ACTIVE CHAT
    // =========================================================

    public static void setActiveChatEmployee(
            String empCode) {

        activeChatEmployee =
                empCode == null
                        ? null
                        : empCode.trim();
    }

    public static void clearActiveChatEmployee(
            String empCode) {

        if (activeChatEmployee == null) {
            return;
        }

        if (empCode == null ||
                activeChatEmployee
                        .equalsIgnoreCase(
                                empCode.trim())) {

            activeChatEmployee = null;
        }
    }

    // =========================================================
    // RECEIVE FCM
    // =========================================================

    @Override
    public void onMessageReceived(
            RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        if (remoteMessage == null) {
            return;
        }

        Log.d(
                TAG,
                "FCM from = " +
                        remoteMessage.getFrom()
        );

        Map<String, String> data =
                remoteMessage.getData();

        if (data != null &&
                !data.isEmpty()) {

            Log.d(
                    TAG,
                    "FCM data = " + data
            );

            handleData(data);

            return;
        }

        // Legacy/non-data notification.
        if (remoteMessage.getNotification()
                != null) {

            String title =
                    remoteMessage
                            .getNotification()
                            .getTitle();

            String message =
                    remoteMessage
                            .getNotification()
                            .getBody();

            showNotification(
                    title,
                    message,
                    null,
                    false
            );
        }
    }

    // =========================================================
    // HANDLE DATA MESSAGE
    // =========================================================

    private void handleData(
            Map<String, String> data) {

        String title = value(data, "title");
        String rawMsg = value(data, "message");
        String initialMsg = !TextUtils.isEmpty(rawMsg) ? rawMsg : value(data, "body");

        String senderCode = getFirstNonEmpty(value(data, "sender_code"), value(data, "sender"), value(data, "sender_emp_code"));
        String receiverCode = getFirstNonEmpty(value(data, "receiver_code"), value(data, "receiver"), value(data, "receiver_emp_code"));

        String rawAction = value(data, "click_action");
        String clickAction = (!TextUtils.isEmpty(rawAction)) ? rawAction : (!TextUtils.isEmpty(senderCode) ? "chat" : "");

        String rawMsgId = value(data, "id");
        String msgId = !TextUtils.isEmpty(rawMsgId) ? rawMsgId : (!TextUtils.isEmpty(value(data, "m_id")) ? value(data, "m_id") : ("rcv_" + System.currentTimeMillis()));

        String imageUrl = value(data, "image_url");
        String message = formatNotificationMessage(initialMsg, imageUrl);

        String prefMyCode = RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.EMP_CODE);
        String myCode = !TextUtils.isEmpty(prefMyCode) ? prefMyCode : RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.ID);

        Log.d(
                TAG,
                "clickAction=" + clickAction +
                        ", sender=" + senderCode +
                        ", receiver=" + receiverCode +
                        ", id=" + msgId
        );

        // =====================================================
        // CHAT MESSAGE
        // =====================================================

        if ("chat".equalsIgnoreCase(clickAction) &&
                !TextUtils.isEmpty(senderCode) &&
                !TextUtils.isEmpty(myCode)) {

            // Ignore message belonging to another employee.
            if (!TextUtils.isEmpty(receiverCode) &&
                    !receiverCode
                            .trim()
                            .equalsIgnoreCase(
                                    myCode.trim())) {

                Log.d(
                        TAG,
                        "Message ignored. Receiver mismatch."
                );

                return;
            }

            ChatLocalDBHelper db =
                    ChatLocalDBHelper
                            .getInstance(this);

            if (!db.messageExists(msgId)) {

                db.addMessage(
                        senderCode,
                        myCode,
                        msgId,
                        message,
                        false,
                        System.currentTimeMillis(),
                        false,

                        // FCM has reached receiver.
                        1
                );
            }

            // =================================================
            // NOTIFY OPEN CHAT ACTIVITY
            // =================================================

            Intent broadcast =
                    new Intent(CHAT_BROADCAST);

            broadcast.setPackage(
                    getPackageName()
            );

            broadcast.putExtra(
                    EXTRA_SENDER_CODE,
                    senderCode
            );

            broadcast.putExtra(
                    EXTRA_MESSAGE,
                    message
            );

            broadcast.putExtra(
                    EXTRA_MESSAGE_ID,
                    msgId
            );

            sendBroadcast(broadcast);

            // =================================================
            // SHOW NOTIFICATION ONLY WHEN CHAT NOT OPEN
            // =================================================

            boolean chatIsOpen =

                    activeChatEmployee != null &&

                            activeChatEmployee
                                    .equalsIgnoreCase(
                                            senderCode.trim());

            if (!chatIsOpen) {

                showNotification(
                        TextUtils.isEmpty(title)
                                ? "New Message"
                                : title,

                        message,

                        senderCode,

                        true
                );
            }

            return;
        }

        // Other ARC application notifications.
        showNotification(
                title,
                message,
                senderCode,
                false
        );
    }

    private String formatNotificationMessage(String message, String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) return message != null ? message : "";
        if (message != null && (message.startsWith("[IMAGE]") || message.startsWith("[DOCUMENT]") || message.startsWith("[VIDEO]") || message.startsWith("[VOICE]"))) {
            return message;
        }
        String lowerUrl = imageUrl.trim().toLowerCase();
        if (lowerUrl.endsWith(".pdf") || lowerUrl.endsWith(".doc") || lowerUrl.endsWith(".docx") || lowerUrl.endsWith(".xlsx") || lowerUrl.endsWith(".xls") || lowerUrl.endsWith(".txt")) {
            String docName = (!TextUtils.isEmpty(message) && !message.equalsIgnoreCase("Image Attachment")) ? message : "Document.pdf";
            return "[DOCUMENT] " + imageUrl + "|" + docName;
        } else if (lowerUrl.endsWith(".mp4") || lowerUrl.endsWith(".3gp") || lowerUrl.endsWith(".mkv")) {
            return "[VIDEO] " + imageUrl;
        } else if (lowerUrl.endsWith(".mp3") || lowerUrl.endsWith(".m4a") || lowerUrl.endsWith(".aac") || lowerUrl.endsWith(".wav")) {
            return "[VOICE] " + imageUrl;
        } else {
            return "[IMAGE] " + imageUrl;
        }
    }

    private String getFirstNonEmpty(String... values) {
        if (values == null) return "";
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) return v.trim();
        }
        return "";
    }

    // =========================================================
    // HELPER
    // =========================================================

    private String value(
            Map<String, String> data,
            String key) {

        String result =
                data.get(key);

        return result == null
                ? ""
                : result;
    }

    // =========================================================
    // EXISTING ACTION CONSTANTS
    // =========================================================

    public static final String READ_ACTION =
            "com.arcltd.staff.firebase.ACTION_MESSAGE_READ";

    public static final String REPLY_ACTION =
            "com.arcltd.staff.firebase.ACTION_MESSAGE_REPLY";

    public static final String CONVERSATION_ID =
            "conversation_id";

    public static final String EXTRA_VOICE_REPLY =
            "extra_voice_reply";

    // =========================================================
    // NOTIFICATION
    // =========================================================

    private void showNotification(
            String title,
            String message,
            String senderCode,
            boolean isChat) {

        createNotificationChannel();

        NotificationVO notificationVO =
                new NotificationVO();

        notificationVO.setTitle(
                TextUtils.isEmpty(title)
                        ? "ARC LTD"
                        : title
        );

        notificationVO.setMessage(
                message == null
                        ? ""
                        : message
        );

        if (isChat) {
            notificationVO.setAction("chat");
        }

        Intent resultIntent =
                new Intent(
                        getApplicationContext(),
                        SplashScreenActivity.class
                );

        if (isChat) {

            resultIntent.putExtra(
                    "click_action",
                    "chat"
            );

            resultIntent.putExtra(
                    "sender_code",
                    senderCode
            );
        }

        NotificationUtils utils =
                new NotificationUtils(
                        getApplicationContext()
                );

        utils.displayNotification(
                notificationVO,
                resultIntent
        );

        utils.playNotificationSound();
    }

    // =========================================================
    // NOTIFICATION CHANNEL
    // =========================================================

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT <
                Build.VERSION_CODES.O) {

            return;
        }

        String id =
                "my_channel_01";

        String name =
                "Chat messages";

        NotificationChannel channel =
                new NotificationChannel(
                        id,
                        name,
                        NotificationManager
                                .IMPORTANCE_HIGH
                );

        channel.setDescription(
                "ARC LTD chat notifications"
        );

        channel.setShowBadge(true);

        NotificationManager manager =
                (NotificationManager)
                        getSystemService(
                                Context.NOTIFICATION_SERVICE
                        );

        if (manager != null) {

            manager.createNotificationChannel(
                    channel
            );
        }
    }
}