package com.example.a5dayappchat2.Activity.Sticker_Sotre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.a5dayappchat2.Activity.StickerStoreViewHolder;
import com.example.a5dayappchat2.Activity.Utills.StickerStore;
import com.example.a5dayappchat2.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class StickerMain extends AppCompatActivity {

    FirebaseRecyclerOptions<StickerStore> optionsSticker;
    FirebaseRecyclerAdapter<StickerStore, StickerStoreViewHolder> adapterSticker;

    RecyclerView recyclerViewStickerStore;

    DatabaseReference mStickerStoreRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_main);

        recyclerViewStickerStore = findViewById(R.id.recyclerViewStickerStore);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewStickerStore.setLayoutManager(layoutManager);

        mStickerStoreRef = FirebaseDatabase.getInstance().getReference().child("Sticker");

        LoadSticker("");



    }

    private void LoadSticker(String s) {
        Query query = mStickerStoreRef.orderByChild("NameSticker").startAt(s).endAt(s+"\uf8ff");
        optionsSticker = new FirebaseRecyclerOptions.Builder<StickerStore>().setQuery(query,StickerStore.class).build();
        adapterSticker = new FirebaseRecyclerAdapter<StickerStore, StickerStoreViewHolder>(optionsSticker) {


            @Override
            protected void onBindViewHolder(@NonNull StickerStoreViewHolder holder, int position, @NonNull StickerStore model) {


                holder.tvStickerName.setText(model.getNameSticker());
                Picasso.get().load(model.getImageIcon()).into(holder.ivStickerIcon);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String StickerKey = getRef(position).getKey().toString();

                        Intent intent = new Intent(StickerMain.this,StickerDetail.class);
                        intent.putExtra("StickerKey",StickerKey);
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public StickerStoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_cell_sticker_store,parent,false);

                return new StickerStoreViewHolder(view);
            }
        };
        adapterSticker.startListening();
        recyclerViewStickerStore.setAdapter(adapterSticker);

    }
}