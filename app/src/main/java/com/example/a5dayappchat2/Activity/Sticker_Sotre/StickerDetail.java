package com.example.a5dayappchat2.Activity.Sticker_Sotre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a5dayappchat2.Activity.StickerAllListViewHolder;
import com.example.a5dayappchat2.Activity.StickerStoreViewHolder;
import com.example.a5dayappchat2.Activity.Utills.StickerAllList;
import com.example.a5dayappchat2.Activity.Utills.StickerStore;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class StickerDetail extends AppCompatActivity {

    DatabaseReference mStickerStoreRef,mStickerBasket,mOrder;
    ImageView ivStickerIconDetail;
    TextView ivStickerNameDetail,tvStickerDesDetail,tvStickerPrice;
    Button btBuyStickers,btAddStickerInBasket;
    RecyclerView recyclerViewStickerAll;



    String StickerKey;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    FirebaseRecyclerOptions<StickerAllList> optionsSticker;
    FirebaseRecyclerAdapter<StickerAllList, StickerAllListViewHolder> adapterSticker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_detail);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mStickerStoreRef = FirebaseDatabase.getInstance().getReference().child("Sticker");
        mStickerBasket = FirebaseDatabase.getInstance().getReference().child("Sticker Basket");

        ivStickerIconDetail = findViewById(R.id.ivStickerIconDetail);
        ivStickerNameDetail = findViewById(R.id.ivStickerNameDetail);
        tvStickerDesDetail = findViewById(R.id.tvStickerDesDetail);
        tvStickerPrice = findViewById(R.id.tvStickerPrice);
        btBuyStickers = findViewById(R.id.btBuyStickers);
        btAddStickerInBasket = findViewById(R.id.btAddStickerInBasket);
        recyclerViewStickerAll = findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerViewStickerAll.setLayoutManager(layoutManager);

        mOrder = FirebaseDatabase.getInstance().getReference().child("Order");


        StickerKey = getIntent().getStringExtra("StickerKey");

        btBuyStickers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuySticker();
            }
        });

        btAddStickerInBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddStickerInBasket();
            }
        });

        ShowSticker();
        ShowListSticker();
    }

    private void AddStickerInBasket() {

        mStickerBasket.child(mUser.getUid()).child(StickerKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(StickerDetail.this, "HAVE STICKER IN BASKET", Toast.LENGTH_SHORT).show();
                }else {
                    HashMap hashMap = new HashMap();
                    hashMap.put("StickerId",StickerKey);
                    hashMap.put("StikcerStatusCheck","false");


                    mStickerBasket.child(mUser.getUid()).child(StickerKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            Toast.makeText(StickerDetail.this, "Add Sticker In Basket", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(StickerDetail.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StickerDetail.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });





    }

    private void BuySticker() {
        HashMap hashMap = new HashMap();
        hashMap.put("OderStatus","ChosePaymentWay");

        final String key = mOrder.push().getKey();
        Toast.makeText(StickerDetail.this, key, Toast.LENGTH_SHORT).show();

        mOrder.child(mUser.getUid()).child(key).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Log.d("ASDDADADAD",StickerKey.toString());

                AddStickerKeyInOrder(StickerKey,key);

                Intent intent = new Intent(StickerDetail.this,StickerPayment.class);
                intent.putExtra("OrderKey", key);

                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StickerDetail.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



/*
        Intent intent = new Intent(StickerDetail.this,StickerPayment.class);
        intent.putExtra("StickerKey",StickerKey);
        startActivity(intent);

 */







    }

    private void AddStickerKeyInOrder(String stickerKey, String key) {

        HashMap hashMap = new HashMap();
        hashMap.put("StickerId",stickerKey);

        mOrder.child(mUser.getUid()).child(key).child("StickerId").push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                mStickerBasket.child(mUser.getUid()).child(stickerKey).removeValue();
            }
        });
    }

    private void ShowListSticker() {
        Query query = mStickerStoreRef.child(StickerKey).child("StikcerImage");
        optionsSticker = new FirebaseRecyclerOptions.Builder<StickerAllList>().setQuery(query,StickerAllList.class).build();
        adapterSticker = new FirebaseRecyclerAdapter<StickerAllList, StickerAllListViewHolder>(optionsSticker) {


            @Override
            protected void onBindViewHolder(@NonNull StickerAllListViewHolder holder, int position, @NonNull StickerAllList model) {
                Picasso.get().load(model.getImageURI()).into(holder.tvStickerOne);
            }

            @NonNull
            @Override
            public StickerAllListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_viewe_stickeralllist,parent,false);

                return new StickerAllListViewHolder(view);
            }
        };
        adapterSticker.startListening();
        recyclerViewStickerAll.setAdapter(adapterSticker);

    }

    private void ShowSticker() {
        mStickerStoreRef.child(StickerKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String StIcon = snapshot.child("ImageIcon").getValue().toString();
                    String StName = snapshot.child("NameSticker").getValue().toString();
                    String StDes = snapshot.child("DescriptionSticker").getValue().toString();
                    String Stprice = snapshot.child("StickerPrice").getValue().toString();

                    Picasso.get().load(StIcon).into(ivStickerIconDetail);
                    ivStickerNameDetail.setText(StName);
                    tvStickerDesDetail.setText(StDes);
                    tvStickerPrice.setText(Stprice + "Thai Baht");


                }else {
                    Toast.makeText(StickerDetail.this, ""+ Constant.ERROR_TEXT_NOT_EXISTS, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StickerDetail.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}