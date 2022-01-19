package com.example.opendata_app;

import android.os.Handler;
import android.util.Log;

public class ThreadManager {
    public static void createMainThread(){
        Handler handler = new Handler();
        Runnable runnable = new Runnable(){
            @Override
            public void run(){
                Log.d("thread","testing");
                handler.postDelayed(this,2000);
            }
        };

        handler.postDelayed(runnable,2000);
    }
}
