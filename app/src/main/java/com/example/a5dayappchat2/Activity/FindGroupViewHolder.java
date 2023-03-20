package com.example.a5dayappchat2.Activity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindGroupViewHolder extends RecyclerView.ViewHolder {

    CircleImageView circleImageProfileGroup;
    TextView textGroupName;
    ImageButton btAcGroup,btDcGroup;
    public FindGroupViewHolder(@NonNull View itemView) {
        super(itemView);

        circleImageProfileGroup = itemView.findViewById(R.id.ciSettingProfileGroup);
        textGroupName = itemView.findViewById(R.id.tvGroupName);
        btAcGroup = itemView.findViewById(R.id.btAcGroup);
        btDcGroup = itemView.findViewById(R.id.btDcGroup);

    }
}
