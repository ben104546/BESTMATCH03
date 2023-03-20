package com.example.a5dayappchat2.Activity.ActivityDetailAndSetting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NotificationSpDate extends BroadcastReceiver {


    DatabaseReference  mGroupRef;

    FirebaseAuth mAuth;
    FirebaseUser mUser;



    String URL = "https://fcm.googleapis.com/fcm/send";

    RequestQueue requestQueue;


    public NotificationSpDate() {
        // Default constructor
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        requestQueue = Volley.newRequestQueue(context);


        String GroupToken = intent.getStringExtra("GroupToken");
        String DoingToken = intent.getStringExtra("DoingToken");

        Log.d("asdsadasdasdasdasd",GroupToken);
        Log.d("asdsadasdasdasdasd",DoingToken);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

          mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");

        mGroupRef.child(GroupToken).child("Activity").child(DoingToken).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String AcitvityName = snapshot.child("DoingsName").getValue().toString();
                String ActivityDate = snapshot.child("DoingsDate").getValue().toString();
                String ActivityTime = snapshot.child("DoingsTime").getValue().toString();

                for (DataSnapshot child : snapshot.child("PeopleOfActivity").getChildren()){
                    String keyUser = child.getKey();
                    Log.d("asdsadasdasdasdasd",keyUser);
                    Log.d("asdsadasdasdasdasd",AcitvityName);
                    Log.d("asdsadasdasdasdasd",ActivityDate);
                    Log.d("asdsadasdasdasdasd",ActivityTime);

                    SendNotification(keyUser,AcitvityName,ActivityDate,ActivityTime);



                    if(keyUser != mUser.getUid()){
                        if(snapshot.child("PeopleOfActivity").child(keyUser).child("Notification").getValue().equals(true)){
                            SendNotification(keyUser,AcitvityName,ActivityDate,ActivityTime);
                        }

                    }
                    

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    private void SendNotification(String keyUser, String acitvityName, String activityDate, String activityTime) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("to","/topics/"+keyUser);

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("title",acitvityName);
            jsonObject1.put("body","Activity Start at Date : "+activityDate + "Time : " + activityTime);

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
    }

