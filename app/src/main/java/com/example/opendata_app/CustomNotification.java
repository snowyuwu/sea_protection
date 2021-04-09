package com.example.opendata_app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class CustomNotification {
    public static void NotificationTrigger(Context context){
        NotificationManager manager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel("Channel_ID","ChannelName"
                    , NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(notificationChannel);
        }


        Notification notification = new NotificationCompat.Builder(context,"Channel_ID")
                .setContentTitle("緊急狀況發生")
                .setContentText("請儘快前往避難!")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.raindrop)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.raindrop))
                .build();
        manager.notify(1,notification);

        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(1500);
    }
}
