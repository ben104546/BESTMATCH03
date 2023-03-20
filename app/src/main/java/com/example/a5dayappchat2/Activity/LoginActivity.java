package com.example.a5dayappchat2.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a5dayappchat2.Activity.Loginwithphone.LoginWithPhoneNew;
import com.example.a5dayappchat2.R;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";
    private static final String TAG2 = "FacebookLogin";
    private TextInputLayout inputEmail,inputPassword;
    Button btnLogin;
    TextView forgotPassword,createNewAccount;
    ProgressDialog mLoadingBar;
    FirebaseAuth mAunt;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    TextInputLayout llPassword;



    MaterialButton btLoginGoogle,btLoginByPhone;

    private GoogleSignInClient mGoogleSignInClient;

    CallbackManager callbackManager;

    boolean VerifyFalse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();


        inputEmail = findViewById(R.id.llEmail);
        inputPassword = findViewById(R.id.llPassword);
        btnLogin = findViewById(R.id.btLogin);
        forgotPassword = findViewById(R.id.tvForgotPassword);
        createNewAccount = findViewById(R.id.tvCreateNewAccount);
        btLoginGoogle = findViewById(R.id.btLoginGoogle);
        btLoginByPhone = findViewById(R.id.btLoginByPhone);

        VerifyFalse = getIntent().getBooleanExtra("VerifyFalse",true);



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

        btLoginByPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, LoginWithPhoneNew.class);
                startActivity(intent);
                finish();

            }
        });

        btLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mLoadingBar = new ProgressDialog(this);
        mAunt = FirebaseAuth.getInstance();

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtamptLogin();
            }
        });



        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });
        
        CheckSplashVerify();


    }

    private void CheckSplashVerify() {
        if(!VerifyFalse){
    final Dialog dialog = new Dialog(LoginActivity.this);
    dialog.setContentView(R.layout.pop_up_verify_email_not_right_register);
    Button sendVerify = dialog.findViewById(R.id.sendVerify);

    TextView tvVerfytitle = dialog.findViewById(R.id.tvVerfytitle);


    sendVerify.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mAunt.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(LoginActivity.this, "Send Verify", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    });

    dialog.show();


    VerifyFalse = true;
}
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAunt.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());

            }catch (ApiException e){
                Log.w(TAG, "Google sign in failed", e);
            }
        }



    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAunt.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG2, "signInWithCredential:success");
                            FirebaseUser user = mAunt.getCurrentUser();
                            updateUI(user);

                            Intent intent = new Intent(LoginActivity.this, SetupActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG2, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void updateUI(Object o) {
    }



    private void AtamptLogin() {
        String email = inputEmail.getEditText().getText().toString().trim();
        String password = inputPassword.getEditText().getText().toString().trim();

        if(email.isEmpty()){
            showError(inputEmail,"Email is not Vaild");
        }else if(password.isEmpty() || password.length()<5 ){
            showError(inputPassword,"Password must be greated than 5 latter");
        }else{


            mAunt.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        if(mAunt.getCurrentUser().isEmailVerified()){

                                    final Dialog dialog = new Dialog(LoginActivity.this);
                                    dialog.setContentView(R.layout.pop_up_verify_email_right_register);

                                    dialog.show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    Toast.makeText(LoginActivity.this,"Login is Successful",Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this,SplashActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 3000);



                        }else {

                            final Dialog dialog = new Dialog(LoginActivity.this);
                            dialog.setContentView(R.layout.pop_up_verify_email_not_right_register);
                            Button sendVerify = dialog.findViewById(R.id.sendVerify);

                            sendVerify.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mAunt.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(LoginActivity.this, "Send Verify", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LoginActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                            dialog.show();
                        }

                    }else {
                        Toast.makeText(LoginActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
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