package com.example.a5dayappchat2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.Activity.Preferences.MemberNoInGroup;
import com.example.a5dayappchat2.Activity.Preferences.PreferencesFriendsId;
import com.example.a5dayappchat2.Activity.Preferences.PreferencesMember;
import com.example.a5dayappchat2.Activity.Preferences.PreferencesUser;
import com.example.a5dayappchat2.Activity.Utills.InviteFriend;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class InviteMemberActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    PreferencesUser preferencesUser;
    PreferencesFriendsId preferencesFriendsId;
    PreferencesMember preferencesMember;
    MemberNoInGroup memberNoInGroup;


    DatabaseReference mGroupRef, mFriendRef, mUserRef,mInvitedGroup;
    String user_id, groupToken;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    FirebaseUser mUser;



    FirebaseRecyclerOptions<InviteFriend> options;
    FirebaseRecyclerAdapter<InviteFriend, InviteFriendViewHolder> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_member);
        preferencesUser = new PreferencesUser(getApplicationContext());
        preferencesFriendsId = new PreferencesFriendsId(getApplicationContext());
        preferencesMember = new PreferencesMember(getApplicationContext());
        memberNoInGroup = new MemberNoInGroup(getApplicationContext());

        groupToken = getIntent().getStringExtra("groupTokenForInvite");

        user_id = preferencesUser.getString(Constant.USER_ID);

        recyclerView = findViewById(R.id.recycleViewGroup);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");
        mFriendRef = FirebaseDatabase.getInstance().getReference().child("Friend");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mInvitedGroup = FirebaseDatabase.getInstance().getReference().child("InvitedGroup");

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getText(R.string.invited_members));

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();



        LoadFriendId("");




    }


    private void LoadFriendId(String s) {
        Query query = mFriendRef.child(mUser.getUid());
        options = new FirebaseRecyclerOptions.Builder<InviteFriend>().setQuery(query, InviteFriend.class).build();
        adapter = new FirebaseRecyclerAdapter<InviteFriend, InviteFriendViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull InviteFriendViewHolder holder, int position, @NonNull InviteFriend model) {

                String friendId = getRef(position).getKey().toString();

                mGroupRef.child(groupToken).child("Member").child(friendId).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            holder.itemView.setVisibility(View.GONE);
                            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));

                        }else {

                            mUserRef.child(friendId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String FUsername = snapshot.child("username").getValue().toString();
                                    String FImageProfile = snapshot.child("profileImage").getValue().toString();


                                    Picasso.get().load(FImageProfile).into(holder.FriendProfile);
                                    holder.FriendName.setText(FUsername);
                                    Toast.makeText(InviteMemberActivity.this, "Load Friends Success", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                                    holder.AddMember.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            mInvitedGroup.child(friendId).child(groupToken).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(!snapshot.exists()){

                                                        HashMap hashMap = new HashMap();
                                                        hashMap.put("Status","SendInvite");

                                                        mInvitedGroup.child(friendId).child(groupToken).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {
                                                                holder.AddMember.setImageDrawable(getDrawable(R.drawable.ic_wrong));
                                                                holder.AddMember.setRotation(45);
                                                            }
                                                        });


                                                        Toast.makeText(InviteMemberActivity.this, "Send Friend Token"+" "+ model.getUsername(), Toast.LENGTH_SHORT).show();




                                                    }else {
                                                        mInvitedGroup.child(friendId).child(groupToken).removeValue();
                                                        holder.AddMember.setImageDrawable(getDrawable(R.drawable.ic_add_friend_in_group));
                                                        holder.AddMember.setRotation(0);


                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });



                                        }
                                    });
                                }

                        }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(InviteMemberActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }


            @NonNull
            @Override
            public InviteFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_invite_member_cell, parent, false);
                return new InviteFriendViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                LoadFriendId(s);
                return false;
            }
        });

        return true;
    }
}