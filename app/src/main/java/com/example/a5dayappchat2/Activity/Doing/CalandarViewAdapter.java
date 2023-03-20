package com.example.a5dayappchat2.Activity.Doing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

import java.util.ArrayList;

public class CalandarViewAdapter extends RecyclerView.Adapter<CaladarViewHolder> {

    private final ArrayList<String> DayOFMonth;
    private final OnItemListener onItemListener;


    public CalandarViewAdapter(ArrayList<String> dayOFMonth, OnItemListener onItemListener) {
        this.DayOFMonth = dayOFMonth;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CaladarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.doing_calendar_cell,parent,false);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight()*0.166666666);

        return new CaladarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CaladarViewHolder holder, int position) {
        holder.tvDoingDay.setText(DayOFMonth.get(position));
    }


    @Override
    public int getItemCount() {
        return DayOFMonth.size();
    }

    public interface OnItemListener{
        void onItemClick(int position, String dayText);
    }
}
