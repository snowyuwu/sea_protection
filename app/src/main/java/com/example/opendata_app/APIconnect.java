package com.example.opendata_app;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIconnect {
    public static String URL_randomPlace = "http://120.119.77.72:8080/api/getRandomPlace";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void randomPlace(Context context, String tableName,List<BeachData> mList) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_randomPlace, response -> {
//                JSONObject jsonObject = new JSONObject(response);
            Log.d("myresponse", response);
        },
                error -> Toast.makeText(context, "AddTime Error!" + error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("tableName", tableName);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static Data responseAnalysis(String response,List<BeachData> mList) throws JSONException {
        Log.d("response analysis","start");
        JSONObject jsonObject = new JSONObject(response);
        JSONArray test = jsonObject.getJSONArray("random");
        for (int i = 0; i < test.length(); i++) {
            JSONObject tmp = test.getJSONObject(i);
            String area = tmp.getString("Area");
            String swim = tmp.getString("Swim");
            String diving = tmp.getString("Diving");
            String surfing = tmp.getString("Surfing");
            String jetski = tmp.getString("Jetski");
            String bananaboat = tmp.getString("BananaBoat");
            String aquaboard = tmp.getString("AquaBoard");
            String viewimage = tmp.getString("image");
            String city = tmp.getString("name");
            mList.add(new BeachData(area, swim, diving, surfing, jetski, bananaboat, aquaboard, viewimage, city));
        }
        Log.d("response analysis", String.valueOf(mList.size()));
        return new Data(mList);
    }
}
