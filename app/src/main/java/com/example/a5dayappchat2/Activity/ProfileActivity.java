package com.example.a5dayappchat2.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.a5dayappchat2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    CircleImageView profileImage;

    TextView tvUserName,tvDescription;
    Button updateButton;
    Uri imageUri;

    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    StorageReference storageRef;

    ConstraintLayout layoutChangeLanguage;

    RadioGroup rbLanguage;
    TextView tvChangeLG;
    FloatingActionButton btChangLanguage;

    ImageView btBackPage;
    TextView tvPageTitle;

    ImageButton bySettingPorfile;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.ciProfile);
        updateButton = findViewById(R.id.btUpdate);
        layoutChangeLanguage = findViewById(R.id.layoutChangeLanguage);
        rbEng = findViewById(R.id.rbEng);
        rbThai = findViewById(R.id.rbThai);
        rbLanguage = findViewById(R.id.rbLanguage);
        tvChangeLG = findViewById(R.id.tvChangeLG);
        btChangLanguage = findViewById(R.id.btChangLanguage);
        tvUserName = findViewById(R.id.tvUserName);
        tvDescription = findViewById(R.id.tvDescription);
        bySettingPorfile = findViewById(R.id.bySettingPorfile);

        btBackPage = findViewById(R.id.btBackPage);
        tvPageTitle = findViewById(R.id.tvPageTitle);

        storageRef = FirebaseStorage.getInstance().getReference().child("ProfileImages");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        btBackPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bySettingPorfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenPopUpEditProfile();
            }
        });
        
        tvPageTitle.setText(getText(R.string.profile));

        LoadProfile();


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        btChangLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAndHideCNLG();
            }
        });



    }

    private void LoadProfile() {
        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String profileImageUrl = snapshot.child("profileImage").getValue().toString();
                    //String city = snapshot.child("city").getValue().toString();
                    //String country = snapshot.child("country").getValue().toString();
                    String description = snapshot.child("description").getValue().toString();
                    String username = snapshot.child("username").getValue().toString();

                    Picasso.get().load(profileImageUrl).into(profileImage);
                    tvUserName.setText(username);
                    //cityEdittext.setText(city);
                    //countryEdittext.setText(country);
                    tvDescription.setText(description);
                } else {
                    Toast.makeText(ProfileActivity.this, "data don't exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "" + error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    CircleImageView ciProfile;
    TextView tvChooseProfileImage;
    TextInputLayout EditUserName,EditDescription;
    MaterialButton btSavePorfile;

    RadioButton rbEng,rbThai;


    private void OpenPopUpEditProfile() {
        dialogBuilder = new AlertDialog.Builder(this);
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_edit_profile,null);


        ciProfile = contarctPopView.findViewById(R.id.ciProfile);
        tvChooseProfileImage = contarctPopView.findViewById(R.id.tvChooseProfileImage);
        EditUserName = contarctPopView.findViewById(R.id.EditUserName);
        EditDescription = contarctPopView.findViewById(R.id.EditDescription);
        rbEng = contarctPopView.findViewById(R.id.rbEng);
        rbThai = contarctPopView.findViewById(R.id.rbThai);
        btSavePorfile = contarctPopView.findViewById(R.id.btSavePorfile);
        rbLanguage = contarctPopView.findViewById(R.id.rbLanguage);

        btBackPage = contarctPopView.findViewById(R.id.btBackPage);
        tvPageTitle = contarctPopView.findViewById(R.id.tvPageTitle);

        btBackPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvPageTitle.setText(getText(R.string.Edit_Profile));



        ciProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        tvChooseProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });



        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String profileImageUrl = snapshot.child("profileImage").getValue().toString();
                    //String city = snapshot.child("city").getValue().toString();
                    //String country = snapshot.child("country").getValue().toString();
                    String description = snapshot.child("description").getValue().toString();
                    String username = snapshot.child("username").getValue().toString();
                    String LG = snapshot.child("language").getValue().toString();

                    Picasso.get().load(profileImageUrl).into(ciProfile);
                    EditUserName.getEditText().setText(username);
                    //cityEdittext.setText(city);
                    //countryEdittext.setText(country);
                    EditDescription.getEditText().setText(description);


                    if(LG.equals("en")){
                        rbEng.setChecked(true);
                    }else if(LG.equals("th")){
                        rbThai.setChecked(true);

                    }else {
                        rbEng.setChecked(true);
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "data don't exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "" + error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });


        btSavePorfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpDataProfile();
            }
        });



        rbLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){

                    case R.id.rbEng:

                        HashMap hashMap = new HashMap();
                        hashMap.put("language","en");
                        mUserRef.child(mUser.getUid()).updateChildren(hashMap);

                        String language = "en";
                        setLocale(language);
                        break;

                    case R.id.rbThai:

                        HashMap hashMap2 = new HashMap();
                        hashMap2.put("language","th");
                        mUserRef.child(mUser.getUid()).updateChildren(hashMap2);

                        language = "th";
                        setLocale(language);
                        break;
                }

            }
        });


        dialogBuilder.setView(contarctPopView);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void ShowAndHideCNLG() {
        if(layoutChangeLanguage.isShown()){
            layoutChangeLanguage.setVisibility(View.GONE);
            btChangLanguage.setSize(FloatingActionButton.SIZE_NORMAL);
        }else {
            layoutChangeLanguage.setVisibility(View.VISIBLE);
            btChangLanguage.setSize(FloatingActionButton.SIZE_MINI);
        }
    }

    private void setLocale(String language) {
        Resources resource = getResources();
        DisplayMetrics metrics = resource.getDisplayMetrics();
        Configuration configuration = resource.getConfiguration();
        configuration.locale = new Locale(language);
        resource.updateConfiguration(configuration,metrics);
        onConfigurationChanged(configuration);
    }


    /*
    //กำหนดถาษา
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        tvChangeLG.setText(R.string.change_language);
        rbEng.setText(R.string.english_language);
        rbThai.setText(R.string.thai_language);
        updateButton.setText(R.string.update);

    }

     */

    private void UpDataProfile() {
        String username = EditUserName.getEditText().getText().toString();
        //String city = cityEdittext.getText().toString();
        //String country = countryEdittext.getText().toString();
        String description = EditDescription.getEditText().getText().toString();

        try {


            storageRef.child(mUser.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storageRef.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap hashMap = new HashMap();
                                hashMap.put("profileImage", uri.toString());
                                hashMap.put("username", username);
                                //hashMap.put("city", city);
                                //hashMap.put("country", country);
                                hashMap.put("description", description);
                                hashMap.put("status", "offline");

                                mUserRef.child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {

                                        Toast.makeText(ProfileActivity.this, "Success Upload", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProfileActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }catch (Exception ex){
            HashMap hashMap = new HashMap();
            hashMap.put("username", username);
            //hashMap.put("city", city);
            //hashMap.put("country", country);
            hashMap.put("description", description);
            hashMap.put("status", "offline");

            mUserRef.child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {

                    Toast.makeText(ProfileActivity.this, "Success Upload", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null){
            imageUri=data.getData();
            ciProfile.setImageURI(imageUri);

        }
    }
}
