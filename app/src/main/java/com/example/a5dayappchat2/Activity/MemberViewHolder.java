package com.example.a5dayappchat2.Activity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberViewHolder extends RecyclerView.ViewHolder {

    CircleImageView memberProfile;
    TextView memberName,memberPosition;



    public MemberViewHolder(@NonNull View itemView) {
        super(itemView);

        memberProfile = itemView.findViewById(R.id.ciSettingProfileGroup);
        memberName = itemView.findViewById(R.id.tvMemberName);
        memberPosition = itemView.findViewById(R.id.tvPosition);




    }
}
