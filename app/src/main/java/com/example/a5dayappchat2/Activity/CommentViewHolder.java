package com.example.a5dayappchat2.Activity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentViewHolder extends RecyclerView.ViewHolder {

    CircleImageView profileImage;
    TextView username,comment;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);

        profileImage = itemView.findViewById(R.id.civProfileImageComment);
        username = itemView.findViewById(R.id.tvCommentUsername);
        comment = itemView.findViewById(R.id.tvComment);
    }


}
