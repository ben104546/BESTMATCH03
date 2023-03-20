package com.example.a5dayappchat2.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a5dayappchat2.Activity.Preferences.PreferencesUser;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AcceptGroupActivity extends AppCompatActivity {

    String Group_id ;

    CircleImageView groupImageProfile;
    TextView GroupName;
    Button buttonYes,buttonNo;

    PreferencesUser preferencesUser;

    DatabaseReference mGroupRef,mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_group);

        Group_id = getIntent().getStringExtra("groupKey");

        groupImageProfile = findViewById(R.id.ciSettingProfileGroup);
        preferencesUser = new PreferencesUser(getApplicationContext());
        GroupName = findViewById(R.id.tvNameGroup);
        buttonYes = findViewById(R.id.btYes);
        buttonNo = findViewById(R.id.btNo);

        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        LoadGroupData();

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMember();
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }

    private void AddMember() {
        mGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    mUserRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){

                                String user_id = preferencesUser.getString(Constant.USER_ID);
                                String username = snapshot.child(user_id).child("username").getValue().toString();
                                String city = snapshot.child(user_id).child("city").getValue().toString();
                                String country = snapshot.child(user_id).child("country").getValue().toString();
                                String description = snapshot.child(user_id).child("description").getValue().toString();
                                String profileImage = snapshot.child(user_id).child("profileImage").getValue().toString();



                                HashMap hashMap = new HashMap();
                                hashMap.put("Status","InGroup");
                                hashMap.put("city",city);
                                hashMap.put("country",country);
                                hashMap.put("description",description);
                                hashMap.put("profileImage",profileImage);
                                hashMap.put("position","Member Group");
                                hashMap.put("username",username);


                                mGroupRef.child(Group_id).child("Member").child(user_id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(AcceptGroupActivity.this, "ADD GROUP SUCCESS", Toast.LENGTH_SHORT).show();
                                        finish();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AcceptGroupActivity.this, ""+e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }else {
                                Toast.makeText(AcceptGroupActivity.this, "LOAD DATA FAIL", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AcceptGroupActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


                }else {
                    Toast.makeText(AcceptGroupActivity.this, "ADD GROUP FAIL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AcceptGroupActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void LoadGroupData() {
        mGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String groupProfile = snapshot.child(Group_id).child("GroupProfileImage").getValue().toString();
                    String groupName = snapshot.child(Group_id).child("GroupName").getValue().toString();

                    Picasso.get().load(groupProfile).into(groupImageProfile);
                    GroupName.setText(groupName);
                }else {
                    Toast.makeText(AcceptGroupActivity.this, "DATA NOT EXIST", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AcceptGroupActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}