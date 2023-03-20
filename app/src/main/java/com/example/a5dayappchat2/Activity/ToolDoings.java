package com.example.a5dayappchat2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.a5dayappchat2.Activity.Preferences.PreferencesDoingToken;
import com.example.a5dayappchat2.Activity.Preferences.PreferencesGroup;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ToolDoings extends AppCompatActivity {

    ConstraintLayout layoutPlan;
    PreferencesDoingToken preferencesDoingToken;
    PreferencesGroup preferencesGroup;
    String DoingToken;
    String GroupToken;
    CircleImageView ciDoings;
    TextView tvNameDoings;
    DatabaseReference mGroupRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_doings);
        preferencesDoingToken = new PreferencesDoingToken(getApplicationContext());
        preferencesGroup = new PreferencesGroup(getApplicationContext());
        DoingToken = preferencesDoingToken.getString(Constant.DOINGS_TOKEN);
        GroupToken = preferencesGroup.getString(Constant.GROUP_TOKEN);

        layoutPlan = findViewById(R.id.layoutPlan);
        ciDoings = findViewById(R.id.ciDoings);
        tvNameDoings = findViewById(R.id.tvNameDoings);
        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");

        LoadActivity();



        layoutPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ToolDoings.this,PlanActivity.class);
                startActivity(intent);
            }
        });
    }

    private void LoadActivity() {
        mGroupRef.child(GroupToken).child("Activity").child(DoingToken).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String profileActivity = snapshot.child("DoingsImage").getValue().toString();
                    String NameActivity = snapshot.child("DoingsName").getValue().toString();

                    Picasso.get().load(profileActivity).into(ciDoings);
                    tvNameDoings.setText(NameActivity);


                }else {
                    Toast.makeText(ToolDoings.this, "Load Data Fails", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ToolDoings.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}