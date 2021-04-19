package com.example.opendata_app;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class ViewDialog extends DialogFragment {
    public String beachName;
    public String tableName;
    public ViewDialog(String beachName, String tableName){
        this.beachName = beachName;
        this.tableName = tableName;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.testing_layout, container);

        StringRequest keywordSearch = new StringRequest(Request.Method.POST, "http://120.119.77.72:8080/api/getAreaWeather", response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                }
                Log.d("viewdialog",response);
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
        keywordRequest.add(keywordSearch);
        return view;
    }
}
