package com.example.a5dayappchat2.Activity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolderMemberActivity extends  RecyclerView.ViewHolder{

    public CircleImageView civProfileActvityMember;
    public TextView tvNameActiviyMember;

    public ViewHolderMemberActivity(@NonNull View itemView) {
        super(itemView);

        civProfileActvityMember = itemView.findViewById(R.id.civProfileActvityMember);
        tvNameActiviyMember = itemView.findViewById(R.id.tvPlanName);


    }
}
