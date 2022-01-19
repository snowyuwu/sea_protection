package com.example.opendata_app;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.CustomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.SimpleOnSearchActionListener;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.opendata_app.APIconnect.CitySearchResponseAnalysis;
import static com.example.opendata_app.APIconnect.RandomPlaceResponseAnalysis;
import static com.example.opendata_app.APIconnect.SearchResponseAnalysis;
import static com.kongzue.dialogx.interfaces.BaseDialog.getContext;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public MyAdapter myadapter;
    public Data mData;
    public Context mContext;
    public boolean asked = false;
    public boolean gps = false;
    public boolean viewRefresh = false;
    String userPlace;
    String choice = "-- 請選擇 --";
    String chosenLocation;
    String tmp;
    MaterialSearchBar search;
    LocationManager locationManager;
    List<BeachData> beachDataList = new ArrayList<>();
    List<BeachData> mList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        recyclerView = findViewById(R.id.recyclerView);
        mContext = recyclerView.getContext();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new CustomLocationListener(mContext);
        LocationListener SweetLocationListener = new SweetLocationListener(mContext);
        DialogX.init(mContext);
        String tableName = "beachpark";
        String URL_randomPlace = "http://120.119.77.72:8081/api/getRandomPlace";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_randomPlace, response -> {
            try {
                SessionManager sessionManager;
                sessionManager = new SessionManager(this);
                mData = RandomPlaceResponseAnalysis(response, mList);
                beachDataList.addAll(mData.getAllData());
                myadapter = new MyAdapter(beachDataList, fragmentManager, "beachpark");
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(myadapter);
                mContext = recyclerView.getContext();

                search = findViewById(R.id.searchBar);
                search.addTextChangeListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (count != 0) {
                            List<String> searchText = new ArrayList<>();
                            StringRequest keywordSearch = new StringRequest(Request.Method.POST, "http://120.119.77.72:8081/api/keyWordSearch", response -> {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Log.d("keywordSearch", jsonArray.getJSONObject(i).getString("Area"));
                                        searchText.add(jsonArray.getJSONObject(i).getString("Area"));
                                        search.updateLastSuggestions(searchText);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            },
                                    error -> Log.d("keywordSearchError", error.toString())) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> data = new HashMap<>();
                                    data.put("searchString", s.toString());
                                    return data;
                                }
                            };
                            RequestQueue keywordRequest = Volley.newRequestQueue(mContext);
                            keywordRequest.add(keywordSearch);
                        } else
                            search.hideSuggestionsList();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                search.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
                    @Override
                    public void OnItemClickListener(int position, View v) {
                        search.setText(String.valueOf(search.getLastSuggestions().get(position)));
                    }

                    @Override
                    public void OnItemDeleteListener(int position, View v) {

                    }
                });
                search.setOnSearchActionListener(new SimpleOnSearchActionListener() {
                    @Override
                    public void onSearchConfirmed(CharSequence text) {
                        StringRequest keywordSearch = new StringRequest(Request.Method.POST, "http://120.119.77.72:8081/api/areaSearch", response -> {
                            try {
                                List<BeachData> searchList = new ArrayList<>();
                                beachDataList.clear();
                                mData = SearchResponseAnalysis(response, searchList);
                                beachDataList.addAll(mData.getAllData());
                                myadapter.notifyDataSetChanged();
                                recyclerView.setAdapter(myadapter);
                            } catch (JSONException e) {
                                Log.e("keywordSearch", Log.getStackTraceString(e));
                            }
                        },
                                error -> Log.d("keywordSearchError", "error")) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> data = new HashMap<>();
                                data.put("searchArea", text.toString());
                                return data;
                            }
                        };
                        RequestQueue keywordRequest = Volley.newRequestQueue(mContext);
                        keywordRequest.add(keywordSearch);
                        Toast.makeText(MainActivity.this, "Search " + text.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                ImageView title = findViewById(R.id.title);
                title.setOnClickListener(v -> {
                    beachDataList.clear();
                    try {
                        beachDataList.clear();
                        mData = RandomPlaceResponseAnalysis(response, mList);
                        beachDataList.addAll(mData.getAllData());
                        myadapter.notifyDataSetChanged();
                        recyclerView.setAdapter(myadapter);
                        search.closeSearch();
                        search.setPlaceHolder("搜尋");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

                ImageButton notify = findViewById(R.id.notify);
                notify.setOnClickListener(v -> {
                    asked = false;
                    viewRefresh = false;
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 103);
                    } else {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, SweetLocationListener);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, SweetLocationListener);
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            View sweet = View.inflate(this, R.layout.sweetalert_custom_page1, null);
                            SweetAlertDialog sweetAlertPage1 = new SweetAlertDialog(this);
                            sweetAlertPage1.setCustomView(sweet);
                            NiceSpinner niceSpinner = (NiceSpinner) sweet.findViewById(R.id.nice_spinner);
                            List<String> dataset = new LinkedList<>(Arrays.asList("-- 請選擇 --", "海水浴場", "海釣"));
                            niceSpinner.attachDataSource(dataset);
                            niceSpinner.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {
                                choice = parent.getItemAtPosition(position).toString();
                                Log.d("choice", choice);
                                if (choice.equals("-- 請選擇 --")) {
                                    sweetAlertPage1.getButton(SweetAlertDialog.BUTTON_CONFIRM).setEnabled(false);
                                } else if (!choice.equals("-- 請選擇 --")) {
                                    List<String> place = new LinkedList<>(Collections.emptyList());
                                    sweetAlertPage1.getButton(SweetAlertDialog.BUTTON_CONFIRM).setEnabled(true);
                                    switch (choice) {
                                        case "海水浴場":
                                            tmp = "beachpark";
                                            break;
                                        case "海釣":
                                            tmp = "fish";
                                            break;
                                    }
                                    StringRequest areaPlace = new StringRequest(Request.Method.POST, "http://120.119.77.72:8081/api/areaPlaceSearch", areaPlaceResponse -> {
                                        try {
                                            JSONArray jsonArray = new JSONArray(areaPlaceResponse);
                                            place.add("-- 請選擇 --");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                place.add(jsonArray.getJSONObject(i).getString("Area"));
                                                Log.d("areaPlace", jsonArray.getJSONObject(i).getString("Area"));
                                            }
                                        } catch (JSONException e) {
                                            Log.e("keywordSearch", Log.getStackTraceString(e));
                                        }
                                    },
                                            error -> Log.d("keywordSearchError", "error")) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> data = new HashMap<>();
                                            data.put("country", userPlace);
                                            data.put("place", tmp);
                                            return data;
                                        }
                                    };
                                    RequestQueue areaPlaceRequest = Volley.newRequestQueue(getBaseContext());
                                    areaPlaceRequest.add(areaPlace);
                                    sweetAlertPage1.setConfirmClickListener(sweetAlertDialog -> {
                                        sweetAlertPage1.dismiss();
                                        View sweetPage2 = View.inflate(this, R.layout.sweetalert_custom_page2, null);
                                        SweetAlertDialog sweetAlertPage2 = new SweetAlertDialog(getContext());
                                        sweetAlertPage2.setCustomView(sweetPage2)
                                                .setConfirmText("確定");
                                        TextView areaText = sweetPage2.findViewById(R.id.areaText);
                                        areaText.setText(userPlace);
                                        TextView placeText = sweetPage2.findViewById(R.id.placeText);
                                        placeText.setText(choice);
                                        NiceSpinner nicePage2 = (NiceSpinner) sweetPage2.findViewById(R.id.nice_spinner);
                                        nicePage2.attachDataSource(place);
                                        nicePage2.setOnSpinnerItemSelectedListener((parent2, view2, position2, id2) -> {
                                            chosenLocation = parent2.getItemAtPosition(position2).toString();
                                            if (chosenLocation.equals("-- 請選擇 --")) {
                                                sweetAlertPage2.getButton(SweetAlertDialog.BUTTON_CONFIRM).setEnabled(false);
                                            } else {
                                                sweetAlertPage2.getButton(SweetAlertDialog.BUTTON_CONFIRM).setEnabled(true);
                                                sweetAlertPage2.setConfirmClickListener(sweetAlertDialog1 -> {
                                                    sweetAlertPage2.dismiss();
                                                    SweetAlertDialog sweetAlertDialog3 = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                                            .setTitleText("成功!")
                                                            .setContentText("您已關注"+userPlace+" - "+chosenLocation+"\n若有緊急情況將會通知您。");
                                                    sweetAlertDialog3.setConfirmClickListener(sweetAlertDialog2 -> {
                                                        sweetAlertDialog3.dismiss();
                                                        Handler handler = new Handler();
                                                        handler.postDelayed(() -> new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                                                .setTitleText("適宜活動變更")
                                                                .setContentText("您的所在場所「不適合」：\n游泳、潛水\n請儘量避免遊玩以上活動!")
                                                                .setConfirmText("我知道了")
                                                                .show(),5000);
                                                    });
                                                    sweetAlertDialog3.show();
                                                    Intent startIntent = new Intent(getContext(), CustomService.class);
                                                    startIntent.putExtra("location", chosenLocation);
                                                    startIntent.putExtra("place", tmp);
                                                    startService(startIntent);
                                                });
                                            }
                                        });
                                        sweetAlertPage2.show();
                                        sweetAlertPage2.getButton(SweetAlertDialog.BUTTON_CONFIRM).setEnabled(false);
                                    }).show();
                                }
                            });
                            sweetAlertPage1.show();
                            sweetAlertPage1.getButton(SweetAlertDialog.BUTTON_CONFIRM).setEnabled(false);
                        } else {
                            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("無法進行定位")
                                    .setContentText("請先開啟您的手機gps服務，\n方可進行後續操作")
                                    .setConfirmText("我知道了")
                                    .show();
                        }
                    }
                });

                ImageButton GPS = findViewById(R.id.GPS);
                GPS.setOnClickListener(v -> {
                    asked = false;
                    viewRefresh = false;
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 103);
                    } else {
                        CustomDialog.show(new OnBindView<CustomDialog>(R.layout.loading_view) {
                            @Override
                            public void onBind(CustomDialog dialog, View v) {
                                new Thread(() -> {
                                    try {
                                        sleep(1000);
                                        dialog.dismiss();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }).start();
                            }
                        });
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);    //每2秒，並且使用者移動5公尺時update一次

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },
                error -> Toast.makeText(this, "AddTime Error!" + error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("tableName", tableName);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private class CustomLocationListener implements LocationListener {
        Context context;

        public CustomLocationListener(Context context) {
            this.context = context;
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (!viewRefresh) {
                try {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
//                Log.d("GPS", "latitude :" + lat + "\nlongitude：" + lng);
                    Geocoder geocoder = new Geocoder(mContext);
                    List<Address> address = geocoder.getFromLocation(lat, lng, 1);
                    Address userLocation = address.get(0);
                    userPlace = userLocation.getAdminArea();
                    search.clearSuggestions();
                    search.setPlaceHolder(userPlace);
                    StringRequest citySearch = new StringRequest(Request.Method.POST, "http://120.119.77.72:8081/api/getAreaTotalPlace", response -> {
                        try {
                            List<BeachData> CitySearchList = new ArrayList<>();
                            beachDataList.clear();
                            mData = CitySearchResponseAnalysis(response, CitySearchList, userPlace);
                            beachDataList.addAll(mData.getAllData());
                            myadapter.notifyDataSetChanged();
                            recyclerView.setAdapter(myadapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                            error -> Log.d("keywordSearchError", "error")) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<>();
                            data.put("country", userPlace);
                            return data;
                        }
                    };
                    RequestQueue citySearchRequest = Volley.newRequestQueue(mContext);
                    citySearchRequest.add(citySearch);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                viewRefresh = true;
            }
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            Log.d("GPS", "gps turned on");
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            if (!asked) {
                asked = true;
                search.closeSearch();
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("GPS開啟")
                        .setContentText("您的定位服務尚未開啟，\n請問是否要前往開啟畫面？")
                        .setConfirmText("前往")
                        .setConfirmClickListener(sweetAlertDialog -> {
                            gps = true;
                            sweetAlertDialog.cancel();
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        })
                        .setCancelText("拒絕")
                        .setCancelClickListener(SweetAlertDialog::cancel)
                        .show();
            } else {
                Log.d("GPS", "gps turned off");
            }
        }
    }

    private class SweetLocationListener implements LocationListener {
        Context context;

        public SweetLocationListener(Context context) {
            this.context = context;
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (!viewRefresh) {
                try {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    Geocoder geocoder = new Geocoder(mContext);
                    List<Address> address = geocoder.getFromLocation(lat, lng, 1);
                    Address userLocation = address.get(0);
                    userPlace = userLocation.getAdminArea();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                viewRefresh = true;
            }
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
        }
    }

    public void onPause() {
        super.onPause();
        if (gps)
            Log.d("pause", "pausing...");
    }

    public void onResume() {
        super.onResume();
        if (gps) {
            Log.d("pause", "resuming...");
            gps = false;
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                CustomDialog.show(new OnBindView<CustomDialog>(R.layout.loading_view) {
                    @Override
                    public void onBind(CustomDialog mdialog, View v) {
                        new Thread(() -> {
                            try {
                                sleep(1000);
                                mdialog.dismiss();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }
                });
            }
        }
    }
}