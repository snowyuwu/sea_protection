package com.example.opendata_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ligl.android.widget.iosdialog.IOSDialog;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CustomViewHolder> {
    private Context context;
    List<BeachData> mList;
    FragmentManager fragmentManager;
    String tableName;
    
    public MyAdapter(List<BeachData> mList, FragmentManager fragmentManager, String tableName){
        this.mList = mList;
        this.fragmentManager = fragmentManager;
        this.tableName = tableName;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        context = itemView.getContext();
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(12,12,12,12);
        holder.card_view.setLayoutParams(layoutParams);

        //下面的寫法是從抓來的資料一個一個放到對應的一個cardview中，
        String test = mList.get(position).getImage();
        String uri = "http://120.119.77.72:8080/images/beachpark_image/"+test;
        Picasso.get().load(uri).into(holder.viewImage);
        holder.beachName.setText(mList.get(position).getArea());
        holder.location.setText(mList.get(position).getCity());
        if(!mList.get(position).getSwim().equals("0")) {
            holder.Swim.setColorFilter(ContextCompat.getColor(context,
                    R.color.UnavailableEvent), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        if(!mList.get(position).getDiving().equals("0")) {
            holder.Dive.setColorFilter(ContextCompat.getColor(context,
                    R.color.UnavailableEvent), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        if(!mList.get(position).getSurfing().equals("0")) {
            holder.Surf.setColorFilter(ContextCompat.getColor(context,
                    R.color.UnavailableEvent), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        if(!mList.get(position).getBananaBoat().equals("0")) {
            holder.Banana.setColorFilter(ContextCompat.getColor(context,
                    R.color.UnavailableEvent), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        if(!mList.get(position).getJetSki().equals("0")) {
            holder.Jet.setColorFilter(ContextCompat.getColor(context,
                    R.color.UnavailableEvent), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        if(!mList.get(position).getAquaBoard().equals("0")) {
            holder.Aqua.setColorFilter(ContextCompat.getColor(context,
                    R.color.UnavailableEvent), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        holder.card_view.setOnClickListener(view -> {
            String beachName = holder.beachName.getText().toString();
            ViewDialog viewDialog = new ViewDialog(beachName,tableName);
            viewDialog.show(fragmentManager,"dialog");
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView viewImage,Swim,Dive,Surf,Banana,Jet,Aqua;
        private TextView beachName,location;
        private CardView card_view;

        public CustomViewHolder(View x) {
            super(x);
            viewImage = x.findViewById(R.id.ViewImage);
            beachName = x.findViewById(R.id.beachName);
            location = x.findViewById(R.id.location);
            card_view = x.findViewById(R.id.card_view);
            Swim = x.findViewById(R.id.swimming);
            Dive = x.findViewById(R.id.diving);
            Surf = x.findViewById(R.id.surfing);
            Banana = x.findViewById(R.id.BananaBoat);
            Jet = x.findViewById(R.id.JetSki);
            Aqua = x.findViewById(R.id.AquaBoard);
        }
    }
}

