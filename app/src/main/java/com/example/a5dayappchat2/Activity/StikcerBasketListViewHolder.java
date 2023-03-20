package com.example.a5dayappchat2.Activity;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class StikcerBasketListViewHolder extends RecyclerView.ViewHolder{

    public RadioButton rbSlectStikcer;
    public CircleImageView ciIvStikcerIcon;
    public TextView tvInfoSticker,tvPrice;

    public StikcerBasketListViewHolder(@NonNull View itemView) {
        super(itemView);

        rbSlectStikcer = itemView.findViewById(R.id.rbSlectStikcer);
        ciIvStikcerIcon = itemView.findViewById(R.id.ciIvStikcerIcon);
        tvInfoSticker = itemView.findViewById(R.id.tvInfoSticker);
        tvPrice = itemView.findViewById(R.id.tvPrice);



    }
}
