package com.example.a5dayappchat2.Activity.createGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class VpViewHolder extends FragmentStateAdapter {


    public VpViewHolder(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:

                return new create_group_picture();
            case 1:

                return new create_group_color();
            default:

                return new create_group_picture();
        }


    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
