package com.example.a5dayappchat2.Activity;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

public class ViewHolderActivityImage extends RecyclerView.ViewHolder {

    public ImageView ivPictureDoings;

    public ViewHolderActivityImage(@NonNull View itemView) {
        super(itemView);

        ivPictureDoings = itemView.findViewById(R.id.ivPictureDoings);
    }
}
