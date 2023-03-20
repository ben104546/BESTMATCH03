package com.example.a5dayappchat2.Activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;


public class StickerStoreViewHolder extends RecyclerView.ViewHolder {

      public ImageView ivStickerIcon;
      public TextView tvStickerName;

    public StickerStoreViewHolder(@NonNull View itemView) {
        super(itemView);

        ivStickerIcon = itemView.findViewById(R.id.ivStickerIcon);
        tvStickerName = itemView.findViewById(R.id.tvStickerName);
    }
}
