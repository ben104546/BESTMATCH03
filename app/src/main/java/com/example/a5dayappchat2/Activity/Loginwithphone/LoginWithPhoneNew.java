package com.example.a5dayappchat2.Activity.Loginwithphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a5dayappchat2.Activity.SetupActivity;
import com.example.a5dayappchat2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginWithPhoneNew extends AppCompatActivity {



    TextInputLayout etPhone;
    Button btNextOtp;
    FirebaseAuth mAuth;

    TextInputLayout llOtp;
    Button btOtp;

    AppKeyBoard KeyBoard;

    String verificationID;

    TextView tvTitleLoginWithPhone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_phone);

        mAuth = FirebaseAuth.getInstance();

        etPhone = findViewById(R.id.etPhone);
        btNextOtp = findViewById(R.id.btNextOtp);

        llOtp = findViewById(R.id.llOtp);
        btOtp = findViewById(R.id.btOtp);


        llOtp.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextInputEditText editText = (TextInputEditText) llOtp.getEditText();

                if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });


        tvTitleLoginWithPhone = findViewById(R.id.tvTitleLoginWithPhone);


        KeyBoard = (AppKeyBoard) findViewById(R.id.keyboard);

        etPhone.getEditText().setRawInputType(InputType.TYPE_CLASS_TEXT);
        etPhone.getEditText().setTextIsSelectable(true);

        InputConnection ic = etPhone.getEditText().onCreateInputConnection(new EditorInfo());
        KeyBoard.setInputConnection(ic);

        btNextOtp.clearFocus();


        etPhone.getEditText().setInputType(InputType.TYPE_NULL);





        btNextOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                etPhone.setVisibility(View.GONE);
                btNextOtp.setVisibility(View.GONE);

                llOtp.setVisibility(View.VISIBLE);
                btOtp.setVisibility(View.VISIBLE);


                tvTitleLoginWithPhone.setText("Enter The OTP Number");


                KeyBoard = (AppKeyBoard) findViewById(R.id.keyboard);

                llOtp.getEditText().setRawInputType(InputType.TYPE_CLASS_TEXT);
                llOtp.getEditText().setTextIsSelectable(true);

                InputConnection ic = llOtp.getEditText().onCreateInputConnection(new EditorInfo());
                KeyBoard.setInputConnection(ic);

                llOtp.getEditText().setInputType(InputType.TYPE_NULL);


                String number = etPhone.getEditText().getText().toString();
                SendVerifyCode(number);



            }
        });


        btOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(llOtp.getEditText().getText().toString())){
                    Toast.makeText(LoginWithPhoneNew.this, "Wrong OTP", Toast.LENGTH_SHORT).show();
                }else {
                    VerifyCode(llOtp.getEditText().getText().toString());
                }

            }
        });


    }

    private void SendVerifyCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+66"+number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.

            final String code = credential.getSmsCode();
            if(code!=null){
                VerifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(LoginWithPhoneNew.this, "Verify Phone Fail", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginWithPhoneNew.this,LoginWithPhoneNew.class);
            startActivity(intent);
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.

            // Save verification ID and resending token so we can use them later
            super.onCodeSent(verificationId,token);
            verificationID = verificationId;
        }
    };

    private void VerifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        siginbyCredentials(credential);
    }

    private void siginbyCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginWithPhoneNew.this, "VerifyCodeSuccess", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginWithPhoneNew.this,SetupActivity.class);
                            startActivity(intent);
                            finish();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginWithPhoneNew.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

