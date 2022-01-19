package com.example.opendata_app;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import static com.kongzue.dialogx.interfaces.BaseDialog.getContext;

public class RestartService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        Intent startIntent = new Intent(getContext(), CustomService.class);
        startService(startIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
