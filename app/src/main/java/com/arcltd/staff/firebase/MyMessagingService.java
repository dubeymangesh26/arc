package com.arcltd.staff.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.arcltd.staff.authentication.SplashScreenActivity;
import com.arcltd.staff.remote.RetrofitClient;
import com.arcltd.staff.utility.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyMessagingService extends FirebaseMessagingService {
    private static final String TITLE = "body";
    private static final String MESSAGE = "message";
    private static final String BODY = "message";
    private static final String IMAGE = "myicon";
    private static final String ACTION = "action";
    private static final String DATA = "data";
    private static final String ORDER_ID = "order_id";
    private static final String CLICK_ACTION = "click_action";
    private static final String ACTION_DESTINATION = "action_destination";

    public static final String READ_ACTION =
            "com.arcltd.staff.firebase.ACTION_MESSAGE_READ";
    public static final String REPLY_ACTION =
            "com.arcltd.staff.firebase.ACTION_MESSAGE_REPLY";
    public static final String CONVERSATION_ID = "conversation_id";
    public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";
    private static final String TAG = MyMessagingService.class.getSimpleName();
    private NotificationManagerCompat mNotificationManager;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            handleData(data);

        } else if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification());
        }// Check if message contains a notification payload.


        String id = "my_channel_01";
        CharSequence name = TITLE;
        String description =BODY;
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        mChannel.setDescription(description);
        mChannel.setShowBadge(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(mChannel);




    }

    private void handleNotification(RemoteMessage.Notification RemoteMsgNotification) {
        String message = RemoteMsgNotification.getBody();
        String title = RemoteMsgNotification.getTitle();
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);

        Intent resultIntent = new Intent(getApplicationContext(), SplashScreenActivity.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
        notificationUtils.playNotificationSound();
    }

    private void handleData(Map<String, String> data) {
        String title = data.get(TITLE);
        String message = data.get(BODY);
        String iconUrl = data.get(IMAGE);
        String action = data.get(ACTION);
        String click_action = data.get(CLICK_ACTION);
        String actionDestination = data.get(ACTION_DESTINATION);
        String order_id = data.get(ORDER_ID);
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);
        notificationVO.setOrder_id(order_id);
        notificationVO.setIconUrl(iconUrl);
        notificationVO.setAction(click_action);
        notificationVO.setActionDestination(actionDestination);

        if (RetrofitClient.getStringUserPreference(getApplicationContext(), Constants.EMP_CODE) != null) {
            if (click_action.equals("order")) {
                Intent resultIntent = new Intent(getApplicationContext(), SplashScreenActivity.class)
                        .putExtra("order_ID", order_id);
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.displayNotification(notificationVO, resultIntent);
                notificationUtils.playNotificationSound();
            } else if (click_action.equals("general")) {
                Intent resultIntent = new Intent(getApplicationContext(), SplashScreenActivity.class);
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.displayNotification(notificationVO, resultIntent);
                notificationUtils.playNotificationSound();
            }
        } else {
            Intent resultIntent = new Intent(getApplicationContext(), SplashScreenActivity.class);
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.displayNotification(notificationVO, resultIntent);
            notificationUtils.playNotificationSound();

        }
    }



    public interface NotifificationCallBack {
        void notifificationCallBack(String data);
    }

}