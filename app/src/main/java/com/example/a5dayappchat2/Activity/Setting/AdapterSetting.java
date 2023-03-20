package com.example.a5dayappchat2.Activity.Setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

public class AdapterSetting extends RecyclerView.Adapter<AdapterSetting.ViewHolder>{

    Context context;
    String[] SettingName;
    int[] ImageIcon;
    String Boss;

    private final AdapterSetting.OnItemListener onItemListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ivSettingGroup;
        TextView tvSettingName;
        private  final  AdapterSetting.OnItemListener onItemListener;

        public ViewHolder(@NonNull View itemView,AdapterSetting.OnItemListener onItemListener) {
            super(itemView);

            ivSettingGroup = itemView.findViewById(R.id.ivSettingGroup);
            tvSettingName = itemView.findViewById(R.id.tvSettingName);

            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }


    public AdapterSetting(Context context, String[] settingName, int[] imageIcon, String boss, AdapterSetting.OnItemListener onItemListener) {
        this.context = context;
        SettingName = settingName;
        ImageIcon = imageIcon;
        Boss = boss;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_view_group_setting, parent, false);

        ViewHolder viewHolder = new ViewHolder(view,onItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvSettingName.setText(SettingName[position]);
        holder.ivSettingGroup.setImageResource(ImageIcon[position]);

        if(Boss.equals("B")){
            if (position == 0 || position == 3 || position == 4){
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }else {
                holder.itemView.setVisibility(View.VISIBLE);
            }
        }else {
            holder.itemView.setVisibility(View.VISIBLE);
        }

    }



    @Override
    public int getItemCount() {
        return SettingName.length;
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }
}