package com.example.intro.firebase;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.intro.R;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.CustomViewHolder> {

    private ArrayList<Context> arrayList;
    private DetailList context;//이부분 이상함 oncreate에서 this면 context로 넘어가야하는데 context가 아닌 MainActivity반환

    public DetailAdapter(ArrayList<Context> arrayList, DetailList context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipe,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getImage())
                .into(holder.iv_Image);
        holder.tv_Context.setText(arrayList.get(position).getRecipe());
        holder.tv_Context.setTextSize(15);
        holder.tv_Context.setTextColor(Color.BLACK);

    }

    @Override
    public int getItemCount() {
        return (arrayList!=null ? arrayList.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_Image;
        TextView tv_Context;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_Image=itemView.findViewById(R.id.iv_image);
            this.tv_Context=itemView.findViewById(R.id.Context);
        }
    }
}
