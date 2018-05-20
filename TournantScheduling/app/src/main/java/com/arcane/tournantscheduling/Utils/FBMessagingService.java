package com.arcane.tournantscheduling.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.arcane.tournantscheduling.Activities.HomeScreenActivity;
import com.arcane.tournantscheduling.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;


public class FBMessagingService extends FirebaseMessagingService {
    public static final String TAG = "FBMS";
    String default_notification_channel_id = "SOME_CHANNEL";
    String sender;
    String message;
    String companyMessage;
    String timeOffStart;
    String timeOffEnd;
    String name;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if(remoteMessage.getData() != null){
            Log.d(TAG, "Recieved Data " + remoteMessage.getData().get("sender"));
            sender = remoteMessage.getData().get("sender");
            name = remoteMessage.getData().get("senderName");
            message = remoteMessage.getData().get("message");
            companyMessage = remoteMessage.getData().get("companyMessage");
            timeOffStart = remoteMessage.getData().get("timeOffStart");
            timeOffEnd = remoteMessage.getData().get("timeOffEnd");
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }

    }


    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.d("SENDER", sender);
        if(message != null){
        intent.putExtra("message", sender);
        }else if(companyMessage != null){
            intent.putExtra("companyMessage",companyMessage);
            intent.putExtra("senderName", name);
            Log.d("Company", companyMessage);
        }else if(timeOffStart != null){
            intent.putExtra("timeOffStart", timeOffStart);
            Log.d("TimeOff", timeOffStart);

        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId =default_notification_channel_id;
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_send)
                        .setContentTitle("FCM Message")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
