package com.example.a5dayappchat2.Activity.Sticker_Sotre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.a5dayappchat2.Activity.Doing.AllDoings;
import com.example.a5dayappchat2.Activity.Doing.DoingsJoined;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.ActivityDoingsMainRealBinding;
import com.example.a5dayappchat2.databinding.ActivityMenuStickerMainBinding;
import com.example.a5dayappchat2.databinding.FragmentAllDoingsBinding;
import com.example.a5dayappchat2.databinding.FragmentDoingsJoinedBinding;
import com.example.a5dayappchat2.databinding.FragmentStickerBasketBinding;
import com.example.a5dayappchat2.databinding.FragmentStickerStoreMainBinding;
import com.example.a5dayappchat2.databinding.FragmentStickerUserDataBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MenuStickerMain extends AppCompatActivity {
    private ActivityMenuStickerMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuStickerMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        FragmentStickerStoreMainBinding fragmentStickerStoreMainBinding = FragmentStickerStoreMainBinding.inflate(getLayoutInflater());
        View view2 = fragmentStickerStoreMainBinding.getRoot();
        setContentView(view2);

        FragmentStickerBasketBinding fragmentStickerBasketBinding = FragmentStickerBasketBinding.inflate(getLayoutInflater());
        View view3 = fragmentStickerBasketBinding.getRoot();
        setContentView(view3);


        setContentView(view);



        replaceFragment(new StickerStoreMain());


        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.STStore:
                        replaceFragment(new StickerStoreMain());
                        break;
                    case R.id.STBasket:
                        replaceFragment(new Sticker_Basket());
                        break;
                    case R.id.StOrder:
                        replaceFragment(new StickerOrder());
                        break;

                }
                return true;
            }
        });



    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}