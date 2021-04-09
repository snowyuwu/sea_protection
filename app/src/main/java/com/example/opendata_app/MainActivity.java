package com.example.opendata_app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.ligl.android.widget.iosdialog.IOSDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public MyAdapter myadapter;
    public Data mData;
    public Context mContext;
    List<BeachData> beachDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        mData = Util.getData();
        beachDataList.addAll(mData.getAllData());
        myadapter = new MyAdapter(beachDataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myadapter);

        mContext = recyclerView.getContext();

        MaterialSpinner spinner = findViewById(R.id.arrange);
        spinner.setItems("以地區排序", "以活動排序", "以風力排序");
        spinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show());

        FloatingActionButton fab = findViewById(R.id.button_1);
        fab.setOnClickListener(view -> {
            CustomNotification.NotificationTrigger(mContext);
            MediaPlayer mp = MediaPlayer.create(mContext,R.raw.placeholder);
            mp.start();

            IOSDialog.Builder builder = new IOSDialog.Builder(mContext);
            builder.setTitle("已發生緊急事故")
                    .setMessage("請馬上進行緊急疏散")
                    .setPositiveButton("OK", (dialog, which) -> mp.stop())
                    .setNegativeButton("Cancel", (dialog, which) -> mp.stop())
                    .setCancelable(false)
                    .show();
        });

    }
}