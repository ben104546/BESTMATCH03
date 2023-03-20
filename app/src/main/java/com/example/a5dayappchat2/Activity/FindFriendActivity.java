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

import com.example.a5dayappchat2.Activity.Utills.Users;
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

public class FindFriendActivity extends AppCompatActivity {

    FirebaseRecyclerOptions<Users>options;
    FirebaseRecyclerAdapter<Users, FindFriendViewHolder>adapter;
    Toolbar toolbar;

    DatabaseReference mUserRef,mFriendRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        toolbar=findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getText(R.string.find_friends));
        recyclerView = findViewById(R.id.recycleViewGroup);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mFriendRef = FirebaseDatabase.getInstance().getReference().child("Friend");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        LoadUsers("");
    }



    private void LoadUsers(String s) {
        Query query = mUserRef.orderByChild("username").startAt(s).endAt(s+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(query,Users.class).build();
        adapter = new FirebaseRecyclerAdapter<Users, FindFriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindFriendViewHolder holder,int position, @NonNull Users model) {
                if(!mUser.getUid().equals(getRef(position).getKey().toString())){

                    String OtherUserId = getRef(position).getKey().toString();


                    mFriendRef.child(mUser.getUid()).child(OtherUserId).child("status").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String Status = snapshot.getValue().toString();

                                if(Status.equals("friend")){
                                    holder.itemView.setVisibility(View.GONE);
                                    ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                                    params.height = 0;
                                    params.width = 0;
                                    holder.itemView.setLayoutParams(params);
                                }else {
                                    Picasso.get().load(model.getProfileImage()).into(holder.ciProfileImage);
                                    holder.tvUsername.setText(model.getUsername());
                                    holder.tvDescription.setText(model.getDescription());
                                }
                            }else {
                                Picasso.get().load(model.getProfileImage()).into(holder.ciProfileImage);
                                holder.tvUsername.setText(model.getUsername());
                                holder.tvDescription.setText(model.getDescription());
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(FindFriendActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });



                } else {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(FindFriendActivity.this,ViewFriendsActivity.class);
                        intent.putExtra("userKey",getRef(position).getKey().toString());
                        startActivity(intent);
                    }
                });



            }

            @NonNull
            @Override
            public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_find_friends,parent,false);

                return new FindFriendViewHolder(view);
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
                LoadUsers(s);
                return false;
            }
        });

        return true;
    }
}