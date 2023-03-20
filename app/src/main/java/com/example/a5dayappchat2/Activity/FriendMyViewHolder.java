package com.example.a5dayappchat2.Activity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendMyViewHolder extends RecyclerView.ViewHolder {

    CircleImageView profileImageUrl;
    TextView username , description;
    View vStatus;
    public FriendMyViewHolder(@NonNull View itemView) {
        super(itemView);

        profileImageUrl = itemView.findViewById(R.id.ciProfileImage);
        username = itemView.findViewById(R.id.tvUsername);
        description = itemView.findViewById(R.id.tvDescription);
        vStatus = itemView.findViewById(R.id.vStatus);

    }
}
