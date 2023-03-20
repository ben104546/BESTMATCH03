package com.example.a5dayappchat2.Activity.Utills;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class OnlineAndOfflineStatus extends AppCompatActivity {

    public static void Online(){
        DatabaseReference mUserRef;
        FirebaseAuth mAuth;
        FirebaseUser mUser;

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");


        HashMap hashMap = new HashMap();
        hashMap.put("status","online");

        mUserRef.child(mUser.getUid()).updateChildren(hashMap);


    }

    public static void Offline(){
        DatabaseReference mUserRef;
        FirebaseAuth mAuth;
        FirebaseUser mUser;

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap hashMap = new HashMap();
        hashMap.put("status","offline");

        mUserRef.child(mUser.getUid()).updateChildren(hashMap);
    }

}
