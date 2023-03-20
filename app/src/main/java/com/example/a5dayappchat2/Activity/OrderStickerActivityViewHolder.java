package com.example.a5dayappchat2.Activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;


public class OrderStickerActivityViewHolder extends RecyclerView.ViewHolder{
    public TextView tvSumPrice;
    public Button btrstatusorder;
    public RecyclerView recyclerViewProductlist;

    public OrderStickerActivityViewHolder(@NonNull View itemView) {
        super(itemView);

        tvSumPrice = itemView.findViewById(R.id.tvSumPrice);
        btrstatusorder = itemView.findViewById(R.id.btrstatusorder);
        recyclerViewProductlist = itemView.findViewById(R.id.recyclerViewProductlist);

    }
}
