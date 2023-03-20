package com.example.a5dayappchat2.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.Activity.Preferences.PreferencesGroup;
import com.example.a5dayappchat2.Activity.Preferences.PreferencesUser;
import com.example.a5dayappchat2.Activity.Utills.Group;
import com.example.a5dayappchat2.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.muddz.styleabletoast.StyleableToast;

public class FindGroupActivity extends AppCompatActivity {

    FloatingActionButton btCreateGroup;
    FirebaseRecyclerOptions<Group>options;
    FirebaseRecyclerAdapter<Group,FindGroupViewHolder>adapter;
    Toolbar toolbar;

    DatabaseReference mGroupRef,mMemberRef,mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    RecyclerView recyclerView;

    PreferencesGroup preferencesGroup;
    PreferencesUser preferencesUser;

    ProgressDialog mLoadingBar;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    ConstraintLayout laytoutcreatenewgroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_group);
        preferencesGroup = new PreferencesGroup(getApplicationContext());
        preferencesUser = new PreferencesUser(getApplicationContext());

        mLoadingBar = new ProgressDialog(this);


        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getText(R.string.find_groups));
        recyclerView = findViewById(R.id.recycleViewGroup);
        laytoutcreatenewgroup = findViewById(R.id.laytoutcreatenewgroup);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mMemberRef = FirebaseDatabase.getInstance().getReference().child("Group").child("Member");

        btCreateGroup = findViewById(R.id.btCreateGroup);

        laytoutcreatenewgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindGroupActivity.this,CreateGroupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindGroupActivity.this,CreateGroupActivity.class);
                startActivity(intent);
                finish();
            }
        });
        LoadGroup("");
    }





    private void LoadGroup(String s) {

                    Query query = mGroupRef.orderByChild("GroupName").startAt(s).endAt(s + "\uf8ff");
                    options = new FirebaseRecyclerOptions.Builder<Group>().setQuery(query, Group.class).build();
                    adapter = new FirebaseRecyclerAdapter<Group, FindGroupViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull FindGroupViewHolder holder, int position, @NonNull Group model) {

                            String groupKey = getRef(position).getKey().toString();

                            if(model.getGroupVision() == null){
                                holder.itemView.setVisibility(View.GONE);
                                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                                params.height = 0;
                                params.width = 0;
                                holder.itemView.setLayoutParams(params);
                            }else {
                                if(model.getGroupVision().equals("Public")){
                                    mGroupRef.child(groupKey).child("Member").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                holder.itemView.setVisibility(View.GONE);
                                                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                                                params.height = 0;
                                                params.width = 0;
                                                holder.itemView.setLayoutParams(params);
                                            }else {
                                                Picasso.get().load(model.getGroupProfileImage()).into(holder.circleImageProfileGroup);
                                                holder.textGroupName.setText(model.getGroupName());
                                                Toast.makeText(FindGroupActivity.this, "Load Group Success", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
//
                                        }
                                    });
                                } else {
                                    holder.itemView.setVisibility(View.GONE);
                                    ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                                    params.height = 0;
                                    params.width = 0;
                                    holder.itemView.setLayoutParams(params);

                                }
                            }




                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ShowPopUpJoinGroup(getRef(position).getKey());
                                    
                                    
                                    /*
                                    Intent intent = new Intent(FindGroupActivity.this, AcceptGroupActivity.class);
                                    intent.putExtra("groupKey", getRef(position).getKey().toString());
                                    startActivity(intent);
                                    
                                     */

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



                CircleImageView ciSettingProfileGroup;
    TextView tvNameGroup;
    MaterialButton btYes,btNo;


    private void ShowPopUpJoinGroup(String key) {
        dialogBuilder = new AlertDialog.Builder(this);
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_join_group,null);


        ciSettingProfileGroup = contarctPopView.findViewById(R.id.ciSettingProfileGroup);
        tvNameGroup = contarctPopView.findViewById(R.id.tvNameGroup);
        btYes = contarctPopView.findViewById(R.id.btYes);
        btNo = contarctPopView.findViewById(R.id.btNo);

        mGroupRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ProfileGroupImage = snapshot.child("GroupProfileImage").getValue().toString();
                String GroupName = snapshot.child("GroupName").getValue().toString();

                Picasso.get().load(ProfileGroupImage).into(ciSettingProfileGroup);
                tvNameGroup.setText(GroupName);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//
            }
        });

        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap hashMap = new HashMap();
                hashMap.put("position","Member Group");
                hashMap.put("Notification","On");

                mGroupRef.child(key).child("Member").child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        dialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //

                    }
                });



            }
        });


        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        dialogBuilder.setView(contarctPopView);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
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
                LoadGroup(s);
                return false;
            }
        });

        return true;
    }
}