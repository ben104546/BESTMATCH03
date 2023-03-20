package com.example.a5dayappchat2.Activity.Doing;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

public class CaladarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public final TextView tvDoingDay;
    private  final  CalandarViewAdapter.OnItemListener onItemListener;

    public CaladarViewHolder(@NonNull View itemView, CalandarViewAdapter.OnItemListener onItemListener) {
        super(itemView);
        tvDoingDay = itemView.findViewById(R.id.tvDoingDay);

        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition(),  (String) tvDoingDay.getText());
    }

}
