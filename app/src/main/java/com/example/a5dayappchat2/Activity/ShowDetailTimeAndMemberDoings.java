package com.example.a5dayappchat2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.a5dayappchat2.Activity.Preferences.PreferencesUser;
import com.example.a5dayappchat2.Activity.Utills.MemberDoings;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShowDetailTimeAndMemberDoings extends AppCompatActivity {

    FloatingActionButton btPlushMember;
    ConstraintLayout layoutBOTT;
    String DoingToken,GroupToken;
    DatabaseReference mGroupRef;
    CircleImageView ciDoingProfile4;
    TextView tvNameDoings3;
    FloatingActionButton btBackDoings;
    CountdownView myCountdown;
    GridView gvMemberDoings;
    ArrayList<MemberDoings> dataModalArrayList;
    FirebaseFirestore db;
    PreferencesUser preferencesUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail_time_and_member_doings);

        btPlushMember = findViewById(R.id.btPlushMember);
        ciDoingProfile4 = findViewById(R.id.ciDoingProfile4);
        tvNameDoings3 = findViewById(R.id.tvNameDoings3);
        btBackDoings = findViewById(R.id.btBackDoings);
        myCountdown = findViewById(R.id.myCountdown);
        gvMemberDoings = findViewById(R.id.gvMemberDoings);

        dataModalArrayList = new ArrayList<>();

        layoutBOTT = findViewById(R.id.layoutBOTT);

        preferencesUser = new PreferencesUser(getApplicationContext());
        GroupToken = getIntent().getStringExtra("TokenGroup");
        DoingToken = getIntent().getStringExtra("TokenDoings");


        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");
        db = FirebaseFirestore.getInstance();


        btBackDoings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowDetailTimeAndMemberDoings.this,ShowDayDoing.class);
                intent.putExtra("TokenGroupForShowDayDoings",GroupToken);
                intent.putExtra("TokenDoingsForShowDayDoings",DoingToken);
                startActivity(intent);

            }
        });

        btPlushMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowMemberDoing();
            }
        });
        
        LoadDataGroup();
        LoadCountDownTime();
        LoadUserDoings();

        layoutBOTT.setVisibility(View.GONE);
    }

    private void LoadUserDoings() {
        db.collection(DoingToken+ Constant.MEMBER_DOINGS).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {

                        MemberDoings memberDoings = d.toObject(MemberDoings.class);

                        dataModalArrayList.add(memberDoings);
                    }
                    MemberDoingsAdapter adapter = new MemberDoingsAdapter(ShowDetailTimeAndMemberDoings.this, dataModalArrayList);
                    gvMemberDoings.setAdapter(adapter);
                }else {
                    Toast.makeText(ShowDetailTimeAndMemberDoings.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShowDetailTimeAndMemberDoings.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void LoadCountDownTime() {
        mGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String Date = snapshot.child(GroupToken).child("Activity").child(DoingToken).child("DoingsDate").getValue().toString();
                    String Time = snapshot.child(GroupToken).child("Activity").child(DoingToken).child("DoingsTime").getValue().toString();
                    String NewDate = Date.replace(" ","-");

                    String DAteAndTime = NewDate + " " + Time;

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy HH:mm");
                    java.util.Date now = new Date();

                    try {
                        Date date = sdf.parse(DAteAndTime);
                        long currentTime = now.getTime();
                        long newYearDate = date.getTime();
                        long countDownToNewYear = newYearDate - currentTime;
                        myCountdown.start(countDownToNewYear);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



                }else {
                    Toast.makeText(ShowDetailTimeAndMemberDoings.this, "Load Data Error", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowDetailTimeAndMemberDoings.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });





    }

    private void LoadDataGroup() {
        mGroupRef.child(GroupToken).child("Activity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String ProfileGroup = snapshot.child(DoingToken).child("DoingsImage").getValue().toString();
                    String NameDoings = snapshot.child(DoingToken).child("DoingsName").getValue().toString();


                    Picasso.get().load(ProfileGroup).into(ciDoingProfile4);
                    tvNameDoings3.setText(NameDoings);

                }else {
                    Toast.makeText(ShowDetailTimeAndMemberDoings.this, "Data is not exists", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowDetailTimeAndMemberDoings.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ShowMemberDoing() {
        if(layoutBOTT.isShown()){
            layoutBOTT.setVisibility(View.GONE);
        }else {
            layoutBOTT.setVisibility(View.VISIBLE);
        }


    }
}