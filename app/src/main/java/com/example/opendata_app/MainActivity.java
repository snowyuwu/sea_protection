package com.example.opendata_app;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.SimpleOnSearchActionListener;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.opendata_app.APIconnect.responseAnalysis;

public class MainActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public MyAdapter myadapter;
    public Data mData;
    public Context mContext;
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

        String tableName = "beachpark";
        String URL_randomPlace = "http://120.119.77.72:8080/api/getRandomPlace";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_randomPlace, response -> {
            try {
                mData = responseAnalysis(response, mList);
                beachDataList.addAll(mData.getAllData());
                myadapter = new MyAdapter(beachDataList,fragmentManager,"beachpark");
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(myadapter);

                mContext = recyclerView.getContext();

                MaterialSearchBar search = findViewById(R.id.searchBar);
                search.addTextChangeListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (count != 0) {
                            List<String> searchText = new ArrayList<>();
                            StringRequest keywordSearch = new StringRequest(Request.Method.POST, "http://120.119.77.72:8080/api/keyWordSearch", response -> {
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
                        Toast.makeText(MainActivity.this, "Search " + text.toString(), Toast.LENGTH_SHORT).show();
                        beachDataList.clear();
                        myadapter.notifyDataSetChanged();
                    }
                });

                ImageButton website = findViewById(R.id.website);
                website.setOnClickListener(v -> {
                    CustomNotification.NotificationTrigger(mContext);
                    MediaPlayer mp = MediaPlayer.create(mContext, R.raw.placeholder);
                    mp.start();

                    IOSDialog.Builder builder = new IOSDialog.Builder(mContext);
                    builder.setTitle("颱風海上警報")
                            .setMessage("您的所在場所出現颱風海上警報，\n請即時做好防範措施！")
                            .setPositiveButton("OK", (dialog, which) -> mp.stop())
                            .setNegativeButton("Cancel", (dialog, which) -> mp.stop())
                            .setCancelable(false)
                            .show();
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

    private void keywordSearch(String s) {
        StringRequest keywordSearch = new StringRequest(Request.Method.POST, "http://120.119.77.72:8080/api/keyWordSearch", response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++)
                    Log.d("keywordSearch", jsonArray.getJSONObject(i).getString("Area"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        },
                error -> Toast.makeText(this, "AddTime Error!" + error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("searchString", s);
                return data;
            }
        };
        RequestQueue keywordRequest = Volley.newRequestQueue(this);
        keywordRequest.add(keywordSearch);
    }
}