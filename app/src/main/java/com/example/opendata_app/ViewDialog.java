package com.example.opendata_app;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class ViewDialog extends DialogFragment {
    public String beachName;
    public String tableName;
    public Context context;
    public ViewDialog(String beachName, String tableName, Context context){
        this.beachName = beachName;
        this.tableName = tableName;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialog_view, container);
        TextView placeName = view.findViewById(R.id.placeName);
        placeName.setText(beachName);


        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int ScreenWidth = dm.widthPixels;   //螢幕的寬
        int ScreenHeight = dm.heightPixels;  //螢幕的高
        Log.d("screen width：", String.valueOf(ScreenWidth));
        Log.d("screen height：", String.valueOf(ScreenHeight));

        ImageView placeImage = view.findViewById(R.id.placeImage);
        TextView temperature = view.findViewById(R.id.temperature);
        TextView humidity = view.findViewById(R.id.humidity);
        TextView windspeed = view.findViewById(R.id.windspeed);
        TextView winddirection = view.findViewById(R.id.winddirection);
        TextView ultraviolet = view.findViewById(R.id.ultraviolet);
        TextView raindrop = view.findViewById(R.id.raindrop);
        TextView comment = view.findViewById(R.id.comment);

        Button swim = view.findViewById(R.id.swim);
        Button diving = view.findViewById(R.id.diving);
        Button surfing = view.findViewById(R.id.surfing);
        Button BananaBoat = view.findViewById(R.id.BananaBoat);
        Button JetSki = view.findViewById(R.id.JetSki);
        Button AquaBoard = view.findViewById(R.id.AquaBoard);

        Button close = view.findViewById(R.id.close);
        close.setOnClickListener(v -> dismiss());
        StringRequest getWeather = new StringRequest(Request.Method.POST, "http://120.119.77.72:8081/api/getAreaWeather", response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                String uri = "http://120.119.77.72:8081/images/"+tableName+"_image/"+jsonArray.getJSONObject(0).getString("image2");
                Picasso.get().load(uri).into(placeImage);

                String windDir = jsonArray.getJSONObject(0).getString("WindDirection");
                dialogSetText(temperature,"溫度：",jsonArray.getJSONObject(0).getString("Temperature")," °C");
                dialogSetText(humidity,"濕度：",jsonArray.getJSONObject(0).getString("Humidity")," %");
                dialogSetText(windspeed,"風速：",jsonArray.getJSONObject(0).getString("WindSpeed")," m/s");
                dialogSetText(winddirection,"風向：", windDir.subSequence(0,windDir.length()-1).toString(),null);
                dialogSetText(ultraviolet,"舒適度：",jsonArray.getJSONObject(0).getString("Ultraviolet"),null);
                dialogSetText(raindrop,"降雨率：",jsonArray.getJSONObject(0).getString("Rainfull")," %");
                dialogSetText(comment,"小語：",jsonArray.getJSONObject(0).getString("WinterDescription"),null);

                if(jsonArray.getJSONObject(0).getString("Swim").equals("1")){
                    swim.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.availableEvent,null)));
                }
                if(jsonArray.getJSONObject(0).getString("Diving").equals("1")){
                    diving.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.availableEvent,null)));
                }
                if(jsonArray.getJSONObject(0).getString("Surfing").equals("1")){
                    Log.d("surfing",jsonArray.getJSONObject(0).getString("Surfing"));
                    surfing.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.availableEvent,null)));
                }
                if(jsonArray.getJSONObject(0).getString("BananaBoat").equals("1")){
                    BananaBoat.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.availableEvent,null)));
                }
                if(jsonArray.getJSONObject(0).getString("Jetski").equals("1")){
                    JetSki.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.availableEvent,null)));
                }
                if(jsonArray.getJSONObject(0).getString("AquaBoard").equals("1")){
                    AquaBoard.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.availableEvent,null)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },
                error -> Log.d("keywordSearchError", error.toString())) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("name", beachName);
                data.put("tableName", tableName);
                return data;
            }
        };
        RequestQueue keywordRequest = Volley.newRequestQueue(view.getContext());
        keywordRequest.add(getWeather);
        return view;
    }
    private void dialogSetText(TextView column,String placeholder,String data,String unit){
        if(unit == null)
            column.setText(MessageFormat.format("{0}{1}", placeholder, data));
        else
            column.setText(MessageFormat.format("{0}{1}{2}", placeholder, data, unit));
    }
}
