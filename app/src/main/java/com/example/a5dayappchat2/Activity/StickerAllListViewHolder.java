package com.example.a5dayappchat2.Activity;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

public class StickerAllListViewHolder extends RecyclerView.ViewHolder{

    public ImageView tvStickerOne;
    ImageView ivStickerIcon;

    public StickerAllListViewHolder(@NonNull View itemView) {
        super(itemView);

        tvStickerOne = itemView.findViewById(R.id.tvStickerOne);
        ivStickerIcon = itemView.findViewById(R.id.ivStickerIcon);
    }
}
