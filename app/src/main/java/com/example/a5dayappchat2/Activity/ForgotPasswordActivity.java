package com.example.a5dayappchat2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a5dayappchat2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etPasswordReset;
    Button btSend;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        etPasswordReset = findViewById(R.id.etPasswordReset);
        btSend = findViewById(R.id.btSend);

        mAuth = FirebaseAuth.getInstance();

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etPasswordReset.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(ForgotPasswordActivity.this, "Please Enter Your E-mail", Toast.LENGTH_SHORT).show();
                }else {
                        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ForgotPasswordActivity.this, "Please Check Your E-mail", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));

                                } else {
                                    Toast.makeText(ForgotPasswordActivity.this, "E-mail not sent !!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            }
        });


    }
}