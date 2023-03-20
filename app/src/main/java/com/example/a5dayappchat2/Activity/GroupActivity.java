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

import com.example.a5dayappchat2.Activity.Preferences.PreferencesGroup;
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

public class GroupActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    DatabaseReference mGroupRef;
    PreferencesGroup preferencesGroup;
    PreferencesUser preferencesUser;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    FirebaseRecyclerOptions<Group>options;
    FirebaseRecyclerAdapter<Group,FindGroupViewHolder>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        preferencesGroup = new PreferencesGroup(getApplicationContext());
        preferencesUser = new PreferencesUser(getApplicationContext());

        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getText(R.string.groups));

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        recyclerView = findViewById(R.id.recycleViewGroup);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            LoadGroup("");
        }catch (Exception e){
            //
        }

    }



    private void LoadGroup(String s) {
                    Query query = mGroupRef.orderByChild("GroupName").startAt(s).endAt(s + "\uf8ff");
                    options = new FirebaseRecyclerOptions.Builder<Group>().setQuery(query, Group.class).build();
                    adapter = new FirebaseRecyclerAdapter<Group, FindGroupViewHolder>(options) {

                        @Override
                        protected void onBindViewHolder(@NonNull FindGroupViewHolder holder, int position, @NonNull Group model) {

                            String groupKey = getRef(position).getKey().toString();

                            mGroupRef.child(groupKey).child("Member").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        Picasso.get().load(model.getGroupProfileImage()).into(holder.circleImageProfileGroup);
                                        holder.textGroupName.setText(model.getGroupName());
                                    }else {

                                        holder.itemView.setVisibility(View.GONE);
                                        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                                        params.height = 0;
                                        params.width = 0;
                                        holder.itemView.setLayoutParams(params);

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
                                    Intent intent = new Intent(GroupActivity.this, ChatGroupActivity.class);
                                    intent.putExtra("groupKeyToChat", getRef(position).getKey().toString());
                                    startActivity(intent);

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
                LoadGroup(s);
                return false;
            }
        });

        return true;
    }
}