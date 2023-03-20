package com.example.a5dayappchat2.Activity;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    CircleImageView profileImage;
    ImageView postImage,likeImage,commentImage,commentSends;
    TextView username,timeAgo,postDes,likeCount,commentCount;
    EditText inputComments;

    public static RecyclerView recyclerView;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage = itemView.findViewById(R.id.profile_Image_Post);
        postImage = itemView.findViewById(R.id.postImage);
        username = itemView.findViewById(R.id.profileUserNamePost);
        timeAgo = itemView.findViewById(R.id.timeAgo);
        postDes = itemView.findViewById(R.id.postDesc);
        likeImage = itemView.findViewById(R.id.ivLike);
        commentImage = itemView.findViewById(R.id.ivComment);
        likeCount = itemView.findViewById(R.id.likeCounter);
        commentCount = itemView.findViewById(R.id.commentCount);
        commentSends = itemView.findViewById(R.id.ivSendComment);
        inputComments = itemView.findViewById(R.id.etComment);
        recyclerView = itemView.findViewById(R.id.recyclerViewComment);


    }

    public void countLike(String postKey, String uid, DatabaseReference likeRef) {
        likeRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int totalLike = (int) snapshot.getChildrenCount();
                    likeCount.setText(totalLike+"");
                }else {
                    likeCount.setText("0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        likeRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(uid).exists()){
                    likeImage.setColorFilter(Color.parseColor("#533483"));

                }else {
                    likeImage.setColorFilter(Color.GRAY);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void countComment(String postKey, String uid, DatabaseReference commentRef) {
        commentRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int totalComment = (int) snapshot.getChildrenCount();
                    commentCount.setText(totalComment+"");
                }else {
                    commentCount.setText("0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
