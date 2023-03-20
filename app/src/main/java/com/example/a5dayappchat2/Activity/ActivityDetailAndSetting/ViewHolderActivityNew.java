package com.example.a5dayappchat2.Activity.ActivityDetailAndSetting;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

public class ViewHolderActivityNew extends RecyclerView.ViewHolder {

    TextView tvPlanName,tvPlanDate;
    RadioButton rbPlanCheck;
    ImageButton tbPlanAdd;
    public static RecyclerView recyclerViewSubTask;

    CardView cardViewDelete;
    TextView tvDelete;

    CardView cardView10;
    ImageButton tbClose;
    Button btAddSubPlan,btEditPlan,btAddPeople;

    ConstraintLayout layoutSettingPlan;
    Button btDeletePlan;
    ImageView imageView19;


    Button btDeleteSubPlan;
    ImageView imageView20;

    public ViewHolderActivityNew(@NonNull View itemView) {
        super(itemView);

        tvPlanName = itemView.findViewById(R.id.tvPlanName);
        tvPlanDate = itemView.findViewById(R.id.tvPlanDate);
        rbPlanCheck = itemView.findViewById(R.id.rbPlanCheck);
        tbPlanAdd = itemView.findViewById(R.id.tbPlanAdd);

        recyclerViewSubTask = itemView.findViewById(R.id.recyclerViewSubTask);

        cardView10 = itemView.findViewById(R.id.cardView10);
        tbClose = itemView.findViewById(R.id.tbClose);
        btAddSubPlan = itemView.findViewById(R.id.btAddSubPlan);
        btEditPlan = itemView.findViewById(R.id.btEditPlan);
        btAddPeople = itemView.findViewById(R.id.btAddPeople);


        layoutSettingPlan = itemView.findViewById(R.id.layoutSettingPlan);
        btDeletePlan = itemView.findViewById(R.id.btDeletePlan);
        imageView19 = itemView.findViewById(R.id.imageView19);

        btDeleteSubPlan = itemView.findViewById(R.id.btDeleteSubPlan);
        imageView20 = itemView.findViewById(R.id.imageView20);


    }



}
