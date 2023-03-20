package com.example.a5dayappchat2.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.Activity.Utills.Friends;
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

import java.net.URL;

public class FriendActivity extends BaseActivity {
    FirebaseRecyclerOptions<Friends>options;
    FirebaseRecyclerAdapter<Friends, FriendMyViewHolder> adapter;

    Toolbar toolbar;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    DatabaseReference mUserRef;
    FirebaseUser mUser;
    String status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setTitle(getText(R.string.friends));


        recyclerView =findViewById(R.id.recyclerViewActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mRef = FirebaseDatabase.getInstance().getReference().child("Friend");

        LoadFriends("");


    }


    private void LoadFriends(String s) {
        Query query = mRef.child(mUser.getUid());
        options= new FirebaseRecyclerOptions.Builder<Friends>().setQuery(query,Friends.class).build();
        adapter = new FirebaseRecyclerAdapter<Friends, FriendMyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendMyViewHolder holder, int position, @NonNull Friends model) {



                mUserRef.child(getRef(position).getKey().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String status = snapshot.child("status").getValue().toString();
                        String UserName = snapshot.child("username").getValue().toString();
                        String Description = snapshot.child("description").getValue().toString();
                        Uri ProfileImage = Uri.parse(snapshot.child("profileImage").getValue().toString());

                        holder.username.setText(UserName);
                        holder.description.setText(Description);

                        Picasso.get().load(ProfileImage).into(holder.profileImageUrl);

                        if(status.equals("online")){
                            holder.vStatus.setBackground(getResources().getDrawable(R.drawable.bg_status_green));
                        }else if(status.equals("offline")){
                            holder.vStatus.setBackground(getResources().getDrawable(R.drawable.bg_status_red));
                        }else {
                            Toast.makeText(FriendActivity.this, "ADASDASDASD", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(FriendActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FriendActivity.this,ViewFriendsActivity.class);
                        intent.putExtra("userKey",getRef(position).getKey().toString());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public FriendMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_friend,parent,false);

                return new FriendMyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


}