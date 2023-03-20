package com.example.a5dayappchat2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewFriendsActivity extends AppCompatActivity {

    DatabaseReference mUserRef,requestRef,friendRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String profileImageUrl,username,city,country;
    //String myProfileImageUrl,myUsername,myCity,myCountry;

    CircleImageView profileImage;
    TextView Username,address;

    Button btPerform,btDecline;

    String CurrentState = "noting";

    String description;
    String myDescription;


    String userID;


    String URL = "https://fcm.googleapis.com/fcm/send";
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friends);



        userID = getIntent().getStringExtra("userKey");

        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        requestRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        friendRef = FirebaseDatabase.getInstance().getReference().child("Friend");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        profileImage = findViewById(R.id.ciProfileImage);
        Username = findViewById(R.id.tvUsername);
        address = findViewById(R.id.tvAddress);

        btPerform = findViewById(R.id.btPerform);
        btDecline = findViewById(R.id.btDecline);

        requestQueue = Volley.newRequestQueue(this);


        LoadUser();



        btPerform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAction(userID);

            }
        });
        CheckUserExistance(userID);

        btDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Unfriend(userID);
            }
        });
    }

    private void Unfriend(String userID) {

        //unfriend
        if (CurrentState.equals("friend")){
            friendRef.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        friendRef.child(userID).child(mUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ViewFriendsActivity.this,"Your are Unfriend",Toast.LENGTH_SHORT).show();
                                    CurrentState="noting";
                                    btPerform.setText(getText(R.string.Send_Friend_Request));
                                    btDecline.setVisibility(View.GONE);

                                    GetDat("Unfriend");
                                }
                            }
                        });
                    }else {
                        Toast.makeText(ViewFriendsActivity.this, "Remove Not Successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if(CurrentState.equals("he sent pending")){
            HashMap hashMap = new HashMap();
            hashMap.put("status","decline");
            requestRef.child(userID).child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ViewFriendsActivity.this,"You have Decline Friend",Toast.LENGTH_SHORT).show();
                        CurrentState = "he sent decline";
                        btPerform.setVisibility(View.GONE);
                        btDecline.setVisibility(View.GONE);

                        GetDat("Decline");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ViewFriendsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void CheckUserExistance(String userID) {
        friendRef.child(mUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    CurrentState="friend";
                    btPerform.setText(getText(R.string.Send_SMS));
                    btDecline.setText(getText(R.string.Unfriend));
                    btDecline.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriendsActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
        friendRef.child(userID).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    CurrentState="friend";
                    btPerform.setText(getText(R.string.Send_SMS));
                    btDecline.setText(getText(R.string.Unfriend));
                    btDecline.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriendsActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
        requestRef.child(mUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        CurrentState = "I sent pending";
                        btPerform.setText(getText(R.string.Cancel_Friend_Request));

                        btDecline.setVisibility(View.GONE);
                    }
                    if(snapshot.child("status").getValue().toString().equals("decline")){
                        CurrentState = "I sent decline";
                        btPerform.setText(getText(R.string.Cancel_Friend_Request));

                        btDecline.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriendsActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
        requestRef.child(userID).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        CurrentState = "he sent pending";
                        btPerform.setText(getText(R.string.Accept_Friend_Request));
                        btDecline.setText(getText(R.string.Decline_Friend));
                        btDecline.setVisibility(View.VISIBLE);

                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriendsActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
        if(CurrentState.equals("noting")){
            CurrentState = "noting";
            btPerform.setText(getText(R.string.Send_Friend_Request));
            btDecline.setVisibility(View.GONE);




        }
    }

    private void PerformAction(String userID) {
        if (CurrentState.equals("noting")){
            HashMap hashMap = new HashMap();
            hashMap.put("status","pending");
            requestRef.child(mUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ViewFriendsActivity.this,"You have sent Friend Request",Toast.LENGTH_SHORT).show();
                        btDecline.setVisibility(View.GONE);
                        CurrentState = "I sent pending";
                        btPerform.setText(getText(R.string.Cancel_Friend_Request));

                        GetDat("Sent Friend Request");
                    }else {
                        Toast.makeText(ViewFriendsActivity.this,""+task.getException().toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(CurrentState.equals("I sent pending") || CurrentState.equals("I sent decline")){
            requestRef.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ViewFriendsActivity.this,"You have cancelled Friend Request",Toast.LENGTH_SHORT).show();
                        CurrentState = "noting";
                        btPerform.setText(getText(R.string.Send_Friend_Request));
                        btDecline.setVisibility(View.GONE);

                        GetDat("Cancelled Friend Request");

                    }else {
                        Toast.makeText(ViewFriendsActivity.this,""+task.getException().toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(CurrentState.equals("he sent pending")){
            requestRef.child(userID).child(mUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        final HashMap hashMap = new HashMap();
                        hashMap.put("status","friend");
                        hashMap.put("Notification",true);
                        /*
                        hashMap.put("username",username);
                        hashMap.put("profileImageUrl",profileImageUrl);
                        hashMap.put("description",description);

                         */

                        //update
                        final HashMap hashMap1 = new HashMap();
                        hashMap1.put("status","friend");
                        hashMap1.put("Notification",true);
                        /*
                        hashMap1.put("username",myUsername);
                        hashMap1.put("profileImageUrl",myProfileImageUrl);
                        hashMap1.put("description",myDescription);

                         */




                        friendRef.child(mUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    friendRef.child(userID).child(mUser.getUid()).updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Toast.makeText(ViewFriendsActivity.this,"You added friend",Toast.LENGTH_SHORT).show();
                                            CurrentState = "friend";
                                            btPerform.setText(getText(R.string.Send_SMS));
                                            btDecline.setText(getText(R.string.Unfriend));
                                            btDecline.setVisibility(View.VISIBLE);


                                            GetDat("Has Be Friend");



                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ViewFriendsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(CurrentState.equals("friend")){
            Intent intent = new Intent(ViewFriendsActivity.this,ChatActivity.class);
            intent.putExtra("OtherUserID",userID);
            startActivity(intent);
        }
    }

    private void GetDat(String STATUS) {
        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String MyUserId = snapshot.child("username").getValue().toString();
                    Sendnotification(STATUS,MyUserId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    private void Sendnotification(String Status, String myUserId) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("to","/topics/"+userID);

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("title",myUserId);
            jsonObject1.put("body",Status);

            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("userID",mUser.getUid());
            jsonObject2.put("type","sms");

            jsonObject.put("notification",jsonObject1);
            jsonObject.put("data",jsonObject2);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String,String>map = new HashMap<>();
                    map.put("content-type","application/json");
                    map.put("authorization","key=AAAAXuPuYeA:APA91bEb3pgPsTIR9bi-YmL6sG4wXWKrXfmm1mZ7BQLcko3Ty-DDLucsNXAtmIqeA7Euy2r8vh7t4X3WBpSabGs9tATBCMNTAZpCS6uVEJXCsTHggQlxEbLFNhIvflDwvvVBOvb_nB9z");
                    return map;
                }
            };

            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void LoadUser() {

        mUserRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    profileImageUrl = snapshot.child("profileImage").getValue().toString();
                    username = snapshot.child("username").getValue().toString();
                    //city = snapshot.child("city").getValue().toString();
                    //country = snapshot.child("country").getValue().toString();
                    description = snapshot.child("description").getValue().toString();

                    Picasso.get().load(profileImageUrl).into(profileImage);
                    Username.setText(username);
                    address.setText(description);



                } else {
                    Toast.makeText(ViewFriendsActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriendsActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

/*
    private void LoadMyProfile() {
        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    myProfileImageUrl = snapshot.child("profileImage").getValue().toString();
                    myUsername = snapshot.child("username").getValue().toString();
                    //myCity = snapshot.child("city").getValue().toString();
                    //myCountry = snapshot.child("country").getValue().toString();
                    myDescription = snapshot.child("description").getValue().toString();

                    Picasso.get().load(myProfileImageUrl).into(profileImage);
                    Username.setText(myUsername);
                    address.setText(myDescription);



                } else {
                    Toast.makeText(ViewFriendsActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFriendsActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

 */


}