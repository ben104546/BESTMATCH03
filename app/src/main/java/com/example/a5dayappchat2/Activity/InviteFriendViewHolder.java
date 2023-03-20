package com.example.a5dayappchat2.Activity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class InviteFriendViewHolder extends RecyclerView.ViewHolder{

    CircleImageView FriendProfile;
    TextView FriendName;
    ImageButton AddMember;

    public InviteFriendViewHolder(@NonNull View itemView) {
        super(itemView);

        FriendProfile = itemView.findViewById(R.id.ciSettingProfileGroup);
        FriendName = itemView.findViewById(R.id.tvMemberName);
        AddMember = itemView.findViewById(R.id.btAddMember);



    }
}
