package com.example.a5dayappchat2.Activity;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

public class HearderStickerChatViewHolder extends RecyclerView.ViewHolder{

    ImageView ivStickerIcon;

    public HearderStickerChatViewHolder(@NonNull View itemView) {
        super(itemView);

        ivStickerIcon = itemView.findViewById(R.id.ivStickerIcon);
    }
}
