package com.example.opendata_app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import cn.pedant.SweetAlert.SweetAlertDialog;

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
                .setContentTitle("適宜活動變更")
                .setContentText("您的所在場所有適宜活動的變更，請留意!")
//                .setContentTitle("適宜活動變更")
//                .setContentText("您的所在場所不適合進行以下活動：游泳、潛水，請儘量避免進行以上活動!")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.alert)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.alert))
                .build();
        manager.notify(1,notification);

        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(3000);
    }
}
