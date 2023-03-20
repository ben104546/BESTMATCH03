package com.example.a5dayappchat2.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a5dayappchat2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mRef;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);



        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mUser != null) {
                    mRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                               DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

                                mUserRef.child(mUser.getUid()).child("language").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            String LG = snapshot.getValue().toString();
                                            setLocale(LG);
                                        }else {
                                            setLocale("en");
                                        }

                                    }

                                    private void setLocale(String lg) {
                                        Resources resource = getResources();
                                        DisplayMetrics metrics = resource.getDisplayMetrics();
                                        Configuration configuration = resource.getConfiguration();
                                        configuration.locale = new Locale(lg);
                                        resource.updateConfiguration(configuration,metrics);
                                        onConfigurationChanged(configuration);


                                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });







                            }else {


                                mAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        boolean isEmailVerified = mAuth.getCurrentUser().isEmailVerified();

                                        if(isEmailVerified){




                                            Intent intent = new Intent(SplashActivity.this,SetupActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else {

                                            Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                                            intent.putExtra("VerifyFalse",false);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SplashActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });





                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else {
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable,5000);
    }
}