package com.example.lab7a_talib;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessageReceiver extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "alarm_notification_channel_final";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = "ICT602";
        String message = "New notification received";

        if (remoteMessage.getNotification() != null) {
            if (remoteMessage.getNotification().getTitle() != null) {
                title = remoteMessage.getNotification().getTitle();
            }

            if (remoteMessage.getNotification().getBody() != null) {
                message = remoteMessage.getNotification().getBody();
            }
        }

        showNotification(title, message);
    }

    private void showNotification(String title, String message) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "FCM Notification Channel",
                            NotificationManager.IMPORTANCE_HIGH
                    );

            channel.setDescription("Firebase Cloud Messaging Channel");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);

            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
                );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        notificationManager.notify(1, builder.build());
    }
}