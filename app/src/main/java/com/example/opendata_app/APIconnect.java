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
    public static String URL_randomPlace = "http://120.119.77.72:8081/api/getRandomPlace";

    public static Data RandomPlaceResponseAnalysis(String response, List<BeachData> mList) throws JSONException {
        Log.d("response analysis", "start");
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
            String type = tmp.getString("type");
            String city = tmp.getString("name");
            mList.add(new BeachData(area, swim, diving, surfing, jetski, bananaboat, aquaboard, viewimage, type, city));
        }
        return new Data(mList);
    }

    public static Data SearchResponseAnalysis(String response, List<BeachData> mList) throws JSONException {
        JSONArray searchJSON = new JSONArray(response);
        Log.d("searchresponse",response);
        for (int i = 0; i < searchJSON.length(); i++) {
            JSONObject tmp = searchJSON.getJSONObject(i);
            Log.d("SearchResponseAnalysis", tmp.getString("Area"));
            String area = tmp.getString("Area");
            String swim = tmp.getString("Swim");
            String diving = tmp.getString("Diving");
            String surfing = tmp.getString("Surfing");
            String jetski = tmp.getString("Jetski");
            String bananaboat = tmp.getString("BananaBoat");
            String aquaboard = tmp.getString("AquaBoard");
            String viewimage = tmp.getString("image");
            String type = tmp.getString("type");
            String city = tmp.getString("name");
            mList.add(new BeachData(area, swim, diving, surfing, jetski, bananaboat, aquaboard, viewimage, type, city));
        }
        return new Data(mList);
    }

    public static Data CitySearchResponseAnalysis(String response, List<BeachData> mList, String UserCity) throws JSONException {
        Log.d("city response analysis", "start");
        JSONArray cityJSON = new JSONArray(response);
        for (int i = 0; i < cityJSON.length(); i++) {
            JSONObject tmp = cityJSON.getJSONObject(i);
            String area = tmp.getString("Area");
            String swim = tmp.getString("Swim");
            String diving = tmp.getString("Diving");
            String surfing = tmp.getString("Surfing");
            String jetski = tmp.getString("Jetski");
            String bananaboat = tmp.getString("BananaBoat");
            String aquaboard = tmp.getString("AquaBoard");
            String viewimage = tmp.getString("image2");
            String type = tmp.getString("type");
            String city = UserCity;
            Log.d("citysearch response",type);
            mList.add(new BeachData(area, swim, diving, surfing, jetski, bananaboat, aquaboard, viewimage, type, city));
        }
        return new Data(mList);
    }
}
