package com.example.a5dayappchat2.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.a5dayappchat2.Activity.Utills.MemberDoings;
import com.example.a5dayappchat2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberDoingsAdapter extends ArrayAdapter<MemberDoings> {


    public MemberDoingsAdapter(@NonNull Context context, ArrayList<MemberDoings> dataModalArrayList) {
        super(context, 0, dataModalArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // below line is use to inflate the
        // layout for our item of list view.
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.single_view_grid_member, parent, false);
        }



        // after inflating an item of listview item
        // we are getting data from array list inside
        // our modal class.
        MemberDoings memberDoings = getItem(position);


        // initializing our UI components of list view item.
        TextView NameUser = listitemView.findViewById(R.id.tvUserName);
        CircleImageView ProfileUser = listitemView.findViewById(R.id.ciDoingProfile);

        // after initializing our items we are
        // setting data to our view.
        // below line is use to set data to our text view.
        NameUser.setText(memberDoings.getUsername());

        // in below line we are using Picasso to load image
        // from URL in our Image VIew.
        Picasso.get().load(memberDoings.getProfileImage()).into(ProfileUser);

        // below line is use to add item
        // click listener for our item of list view.
        listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on the item click on our list view.
                // we are displaying a toast message.
            }
        });
        return listitemView;
    }
}
