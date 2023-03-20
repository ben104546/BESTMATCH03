package com.example.a5dayappchat2.Activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

import cn.iwgang.countdownview.CountdownView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CalendarListViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView firstUserProfileImage;
    public TextView tvNameDoingsList;
    public TextView tvTimeDoings;
    CountdownView myCountdown,myCountdownHours,myCountdownHoursTest;

    public ImageView ivNotificOn,ivNotificOff;

    public CalendarListViewHolder(@NonNull View itemView) {
        super(itemView);

        firstUserProfileImage = itemView.findViewById(R.id.firstUserProfileImage);
        tvNameDoingsList = itemView.findViewById(R.id.tvNameDoingsList);
        tvTimeDoings = itemView.findViewById(R.id.tvTimeDoings);
        myCountdown = itemView.findViewById(R.id.myCountdown);
        myCountdownHours = itemView.findViewById(R.id.myCountdownActivity);

        ivNotificOn = itemView.findViewById(R.id.ivNotificOn);
        ivNotificOff = itemView.findViewById(R.id.ivNotificOff);





    }
}
