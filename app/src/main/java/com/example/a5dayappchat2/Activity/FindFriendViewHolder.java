package com.example.a5dayappchat2.Activity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendViewHolder extends RecyclerView.ViewHolder {

    CircleImageView ciProfileImage;
    TextView tvUsername,tvDescription;

    public FindFriendViewHolder(@NonNull View itemView) {
        super(itemView);

        ciProfileImage = itemView.findViewById(R.id.ciProfileImage);
        tvUsername = itemView.findViewById(R.id.tvUsername);
        tvDescription = itemView.findViewById(R.id.tvDescription);



    }
}
