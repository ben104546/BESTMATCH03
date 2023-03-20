package com.example.a5dayappchat2.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a5dayappchat2.Activity.Preferences.PreferencesUser;
import com.example.a5dayappchat2.Activity.Preferences.PreferencesVerifyTrueFalse;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout inputEmail,inputPassword,inputConfirmPassword;
    Button btnRegister;
    TextView alreadyIHaveAccount;

    FirebaseAuth mAunt;

    ProgressDialog mLoadingBar;

    boolean TrueKey;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail = findViewById(R.id.llEmail);
        inputPassword = findViewById(R.id.llPassword);
        inputConfirmPassword = findViewById(R.id.llCFPassword);

        btnRegister = findViewById(R.id.btRegister);
        alreadyIHaveAccount = findViewById(R.id.tvAlreadyIHaveAccount);

        onStart();



        TrueKey = getIntent().getBooleanExtra("KeyOpenRegsiter",true);


        mLoadingBar = new ProgressDialog(this);


        inputPassword.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextInputEditText editText = (TextInputEditText) inputPassword.getEditText();

                if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });


        inputConfirmPassword.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextInputEditText editText = (TextInputEditText) inputConfirmPassword.getEditText();

                if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtempRegistertion();
            }
        });

        alreadyIHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onStart() {

        mAunt = FirebaseAuth.getInstance();
        super.onStart();
    }






    private void AtempRegistertion() {
        String email = inputEmail.getEditText().getText().toString();
        String password = inputPassword.getEditText().getText().toString();
        String cfpassword = inputConfirmPassword.getEditText().getText().toString();

        if(email.isEmpty()){
            showError(inputEmail,"Email is not Vaild");
        }else
        if(password.isEmpty() || password.length()<5 ){
            showError(inputPassword,"Password must be greated than 5 latter");
        }else if(!cfpassword.equals(password)){
            showError(inputConfirmPassword,"Password did not Match!");
        }else{

            mAunt.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        mAunt.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                final Dialog dialog = new Dialog(RegisterActivity.this);
                                dialog.setContentView(R.layout.pop_up_verify_e_mail_register);

                                TextView tvTextVerify = dialog.findViewById(R.id.tvTextVerify);
                                Button btNextVerify = dialog.findViewById(R.id.btNextVerify);

                                tvTextVerify.setText("Please check your email : "+email+" and verify your email.");

                                btNextVerify.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);


                                    }
                                });

                                dialog.show();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, ""+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });




                    }
                    else {
                        mLoadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this,"Registration is Failed",Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    private void showError(TextInputLayout field, String text) {
        field.setError(text);
        field.requestFocus();
    }
}