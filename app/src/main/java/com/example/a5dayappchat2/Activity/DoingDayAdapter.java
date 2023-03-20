package com.example.a5dayappchat2.Activity;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.a5dayappchat2.Activity.Utills.DetailDoingPicture;
import com.example.a5dayappchat2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DoingDayAdapter extends ArrayAdapter<DetailDoingPicture> {

    // constructor for our list view adapter.
    public DoingDayAdapter(@NonNull Context context, ArrayList<DetailDoingPicture> dataModalArrayList) {
        super(context, 0, dataModalArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // below line is use to inflate the
        // layout for our item of list view.
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.single_view_picture_doings, parent, false);
        }

        // after inflating an item of listview item
        // we are getting data from array list inside
        // our modal class.
        DetailDoingPicture detailDoingPicture = getItem(position);

        // initializing our UI components of list view item.
        ImageView courseIV = listitemView.findViewById(R.id.ivPictureDoings);


        // in below line we are using Picasso to load image
        // from URL in our Image VIew.
        Picasso.get().load(detailDoingPicture.getImageLink()).into(courseIV);

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
