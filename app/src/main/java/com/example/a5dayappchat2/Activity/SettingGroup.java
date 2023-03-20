package com.example.a5dayappchat2.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.a5dayappchat2.Activity.createGroup.VpViewHolder;
import com.example.a5dayappchat2.Activity.createGroup.create_group_color;
import com.example.a5dayappchat2.Activity.createGroup.create_group_picture;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.ActivitySettingGroupBinding;
import com.example.a5dayappchat2.databinding.FragmentCreateGroupPictureBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingGroup extends AppCompatActivity  {

    private static final int REQUEST_CODE1 = 101;
    private static final int REQUEST_CODE2 = 102;

    String GroupToken;
    CircleImageView ciSettingProfileGroup;
    TextInputLayout etSettingGroupName;
    ImageView ivSettingBgGroup;
    Button btSaveSetting;
    DatabaseReference mGroupRef;
    RadioButton rbPublicGroup,rbPrivateGroup;

    Uri imageUriProfileGroup;
    Uri imageUriWallpaperGroup;

    TabLayout tlChoseBackGroupType;
    ViewPager2 vpBackGroundGroup;
    VpViewHolder vpViewHolder;

    StorageReference groupProfile,groupWallpaper;

    TextView tvPageTitle;
    ImageView btBackPage;

    FragmentCreateGroupPictureBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_group);

        binding = FragmentCreateGroupPictureBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();


        GroupToken = getIntent().getStringExtra("groupKeyForSetting");
        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");

        ciSettingProfileGroup = findViewById(R.id.ciSettingProfileGroup);
        etSettingGroupName = findViewById(R.id.etSettingGroupName);
        //ivSettingBgGroup = findViewById(R.id.ivSettingBgGroup);
        btSaveSetting = findViewById(R.id.btSaveSetting);

        tvPageTitle = findViewById(R.id.tvPageTitle);
        btBackPage = findViewById(R.id.btBackPage);


        tlChoseBackGroupType = findViewById(R.id.tlChoseBackGroupType);
        vpBackGroundGroup = findViewById(R.id.vpBackGroundGroup);

        groupProfile = FirebaseStorage.getInstance().getReference().child("GroupProfileImage");
        groupWallpaper = FirebaseStorage.getInstance().getReference().child("GroupWallpaperImage");

        rbPublicGroup = findViewById(R.id.rbPublicGroup);
        rbPrivateGroup = findViewById(R.id.rbPrivateGroup);

        ciSettingProfileGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE1);
            }
        });

        btBackPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvPageTitle.setText(getText(R.string.setting_group));


        /*ivSettingBgGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE2);
            }
        });*/


        btSaveSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SaveSetting();

            }
        });

        vpViewHolder = new VpViewHolder(this);
        vpBackGroundGroup.setAdapter(vpViewHolder);



        LoadDataGroup();


    }

    private void SaveSetting() {
        String UserGroup = etSettingGroupName.getEditText().getText().toString();
        String GroupVisionSaveSetting = "";

        if(rbPublicGroup.isChecked()){
            GroupVisionSaveSetting = "Public";

        }else if(rbPrivateGroup.isChecked()){
            GroupVisionSaveSetting = "Private";
        }else {
//
        }

        HashMap hashMap = new HashMap();
        UpLoadProfileGroup();
        hashMap.put("GroupName",UserGroup);
        UpLoadBgGroup();
        hashMap.put("groupVision",GroupVisionSaveSetting);


        mGroupRef.child(GroupToken).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                onBackPressed();
                Toast.makeText(SettingGroup.this, "Save Setting Success", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SettingGroup.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



    private void UpLoadBgGroup() {
        create_group_picture create_group_picture = new create_group_picture();
        Uri ImageURL3 = create_group_picture.imageUriWallpaperGroup;

        create_group_color create_group_color = new create_group_color();
        int Color = create_group_color.ColorCode;

        String PICTURESTR = create_group_picture.PICTURESTR;
        String ColorSTR = create_group_color.ColorSTR;

        Toast.makeText(this, ""+PICTURESTR+ColorSTR, Toast.LENGTH_SHORT).show();

        if(ColorSTR.equals("null") && PICTURESTR.equals("null")){
            Toast.makeText(this, "CHOSE BACKGROUND", Toast.LENGTH_SHORT).show();

        }else if(ColorSTR.equals("Color")){
            Toast.makeText(this, "COLOR", Toast.LENGTH_SHORT).show();

            HashMap hashMap = new HashMap();
            hashMap.put("colorWallpaper",Color);

            mGroupRef.child(GroupToken).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(SettingGroup.this, "Create Group Success", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SettingGroup.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            mGroupRef.child(GroupToken).child("groupWallpaper").removeValue();

        }else if(PICTURESTR.equals("Picture")){
            Toast.makeText(this, "PCITURE", Toast.LENGTH_SHORT).show();

            groupWallpaper.child(GroupToken).putFile(ImageURL3).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        groupWallpaper.child(GroupToken).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap hashMap = new HashMap();
                                hashMap.put("groupWallpaper",uri.toString());


                                mGroupRef.child(GroupToken).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(SettingGroup.this, "Create Group Success", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SettingGroup.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
            });

            mGroupRef.child(GroupToken).child("colorWallpaper").removeValue();
        }else {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();

        }

    }

    private void UpLoadProfileGroup() {
        try {
            groupProfile.child(GroupToken).putFile(imageUriProfileGroup).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    groupProfile.child(GroupToken).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            HashMap hashMap = new HashMap();
                            hashMap.put("GroupProfileImage", uri.toString());

                            mGroupRef.child(GroupToken).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(SettingGroup.this, "Upload Profile Group Success", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SettingGroup.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SettingGroup.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SettingGroup.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            //
        }
    }



    private void LoadDataGroup() {


    mGroupRef.child(GroupToken).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                String GroupProfile = snapshot.child("GroupProfileImage").getValue().toString();
                String GroupUsername = snapshot.child("GroupName").getValue().toString();

                String GroupVision = snapshot.child("groupVision").getValue().toString();

                DataSnapshot GroupWallpaperSnap = snapshot.child("groupWallpaper");
                DataSnapshot GroupColorSnap = snapshot.child("colorWallpaper");

                if(GroupWallpaperSnap.exists()){
                    String GroupWallpaper = snapshot.child("groupWallpaper").getValue().toString();

                    Picasso.get().load(GroupWallpaper).into(binding.ivSettingBgGroup);


                    tlChoseBackGroupType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {

                            vpBackGroundGroup.setCurrentItem(tab.getPosition());
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {


                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {


                        }
                    });

                }else if(GroupColorSnap.exists()){
                    String ColorWallpaper = snapshot.child("colorWallpaper").getValue().toString();

                    tlChoseBackGroupType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            vpBackGroundGroup.setCurrentItem(tab.getPosition());
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {


                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {


                        }
                    });

                }else {
                    Toast.makeText(SettingGroup.this, "ERROR", Toast.LENGTH_SHORT).show();
                }

                Picasso.get().load(GroupProfile).into(ciSettingProfileGroup);
                etSettingGroupName.getEditText().setText(GroupUsername);
                //Picasso.get().load(GroupWallpaper).into(ivSettingBgGroup);

                if(GroupVision.equals("Public")){
                    rbPublicGroup.setChecked(true);

                }else if(GroupVision.equals("Private")){
                    rbPrivateGroup.setChecked(true);

                }else {

                }


            }else {
                Toast.makeText(SettingGroup.this, "Load Data Fails", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(SettingGroup.this, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE1 && resultCode == RESULT_OK && data != null){
            imageUriProfileGroup=data.getData();
            ciSettingProfileGroup.setImageURI(imageUriProfileGroup);
        }

        if(requestCode == REQUEST_CODE2 && resultCode == RESULT_OK && data != null){
            imageUriWallpaperGroup=data.getData();
            ivSettingBgGroup.setImageURI(imageUriWallpaperGroup);
        }


    }




}