package com.example.a5dayappchat2.Activity;

import android.app.ProgressDialog;
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

import com.example.a5dayappchat2.Activity.Preferences.PreferencesGroup;
import com.example.a5dayappchat2.Activity.Preferences.PreferencesOtherId;
import com.example.a5dayappchat2.Activity.Preferences.PreferencesUser;
import com.example.a5dayappchat2.Activity.Utills.Group;
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

public class ReceivedGroupActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String userId,friendId,groupToken;
    DatabaseReference mGroupRef,mInvitedGroup,mUserRef;
    PreferencesUser preferencesUser;
    Toolbar toolbar;
    PreferencesGroup preferencesGroup;
    PreferencesOtherId preferencesOtherId;

    ProgressDialog mLoadingBar;

    FirebaseAuth mAuth;
    FirebaseUser mUser;


    FirebaseRecyclerOptions<Group>options;
    FirebaseRecyclerAdapter<Group,FindGroupViewHolder>adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_group);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        preferencesUser = new PreferencesUser(getApplicationContext());
        preferencesGroup = new PreferencesGroup(getApplicationContext());
        preferencesOtherId = new PreferencesOtherId(getApplicationContext());

        mLoadingBar = new ProgressDialog(this);

        recyclerView = findViewById(R.id.recycleViewGroup);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mInvitedGroup = FirebaseDatabase.getInstance().getReference().child("InvitedGroup");

        userId = preferencesUser.getString(Constant.USER_ID);

        friendId = getIntent().getStringExtra("friendTokenForInvite");
        groupToken = getIntent().getStringExtra("groupTokenForReceived");

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getText(R.string.invited_group));
        
        LoadGroupReceived("");



    }




    private void LoadGroupReceived(String s) {

                    Query query = mInvitedGroup.child(mUser.getUid());
                    options = new FirebaseRecyclerOptions.Builder<Group>().setQuery(query, Group.class).build();
                    adapter = new FirebaseRecyclerAdapter<Group, FindGroupViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull FindGroupViewHolder holder, int position, @NonNull Group model) {

                            String groupKey = getRef(position).getKey().toString();


                            mGroupRef.child(groupKey).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String groupIcon = snapshot.child("GroupProfileImage").getValue().toString();
                                    String groupName = snapshot.child("GroupName").getValue().toString();


                                    Picasso.get().load(groupIcon).into(holder.circleImageProfileGroup);
                                    holder.textGroupName.setText(groupName);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            holder.btAcGroup.setVisibility(View.VISIBLE);
                            holder.btDcGroup.setVisibility(View.VISIBLE);

                            holder.btAcGroup.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mLoadingBar.setTitle("Adding Data Profile");
                                    mLoadingBar.setCanceledOnTouchOutside(false);
                                    mLoadingBar.show();

                                    HashMap hashMap = new HashMap();
                                    hashMap.put("position","Member Group");
                                    hashMap.put("Notification","On");


                                    mGroupRef.child(groupKey).child("Member").child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            mLoadingBar.dismiss();

                                            mInvitedGroup.child(mUser.getUid()).child(getRef(position).getKey()).removeValue();

                                            Intent intent = new Intent(ReceivedGroupActivity.this, ChatGroupActivity.class);
                                            intent.putExtra("groupKeyToChat", getRef(position).getKey().toString());
                                            startActivity(intent);

                                            Toast.makeText(ReceivedGroupActivity.this, "ADD GROUP SUCCESS", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            mLoadingBar.dismiss();
                                            Toast.makeText(ReceivedGroupActivity.this, ""+e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            });


                            holder.btDcGroup.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    mInvitedGroup.child(mUser.getUid()).child(getRef(position).getKey()).removeValue();


                                }
                            });

                        }

                        @NonNull
                        @Override
                        public FindGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_group_cell, parent, false);
                            return new FindGroupViewHolder(view);
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
                LoadGroupReceived(s);
                return false;
            }
        });

        return true;
    }




}