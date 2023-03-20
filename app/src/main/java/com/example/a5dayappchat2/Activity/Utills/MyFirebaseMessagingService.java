package com.example.a5dayappchat2.Activity.Utills;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.a5dayappchat2.Activity.ChatActivity;
import com.example.a5dayappchat2.Activity.MainActivity;
import com.example.a5dayappchat2.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        String title = message.getNotification().getTitle();
        String body = message.getNotification().getBody();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"CHAT");
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setSmallIcon(R.drawable.ic_logo_2);

        Intent intent = null;

        if(message.getData().get("type").equalsIgnoreCase("sms")){

            intent = new Intent(this, ChatActivity.class);
            intent.putExtra("OtherUserID",message.getData().get("userID"));

        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this,101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1234,builder.build());

        System.out.println("From: " + message.getFrom());

        // Check if message contains a notification payload.
        if (message.getNotification() != null) {
            System.out.println("Message Notification Body: " + message.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        sendNotification(message.getFrom(), message.getNotification().getBody());
        sendNotification02(message.getNotification().getBody(),message.getNotification().getTitle());


    }



    private void sendNotification(String from, String body) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(MyFirebaseMessagingService.this.getApplicationContext(), from + " -> " + body,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotification02(String messageBody, String title) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "My channel ID";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_logo_2)
                        .setContentTitle(title)
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
