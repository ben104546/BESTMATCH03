package com.example.a5dayappchat2.Activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

public class ViewHolderActivity extends RecyclerView.ViewHolder{

    TextView tvNameDoingsList,tvTimeDoings,tvPlanDescription;
    ImageView ivSettingActivtiy,ivRight;
    RadioButton rbFNPlan;
    public static RecyclerView recyclerView2;




    public ViewHolderActivity(@NonNull View itemView) {
        super(itemView);


        tvNameDoingsList = itemView.findViewById(R.id.tvNameDoingsList);
        tvTimeDoings = itemView.findViewById(R.id.tvTimeDoings);
        ivSettingActivtiy = itemView.findViewById(R.id.ivSettingActivtiy);
        rbFNPlan = itemView.findViewById(R.id.rbFNPlan);
        recyclerView2 = itemView.findViewById(R.id.recyclerViewActvity);
        tvPlanDescription = itemView.findViewById(R.id.tvPlanDescription);
        ivRight = itemView.findViewById(R.id.ivRight);






    }
}
