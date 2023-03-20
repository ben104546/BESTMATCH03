package com.example.a5dayappchat2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.Activity.Utills.Chat;
import com.example.a5dayappchat2.Activity.Utills.Friends;
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

public class ChatUserActivity extends BaseActivity {
    FirebaseRecyclerOptions<Friends> options;
    FirebaseRecyclerAdapter<Friends, FriendMyViewHolder> adapter;

    FirebaseRecyclerOptions<Chat>optionsLoadChatToken;
    FirebaseRecyclerAdapter<Chat, ChatMyViewHolder>adapterLoadChatToken;

    FirebaseRecyclerOptions<Friends> options3;
    FirebaseRecyclerAdapter<Friends, FriendMyViewHolder> adapter3;

    Toolbar toolbar;
    RecyclerView recyclerView,recyclerViewLoadChatLast,recyclerViewLISTALLFRIEND;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    FirebaseUser mUser;
    DatabaseReference mUserRef,mMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Friend");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mMessage = FirebaseDatabase.getInstance().getReference().child("Message");


        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getText(R.string.chat));


        recyclerView =findViewById(R.id.recyclerViewActivity);
        recyclerViewLoadChatLast =findViewById(R.id.recyclerViewLoadChatLast);
        recyclerViewLISTALLFRIEND =findViewById(R.id.recyclerViewLISTALLFRIEND);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLoadChatLast.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerViewLISTALLFRIEND.setLayoutManager(layoutManager);


        LoadChatIteam();

        //LoadFriends();


    }

    private void LoadChatIteam() {
        Query query = mMessage.child(mUser.getUid()).orderByChild("ChatTimeLate").startAt(Integer.MAX_VALUE);
        options3= new FirebaseRecyclerOptions.Builder<Friends>().setQuery(query,Friends.class).build();
        adapter3 = new FirebaseRecyclerAdapter<Friends, FriendMyViewHolder>(options3) {
            @Override
            protected void onBindViewHolder(@NonNull FriendMyViewHolder holder, int position, @NonNull Friends model) {
                String OtherUserId = getRef(position).getKey().toString();

                mUserRef.child(getRef(position).getKey().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(!snapshot.child("status").exists()){
                            holder.vStatus.setVisibility(View.GONE);

                        }else {
                            String status = snapshot.child("status").getValue().toString();
                            if(status.equals("online")){
                                holder.vStatus.setBackground(getResources().getDrawable(R.drawable.bg_status_green));
                            }else if(status.equals("offline")){
                                holder.vStatus.setBackground(getResources().getDrawable(R.drawable.bg_status_red));
                            }else {
                                //
                            }
                        }


                        if(!snapshot.child("profileImage").exists()){
                            holder.profileImageUrl.setImageResource(R.drawable.ic_logo_2);
                        }else {
                            Picasso.get().load(snapshot.child("profileImage").getValue().toString()).into(holder.profileImageUrl);
                        }

                        if(!snapshot.child("username").exists()){
                            holder.username.setText("Admin");
                        }else {

                            holder.username.setText(snapshot.child("username").getValue().toString());

                        }






                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ChatUserActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                });


                mMessage.child(mUser.getUid()).child(OtherUserId).child("Chat").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String lastChildKey = dataSnapshot.getChildren().iterator().next().getKey();
                        if(!lastChildKey.isEmpty()){

                            mMessage.child(mUser.getUid()).child(OtherUserId).child("Chat").child(lastChildKey).child("sms").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){

                                        String SMS = snapshot.getValue().toString();
                                        holder.description.setText(SMS);


                                    }else {



                                        Toast.makeText(ChatUserActivity.this, Constant.ERROR_TEXT_NOT_EXISTS, Toast.LENGTH_SHORT).show();

                                    }



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
//
                                }
                            });


                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ChatUserActivity.this,ChatActivity.class);
                                    intent.putExtra("OtherUserID",getRef(position).getKey().toString());
                                    startActivity(intent);
                                }
                            });


                        }else {
                            holder.description.setText("None Last Message");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // handle error here
                    }
                });


            }


            @NonNull
            @Override
            public FriendMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_chatlist,parent,false);

                return new FriendMyViewHolder(view);
            }
        };

        adapter3.startListening();
        recyclerViewLISTALLFRIEND.setAdapter(adapter3);


    }


    private void LoadListChat(String otherId, String chatToken,String s) {
        Query query = mRef.child(mUser.getUid()).orderByChild("username").startAt(s).endAt(s+"\uf8ff");

        options3= new FirebaseRecyclerOptions.Builder<Friends>().setQuery(query,Friends.class).build();
        adapter3 = new FirebaseRecyclerAdapter<Friends, FriendMyViewHolder>(options3) {

            @Override
            protected void onBindViewHolder(@NonNull FriendMyViewHolder holder, int position, @NonNull Friends model) {
                getRef(position);

                String FriendKey = getRef(position).getKey().toString();

                if(otherId == FriendKey){
                    Picasso.get().load(model.getProfileImageUrl()).into(holder.profileImageUrl);
                    holder.username.setText(model.getUsername());
                }else {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }


                mUserRef.child(getRef(position).getKey().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String status = snapshot.child("status").getValue().toString();
                        if(status.equals("online")){
                            holder.vStatus.setBackground(getResources().getDrawable(R.drawable.bg_status_green));
                        }else if(status.equals("offline")){
                            holder.vStatus.setBackground(getResources().getDrawable(R.drawable.bg_status_red));
                        }else {
                            Toast.makeText(ChatUserActivity.this, "ADASDASDASD", Toast.LENGTH_SHORT).show();
                        }


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ChatUserActivity.this,ChatActivity.class);
                                intent.putExtra("OtherUserID",getRef(position).getKey().toString());
                                startActivity(intent);
                            }
                        });



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ChatUserActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                });

                mMessage.child(otherId).child(mUser.getUid()).child(chatToken).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String LAST_CHAT = snapshot.child("sms").getValue().toString();
                            if(!LAST_CHAT.isEmpty()){
                                holder.description.setText(LAST_CHAT);
                            }else {
                                holder.description.setText("None Last Message");
                            }


                        }else {
                            Toast.makeText(ChatUserActivity.this, "ERROR LoadListChat", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ChatUserActivity.this, "ERROR LoadListChat", Toast.LENGTH_SHORT).show();
                    }
                });




            }

            @NonNull
            @Override
            public FriendMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_chatlist,parent,false);

                return new FriendMyViewHolder(view);
            }
        };

        adapter3.startListening();
        recyclerViewLISTALLFRIEND.setAdapter(adapter3);

    }




}