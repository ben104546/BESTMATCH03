package com.example.a5dayappchat2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.a5dayappchat2.Activity.Preferences.PreferencesDoingToken;
import com.example.a5dayappchat2.Activity.Preferences.PreferencesGroup;
import com.example.a5dayappchat2.Activity.Preferences.PreferencesUser;
import com.example.a5dayappchat2.Activity.Utills.DetailDoingPicture;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowDayDoing extends AppCompatActivity {

    String DoingToken;
    String GroupToken;
    DatabaseReference mGroupRef;
    CircleImageView ciDoingProfile;
    TextView tvNameDoings,tvDescriptionDoings;

    FloatingActionButton btNextPage;
    FloatingActionButton btToolActivity;
    Button btJoinDoings;

    PreferencesUser preferencesUser;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    GridView gvImageDoings;

    ArrayList<DetailDoingPicture> dataModalArrayList;

    FirebaseFirestore db;
    ConstraintLayout layoutBOTT;

    PreferencesDoingToken preferencesDoingToken;
    PreferencesGroup preferencesGroup;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    CircleImageView ciDoings;
    ImageView tvCanclePopUp;
    ConstraintLayout layoutPlan,layoutFundraising;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_day_doing);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        GroupToken = getIntent().getStringExtra("GroupToken");
        DoingToken = getIntent().getStringExtra("DoingToken");



        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");
        ciDoingProfile = findViewById(R.id.ciDoingProfile);
        tvNameDoings = findViewById(R.id.tvNameDoings);
        tvDescriptionDoings = findViewById(R.id.tvDescriptionDoings);
        btNextPage = findViewById(R.id.btNextPage);
        btJoinDoings = findViewById(R.id.btJoinDoings);
        layoutBOTT = findViewById(R.id.layoutBOTT);
        btToolActivity = findViewById(R.id.btToolActivity);



        gvImageDoings = findViewById(R.id.gvImageDoings);
        dataModalArrayList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        preferencesDoingToken = new PreferencesDoingToken(getApplicationContext());
        preferencesGroup = new PreferencesGroup(getApplicationContext());


        preferencesUser = new PreferencesUser(getApplicationContext());

        btNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextPage();
            }
        });

        btJoinDoings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPeopleOnActivity();
            }

        });

        btToolActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopUpToolActivity();

            }
        });


        
        LoadDataDoings();
        HideORShowButtonJoinActivity();
        ShowAndHideSettingActivity();

        LoadImageGridView();
        




    }

    private void LoadDataActviityPopUp() {
        mGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ActivityImage = snapshot.child(GroupToken).child("Activity").child(DoingToken).child("DoingsImage").getValue().toString();
                String ActivityName = snapshot.child(GroupToken).child("Activity").child(DoingToken).child("DoingsName").getValue().toString();

                Picasso.get().load(ActivityImage).into(ciDoings);
                tvNameDoings.setText(ActivityName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowDayDoing.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowPopUpToolActivity() {
        preferencesDoingToken.putString(Constant.DOINGS_TOKEN,DoingToken);
        preferencesGroup.putString(Constant.GROUP_TOKEN,GroupToken);

        dialogBuilder = new AlertDialog.Builder(this);
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_tool_activity,null);

        ciDoings = contarctPopView.findViewById(R.id.ciDoings);
        tvNameDoings = contarctPopView.findViewById(R.id.tvNameDoings);
        tvCanclePopUp = contarctPopView.findViewById(R.id.tvCanclePopUp);
        layoutPlan = contarctPopView.findViewById(R.id.layoutPlan);
        layoutFundraising = contarctPopView.findViewById(R.id.layoutFundraising);

        LoadDataActviityPopUp();



        layoutPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowDayDoing.this,PlanActivity.class);
                startActivity(intent);
            }
        });
        tvCanclePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialogBuilder.setView(contarctPopView);
        dialog = dialogBuilder.create();
        dialog.show();

    }

    private void ShowAndHideSettingActivity() {
        mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("PeopleOfActivity").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    btToolActivity.setVisibility(View.VISIBLE);
                }else {
                    btToolActivity.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowDayDoing.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void LoadImageGridView() {

        db.collection(DoingToken+Constant.IMAGE_DOINGS).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list){
                        DetailDoingPicture detailDoingPicture = d.toObject(DetailDoingPicture.class);
                        dataModalArrayList.add(detailDoingPicture);
                    }
                    DoingDayAdapter adapter = new DoingDayAdapter(ShowDayDoing.this, dataModalArrayList);

                    gvImageDoings.setAdapter(adapter);
                }else {
                    Toast.makeText(ShowDayDoing.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShowDayDoing.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void HideORShowButtonJoinActivity() {
        mGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String userId = preferencesUser.getString(Constant.USER_ID);
                    try {
                        String ListPeopleActivity = snapshot.child(GroupToken).child("Activity").child(DoingToken).child("PeopleOfActivity").child(userId).child("User_Id").getValue().toString();


                        if(ListPeopleActivity.equals(userId)){
                            btJoinDoings.setVisibility(View.GONE);

                        }
                    }catch (Exception e){
                        btJoinDoings.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowDayDoing.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void AddPeopleOnActivity() {
        mGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userid = preferencesUser.getString(Constant.USER_ID);

                if(snapshot.exists()){
                    String UserName = snapshot.child(GroupToken).child("Member").child(userid).child("username").getValue().toString();
                    String UserProfile = snapshot.child(GroupToken).child("Member").child(userid).child("profileImage").getValue().toString();


                    HashMap hashMap = new HashMap();
                    hashMap.put("User_Id",userid);
                    hashMap.put("username",UserName);
                    hashMap.put("profileImage",UserProfile);

                    db.collection(DoingToken+Constant.MEMBER_DOINGS).document(userid).set(hashMap);

                    mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("PeopleOfActivity").child(userid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(ShowDayDoing.this, "Join Activity Success", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ShowDayDoing.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


                }else {
                    Toast.makeText(ShowDayDoing.this, "Load Data Fails", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowDayDoing.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void NextPage() {
        Intent intent = new Intent(ShowDayDoing.this,ShowDetailTimeAndMemberDoings.class);
        intent.putExtra("TokenDoings",DoingToken);
        intent.putExtra("TokenGroup",GroupToken);
        startActivity(intent);

    }


    private void LoadDataDoings() {
            if(GroupToken==null && DoingToken==null){
                GroupToken = getIntent().getStringExtra("TokenGroupForShowDayDoings");
                DoingToken = getIntent().getStringExtra("TokenDoingsForShowDayDoings");

                mGroupRef.child(GroupToken).child("Activity").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String ProfileGroup = snapshot.child(DoingToken).child("DoingsImage").getValue().toString();
                            String NameDoings = snapshot.child(DoingToken).child("DoingsName").getValue().toString();
                            String DescriptionDoings = snapshot.child(DoingToken).child("DoingsDescription").getValue().toString();

                            Picasso.get().load(ProfileGroup).into(ciDoingProfile);
                            tvNameDoings.setText(NameDoings);
                            tvDescriptionDoings.setText(DescriptionDoings);
                        } else {
                            Toast.makeText(ShowDayDoing.this, "Data is not exists", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ShowDayDoing.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else {


                mGroupRef.child(GroupToken).child("Activity").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String ProfileGroup = snapshot.child(DoingToken).child("DoingsImage").getValue().toString();
                            String NameDoings = snapshot.child(DoingToken).child("DoingsName").getValue().toString();
                            String DescriptionDoings = snapshot.child(DoingToken).child("DoingsDescription").getValue().toString();

                            Picasso.get().load(ProfileGroup).into(ciDoingProfile);
                            tvNameDoings.setText(NameDoings);
                            tvDescriptionDoings.setText(DescriptionDoings);
                        } else {
                            Toast.makeText(ShowDayDoing.this, "Data is not exists", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ShowDayDoing.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
}