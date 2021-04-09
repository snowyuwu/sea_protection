package com.example.opendata_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ligl.android.widget.iosdialog.IOSDialog;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CustomViewHolder> {
    private Context context;
    List<BeachData> mList = new ArrayList<>();
    
    public MyAdapter(List<BeachData> mList){
        this.mList = mList;
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
        holder.card_view.setOnClickListener(onClickListener());


        //下面的寫法是從抓來的資料一個一個放到對應的一個cardview中，
        // 寫法：holder.卡片的哪個部分.setText(mList.get(position).get那個部分對應的資料名稱);
        holder.viewImage.setImageResource(mList.get(position).getView());
        holder.beachName.setText(mList.get(position).getName());
        holder.location.setText(mList.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private View.OnClickListener onClickListener(){
        return v -> {
            IOSDialog dialog = new IOSDialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            View customView = View.inflate(context,R.layout.dialog_view,null);
            IOSDialog.Builder builder = new IOSDialog.Builder(context);
            builder.setContentView(customView);
            builder.setPositiveButton("OK", null);
            builder.setNegativeButton("Cancel", null);

            dialog = builder.create();
            dialog.show();
        };
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView viewImage;
        private TextView beachName,location;
        private CardView card_view;

        public CustomViewHolder(View x) {
            super(x);
            viewImage = x.findViewById(R.id.ViewImage);
            beachName = x.findViewById(R.id.beachName);
            location = x.findViewById(R.id.location);
            card_view = x.findViewById(R.id.card_view);
        }
    }
}

