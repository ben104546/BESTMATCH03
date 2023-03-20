package com.example.a5dayappchat2.Activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;
import com.google.android.material.button.MaterialButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMyViewHolder extends RecyclerView.ViewHolder {

    CircleImageView FirstUserImage, SecondUserImage;
    TextView FirstTextUser,SecondTextUser;
    ImageView firstImageSend,secondImageSend;
    VideoView firstVideoViewChat,secondVideoViewChat;
    ImageView ivFirstMp3,ivSecondMp3;
    ImageView ivFirstPdf,ivSecondPdf;
    ConstraintLayout layoutFirstButton,layoutSecondButton;
    ImageView ivFirstSave,ivSecondtSave,ivSecondtDel;
    ImageView firstSticker,secondSticker;

    MaterialButton btViewIcon;

    public ChatMyViewHolder(@NonNull View itemView) {
        super(itemView);

        FirstUserImage = itemView.findViewById(R.id.firstUserProfileImage);
        SecondUserImage = itemView.findViewById(R.id.secondsUserProfileImage);

        FirstTextUser = itemView.findViewById(R.id.tvHisText);
        SecondTextUser = itemView.findViewById(R.id.tvMeText);

        firstImageSend = itemView.findViewById(R.id.firstImageSend);
        secondImageSend = itemView.findViewById(R.id.secondImageSend);

        firstVideoViewChat = itemView.findViewById(R.id.firstVideoViewChat);
        secondVideoViewChat = itemView.findViewById(R.id.secondVideoViewChat);

        ivFirstMp3 = itemView.findViewById(R.id.ivFirstMp3);
        ivSecondMp3 = itemView.findViewById(R.id.ivSecondMp3);

        ivFirstPdf = itemView.findViewById(R.id.ivFirstPdf);
        ivSecondPdf = itemView.findViewById(R.id.ivSecondPdf);

        layoutFirstButton = itemView.findViewById(R.id.layoutFirstButton);
        layoutSecondButton = itemView.findViewById(R.id.layoutSecondButton);
        ivFirstSave = itemView.findViewById(R.id.ivFirstSave);
        ivSecondtSave = itemView.findViewById(R.id.ivSecondtSave);
        ivSecondtDel = itemView.findViewById(R.id.ivSecondtDel);

        firstSticker = itemView.findViewById(R.id.firstSticker);
        secondSticker = itemView.findViewById(R.id.secondSticker);

        btViewIcon = itemView.findViewById(R.id.btViewIcon);






    }
}
