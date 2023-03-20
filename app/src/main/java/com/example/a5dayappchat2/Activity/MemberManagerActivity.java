package com.example.a5dayappchat2.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a5dayappchat2.Activity.Preferences.PreferencesUser;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberManagerActivity extends AppCompatActivity {

    Spinner spinner;
    String userMember,groupToken;
    CircleImageView memberProfilePicture;
    TextView memberNameText;
    Button kickMember;
    DatabaseReference mGroupRef,mUserRef;
    PreferencesUser preferencesUser;
    RadioButton rbPublicGroup,rbPrivateGroup;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    TextView tvPageTitle;
    ImageView btBackPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_manager);
        userMember = getIntent().getStringExtra("userIdForManagers");
        groupToken = getIntent().getStringExtra("groupTokenForManagers");

        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");

        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        preferencesUser = new PreferencesUser(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        memberProfilePicture = findViewById(R.id.circleImageView);
        memberNameText = findViewById(R.id.tvMemberName);
        kickMember = findViewById(R.id.btKickMember);

        tvPageTitle = findViewById(R.id.tvPageTitle);
        btBackPage = findViewById(R.id.btBackPage);


        rbPublicGroup = findViewById(R.id.rbPublicGroup);
        rbPrivateGroup = findViewById(R.id.rbPrivateGroup);


        LoadMemberProfile();
        ON_OFF_KickButton();

        btBackPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvPageTitle.setText("Edit Member");

        kickMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KickMember();
            }
        });

        rbPublicGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetPositionAdmin();
            }
        });

        rbPrivateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetPositionMember();

            }
        });

    }



    private void SetPositionMember() {
        HashMap hashMap = new HashMap();
        hashMap.put("position","Member Group");

        SetPosition(hashMap);
    }

    private void SetPositionAdmin() {
        HashMap hashMap = new HashMap();
        hashMap.put("position","Admin Group");

        SetPosition(hashMap);

    }

    private void SetPosition(HashMap hashMap) {
        mGroupRef.child(groupToken).child("Member").child(userMember).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(MemberManagerActivity.this, "Update Position Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MemberManagerActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void ON_OFF_KickButton() {

        if(mUser.getUid().equals(userMember)){
            kickMember.setVisibility(View.GONE);
        }else {

            mGroupRef.child(groupToken).child("Member").child(userMember).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String Position = snapshot.child("position").getValue().toString();

                        if(Position.equals("Admin Group")){
                            kickMember.setVisibility(View.GONE);
                        }else {
                            kickMember.setVisibility(View.VISIBLE);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            kickMember.setVisibility(View.VISIBLE);
        }
    }

    private void KickMember() {

        mGroupRef.child(groupToken).child("Member").child(userMember).removeValue().addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(MemberManagerActivity.this, "Kick Member Success", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MemberManagerActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void LoadMemberProfile() {
        mUserRef.child(userMember).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String memberProfile = snapshot.child("profileImage").getValue().toString();
                    String memberName = snapshot.child("username").getValue().toString();

                    Picasso.get().load(memberProfile).into(memberProfilePicture);
                    memberNameText.setText(memberName);




                }else {
                    Toast.makeText(MemberManagerActivity.this, "Load Data Fail", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MemberManagerActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        mGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String memberPosition = snapshot.child(groupToken).child("Member").child(userMember).child("position").getValue().toString();

                if(memberPosition.equals("Admin Group")){
                    rbPublicGroup.setChecked(true);

                }else if(memberPosition.equals("Member Group")){
                    rbPrivateGroup.setChecked(true);
                }else {
                    Toast.makeText(MemberManagerActivity.this, "AAAAAAAAAAAAAA", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}