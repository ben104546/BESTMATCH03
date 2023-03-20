package com.example.a5dayappchat2.Activity.Setting;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.Activity.Doing.ActivityDoingsMainReal;
import com.example.a5dayappchat2.Activity.InviteMemberActivity;
import com.example.a5dayappchat2.Activity.MainActivity;
import com.example.a5dayappchat2.Activity.MemberActivity;
import com.example.a5dayappchat2.Activity.SettingGroup;
import com.example.a5dayappchat2.Activity.SetupActivity;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.ActivitySettingGroupNewBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.github.muddz.styleabletoast.StyleableToast;

public class SettingGroupNew extends AppCompatActivity implements AdapterSetting.OnItemListener {



    private ActivitySettingGroupNewBinding binding;

    RecyclerView recyclerViewGroupSetting;
    RecyclerView.Adapter AdapterSetting;
    RecyclerView.LayoutManager LayoutManager;

    FirebaseAuth mAuth;
    FirebaseUser mUser;


    String Boss;

    DatabaseReference mGroupRef;

    String groupToken;

    String[] SettingName = {"invited members","Members","Activities","Settings Group","Remove Group","Leave Group"};

    // Define an integer array to hold the image recourse ids
    int[] ImageIcon = {R.drawable.ic_settinggroup_invited,R.drawable.ic_settinggroup_member,R.drawable.ic_settinggroup_activity,R.drawable.ic_settinggroup_settinggroup,
    R.drawable.ic_settinggroup_removegroup,R.drawable.ic_settinggroup_leavegroup};

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingGroupNewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");

        groupToken = getIntent().getStringExtra("groupKeyForSetting");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        recyclerViewGroupSetting = findViewById(R.id.recyclerViewGroupSetting);





        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ShowAndHide();


    }

    private void LoadList() {
        recyclerViewGroupSetting.setHasFixedSize(true);

        LayoutManager = new LinearLayoutManager(this);
        recyclerViewGroupSetting.setLayoutManager(LayoutManager);

        AdapterSetting = new AdapterSetting(this,SettingName, ImageIcon,Boss,this);
        recyclerViewGroupSetting.setAdapter(AdapterSetting);
    }

    private void ShowAndHide() {

        mGroupRef.child(groupToken).child("Member").child(mUser.getUid()).child("position").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String positionMember = snapshot.getValue().toString();

                    if(positionMember.equals("Admin Group")){
                        Boss = "A";
                        LoadList();


                    }else if(positionMember.equals("Member Group")){
                        Boss = "B";
                        LoadList();

                    }else {

                    }

                }else {
                    Intent intent6 = new Intent(SettingGroupNew.this, MainActivity.class);
                    startActivity(intent6);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public void onItemClick(int position) {

        switch(position) {

            case 0:

                Intent intent = new Intent(SettingGroupNew.this, InviteMemberActivity.class);
                intent.putExtra("groupTokenForInvite",groupToken);
                startActivity(intent);
                break;
            case 1:

                Intent intent2 = new Intent(SettingGroupNew.this, MemberActivity.class);
                intent2.putExtra("groupKeyForMember",groupToken);
                startActivity(intent2);
                break;

            case 2:

                Intent intent3 = new Intent(SettingGroupNew.this, ActivityDoingsMainReal.class);
                intent3.putExtra("GroupKey",groupToken);
                startActivity(intent3);
                break;

            case 3:

                Intent intent4 = new Intent(SettingGroupNew.this, SettingGroup.class);
                intent4.putExtra("groupKeyForSetting",groupToken);
                startActivity(intent4);
                break;

            case 4:
                
                ShowAlertDialogRemoveGroup();
                break;

            case 5:


                ShowAlertDialogLeaveGroup();
                break;

            default:

                break;

        }

    }


    TextView tvTitle;
    ImageButton btCancelDelete,btDeletePlan;

    private void ShowAlertDialogLeaveGroup() {
        dialogBuilder = new AlertDialog.Builder(this);
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_delete_plan,null);
        tvTitle = contarctPopView.findViewById(R.id.tvTitle);
        btCancelDelete = contarctPopView.findViewById(R.id.btCancelDelete);
        btDeletePlan = contarctPopView.findViewById(R.id.btDeletePlan);


        tvTitle.setText(R.string.Will_delete_the_group);

        btCancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        btDeletePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGroupRef.child(groupToken).child("Member").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {

                            String key = childSnapshot.getKey();

                                String PositionOther = snapshot.child(key).child("position").getValue().toString();


                                if(key.equals(mUser.getUid())){
                                    if(PositionOther.equals("Member Group")){
                                        mGroupRef.child(groupToken).child("Member").child(mUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Intent intent6 = new Intent(SettingGroupNew.this, MainActivity.class);
                                                startActivity(intent6);
                                            }
                                        });
                                    }else if(PositionOther.equals("Admin Group")){
                                        for (DataSnapshot childSnapshot2 : snapshot.getChildren()){
                                            String key2 = childSnapshot2.getKey();

                                            if(!key2.equals(mUser.getUid())){
                                                String PositionOther2 = snapshot.child(key2).child("position").getValue().toString();

                                                if(PositionOther2.equals("Admin Group")){

                                                    mGroupRef.child(groupToken).child("Member").child(mUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Intent intent6 = new Intent(SettingGroupNew.this, MainActivity.class);
                                                            startActivity(intent6);
                                                        }
                                                    });


                                                    break;
                                                }else {

                                                    StyleableToast.makeText(SettingGroupNew.this, getString(R.string.You_cannot_leave_the_group), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();

                                                    break;
                                                }


                                            }else {
                                                mGroupRef.child(groupToken).child("Member").child(mUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Intent intent6 = new Intent(SettingGroupNew.this, MainActivity.class);
                                                        startActivity(intent6);
                                                    }
                                                });

                                                break;
                                            }


                                        }

                                    }
                                }



                            }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SettingGroupNew.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

/*

 */


            }
        });

        dialogBuilder.setView(contarctPopView);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

    }

    private void ShowAlertDialogRemoveGroup() {
        dialogBuilder = new AlertDialog.Builder(this);
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_delete_plan,null);

        tvTitle.setText(R.string.leave_the_group);

        btCancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        btDeletePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGroupRef.child(groupToken).removeValue();
                Intent intent5 = new Intent(SettingGroupNew.this, MainActivity.class);
                startActivity(intent5);

            }
        });

        dialogBuilder.setView(contarctPopView);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }


}