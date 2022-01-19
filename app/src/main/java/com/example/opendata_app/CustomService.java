package com.example.opendata_app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ligl.android.widget.iosdialog.IOSDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.kongzue.dialogx.interfaces.BaseDialog.getContext;

public class CustomService extends Service {
    public String userLocation;
    public String userArea;
    public String warningFront = "您的所在場所已不適合進行以下活動：\n";
    public String warningEnd = "\n請盡量避免進行以上活動!";
    public boolean warning = false;
    public Context context;
    public SessionManager sessionManager;
    public HashMap<String, String> locationDetails;
    public static final String TAG = "MyService";

    public CustomService() {
    }

    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
        sessionManager = new SessionManager(context);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userLocation = intent.getStringExtra("location");
        userArea = intent.getStringExtra("place");
        Handler handler = new Handler();
        Runnable runnable = () -> {
//                StringRequest areaPlace = new StringRequest(Request.Method.POST, "http://120.119.77.72:8081/api/placeActivity", response -> {
//                    try {
//                        JSONArray jsonArray = new JSONArray(response);
//                        if(locationDetails != null){
//                            if (Integer.parseInt(locationDetails.get("Swim")) -
//                                    Integer.parseInt(jsonArray.getJSONObject(0).getString("Swim")) == 1){
//                                warningFront = warningFront + "游泳";
//                                warning = true;
//                            }
//                            if (Integer.parseInt(locationDetails.get("Diving")) - Integer.parseInt(jsonArray.getJSONObject(0).getString("Diving")) == 1){
//                                warningFront = warningFront + "潛水";
//                                warning = true;
//                            }
//                            if (Integer.parseInt(locationDetails.get("Surfing")) - Integer.parseInt(jsonArray.getJSONObject(0).getString("Surfing")) == 1){
//                                warningFront = warningFront + "衝浪";
//                                warning = true;
//                            }
//                            if (Integer.parseInt(locationDetails.get("Jetski")) - Integer.parseInt(jsonArray.getJSONObject(0).getString("Jetski")) == 1){
//                                warningFront = warningFront + "水上摩托車";
//                                warning = true;
//                            }
//                            if (Integer.parseInt(locationDetails.get("BananaBoat")) - Integer.parseInt(jsonArray.getJSONObject(0).getString("BananaBoat")) == 1){
//                                warningFront = warningFront + "香蕉船";
//                                warning = true;
//                            }
//                            if (Integer.parseInt(locationDetails.get("AquaBoard")) - Integer.parseInt(jsonArray.getJSONObject(0).getString("AquaBoard")) == 1){
//                                warningFront = warningFront + "滑水板";
//                                warning = true;
//                            }
//                        }
//
//                        if(warning){
//                            CustomNotification.NotificationTrigger(context);
//                            MediaPlayer mp = MediaPlayer.create(context, R.raw.placeholder);
//                            mp.start();
//                            new IOSDialog.Builder(context)
//                                    .setTitle("緊急情況通知")
//                                    .setMessage(warningFront + warningEnd).show();
//                        }
//
//                        sessionManager.refresh(jsonArray.getJSONObject(0).getString("Area"),
//                                jsonArray.getJSONObject(0).getString("Swim"),
//                                jsonArray.getJSONObject(0).getString("Diving"),
//                                jsonArray.getJSONObject(0).getString("Surfing"),
//                                jsonArray.getJSONObject(0).getString("Jetski"),
//                                jsonArray.getJSONObject(0).getString("BananaBoat"),
//                                jsonArray.getJSONObject(0).getString("AquaBoard"));
//                        locationDetails = sessionManager.getPlaceDetails();
//                    } catch (JSONException e) {
//                        Log.e("keywordSearch", Log.getStackTraceString(e));
//                    }
//                },
//                        error -> Log.d("keywordSearchError", "error")) {
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> data = new HashMap<>();
//                        data.put("area", userLocation);
//                        data.put("place", userArea);
//                        return data;
//                    }
//                };
//                RequestQueue areaPlaceRequest = Volley.newRequestQueue(getBaseContext());
//                areaPlaceRequest.add(areaPlace);
            CustomNotification.NotificationTrigger(context);
            MediaPlayer mp = MediaPlayer.create(context, R.raw.warning);
            mp.start();
            Runnable mpStop = mp::stop;
            handler.postDelayed(mpStop, 5000);
        };
        handler.postDelayed(runnable, 5000);
//        return START_REDELIVER_INTENT;
        return START_STICKY;
//        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Handler handler = new Handler();
        CustomNotification.NotificationTrigger(context);
        MediaPlayer mp = MediaPlayer.create(context, R.raw.warning);
        mp.start();
        Runnable mpStop = mp::stop;
        handler.postDelayed(mpStop, 5000);
//        int delay = (int)(Math.random()*240001+60000);
//        Log.d("destroy", String.valueOf(delay));
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                CustomNotification.NotificationTrigger(context);
//                MediaPlayer mp = MediaPlayer.create(context, R.raw.placeholder);
//                mp.start();
//            }
//        }, delay);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}