package com.example.a5dayappchat2.Activity;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.a5dayappchat2.Activity.Utills.Chat;
import com.example.a5dayappchat2.Activity.Utills.StickerBasket;
import com.example.a5dayappchat2.Activity.Utills.StickerChatMenu;
import com.example.a5dayappchat2.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.muddz.styleabletoast.StyleableToast;

public class ChatActivity extends BaseActivity {

    private static final int REQUEST_CODE = 101;
    private static final int REQUEST_CODE_VIDEO = 102;
    private static final int REQUEST_CODE_AUDIO = 103;
    private static final int REQUEST_CODE_DOC = 104;

    Toolbar toolbar;
    RecyclerView recyclerView;
    EditText etTextSend;
    ImageView  ivSendText,ivUpload,ivSendPicture,ivSendVideo,ivSendSound,ivSendDoc,ivSendActivityOne,ivSticker;
    CircleImageView ciProfile;
    TextView tvUsername,tvStatus;
    ConstraintLayout constraintLayout3,constraintLayout2;

    DatabaseReference mUserRef,smsRef,mActivityOneOnOne,mStickerStoreRef,mMystickerlist,mFriendRef,mMySticker,mOrder;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    VideoView firstVideoViewChat,secondVideoViewChat;

    ImageView ivNotify,ivNotifyOff;

    Uri imageUri;
    Uri videoUri;
    Uri audioUri;
    Uri docUri;

    String OtherUserID;

    String myProfileImage;
    String username;



    String OtherUsername , OtherUserProfileImage = null , OtherUserStatus;

    FirebaseRecyclerOptions<Chat>options;
    FirebaseRecyclerAdapter<Chat, ChatMyViewHolder>adapter;

    ImageButton btScollToDown4;




    ConstraintLayout constraintLayoutSticker;

    StorageReference storageChatRef,storageChatVDORef,storageChatAudioRef,storageChatDocRef;

    RecyclerView recyclerViewStickerList;
    RecyclerView recyclerViewStickerListButton;

    FirebaseRecyclerOptions<StickerChatMenu>optionsStickerHead;
    FirebaseRecyclerAdapter<StickerChatMenu,StickerAllListViewHolder>adapterStickerHead;

    FirebaseRecyclerOptions<StickerChatMenu>optionsStickerBody;
    FirebaseRecyclerAdapter<StickerChatMenu,StickerAllListViewHolder>adapterStickerBody;

    ArrayList<String> LisStickerMyKey = new ArrayList<>() ;

    TextView tvOrderId,tvPriceOrder;
    RecyclerView recyclerViewSticker;
    ImageView tvSlipPayment;
    Button btClosedilog;


    FirebaseRecyclerOptions<StickerBasket> optionsStickerBK;
    FirebaseRecyclerAdapter<StickerBasket, StikcerBasketListViewHolder> adapterStickerBK;


    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycleViewGroup);
        recyclerViewStickerList = findViewById(R.id.recyclerViewStickerList);
        recyclerViewStickerListButton = findViewById(R.id.recyclerViewStickerListButton);

        etTextSend = findViewById(R.id.etTextSend);
        ivSendText = findViewById(R.id.ivSendText);
        ivUpload = findViewById(R.id.ivUpload);
        ivSendPicture = findViewById(R.id.ivSendPicture);
        ivSendVideo = findViewById(R.id.ivSendVideo);
        ivSendSound = findViewById(R.id.ivSendSound);
        ivSendDoc = findViewById(R.id.ivSendDoc);
        ivSticker = findViewById(R.id.ivSticker);

        ivNotify = findViewById(R.id.ivNotify);
        ivNotifyOff = findViewById(R.id.ivNotifyOff);

        firstVideoViewChat = findViewById(R.id.firstVideoViewChat);
        secondVideoViewChat = findViewById(R.id.secondVideoViewChat);

        constraintLayout3 = findViewById(R.id.constraintLayout3);
        constraintLayout2 = findViewById(R.id.constraintLayout2);

        constraintLayoutSticker = findViewById(R.id.constraintLayoutSticker);


        btScollToDown4 = findViewById(R.id.btScollToDown4);



        ciProfile = findViewById(R.id.ciProfile);
        tvUsername = findViewById(R.id.tvUsername);
        tvStatus = findViewById(R.id.tvStatus);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStickerList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerViewStickerListButton.setLayoutManager(layoutManager);

        OtherUserID = getIntent().getStringExtra("OtherUserID");


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mActivityOneOnOne = FirebaseDatabase.getInstance().getReference().child("ActivityOneOnOne");


        mStickerStoreRef = FirebaseDatabase.getInstance().getReference().child("Sticker");
        mFriendRef = FirebaseDatabase.getInstance().getReference().child("Friend");
        mMySticker = FirebaseDatabase.getInstance().getReference().child("MySticker");
        mMystickerlist = FirebaseDatabase.getInstance().getReference().child("MySticker");
        smsRef = FirebaseDatabase.getInstance().getReference().child("Message");
        storageChatRef = FirebaseStorage.getInstance().getReference().child("ChatImage");
        storageChatVDORef = FirebaseStorage.getInstance().getReference().child("ChatVDO");
        storageChatAudioRef = FirebaseStorage.getInstance().getReference().child("ChatAudio");
        storageChatDocRef = FirebaseStorage.getInstance().getReference().child("ChatDoc");

        mOrder = FirebaseDatabase.getInstance().getReference().child("Order");


        LoadOtherUser();
        loadMyProfile();

        CheckNotificationButton();


        ivSendDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"application/pdf", "application/msword", "text/plain","application/vnd.openxmlformats-officedocument.wordprocessingml.document"}); // Filter by MIME type
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Allow multiple files to be selected
                startActivityForResult(intent,REQUEST_CODE_DOC);


            }
        });


        ivSendSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(intent,REQUEST_CODE_AUDIO);

                Toast.makeText(ChatActivity.this, "AUDIO", Toast.LENGTH_SHORT).show();
            }
        });





        ivSendVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent,REQUEST_CODE_VIDEO);


            }
        });

        ivSendPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE);




            }
        });

        ivUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowButton();
            }
        });

        ivSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendChat();
            }
        });



        ivSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(constraintLayoutSticker.isShown()){
                    constraintLayoutSticker.setVisibility(View.GONE);
                }else {
                    constraintLayoutSticker.setVisibility(View.VISIBLE);
                }
            }
        });


        


        ivNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivNotify.setVisibility(View.GONE);
                ivNotifyOff.setVisibility(View.VISIBLE);


                HashMap hashMap = new HashMap();
                hashMap.put("Notification",false);


                mFriendRef.child(mUser.getUid()).child(OtherUserID).updateChildren(hashMap);


            }
        });

        ivNotifyOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivNotifyOff.setVisibility(View.GONE);
                ivNotify.setVisibility(View.VISIBLE);


                HashMap hashMap = new HashMap();
                hashMap.put("Notification",true);



                mFriendRef.child(mUser.getUid()).child(OtherUserID).updateChildren(hashMap);


            }
        });





        LoadStickerMyKey();


        LoadSms();
    }

    private void CheckNotificationButton() {
        mFriendRef.child(mUser.getUid()).child(OtherUserID).child("Notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    boolean Noti = (boolean) snapshot.getValue();

                    if(Noti){
                        ivNotify.setVisibility(View.VISIBLE);
                        ivNotifyOff.setVisibility(View.GONE);
                    }else {
                        ivNotify.setVisibility(View.GONE);
                        ivNotifyOff.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LoadStickerMyKey() {
        mMySticker.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot child : snapshot.getChildren()) {

            String key = child.getKey();

            LoadSticker(key);


        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Toast.makeText(ChatActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
    }
});

    }




    private void LoadSticker(String key) {


        Query query = mStickerStoreRef;
        optionsStickerHead = new FirebaseRecyclerOptions.Builder<StickerChatMenu>().setQuery(query, StickerChatMenu.class).build();
        adapterStickerHead = new FirebaseRecyclerAdapter<StickerChatMenu, StickerAllListViewHolder>(optionsStickerHead) {
            @Override
            protected void onBindViewHolder(@NonNull StickerAllListViewHolder holder, int position, @NonNull StickerChatMenu model) {

                if(model.getStickerId().equals(key)){
                    Picasso.get().load(model.getImageIcon()).into(holder.ivStickerIcon);
                }else {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }


                boolean methodLoaded = false;

                if (!methodLoaded) {
                    LoadStickerAllList(key);
                    methodLoaded = true;
                } else {
                    // do other things
                }



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String KeySticker = getRef(position).getKey().toString();
                        LoadStickerAllList(KeySticker);
                    }
                });

            }

            @NonNull
            @Override
            public StickerAllListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_cell_sticker_icon,parent,false);

                return new StickerAllListViewHolder(view);
            }
        };
        adapterStickerHead.startListening();
        recyclerViewStickerList.setAdapter(adapterStickerHead);

    }

    private void LoadStickerAllList(String keySticker) {
        Query query = mStickerStoreRef.child(keySticker).child("StikcerImage");
        optionsStickerBody = new FirebaseRecyclerOptions.Builder<StickerChatMenu>().setQuery(query,StickerChatMenu.class).build();
        adapterStickerBody = new FirebaseRecyclerAdapter<StickerChatMenu, StickerAllListViewHolder>(optionsStickerBody) {
            @Override
            protected void onBindViewHolder(@NonNull StickerAllListViewHolder holder, int position, @NonNull StickerChatMenu model) {
                Picasso.get().load(model.getImageURI()).into(holder.tvStickerOne);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ImageUri = model.getImageURI().toString();
                        SendSticker(ImageUri);
                    }
                });
            }

            @NonNull
            @Override
            public StickerAllListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_viewe_stickeralllist,parent,false);
                return new StickerAllListViewHolder(view);
            }
        };
        adapterStickerBody.startListening();
        recyclerViewStickerListButton.setAdapter(adapterStickerBody);

    }

    private void SendSticker(String imageUri) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss:SSS");
        String strDate = formatter.format(date);

        HashMap hashMap = new HashMap();
        hashMap.put("sticker",imageUri);
        hashMap.put("userID",mUser.getUid());
        hashMap.put("picture","");
        hashMap.put("status","unseen");
        hashMap.put("sms","");
        hashMap.put("video","");
        hashMap.put("audio","");
        hashMap.put("doc","");

        smsRef.child(mUser.getUid()).child(OtherUserID).child("Chat").child(strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                smsRef.child(OtherUserID).child(mUser.getUid()).child("Chat").child(strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Toast.makeText(ChatActivity.this, "SendSticker Successful", Toast.LENGTH_SHORT).show();

                        mFriendRef.child(OtherUserID).child(mUser.getUid()).child("Notification").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UpdataDataTimeNewest(strDate);

                                if(snapshot.exists()){
                                    boolean NotificationStatus = (boolean) snapshot.getValue();

                                    if(NotificationStatus){

                                        sendNotification("Sticker from ","");
                                    }else {
                                        //
                                    }

                                }else {
                                    //sendNotification("Sticker from ","");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(ChatActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });






            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this, ""+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {

        if(constraintLayoutSticker.isShown()){
            constraintLayoutSticker.setVisibility(View.GONE);
        }else {
            constraintLayoutSticker.setVisibility(View.VISIBLE);
            super.onBackPressed();
        }


    }

    private void SendDoc() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss:SSS");
        String strDate = formatter.format(date);

        storageChatDocRef.child(OtherUserID).child(mUser.getUid()+strDate).putFile(docUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    storageChatDocRef.child(OtherUserID).child(mUser.getUid()+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            HashMap hashMap = new HashMap();
                            hashMap.put("picture","");
                            hashMap.put("status","unseen");
                            hashMap.put("userID",mUser.getUid());
                            hashMap.put("audio","");
                            hashMap.put("sms","");
                            hashMap.put("video","");
                            hashMap.put("sticker","");
                            hashMap.put("doc",uri.toString());

                            smsRef.child(OtherUserID).child(mUser.getUid()).child("Chat").child(strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    smsRef.child(mUser.getUid()).child(OtherUserID).child("Chat").child(strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Toast.makeText(ChatActivity.this, "Upload Doc Success", Toast.LENGTH_SHORT).show();
                                            UpdataDataTimeNewest(strDate);

                                            mFriendRef.child(OtherUserID).child(mUser.getUid()).child("Notification").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        boolean NotificationStatus = (boolean) snapshot.getValue();

                                                        if(NotificationStatus){

                                                            sendNotification("Document  from ","");
                                                        }else {
                                                            //
                                                        }

                                                    }else {
                                                       // sendNotification("Document  from ","");
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(ChatActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void SendAudio() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss:SSS");
        String strDate = formatter.format(date);

        storageChatAudioRef.child(OtherUserID).child(mUser.getUid()+strDate).putFile(audioUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                UpdataDataTimeNewest(strDate);

                if(task.isSuccessful()){
                    storageChatAudioRef.child(OtherUserID).child(mUser.getUid()+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            HashMap hashMap = new HashMap();
                            hashMap.put("picture","");
                            hashMap.put("status","unseen");
                            hashMap.put("userID",mUser.getUid());
                            hashMap.put("audio",uri.toString());
                            hashMap.put("sms","");
                            hashMap.put("video","");
                            hashMap.put("doc","");
                            hashMap.put("sticker","");

                            smsRef.child(OtherUserID).child(mUser.getUid()).child("Chat").child(strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    smsRef.child(mUser.getUid()).child(OtherUserID).child("Chat").child(strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Toast.makeText(ChatActivity.this, "Upload Audio Success", Toast.LENGTH_SHORT).show();


                                            mFriendRef.child(OtherUserID).child(mUser.getUid()).child("Notification").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        boolean NotificationStatus = (boolean) snapshot.getValue();

                                                        if(NotificationStatus){

                                                            sendNotification("Audio  from ","");
                                                        }else {
                                                            //
                                                        }

                                                    }else {
                                                       // sendNotification("Audio  from ","");
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(ChatActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });



                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SendVideoChat() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss:SSS");
        String strDate = formatter.format(date);

        storageChatVDORef.child(OtherUserID).child(mUser.getUid()+strDate).putFile(videoUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                UpdataDataTimeNewest(strDate);

                if(task.isSuccessful()){
                    storageChatVDORef.child(OtherUserID).child(mUser.getUid()+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            HashMap hashMap = new HashMap();
                            hashMap.put("picture","");
                            hashMap.put("status","unseen");
                            hashMap.put("userID",mUser.getUid());
                            hashMap.put("sms","");
                            hashMap.put("video",uri.toString());
                            hashMap.put("audio","");
                            hashMap.put("doc","");
                            hashMap.put("sticker","");

                            smsRef.child(OtherUserID).child(mUser.getUid()).child("Chat").child(strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    smsRef.child(mUser.getUid()).child(OtherUserID).child("Chat").child(strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Toast.makeText(ChatActivity.this, "Upload VDO Success", Toast.LENGTH_SHORT).show();

                                            mFriendRef.child(OtherUserID).child(mUser.getUid()).child("Notification").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        boolean NotificationStatus = (boolean) snapshot.getValue();

                                                        if(NotificationStatus){

                                                            sendNotification("Video from ","");
                                                        }else {
                                                            //
                                                        }

                                                    }else {
                                                        //sendNotification("Video from ","");
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(ChatActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });




                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void SendPicture() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss:SSS");
        String strDate = formatter.format(date);

        storageChatRef.child(OtherUserID).child(mUser.getUid()+strDate).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                UpdataDataTimeNewest(strDate);

                if(task.isSuccessful()){
                    storageChatRef.child(OtherUserID).child(mUser.getUid()+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            HashMap hashMap = new HashMap();
                            hashMap.put("picture",uri.toString());
                            hashMap.put("status","unseen");
                            hashMap.put("userID",mUser.getUid());
                            hashMap.put("sms","");
                            hashMap.put("video","");
                            hashMap.put("audio","");
                            hashMap.put("doc","");
                            hashMap.put("sticker","");

                            smsRef.child(OtherUserID).child(mUser.getUid()).child("Chat").child(strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    smsRef.child(mUser.getUid()).child(OtherUserID).child("Chat").child(strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Toast.makeText(ChatActivity.this, "Picture Sent", Toast.LENGTH_SHORT).show();


                                            mFriendRef.child(OtherUserID).child(mUser.getUid()).child("Notification").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        boolean NotificationStatus = (boolean) snapshot.getValue();

                                                        if(NotificationStatus){

                                                            sendNotification("Picture from ","");
                                                        }else {
                                                            //
                                                        }

                                                    }else {
                                                       // sendNotification("Picture from ","");
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(ChatActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });



                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(ChatActivity.this, "Upload Picture Fail", Toast.LENGTH_SHORT).show();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void ShowButton() {
        if(constraintLayout3.isShown()){
            constraintLayout3.setVisibility(View.GONE);
            constraintLayout2.setVisibility(View.VISIBLE);

        }else {
            constraintLayout3.setVisibility(View.VISIBLE);
            constraintLayout2.setVisibility(View.INVISIBLE);

        }

    }

    private void loadMyProfile() {
        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    myProfileImage = snapshot.child("profileImage").getValue().toString();
                    username = snapshot.child("username").getValue().toString();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this,""+error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void LoadSms() {
        options = new FirebaseRecyclerOptions.Builder<Chat>().setQuery(smsRef.child(mUser.getUid()).child(OtherUserID).child("Chat"),Chat.class).build();
        adapter = new FirebaseRecyclerAdapter<Chat,ChatMyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatMyViewHolder holder, int position, @NonNull Chat model) {

                if(getRef(position).getKey().toString().equals("ChatTimeLate")){

                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));

                }else {


                    if (model.getUserID().equals(mUser.getUid())) {
                        holder.FirstUserImage.setVisibility(View.GONE);

                        holder.SecondUserImage.setVisibility(View.VISIBLE);


                        int ScollingNum = recyclerView.getAdapter().getItemCount() - 1;

                        recyclerView.smoothScrollToPosition(ScollingNum);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                        recyclerView.stopScroll();
                                        btScollToDown4.setVisibility(View.VISIBLE);
                                        super.onScrolled(recyclerView, dx, dy);
                                    }
                                });
                            }
                        }, 1000);


                        btScollToDown4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                recyclerView.scrollToPosition(ScollingNum);
                                btScollToDown4.setVisibility(View.GONE);

                            }
                        });


                        // show Text
                        if (!model.getSms().isEmpty()) {
                            holder.SecondTextUser.setVisibility(View.VISIBLE);
                            holder.FirstTextUser.setVisibility(View.GONE);

                            holder.secondImageSend.setVisibility(View.GONE);
                            holder.firstImageSend.setVisibility(View.GONE);

                            holder.secondVideoViewChat.setVisibility(View.GONE);
                            holder.firstVideoViewChat.setVisibility(View.GONE);

                            holder.ivSecondMp3.setVisibility(View.GONE);
                            holder.ivFirstMp3.setVisibility(View.GONE);

                            holder.ivSecondPdf.setVisibility(View.GONE);
                            holder.ivFirstPdf.setVisibility(View.GONE);

                            holder.secondSticker.setVisibility(View.GONE);
                            holder.firstSticker.setVisibility(View.GONE);

                            holder.btViewIcon.setVisibility(View.GONE);


                            holder.SecondTextUser.setText(model.getSms());

                            holder.SecondTextUser.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {

                                    if (holder.layoutSecondButton.isShown()) {
                                        holder.layoutSecondButton.setVisibility(View.GONE);
                                    } else {
                                        holder.layoutSecondButton.setVisibility(View.VISIBLE);
                                    }

                                    return true;
                                }
                            });

                            holder.ivSecondtSave.setVisibility(View.GONE);
                            holder.ivSecondtDel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String PathText = getRef(position).getKey().toString();

                                    smsRef.child(OtherUserID).child(mUser.getUid()).child("Chat").child(PathText).removeValue();
                                    smsRef.child(mUser.getUid()).child(OtherUserID).child("Chat").child(PathText).removeValue();
                                }
                            });

                            //show picture
                        } else if (!model.getPicture().isEmpty()) {
                            holder.SecondTextUser.setVisibility(View.GONE);
                            holder.FirstTextUser.setVisibility(View.GONE);

                            holder.secondImageSend.setVisibility(View.VISIBLE);
                            holder.firstImageSend.setVisibility(View.GONE);

                            holder.secondVideoViewChat.setVisibility(View.GONE);
                            holder.firstVideoViewChat.setVisibility(View.GONE);

                            holder.ivSecondMp3.setVisibility(View.GONE);
                            holder.ivFirstMp3.setVisibility(View.GONE);

                            holder.ivSecondPdf.setVisibility(View.GONE);
                            holder.ivFirstPdf.setVisibility(View.GONE);

                            holder.secondSticker.setVisibility(View.GONE);
                            holder.firstSticker.setVisibility(View.GONE);

                            holder.btViewIcon.setVisibility(View.GONE);


                            Picasso.get().load(model.getPicture()).into(holder.secondImageSend);

                            holder.secondImageSend.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if (holder.layoutSecondButton.isShown()) {
                                        holder.layoutSecondButton.setVisibility(View.GONE);
                                    } else {
                                        holder.layoutSecondButton.setVisibility(View.VISIBLE);
                                    }
                                    return true;
                                }

                                
                            });

                            holder.ivSecondtSave.setVisibility(View.VISIBLE);

                            holder.ivSecondtSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Uri ImageURI = Uri.parse(model.getPicture());
                                    /*
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(ImageURI));
                                    String title = URLUtil.guessFileName(ImageURI, null, null);


                                    request.setTitle(title);
                                    request.setDescription("Download File");
                                    String cookie = CookieManager.getInstance().getCookie(ImageURI);
                                    request.addRequestHeader("cookie", cookie);
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

                                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                    downloadManager.enqueue(request);

                                    Toast.makeText(ChatActivity.this, "Download Image Success", Toast.LENGTH_SHORT).show();


                                     */



                                    DownloadManager.Request request = new DownloadManager.Request(ImageURI);
                                    String title = URLUtil.guessFileName(ImageURI.toString(), null, null);

                                    request.setTitle(title);
                                    request.setDescription("Downloading...");
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "BEST_MATCH");

                                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                    manager.enqueue(request);
                                }
                            });

                            holder.ivSecondtDel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String PathText = getRef(position).getKey().toString();

                                    smsRef.child(OtherUserID).child(mUser.getUid()).child("Chat").child(PathText).removeValue();
                                    smsRef.child(mUser.getUid()).child(OtherUserID).child("Chat").child(PathText).removeValue();
                                }
                            });


                            //Show Video
                        } else if (!model.getVideo().isEmpty()) {
                            holder.SecondTextUser.setVisibility(View.GONE);
                            holder.FirstTextUser.setVisibility(View.GONE);

                            holder.secondImageSend.setVisibility(View.GONE);
                            holder.firstImageSend.setVisibility(View.GONE);

                            holder.secondVideoViewChat.setVisibility(View.VISIBLE);
                            holder.firstVideoViewChat.setVisibility(View.GONE);

                            holder.ivSecondMp3.setVisibility(View.GONE);
                            holder.ivFirstMp3.setVisibility(View.GONE);

                            holder.ivSecondPdf.setVisibility(View.GONE);
                            holder.ivFirstPdf.setVisibility(View.GONE);

                            holder.secondSticker.setVisibility(View.GONE);
                            holder.firstSticker.setVisibility(View.GONE);
                            holder.btViewIcon.setVisibility(View.GONE);


                            MediaController mediaController = new MediaController(ChatActivity.this);
                            holder.secondVideoViewChat.setVideoPath(model.getVideo());

                            mediaController.setAnchorView(holder.secondVideoViewChat);
                            holder.secondVideoViewChat.setMediaController(mediaController);
                            holder.secondVideoViewChat.start();


                            holder.secondVideoViewChat.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if (holder.layoutSecondButton.isShown()) {
                                        holder.layoutSecondButton.setVisibility(View.GONE);
                                    } else {
                                        holder.layoutSecondButton.setVisibility(View.VISIBLE);
                                    }
                                    return true;
                                }
                            } );
                            holder.ivSecondtSave.setVisibility(View.VISIBLE);

                            holder.ivSecondtSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String VdoURI = model.getVideo().toString();

                                    /*

                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(VdoURI));
                                    String title = URLUtil.guessFileName(VdoURI, null, null);
                                    request.setTitle(title);
                                    request.setDescription("Download File");
                                    String cookie = CookieManager.getInstance().getCookie(VdoURI);
                                    request.addRequestHeader("cookie", cookie);
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

                                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                    downloadManager.enqueue(request);

                                    Toast.makeText(ChatActivity.this, "Download Video Success", Toast.LENGTH_SHORT).show();

                                     */


                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(VdoURI));
                                    String title = URLUtil.guessFileName(VdoURI, null, null);

                                    request.setTitle(title);
                                    request.setDescription("Downloading...");
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "BEST_MATCH");

                                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                    manager.enqueue(request);
                                }
                            });

                            holder.ivSecondtDel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String PathText = getRef(position).getKey().toString();

                                    smsRef.child(OtherUserID).child(mUser.getUid()).child("Chat").child(PathText).removeValue();
                                    smsRef.child(mUser.getUid()).child(OtherUserID).child("Chat").child(PathText).removeValue();
                                }
                            });


                            //ShowMp3
                        } else if (!model.getAudio().isEmpty()) {

                            holder.SecondTextUser.setVisibility(View.GONE);
                            holder.FirstTextUser.setVisibility(View.GONE);

                            holder.secondImageSend.setVisibility(View.GONE);
                            holder.firstImageSend.setVisibility(View.GONE);

                            holder.secondVideoViewChat.setVisibility(View.GONE);
                            holder.firstVideoViewChat.setVisibility(View.GONE);

                            holder.ivSecondMp3.setVisibility(View.VISIBLE);
                            holder.ivFirstMp3.setVisibility(View.GONE);

                            holder.ivSecondPdf.setVisibility(View.GONE);
                            holder.ivFirstPdf.setVisibility(View.GONE);

                            holder.secondSticker.setVisibility(View.GONE);
                            holder.firstSticker.setVisibility(View.GONE);
                            holder.btViewIcon.setVisibility(View.GONE);


                            holder.ivSecondMp3.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if (holder.layoutSecondButton.isShown()) {
                                        holder.layoutSecondButton.setVisibility(View.GONE);
                                    } else {
                                        holder.layoutSecondButton.setVisibility(View.VISIBLE);
                                    }
                                    return true;
                                }
                            });

                            holder.ivSecondtSave.setVisibility(View.VISIBLE);

                            holder.ivSecondtSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String audioURI = model.getAudio().toString();





                                    /*

                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(audioURI));
                                    String title = URLUtil.guessFileName(audioURI, null, null);
                                    request.setTitle(title);
                                    request.setDescription("Download File");
                                    String cookie = CookieManager.getInstance().getCookie(audioURI);
                                    request.addRequestHeader("cookie", cookie);
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

                                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                    downloadManager.enqueue(request);

                                    Toast.makeText(ChatActivity.this, "Download Mp3 Success", Toast.LENGTH_SHORT).show();


                                     */


                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(audioURI));
                                    String title = URLUtil.guessFileName(audioURI, null, null);

                                    request.setTitle(title);
                                    request.setDescription("Downloading...");
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "BEST_MATCH");

                                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                    manager.enqueue(request);
                                }
                            });

                            holder.ivSecondtDel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String PathText = getRef(position).getKey().toString();

                                    smsRef.child(OtherUserID).child(mUser.getUid()).child("Chat").child(PathText).removeValue();
                                    smsRef.child(mUser.getUid()).child(OtherUserID).child("Chat").child(PathText).removeValue();
                                }
                            });


                            //ShowPdf
                        } else if (!model.getDoc().isEmpty()) {
                            holder.SecondTextUser.setVisibility(View.GONE);
                            holder.FirstTextUser.setVisibility(View.GONE);

                            holder.secondImageSend.setVisibility(View.GONE);
                            holder.firstImageSend.setVisibility(View.GONE);

                            holder.secondVideoViewChat.setVisibility(View.GONE);
                            holder.firstVideoViewChat.setVisibility(View.GONE);

                            holder.ivSecondMp3.setVisibility(View.GONE);
                            holder.ivFirstMp3.setVisibility(View.GONE);

                            holder.ivSecondPdf.setVisibility(View.VISIBLE);
                            holder.ivFirstPdf.setVisibility(View.GONE);

                            holder.secondSticker.setVisibility(View.GONE);
                            holder.firstSticker.setVisibility(View.GONE);
                            holder.btViewIcon.setVisibility(View.GONE);


                            holder.ivSecondPdf.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if (holder.layoutSecondButton.isShown()) {
                                        holder.layoutSecondButton.setVisibility(View.GONE);
                                    } else {
                                        holder.layoutSecondButton.setVisibility(View.VISIBLE);
                                    }

                                    return true;
                                }
                            } );


                            holder.ivSecondtSave.setVisibility(View.VISIBLE);

                            holder.ivSecondtSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String pdfURL = model.getDoc().toString();


                                    /*
                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfURL));
                                    String title = URLUtil.guessFileName(pdfURL, null, null);
                                    request.setTitle(title);
                                    request.setDescription("Download File");
                                    String cookie = CookieManager.getInstance().getCookie(pdfURL);
                                    request.addRequestHeader("cookie", cookie);
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

                                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                    downloadManager.enqueue(request);

                                    Toast.makeText(ChatActivity.this, "Download PDF Success", Toast.LENGTH_SHORT).show();

                                     */



                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfURL));
                                    String title = URLUtil.guessFileName(pdfURL, null, null);

                                    request.setTitle(title);
                                    request.setDescription("Downloading...");
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "BEST_MATCH");

                                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                    manager.enqueue(request);
                                }
                            });

                            holder.ivSecondtDel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String PathText = getRef(position).getKey().toString();

                                    smsRef.child(OtherUserID).child(mUser.getUid()).child("Chat").child(PathText).removeValue();
                                    smsRef.child(mUser.getUid()).child(OtherUserID).child("Chat").child(PathText).removeValue();
                                }
                            });


                        } else if (!model.getSticker().isEmpty()) {
                            holder.SecondTextUser.setVisibility(View.GONE);
                            holder.FirstTextUser.setVisibility(View.GONE);

                            holder.secondImageSend.setVisibility(View.GONE);
                            holder.firstImageSend.setVisibility(View.GONE);

                            holder.secondVideoViewChat.setVisibility(View.GONE);
                            holder.firstVideoViewChat.setVisibility(View.GONE);

                            holder.ivSecondMp3.setVisibility(View.GONE);
                            holder.ivFirstMp3.setVisibility(View.GONE);

                            holder.ivSecondPdf.setVisibility(View.GONE);
                            holder.ivFirstPdf.setVisibility(View.GONE);

                            holder.secondSticker.setVisibility(View.VISIBLE);
                            holder.firstSticker.setVisibility(View.GONE);
                            holder.btViewIcon.setVisibility(View.GONE);




                            Picasso.get().load(model.getSticker()).into(holder.secondSticker);

                            holder.secondSticker.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {

                                    if (holder.layoutSecondButton.isShown()) {
                                        holder.layoutSecondButton.setVisibility(View.GONE);
                                    } else {
                                        holder.layoutSecondButton.setVisibility(View.VISIBLE);
                                    }
                                    return true;
                                }
                            } );

                            holder.ivSecondtSave.setVisibility(View.GONE);

                            holder.ivSecondtDel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String PathText = getRef(position).getKey().toString();

                                    smsRef.child(OtherUserID).child(mUser.getUid()).child("Chat").child(PathText).removeValue();
                                    smsRef.child(mUser.getUid()).child(OtherUserID).child("Chat").child(PathText).removeValue();
                                }
                            });


                        }
                         else {
                            Toast.makeText(ChatActivity.this, "Load File Fail", Toast.LENGTH_SHORT).show();
                        }


                        Picasso.get().load(myProfileImage).into(holder.SecondUserImage);


                    } else {

                        holder.FirstUserImage.setVisibility(View.VISIBLE);

                        holder.SecondUserImage.setVisibility(View.GONE);


                        int ScollingNum = recyclerView.getAdapter().getItemCount() - 1;

                        recyclerView.smoothScrollToPosition(ScollingNum);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                        recyclerView.stopScroll();
                                        btScollToDown4.setVisibility(View.VISIBLE);
                                        super.onScrolled(recyclerView, dx, dy);
                                    }
                                });
                            }
                        }, 1000);


                        btScollToDown4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                recyclerView.scrollToPosition(ScollingNum);
                                btScollToDown4.setVisibility(View.GONE);

                            }
                        });


                        // show Text
                        if (!model.getSms().isEmpty()) {
                            holder.SecondTextUser.setVisibility(View.GONE);
                            holder.FirstTextUser.setVisibility(View.VISIBLE);

                            holder.secondImageSend.setVisibility(View.GONE);
                            holder.firstImageSend.setVisibility(View.GONE);

                            holder.secondVideoViewChat.setVisibility(View.GONE);
                            holder.firstVideoViewChat.setVisibility(View.GONE);

                            holder.ivSecondMp3.setVisibility(View.GONE);
                            holder.ivFirstMp3.setVisibility(View.GONE);

                            holder.ivSecondPdf.setVisibility(View.GONE);
                            holder.ivFirstPdf.setVisibility(View.GONE);

                            holder.secondSticker.setVisibility(View.GONE);
                            holder.firstSticker.setVisibility(View.GONE);


                            holder.FirstTextUser.setText(model.getSms());


                            smsRef.child(mUser.getUid()).child(OtherUserID).child("Chat").child(getRef(position).getKey().toString()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if(snapshot.hasChild("order_id")){
                                        if(!model.getOrder_id().isEmpty()){
                                            holder.btViewIcon.setVisibility(View.VISIBLE);

                                            ivSendVideo.setVisibility(View.GONE);
                                            ivSendSound.setVisibility(View.GONE);
                                            ivSendDoc.setVisibility(View.GONE);
                                            ivSticker.setVisibility(View.GONE);

                                        }else {
                                            holder.btViewIcon.setVisibility(View.GONE);
                                        }






                                        holder.btViewIcon.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String OrderId = model.getOrder_id();

                                                dialogBuilder = new AlertDialog.Builder(ChatActivity.this);
                                                final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_show_detail_payment_sticker,null);


                                                tvOrderId = contarctPopView.findViewById(R.id.tvOrderId);
                                                tvPriceOrder = contarctPopView.findViewById(R.id.tvPriceOrder);
                                                recyclerViewSticker = contarctPopView.findViewById(R.id.recyclerViewSticker);
                                                tvSlipPayment = contarctPopView.findViewById(R.id.tvSlipPayment);
                                                btClosedilog = contarctPopView.findViewById(R.id.btClosedilog);

                                                btClosedilog.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                tvOrderId.setText(OrderId);

                                                recyclerViewSticker.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

                                                ArrayList<Integer> lisIntPrice = new ArrayList<>() ;

                                                Query query = mOrder.child(mUser.getUid()).child(OrderId).child("StickerId");
                                                optionsStickerBK = new FirebaseRecyclerOptions.Builder<StickerBasket>().setQuery(query,StickerBasket.class).build();
                                                adapterStickerBK = new FirebaseRecyclerAdapter<StickerBasket, StikcerBasketListViewHolder>(optionsStickerBK) {

                                                    @Override
                                                    protected void onBindViewHolder(@NonNull StikcerBasketListViewHolder holder, int position, @NonNull StickerBasket model) {

                                                        mStickerStoreRef.child(model.getStickerId()).addValueEventListener(new ValueEventListener() {

                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if(snapshot.exists()){
                                                                    Toast.makeText(ChatActivity.this, model.getStickerId(), Toast.LENGTH_SHORT).show();

                                                                    String NameSticker = snapshot.child("NameSticker").getValue().toString();
                                                                    String ImageIcon = snapshot.child("ImageIcon").getValue().toString();
                                                                    String StickerPrice = snapshot.child("StickerPrice").getValue().toString();

                            /*
                            String Check = model.getStikcerStatusCheck().toString();
                            String Id = model.getStickerId().toString();
                             */

                                                                    lisIntPrice.add(Integer.valueOf(StickerPrice));

                                                                    Picasso.get().load(ImageIcon).into(holder.ciIvStikcerIcon);
                                                                    holder.tvInfoSticker.setText(NameSticker);
                                                                    holder.tvPrice.setText(StickerPrice+" ฿");
                                                                    holder.rbSlectStikcer.setVisibility(View.GONE);



                                                                    int sum = 0;
                                                                    for(int i : lisIntPrice) {
                                                                        sum += i;
                                                                    }

                                                                    String sumText = String.valueOf(sum);

                                                                    tvPriceOrder.setText("" + "total amount"+sumText +" ฿");

                                                                }

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                                Toast.makeText(ChatActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                    }

                                                    @NonNull
                                                    @Override
                                                    public StikcerBasketListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_basket_sticker,parent,false);

                                                        return new StikcerBasketListViewHolder(view);
                                                    }
                                                };

                                                adapterStickerBK.startListening();
                                                recyclerViewSticker.setAdapter(adapterStickerBK);



                                                mOrder.child(mUser.getUid()).child(OrderId).child("SendPayment").child("ImagePayment").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()){
                                                            String Image = snapshot.getValue().toString();

                                                            Picasso.get().load(Image).into(tvSlipPayment);
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                                dialogBuilder.setView(contarctPopView);
                                                dialog = dialogBuilder.create();
                                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                                dialog.show();

                                            }
                                        });
                                    }else {
                                        holder.btViewIcon.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(ChatActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });






                            //show picture
                        } else if (!model.getPicture().isEmpty()) {
                            holder.SecondTextUser.setVisibility(View.GONE);
                            holder.FirstTextUser.setVisibility(View.GONE);

                            holder.secondImageSend.setVisibility(View.GONE);
                            holder.firstImageSend.setVisibility(View.VISIBLE);

                            holder.secondVideoViewChat.setVisibility(View.GONE);
                            holder.firstVideoViewChat.setVisibility(View.GONE);

                            holder.ivSecondMp3.setVisibility(View.GONE);
                            holder.ivFirstMp3.setVisibility(View.GONE);

                            holder.ivSecondPdf.setVisibility(View.GONE);
                            holder.ivFirstPdf.setVisibility(View.GONE);

                            holder.secondSticker.setVisibility(View.GONE);
                            holder.firstSticker.setVisibility(View.GONE);
                            holder.btViewIcon.setVisibility(View.GONE);


                            Picasso.get().load(model.getPicture()).into(holder.firstImageSend);

                            holder.firstImageSend.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if (holder.layoutFirstButton.isShown()) {
                                        holder.layoutFirstButton.setVisibility(View.GONE);
                                    } else {
                                        holder.layoutFirstButton.setVisibility(View.VISIBLE);
                                    }
                                    return true;
                                }
                            });

                            holder.ivFirstSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {



                                    String ImageURI = model.getPicture().toString();

                                    /*

                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(ImageURI));
                                    String title = URLUtil.guessFileName(ImageURI, null, null);


                                    request.setTitle(title);
                                    request.setDescription("Download File");
                                    String cookie = CookieManager.getInstance().getCookie(ImageURI);
                                    request.addRequestHeader("cookie", cookie);
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

                                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                    downloadManager.enqueue(request);

                                    Toast.makeText(ChatActivity.this, "Download Image Success", Toast.LENGTH_SHORT).show();

                                     */


                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(ImageURI));
                                    String title = URLUtil.guessFileName(ImageURI.toString(), null, null);

                                    request.setTitle(title);
                                    request.setDescription("Downloading...");
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "BEST_MATCH.png");

                                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                    manager.enqueue(request);
                                }
                            });



                            //Show Video
                        } else if (!model.getVideo().isEmpty()) {
                            holder.SecondTextUser.setVisibility(View.GONE);
                            holder.FirstTextUser.setVisibility(View.GONE);

                            holder.secondImageSend.setVisibility(View.GONE);
                            holder.firstImageSend.setVisibility(View.GONE);

                            holder.secondVideoViewChat.setVisibility(View.GONE);
                            holder.firstVideoViewChat.setVisibility(View.VISIBLE);

                            holder.ivSecondMp3.setVisibility(View.GONE);
                            holder.ivFirstMp3.setVisibility(View.GONE);

                            holder.ivSecondPdf.setVisibility(View.GONE);
                            holder.ivFirstPdf.setVisibility(View.GONE);

                            holder.secondSticker.setVisibility(View.GONE);
                            holder.firstSticker.setVisibility(View.GONE);
                            holder.btViewIcon.setVisibility(View.GONE);


                            MediaController mediaController = new MediaController(ChatActivity.this);
                            holder.firstVideoViewChat.setVideoPath(model.getVideo());

                            mediaController.setAnchorView(holder.firstVideoViewChat);
                            holder.firstVideoViewChat.setMediaController(mediaController);
                            holder.firstVideoViewChat.start();

                            holder.firstVideoViewChat.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if (holder.layoutFirstButton.isShown()) {
                                        holder.layoutFirstButton.setVisibility(View.GONE);
                                    } else {
                                        holder.layoutFirstButton.setVisibility(View.VISIBLE);
                                    }
                                    return true;
                                }
                            });

                            holder.ivFirstSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String VdoURI = model.getVideo().toString();

                                    /*

                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(VdoURI));
                                    String title = URLUtil.guessFileName(VdoURI, null, null);
                                    request.setTitle(title);
                                    request.setDescription("Download File");
                                    String cookie = CookieManager.getInstance().getCookie(VdoURI);
                                    request.addRequestHeader("cookie", cookie);
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

                                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                    downloadManager.enqueue(request);

                                    Toast.makeText(ChatActivity.this, "Download Video Success", Toast.LENGTH_SHORT).show();


                                     */



                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(VdoURI));
                                    String title = URLUtil.guessFileName(VdoURI, null, null);

                                    request.setTitle(title);
                                    request.setDescription("Downloading...");
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "BEST_MATCH");

                                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                    manager.enqueue(request);
                                }
                            });




                            //ShowMp3
                        } else if (!model.getAudio().isEmpty()) {
                            holder.SecondTextUser.setVisibility(View.GONE);
                            holder.FirstTextUser.setVisibility(View.GONE);

                            holder.secondImageSend.setVisibility(View.GONE);
                            holder.firstImageSend.setVisibility(View.GONE);

                            holder.secondVideoViewChat.setVisibility(View.GONE);
                            holder.firstVideoViewChat.setVisibility(View.GONE);

                            holder.ivSecondMp3.setVisibility(View.GONE);
                            holder.ivFirstMp3.setVisibility(View.VISIBLE);

                            holder.ivSecondPdf.setVisibility(View.GONE);
                            holder.ivFirstPdf.setVisibility(View.GONE);

                            holder.secondSticker.setVisibility(View.GONE);
                            holder.firstSticker.setVisibility(View.GONE);
                            holder.btViewIcon.setVisibility(View.GONE);


                            holder.ivFirstMp3.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {

                                    if (holder.layoutFirstButton.isShown()) {
                                        holder.layoutFirstButton.setVisibility(View.GONE);
                                    } else {
                                        holder.layoutFirstButton.setVisibility(View.VISIBLE);
                                    }

                                    return true;
                                }
                            });

                            holder.ivFirstSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String audioURI = model.getAudio().toString();

                                    /*

                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(audioURI));
                                    String title = URLUtil.guessFileName(audioURI, null, null);
                                    request.setTitle(title);
                                    request.setDescription("Download File");
                                    String cookie = CookieManager.getInstance().getCookie(audioURI);
                                    request.addRequestHeader("cookie", cookie);
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

                                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                    downloadManager.enqueue(request);

                                    Toast.makeText(ChatActivity.this, "Download Mp3 Success", Toast.LENGTH_SHORT).show();

                                     */

                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(audioURI));
                                    String title = URLUtil.guessFileName(audioURI, null, null);

                                    request.setTitle(title);
                                    request.setDescription("Downloading...");
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "BEST_MATCH");

                                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                    manager.enqueue(request);

                                }
                            });




                            //ShowPdf
                        } else if (!model.getDoc().isEmpty()) {
                            holder.SecondTextUser.setVisibility(View.GONE);
                            holder.FirstTextUser.setVisibility(View.GONE);

                            holder.secondImageSend.setVisibility(View.GONE);
                            holder.firstImageSend.setVisibility(View.GONE);

                            holder.secondVideoViewChat.setVisibility(View.GONE);
                            holder.firstVideoViewChat.setVisibility(View.GONE);

                            holder.ivSecondMp3.setVisibility(View.GONE);
                            holder.ivFirstMp3.setVisibility(View.GONE);

                            holder.ivSecondPdf.setVisibility(View.GONE);
                            holder.ivFirstPdf.setVisibility(View.VISIBLE);

                            holder.secondSticker.setVisibility(View.GONE);
                            holder.firstSticker.setVisibility(View.GONE);
                            holder.btViewIcon.setVisibility(View.GONE);


                            holder.ivFirstPdf.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if (holder.layoutFirstButton.isShown()) {
                                        holder.layoutFirstButton.setVisibility(View.GONE);
                                    } else {
                                        holder.layoutFirstButton.setVisibility(View.VISIBLE);
                                    }
                                    return true;
                                }
                            });

                            holder.ivFirstSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String pdfURL = model.getDoc().toString();

                                    /*

                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfURL));
                                    String title = URLUtil.guessFileName(pdfURL, null, null);
                                    request.setTitle(title);
                                    request.setDescription("Download File");
                                    String cookie = CookieManager.getInstance().getCookie(pdfURL);
                                    request.addRequestHeader("cookie", cookie);
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

                                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                    downloadManager.enqueue(request);

                                    Toast.makeText(ChatActivity.this, "Download PDF Success", Toast.LENGTH_SHORT).show();

                                     */


                                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pdfURL));
                                    String title = URLUtil.guessFileName(pdfURL, null, null);

                                    request.setTitle(title);
                                    request.setDescription("Downloading...");
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "BEST_MATCH");

                                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                    manager.enqueue(request);
                                }
                            });




                        } else if (!model.getSticker().isEmpty()) {
                            holder.SecondTextUser.setVisibility(View.GONE);
                            holder.FirstTextUser.setVisibility(View.GONE);

                            holder.secondImageSend.setVisibility(View.GONE);
                            holder.firstImageSend.setVisibility(View.GONE);

                            holder.secondVideoViewChat.setVisibility(View.GONE);
                            holder.firstVideoViewChat.setVisibility(View.GONE);

                            holder.ivSecondMp3.setVisibility(View.GONE);
                            holder.ivFirstMp3.setVisibility(View.GONE);

                            holder.ivSecondPdf.setVisibility(View.GONE);
                            holder.ivFirstPdf.setVisibility(View.GONE);

                            holder.secondSticker.setVisibility(View.GONE);
                            holder.firstSticker.setVisibility(View.VISIBLE);
                            holder.btViewIcon.setVisibility(View.GONE);


                            Picasso.get().load(model.getSticker()).into(holder.firstSticker);

                            holder.secondSticker.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if (holder.layoutFirstButton.isShown()) {
                                        holder.layoutFirstButton.setVisibility(View.GONE);
                                    } else {
                                        holder.layoutFirstButton.setVisibility(View.VISIBLE);
                                    }
                                    return true;
                                }
                            } );

                            holder.ivSecondtDel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String PathText = getRef(position).getKey().toString();

                                    smsRef.child(OtherUserID).child(mUser.getUid()).child(PathText).removeValue();
                                    smsRef.child(mUser.getUid()).child(OtherUserID).child(PathText).removeValue();
                                }
                            });


                        }
                        else {
                            Toast.makeText(ChatActivity.this, "Load File Fail", Toast.LENGTH_SHORT).show();
                        }

                            Picasso.get().load(OtherUserProfileImage).into(holder.FirstUserImage);


                    }
                }

            }

            @NonNull
            @Override
            public ChatMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_sms,parent,false);

                return new ChatMyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private void SendChat() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss:SSS");
        String strDate = formatter.format(date);

        String sms = etTextSend.getText().toString();
        if(sms.isEmpty()){
            Toast.makeText(this, "Please Write Something", Toast.LENGTH_SHORT).show();
        }else {
            HashMap hashMap = new HashMap();
            hashMap.put("sms",sms);
            hashMap.put("status","unseen");
            hashMap.put("userID",mUser.getUid());
            hashMap.put("picture","");
            hashMap.put("video","");
            hashMap.put("audio","");
            hashMap.put("doc","");
            hashMap.put("sticker","");



            smsRef.child(OtherUserID).child(mUser.getUid()).child("Chat").child(strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    smsRef.child(mUser.getUid()).child(OtherUserID).child("Chat").child(strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                mFriendRef.child(OtherUserID).child(mUser.getUid()).child("Notification").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            boolean NotificationStatus = (boolean) snapshot.getValue();

                                            if(NotificationStatus){

                                                sendNotification("Message from ",sms);
                                            }else {
                                                return;
                                            }

                                        }else {

                                           // sendNotification("Message from ",sms);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(ChatActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                UpdataDataTimeNewest(strDate);

                                etTextSend.setText(null);
                                Toast.makeText(ChatActivity.this, "Sms Sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });



        }
    }

    private void UpdataDataTimeNewest(String strDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy H:mm:ss:SSS");
        Date date = null;
        try {
            date = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long unixTimestamp = date.getTime(); //

        HashMap hashMap2 = new HashMap();
        hashMap2.put("ChatTimeLate",unixTimestamp);

        smsRef.child(OtherUserID).child(mUser.getUid()).updateChildren(hashMap2).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        smsRef.child(mUser.getUid()).child(OtherUserID).updateChildren(hashMap2).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }

    private void sendNotification(String Head, String sms) {

        String URL = "https://fcm.googleapis.com/fcm/send";
        RequestQueue requestQueue;

        requestQueue = Volley.newRequestQueue(this);


            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("to", "/topics/" + OtherUserID);

                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("title", Head + username);
                jsonObject1.put("body", sms);

                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("userID", mUser.getUid());
                jsonObject2.put("type", "sms");

                jsonObject.put("notification", jsonObject1);
                jsonObject.put("data", jsonObject2);

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                        Map<String, String> map = new HashMap<>();
                        map.put("content-type", "application/json");
                        map.put("authorization", "key=AAAAXuPuYeA:APA91bEb3pgPsTIR9bi-YmL6sG4wXWKrXfmm1mZ7BQLcko3Ty-DDLucsNXAtmIqeA7Euy2r8vh7t4X3WBpSabGs9tATBCMNTAZpCS6uVEJXCsTHggQlxEbLFNhIvflDwvvVBOvb_nB9z");
                        return map;
                    }
                };

                requestQueue.add(request);

            } catch (JSONException e) {
                e.printStackTrace();
            }

    }

    private void LoadOtherUser() {


        mUserRef.child(OtherUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    if(!snapshot.child("username").exists()){
                        tvUsername.setText("Admin");
                    }else {
                        OtherUsername = snapshot.child("username").getValue().toString();
                        tvUsername.setText(OtherUsername);
                    }
                        if(!snapshot.child("profileImage").exists()){
                            ciProfile.setImageResource(R.drawable.ic_logo_2);

                        }else {
                            OtherUserProfileImage = snapshot.child("profileImage").getValue().toString();
                            Picasso.get().load(OtherUserProfileImage).into(ciProfile);
                        }

                            if(!snapshot.child("status").exists()){
                                tvStatus.setVisibility(View.GONE);
                            }else {
                                OtherUserStatus = snapshot.child("status").getValue().toString();
                                tvStatus.setText(OtherUserStatus);
                            }






                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null){
            imageUri=data.getData();

            Uri fileUri = data.getData();
            long fileSizeInBytes = -1;

            try {
                ContentResolver contentResolver = getContentResolver();
                InputStream inputStream = contentResolver.openInputStream(fileUri);
                if (inputStream != null) {
                    fileSizeInBytes = inputStream.available();
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            long fileSizeInMB = fileSizeInBytes / (1024 * 1024);

            if (fileSizeInMB > 10) {
                StyleableToast.makeText(ChatActivity.this, getString(R.string.WarnTitleFile), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
            } else {
                SendPicture();
            }





        }

        if(requestCode == REQUEST_CODE_VIDEO && resultCode == RESULT_OK && data != null){
            videoUri=data.getData();

            Uri fileUri = data.getData();
            long fileSizeInBytes = -1;

            try {
                ContentResolver contentResolver = getContentResolver();
                InputStream inputStream = contentResolver.openInputStream(fileUri);
                if (inputStream != null) {
                    fileSizeInBytes = inputStream.available();
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            long fileSizeInMB = fileSizeInBytes / (1024 * 1024);

            if (fileSizeInMB > 10) {
                StyleableToast.makeText(ChatActivity.this, getString(R.string.WarnTitleFile), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
            } else {
                SendVideoChat();
            }



        }

        if(requestCode == REQUEST_CODE_AUDIO && resultCode == RESULT_OK && data != null){
            audioUri=data.getData();
            Uri fileUri = data.getData();
            long fileSizeInBytes = -1;

            try {
                ContentResolver contentResolver = getContentResolver();
                InputStream inputStream = contentResolver.openInputStream(fileUri);
                if (inputStream != null) {
                    fileSizeInBytes = inputStream.available();
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            long fileSizeInMB = fileSizeInBytes / (1024 * 1024);

            if (fileSizeInMB > 10) {
                StyleableToast.makeText(ChatActivity.this, getString(R.string.WarnTitleFile), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
            } else {
                SendAudio();
            }


            

        }

        if(requestCode == REQUEST_CODE_DOC && resultCode == RESULT_OK && data != null){
            docUri=data.getData();

            Uri fileUri = data.getData();
            long fileSizeInBytes = -1;

            try {
                ContentResolver contentResolver = getContentResolver();
                InputStream inputStream = contentResolver.openInputStream(fileUri);
                if (inputStream != null) {
                    fileSizeInBytes = inputStream.available();
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            long fileSizeInMB = fileSizeInBytes / (1024 * 1024);

            if (fileSizeInMB > 10) {
                StyleableToast.makeText(ChatActivity.this, getString(R.string.WarnTitleFile), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
            } else {
                SendDoc();
            }




        }
    }


}