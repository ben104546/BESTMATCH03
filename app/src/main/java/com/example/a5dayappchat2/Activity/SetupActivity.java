package com.example.a5dayappchat2.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.a5dayappchat2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.muddz.styleabletoast.StyleableToast;

public class SetupActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    CircleImageView profileImageView;
    EditText inputCity,inputCountry;
TextInputLayout inputUsername,inputDescription;
    Button btnSave;
    Uri imageUri;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;
    StorageReference storageRef;
    ProgressDialog mLoadingBar;
    Toolbar toolbar;
    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        profileImageView = findViewById(R.id.profile_image);
        inputUsername = findViewById(R.id.inputUserName);
        inputCity = findViewById(R.id.inputCity);
        inputCountry = findViewById(R.id.inputCountry);
        inputDescription = findViewById(R.id.inputDescription);
        btnSave = findViewById(R.id.btSave);
        mLoadingBar = new ProgressDialog(this);
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setup Profile");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        storageRef = FirebaseStorage.getInstance().getReference().child("ProfileImages");

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveData();

            }
        });

        CheckUserValue();
        

    }


    private void CheckUserValue() {

            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.child(mUser.getUid()).exists()){
                        Intent intent = new Intent(SetupActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(!snapshot.child(mUser.getUid()).exists()){
                        Toast.makeText(SetupActivity.this, "SetUp", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(SetupActivity.this, "SetUp", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SetupActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void SaveData() {
        String username = inputUsername.getEditText().getText().toString();
        String description = inputDescription.getEditText().getText().toString();

        if (username.isEmpty() || username.length()<3){
            showError(inputUsername.getEditText(),"Username is not valid");
        }else if (description.isEmpty() || description.length()<3){
            showError(inputDescription.getEditText(),"Description is not valid");
        }else if (imageUri == null){
            StyleableToast.makeText(SetupActivity.this, "Please enter your picture ", Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
        }else {

            mLoadingBar.setTitle("Adding Setup Profile");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

                storageRef.child(mUser.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            storageRef.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("username",username);
                                    hashMap.put("description",description);
                                    hashMap.put("profileImage",uri.toString());
                                    hashMap.put("account_status","user");
                                    hashMap.put("status","offline");
                                    hashMap.put("User-Id",mUser.getUid());
                                    hashMap.put("language","en");


                                    mRef.child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            Intent intent = new Intent(SetupActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            mLoadingBar.dismiss();
                                            Toast.makeText(SetupActivity.this,"Setup Profile Completed",Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            mLoadingBar.dismiss();
                                            Toast.makeText(SetupActivity.this,e.toString(),Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            });
                        }
                    }
                });
        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null){
            imageUri=data.getData();
            profileImageView.setImageURI(imageUri);

        }
    }
}