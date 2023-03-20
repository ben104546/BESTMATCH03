package com.example.a5dayappchat2.Activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity  {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        HashMap hashMap = new HashMap();
        hashMap.put("status","online");

        try {
            mUserRef.child(mUser.getUid()).updateChildren(hashMap);
        }catch (Exception e){

        }




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HashMap hashMap = new HashMap();
        hashMap.put("status","offline");


        try {
            mUserRef.child(mUser.getUid()).updateChildren(hashMap);
        }catch (Exception e){

        }


    }




}
