package com.example.a5dayappchat2.Activity.Sticker_Sotre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.a5dayappchat2.Activity.Sticker_Sotre.Payment.Chooes_bank;
import com.example.a5dayappchat2.Activity.Sticker_Sotre.Payment.SendDataPayMent;
import com.example.a5dayappchat2.Activity.Sticker_Sotre.Payment.ShowPayWay;
import com.example.a5dayappchat2.Activity.Sticker_Sotre.Payment.ShowStatusPayment;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.ActivityStickerPaymentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class StickerPayment extends AppCompatActivity {

    private ActivityStickerPaymentBinding binding;
    String OrderKey;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    DatabaseReference mOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStickerPaymentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        OrderKey = getIntent().getStringExtra("OrderKey");

        mOrder = FirebaseDatabase.getInstance().getReference().child("Order");

        ChosePage();




    }


    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }




    private void ChosePage() {
        mOrder.child(mUser.getUid()).child(OrderKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    String OrderStatus = snapshot.child("OderStatus").getValue().toString();


                    if(OrderStatus.equals("ChosePaymentWay")){
                        try {
                            Chooes_bank fragment = new Chooes_bank();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }catch (Exception e){
                            //
                        }


                    }else if(OrderStatus.equals("ShowPayWay")){
                        try {
                            ShowPayWay fragment = new ShowPayWay();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }catch (Exception e){
                            //
                        }


                    }else if(OrderStatus.equals("SendDataPayMent")){
                        try {
                            SendDataPayMent fragment = new SendDataPayMent();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }catch (Exception e){
                    //
                        }


                    }else if(OrderStatus.equals("ShowStatusPayment")){
                        try {
                            ShowStatusPayment fragment = new ShowStatusPayment();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }catch (Exception e){
                            //
                        }


                    }else if(OrderStatus.equals("Correct")){
                        try {
                            ShowStatusPayment fragment = new ShowStatusPayment();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }catch (Exception e){
                            //
                        }


                    }else if(OrderStatus.equals("InCorrect")){
                        try {
                            ShowStatusPayment fragment = new ShowStatusPayment();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }catch (Exception e){
                            //
                        }


                    }







                }else {
                    Toast.makeText(StickerPayment.this, Constant.ERROR_TEXT_NOT_EXISTS, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StickerPayment.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}