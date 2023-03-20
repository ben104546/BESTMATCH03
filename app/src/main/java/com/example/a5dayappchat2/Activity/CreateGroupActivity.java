package com.example.a5dayappchat2.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.a5dayappchat2.Activity.Preferences.PreferencesGroup;
import com.example.a5dayappchat2.Activity.Preferences.PreferencesUser;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.Activity.createGroup.VpViewHolder;
import com.example.a5dayappchat2.Activity.createGroup.create_group_color;
import com.example.a5dayappchat2.Activity.createGroup.create_group_picture;
import com.example.a5dayappchat2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.muddz.styleabletoast.StyleableToast;

public class CreateGroupActivity extends AppCompatActivity  {

    private static final int REQUEST_CODE1 = 101;
    private static final int REQUEST_CODE2 = 102;
    Spinner spinner;
    CircleImageView groupProfileImage;
    EditText groupName;
    ImageView wallpaperGroup;
    Button btCreateGroup;

    Uri imageUriProfileGroup;
    Uri imageUriWallpaperGroup;
    PreferencesGroup preferencesGroup;
    PreferencesUser preferencesUser;


    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef,mGroupRef,mMemberRef;
    StorageReference groupProfile,groupWallpaper;

    String GroupVision;

    RadioButton rbPublicGroup,rbPrivateGroup;

    TabLayout tlChoseBackGroupType;
    ViewPager2 vpBackGroundGroup;
    VpViewHolder vpViewHolder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        preferencesGroup = new PreferencesGroup(getApplicationContext());
        preferencesUser = new PreferencesUser(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");
        mMemberRef = FirebaseDatabase.getInstance().getReference().child("Member");

        groupProfile = FirebaseStorage.getInstance().getReference().child("GroupProfileImage");
        groupWallpaper = FirebaseStorage.getInstance().getReference().child("GroupWallpaperImage");

        groupProfileImage = findViewById(R.id.ciSettingProfileGroup);
        groupName = findViewById(R.id.etSettingGroupName);
        wallpaperGroup = findViewById(R.id.ivSettingBgGroup);

        rbPublicGroup = findViewById(R.id.rbPublicGroup);
        rbPrivateGroup = findViewById(R.id.rbPrivateGroup);



        btCreateGroup = findViewById(R.id.btCreateGroup);

        tlChoseBackGroupType = findViewById(R.id.tlChoseBackGroupType);
        vpBackGroundGroup = findViewById(R.id.vpBackGroundGroup);

        vpViewHolder = new VpViewHolder(this);
        vpBackGroundGroup.setAdapter(vpViewHolder);

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


        rbPublicGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupVision = "Public";
            }
        });

        rbPrivateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupVision = "Private";
            }
        });

        


        groupProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE1);
            }
        });

        /*wallpaperGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE2);

            }
        });*/





        btCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateGroup();
            }
        });

        LoadUserId();


    }

    public String ChoseBackGroundType = null;

    private void LoadUserId() {
        String user_id = mUserRef.child(mUser.getUid()).getKey().toString();
        preferencesUser.putString(Constant.USER_ID,user_id);


    }

    Boolean Stage1 = false;
    Boolean Stage2 = false;
    Boolean Stage3 = false;

    String ChatGroupToken;


    private void CreateGroup() {


        String GroupName = groupName.getText().toString();


        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss");
        String strDate = formatter.format(date);

        create_group_picture create_group_picture = new create_group_picture();
        Uri ImageURL3 = create_group_picture.imageUriWallpaperGroup;

        create_group_color create_group_color = new create_group_color();
        int Color = create_group_color.ColorCode;

        String PICTURESTR = create_group_picture.PICTURESTR;
        String ColorSTR = create_group_color.ColorSTR;

    if(ColorSTR == null && PICTURESTR == null){
        StyleableToast.makeText(CreateGroupActivity.this, getString(R.string.Please_complete_the_information), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
    }else if(ColorSTR.equals("null") && PICTURESTR.equals("null")){
            Toast.makeText(this, "CHOSE BACKGROUND", Toast.LENGTH_SHORT).show();

        }else if(ColorSTR.equals("Color")){
            if(GroupVision.isEmpty()){
                StyleableToast.makeText(CreateGroupActivity.this, getString(R.string.Please_complete_the_information), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();

            }else {
                HashMap hashMap = new HashMap();
                hashMap.put("colorWallpaper",Color);
                hashMap.put("groupVision",GroupVision);

                mGroupRef.child(mUser.getUid()+strDate).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Stage1 = true;
                        OpenChatGroup();

                    }



                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateGroupActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }



        }else if(PICTURESTR.equals("Picture")){

            groupWallpaper.child(mUser.getUid()+strDate).putFile(ImageURL3).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        groupWallpaper.child(mUser.getUid()+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                if(uri.toString().isEmpty() || GroupVision.isEmpty()){

                                    StyleableToast.makeText(CreateGroupActivity.this, getString(R.string.Please_complete_the_information), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();

                                }else {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("groupWallpaper",uri.toString());
                                    hashMap.put("groupVision",GroupVision);


                                    mGroupRef.child(mUser.getUid()+strDate).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            Stage1 = true;
                                            OpenChatGroup();

                                            Toast.makeText(CreateGroupActivity.this, "Create Group Success", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(CreateGroupActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }


                            }
                        });
                    }
                }
            });

        }else {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();

        }

        if(imageUriProfileGroup == null){
            StyleableToast.makeText(CreateGroupActivity.this, getString(R.string.Please_complete_the_information), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
        }else {
            groupProfile.child(mUser.getUid()+strDate).putFile(imageUriProfileGroup).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        groupProfile.child(mUser.getUid()+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap hashMap = new HashMap();

                                if(GroupName.isEmpty()){
                                    groupName.setError(getText(R.string.please_enter_group_name));
                                }else {
                                    hashMap.put("GroupProfileImage",uri.toString());
                                    hashMap.put("GroupName",GroupName);

                                    mGroupRef.child(mUser.getUid()+strDate).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {

                                            Stage2 = true;
                                            OpenChatGroup();
                                            Toast.makeText(CreateGroupActivity.this, "Create Group Success", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(CreateGroupActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }

                            }
                        });
                    }
                }
            });


            HashMap hashMap = new HashMap();
            hashMap.put("position","Admin Group");
            hashMap.put("Notification","On");

            mGroupRef.child(mUser.getUid()+strDate).child("Member").child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Stage3 = true;
                    OpenChatGroup();
                    preferencesGroup.putString(Constant.GROUP_TOKEN,mUser.getUid()+strDate);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateGroupActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();

                }
            });



            ChatGroupToken = mUser.getUid()+strDate;
        }




    }

    private void OpenChatGroup() {
        if(Stage1 == true && Stage2 == true && Stage3 == true){
            Intent intent = new Intent(CreateGroupActivity.this,ChatGroupActivity.class);
            intent.putExtra("groupKeyToChat",ChatGroupToken);
            startActivity(intent);
        }else {

        }
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE1 && resultCode == RESULT_OK && data != null){
            imageUriProfileGroup=data.getData();
            groupProfileImage.setImageURI(imageUriProfileGroup);
        }

        if(requestCode == REQUEST_CODE2 && resultCode == RESULT_OK && data != null){
            imageUriWallpaperGroup=data.getData();
            wallpaperGroup.setImageURI(imageUriWallpaperGroup);
        }


    }

}