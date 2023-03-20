package com.example.a5dayappchat2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.Activity.Preferences.PreferencesMember;
import com.example.a5dayappchat2.Activity.Preferences.PreferencesUser;
import com.example.a5dayappchat2.Activity.Utills.Member;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import io.github.muddz.styleabletoast.StyleableToast;

public class MemberActivity extends AppCompatActivity {

    PreferencesUser preferencesUser;
    PreferencesMember preferencesMember;
    String userId, groupToken;
    Toolbar toolbar;
    DatabaseReference mGroupRef,mUserRef;

    FirebaseRecyclerOptions<Member> options;
    FirebaseRecyclerAdapter<Member, MemberViewHolder> adapter;
    RecyclerView recyclerView;

    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        preferencesUser = new PreferencesUser(getApplicationContext());
        preferencesMember = new PreferencesMember(getApplicationContext());

        userId = preferencesUser.getString(Constant.USER_ID);
        groupToken = getIntent().getStringExtra("groupKeyForMember");

        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");

        recyclerView = findViewById(R.id.recycleViewGroup);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getText(R.string.Members));

        LoadMember("");

    }

    private void LoadMember(String s) {

                    Query query = mGroupRef.child(groupToken).child("Member");
                    options = new FirebaseRecyclerOptions.Builder<Member>().setQuery(query, Member.class).build();
                    adapter = new FirebaseRecyclerAdapter<Member, MemberViewHolder>(options) {

                        @Override
                        protected void onBindViewHolder(@NonNull MemberViewHolder holder, int position, @NonNull Member model) {

                            String MemberId = getRef(position).getKey().toString();

                            mUserRef.child(MemberId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String userName = snapshot.child("username").getValue().toString();
                                    String profile = snapshot.child("profileImage").getValue().toString();

                                    holder.memberName.setText(userName);
                                    Picasso.get().load(profile).into(holder.memberProfile);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });




                            mGroupRef.child(groupToken).child("Member").child(MemberId).child("position").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String position = snapshot.getValue().toString();
                                    holder.memberPosition.setText(position);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            mGroupRef.child(groupToken).child("Member").child(mUser.getUid()).child("position").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   String PostionMe = snapshot.getValue().toString();


                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(PostionMe.equals("Admin Group")){

                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent(MemberActivity.this, MemberManagerActivity.class);
                                                        intent.putExtra("userIdForManagers", getRef(position).getKey().toString());
                                                        intent.putExtra("groupTokenForManagers",groupToken.toString());
                                                        startActivity(intent);

                                                    }
                                                });

                                            }else if(PostionMe.equals("Member Group")) {
                                                StyleableToast.makeText(MemberActivity.this, "Your position in this group does not have access to this setting. ", Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                                            }else {
                                                //
                                            }
                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                        }

                        @NonNull
                        @Override
                        public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_member_cell, parent, false);
                            return new MemberViewHolder(view);
                        }
                    };
                    adapter.startListening();
                    recyclerView.setAdapter(adapter);


    }
}