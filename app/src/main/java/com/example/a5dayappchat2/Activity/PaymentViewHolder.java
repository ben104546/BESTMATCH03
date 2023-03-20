package com.example.a5dayappchat2.Activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

public class PaymentViewHolder extends RecyclerView.ViewHolder{

    public RadioButton rbChooseWayPayMent;
    public ImageView ivPaymentIcon;
    public TextView PaymentHead,PaymentSecond;

    public PaymentViewHolder(@NonNull View itemView) {
        super(itemView);


        rbChooseWayPayMent = itemView.findViewById(R.id.rbChooseWayPayMent);
        ivPaymentIcon = itemView.findViewById(R.id.ivPaymentIcon);
        PaymentHead = itemView.findViewById(R.id.PaymentHead);
        PaymentSecond = itemView.findViewById(R.id.PaymentSecond);
    }
}
