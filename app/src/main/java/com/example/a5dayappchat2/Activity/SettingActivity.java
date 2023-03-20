package com.example.a5dayappchat2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a5dayappchat2.Activity.Doing.ActivityDoingsMainReal;
import com.example.a5dayappchat2.Activity.Preferences.PreferencesUser;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {

    Button invite,member,activity,leaveGroup,btRemoveGroup,btSettingGroup;
    PreferencesUser preferencesUser;
    String groupToken,userId;
    DatabaseReference mGroupRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        invite = findViewById(R.id.btInvite);
        member = findViewById(R.id.btMember);
        activity = findViewById(R.id.btActivity);
        leaveGroup = findViewById(R.id.btLeaveGroup);
        btRemoveGroup = findViewById(R.id.btRemoveGroup);
        btSettingGroup = findViewById(R.id.btSettingGroup);


        preferencesUser = new PreferencesUser(getApplicationContext());
        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");

        userId = preferencesUser.getString(Constant.USER_ID);

        groupToken = getIntent().getStringExtra("groupKeyForSetting");

        Show_HideInvite();

        btRemoveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGroupRef.child(groupToken).removeValue();
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this,InviteMemberActivity.class);
                intent.putExtra("groupTokenForInvite",groupToken);
                startActivity(intent);
            }
        });

        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this,MemberActivity.class);
                intent.putExtra("groupKeyForMember",groupToken);
                startActivity(intent);
            }
        });

        btSettingGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this,SettingGroup.class);
                intent.putExtra("groupKeyForSetting",groupToken);
                startActivity(intent);
            }
        });

        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SettingActivity.this, ActivityDoingsMainReal.class);
                intent.putExtra("GroupKey",groupToken);
                startActivity(intent);
            }
        });

        leaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap hashMap = new HashMap();
                hashMap.put("Status","OutGroup");
                mGroupRef.child(groupToken).child("Member").child(userId).updateChildren(hashMap);
            }
        });




    }


    private void Show_HideInvite() {
        mGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String position = snapshot.child(groupToken).child("Member").child(userId).child("position").getValue().toString();

                    if(position.equals("Admin Group")){
                        invite.setVisibility(View.VISIBLE);
                        btRemoveGroup.setVisibility(View.VISIBLE);
                        btSettingGroup.setVisibility(View.VISIBLE);

                    }else if(position.equals("Member Group")){
                        invite.setVisibility(View.GONE);
                        btRemoveGroup.setVisibility(View.GONE);
                        btSettingGroup.setVisibility(View.GONE);

                    }else {
                        Toast.makeText(SettingActivity.this, "Position Error", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(SettingActivity.this, "Load Data Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SettingActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}