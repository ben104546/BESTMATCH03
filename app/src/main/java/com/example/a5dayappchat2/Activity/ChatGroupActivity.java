package com.example.a5dayappchat2.Activity;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.a5dayappchat2.Activity.Preferences.PreferencesGroup;
import com.example.a5dayappchat2.Activity.Preferences.PreferencesOtherId;
import com.example.a5dayappchat2.Activity.Preferences.PreferencesUser;
import com.example.a5dayappchat2.Activity.Setting.SettingGroupNew;
import com.example.a5dayappchat2.Activity.Utills.ChatGroup;
import com.example.a5dayappchat2.Activity.Utills.StickerChatMenu;
import com.example.a5dayappchat2.Activity.constant.Constant;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.muddz.styleabletoast.StyleableToast;

public class ChatGroupActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 101;
    private static final int REQUEST_CODE_VIDEO = 102;
    private static final int REQUEST_CODE_AUDIO = 103;
    private static final int REQUEST_CODE_DOC = 104;

    PreferencesUser preferencesUser;
    PreferencesGroup preferencesGroup;
    PreferencesOtherId preferencesOtherId;

    DatabaseReference mGroupRef;
    DatabaseReference mUserRef;
    DatabaseReference mStickerStoreRef;
    DatabaseReference mMySticker;


    RecyclerView recyclerView;
    ImageView sendImage,sendText,setting;
    EditText inputTextSend;
    CircleImageView profileGroup;
    TextView groupName;

    Uri imageUri;
    Uri videoUri;
    Uri audioUri;
    Uri docUri;

    ImageView ivNotify,ivNotifyOff;

    View vBackgroundgroup;
    ImageView ivBackgroundgroup;

    ImageButton btScollToDown;

    String groupToken,userId;

    ImageView ivUpload,ivSendPicture,ivSendVideo,ivSendSound,ivSendDoc,ivSendActivityGroup,ivSticker;

    String UserProfile, UserName;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    ConstraintLayout constraintLayout2,constraintLayout3,constraintLayoutSticker;

    FirebaseRecyclerOptions<ChatGroup> options;
    FirebaseRecyclerAdapter<ChatGroup, ChatGroupMyViewHolder> adapter;


    StorageReference storageGroupChatImageRef,storageGroupChatVDORef,storageGroupChatAudioRef,storageGroupChatDocRef;

    RecyclerView recyclerViewStickerList;
    RecyclerView recyclerViewStickerListButton;

    FirebaseRecyclerOptions<StickerChatMenu>optionsStickerHead;
    FirebaseRecyclerAdapter<StickerChatMenu,StickerAllListViewHolder>adapterStickerHead;

    FirebaseRecyclerOptions<StickerChatMenu>optionsStickerBody;
    FirebaseRecyclerAdapter<StickerChatMenu,StickerAllListViewHolder>adapterStickerBody;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group);

        preferencesGroup = new PreferencesGroup(getApplicationContext());
        preferencesUser = new PreferencesUser(getApplicationContext());
        preferencesOtherId = new PreferencesOtherId(getApplicationContext());

        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        groupToken = getIntent().getStringExtra("groupKeyToChat");


        userId = preferencesUser.getString(Constant.USER_ID);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        recyclerView = findViewById(R.id.recycleViewGroup);
        sendImage = findViewById(R.id.ivUpload);
        sendText = findViewById(R.id.ivSendText);
        inputTextSend = findViewById(R.id.etTextSend);
        setting = findViewById(R.id.ivSetting);
        profileGroup = findViewById(R.id.ciSettingProfileGroup);
        groupName = findViewById(R.id.tvGroupName);
        ivUpload = findViewById(R.id.ivUpload);
        ivBackgroundgroup = findViewById(R.id.ivBackgroundgroup);


        recyclerViewStickerList = findViewById(R.id.recyclerViewStickerList);
        recyclerViewStickerListButton = findViewById(R.id.recyclerViewStickerListButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStickerList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerViewStickerListButton.setLayoutManager(layoutManager);

        vBackgroundgroup = findViewById(R.id.vBackgroundgroup);


        constraintLayout2 = findViewById(R.id.layout2);
        constraintLayout3 = findViewById(R.id.layout3);

        ivSendPicture = findViewById(R.id.ivSendPicture);
        ivSendVideo= findViewById(R.id.ivSendVideo);
        ivSendSound= findViewById(R.id.ivSendSound);
        ivSendDoc= findViewById(R.id.ivSendDoc);
        ivSticker = findViewById(R.id.ivSticker);

        mStickerStoreRef = FirebaseDatabase.getInstance().getReference().child("Sticker");

        constraintLayoutSticker = findViewById(R.id.constraintLayoutSticker);

        btScollToDown= findViewById(R.id.btScollToDown);


        ivNotify = findViewById(R.id.ivNotify);
        ivNotifyOff = findViewById(R.id.ivNotifyOff);


        storageGroupChatImageRef = FirebaseStorage.getInstance().getReference().child("ChatGroupImage");
        storageGroupChatVDORef = FirebaseStorage.getInstance().getReference().child("ChatGroupVDO");
        storageGroupChatAudioRef = FirebaseStorage.getInstance().getReference().child("ChatGroupAudio");
        storageGroupChatDocRef = FirebaseStorage.getInstance().getReference().child("ChatGroupDoc");

        mMySticker = FirebaseDatabase.getInstance().getReference().child("MySticker");


        ivSendPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_IMAGE);

                Toast.makeText(ChatGroupActivity.this, "IMAGE", Toast.LENGTH_SHORT).show();


            }
        });

        ivSendVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent,REQUEST_CODE_VIDEO);

                Toast.makeText(ChatGroupActivity.this, "VIDEO", Toast.LENGTH_SHORT).show();


            }
        });

        ivSendSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(intent,REQUEST_CODE_AUDIO);

                Toast.makeText(ChatGroupActivity.this, "AUDIO", Toast.LENGTH_SHORT).show();


            }
        });

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




        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        LoadProfileGroup();
        LoadChat();
        LoadProfile();

        LoadStickerMyKey();


        ivUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowButton();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatGroupActivity.this, SettingGroupNew.class);
                intent.putExtra("groupKeyForSetting",groupToken);
                startActivity(intent);
            }
        });


        sendText.setOnClickListener(new View.OnClickListener() {
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
                hashMap.put("Notification","Off");



                mGroupRef.child(groupToken).child("Member").child(mUser.getUid()).updateChildren(hashMap);
            }
        });

        ivNotifyOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivNotifyOff.setVisibility(View.GONE);
                ivNotify.setVisibility(View.VISIBLE);


                HashMap hashMap = new HashMap();
                hashMap.put("Notification","On");


                mGroupRef.child(groupToken).child("Member").child(mUser.getUid()).updateChildren(hashMap);
            }
        });


        ShowAndOffButtonNotification();

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
                Toast.makeText(ChatGroupActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowAndOffButtonNotification() {

        mGroupRef.child(groupToken).child("Member").child(mUser.getUid()).child("Notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String NotificationStatus = snapshot.getValue().toString();

                    if(NotificationStatus.equals("On")){
                        ivNotifyOff.setVisibility(View.GONE);
                        ivNotify.setVisibility(View.VISIBLE);
                    }else if(NotificationStatus.equals("Off")){

                        ivNotify.setVisibility(View.GONE);
                        ivNotifyOff.setVisibility(View.VISIBLE);
                    }else {
                    }
                }else {
                    ivNotify.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatGroupActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    recyclerViewStickerList.setBackgroundColor(Color.WHITE);
                }else {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }



                boolean methodLoaded = false;

                if (!methodLoaded) {
                    String KeySticker = getRef(position).getKey().toString();
                    LoadStickerAllList(KeySticker);
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
                recyclerViewStickerListButton.setBackgroundColor(Color.WHITE);

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
        hashMap.put("User_Id",mUser.getUid());
        hashMap.put("picture","");
        hashMap.put("status","unseen");
        hashMap.put("sms","");
        hashMap.put("video","");
        hashMap.put("audio","");
        hashMap.put("doc","");

        mGroupRef.child(groupToken).child("Messenger").push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {



                GetDat("Sticker from ","");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatGroupActivity.this, ""+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
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

    private void SendDocChat() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss:SSS");
        String strDate = formatter.format(date);

        storageGroupChatDocRef.child(groupToken+strDate).putFile(docUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    storageGroupChatDocRef.child(groupToken+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            HashMap hashMap = new HashMap();
                            hashMap.put("User_Id",mUser.getUid());
                            hashMap.put("Time",strDate);
                            hashMap.put("Sms","");
                            hashMap.put("picture","");
                            hashMap.put("video","");
                            hashMap.put("audio","");
                            hashMap.put("doc",uri.toString());
                            hashMap.put("sticker","");

                            mGroupRef.child(groupToken).child("Messenger").push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    Toast.makeText(ChatGroupActivity.this, "Send Doc Success", Toast.LENGTH_SHORT).show();
                                    GetDat("Document  from ","");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChatGroupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatGroupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(ChatGroupActivity.this, "Upload Doc Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatGroupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void SendSoundChat() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss:SSS");
        String strDate = formatter.format(date);

        storageGroupChatAudioRef.child(groupToken+strDate).putFile(audioUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    storageGroupChatAudioRef.child(groupToken+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            HashMap hashMap = new HashMap();
                            hashMap.put("User_Id",mUser.getUid());
                            hashMap.put("Time",strDate);
                            hashMap.put("Sms","");
                            hashMap.put("picture","");
                            hashMap.put("video","");
                            hashMap.put("audio",uri.toString());
                            hashMap.put("doc","");
                            hashMap.put("sticker","");

                            mGroupRef.child(groupToken).child("Messenger").push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    Toast.makeText(ChatGroupActivity.this, "Send Audio Success", Toast.LENGTH_SHORT).show();
                                    GetDat("Audio  from ","");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChatGroupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatGroupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(ChatGroupActivity.this, "Upload Audio Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatGroupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SendVideoChat() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss:SSS");
        String strDate = formatter.format(date);

        storageGroupChatVDORef.child(groupToken+strDate).putFile(videoUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    storageGroupChatVDORef.child(groupToken+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            HashMap hashMap = new HashMap();
                            hashMap.put("User_Id",mUser.getUid());
                            hashMap.put("Time",strDate);
                            hashMap.put("Sms","");
                            hashMap.put("picture","");
                            hashMap.put("video",uri.toString());
                            hashMap.put("audio","");
                            hashMap.put("doc","");
                            hashMap.put("sticker","");

                            mGroupRef.child(groupToken).child("Messenger").push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    Toast.makeText(ChatGroupActivity.this, "Send Video Success", Toast.LENGTH_SHORT).show();
                                    GetDat("Video  from ","");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChatGroupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatGroupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(ChatGroupActivity.this, "Upload Video Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatGroupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void SendPictureChat() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss:SSS");
        String strDate = formatter.format(date);

        storageGroupChatImageRef.child(groupToken+strDate).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    storageGroupChatImageRef.child(groupToken+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            HashMap hashMap = new HashMap();
                            hashMap.put("User_Id",mUser.getUid());
                            hashMap.put("Time",strDate);
                            hashMap.put("Sms","");
                            hashMap.put("picture",uri.toString());
                            hashMap.put("video","");
                            hashMap.put("audio","");
                            hashMap.put("doc","");
                            hashMap.put("sticker","");

                            mGroupRef.child(groupToken).child("Messenger").push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    Toast.makeText(ChatGroupActivity.this, "Send Picture Success", Toast.LENGTH_SHORT).show();
                                    GetDat("Picture  from ","");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChatGroupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatGroupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(ChatGroupActivity.this, "Upload Picture Fail", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatGroupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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


    private void SendChat() {

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss:SSS");
        String strDate = formatter.format(date);

        String sms = inputTextSend.getText().toString();
        if(sms.isEmpty()){
            Toast.makeText(this, "Please Write Text", Toast.LENGTH_SHORT).show();
        }else {
            HashMap hashMap = new HashMap();
            hashMap.put("User_Id",mUser.getUid());
            hashMap.put("Time",strDate);
            hashMap.put("Sms",sms);
            hashMap.put("picture","");
            hashMap.put("video","");
            hashMap.put("audio","");
            hashMap.put("doc","");
            hashMap.put("sticker","");

            mGroupRef.child(groupToken).child("Messenger").push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        inputTextSend.setText(null);
                        Toast.makeText(ChatGroupActivity.this, "Send Text Success", Toast.LENGTH_SHORT).show();
                        GetDat("Picture  from ",sms);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChatGroupActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }

    }

    private void LoadChat() {
        options = new FirebaseRecyclerOptions.Builder<ChatGroup>().setQuery(mGroupRef.child(groupToken).child("Messenger"),ChatGroup.class).build();
        adapter = new FirebaseRecyclerAdapter<ChatGroup, ChatGroupMyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatGroupMyViewHolder holder, int position, @NonNull ChatGroup model) {

                preferencesOtherId.putString(Constant.OTHER_USER, getRef(position).getKey().toString());

                //ME

                if (model.getUser_Id().equals(mUser.getUid())) {
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
                                    btScollToDown.setVisibility(View.VISIBLE);
                                    super.onScrolled(recyclerView, dx, dy);
                                }
                            });
                        }
                    }, 1000);


                    btScollToDown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recyclerView.scrollToPosition(ScollingNum);
                            btScollToDown.setVisibility(View.GONE);

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
                        } );

                        holder.ivSecondtSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(ChatGroupActivity.this, "Can't Download Text", Toast.LENGTH_SHORT).show();
                            }
                        });
                        holder.ivSecondtDel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String PathText = getRef(position).getKey().toString();

                                mGroupRef.child(groupToken).child("Messenger").child(PathText).removeValue();
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
                        } );

                        holder.ivSecondtSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String ImageURI = model.getPicture().toString();

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

                                Toast.makeText(ChatGroupActivity.this, "Download Image Success", Toast.LENGTH_SHORT).show();
                            }
                        });

                        holder.ivSecondtDel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String PathText = getRef(position).getKey().toString();

                                mGroupRef.child(groupToken).child("Messenger").child(PathText).removeValue();
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


                        MediaController mediaController = new MediaController(ChatGroupActivity.this);
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
                        });

                        holder.ivSecondtSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String VdoURI = model.getVideo().toString();

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

                                Toast.makeText(ChatGroupActivity.this, "Download Video Success", Toast.LENGTH_SHORT).show();
                            }
                        });

                        holder.ivSecondtDel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String PathText = getRef(position).getKey().toString();

                                mGroupRef.child(groupToken).child("Messenger").child(PathText).removeValue();
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

                        holder.ivSecondtSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String audioURI = model.getAudio().toString();

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

                                Toast.makeText(ChatGroupActivity.this, "Download Mp3 Success", Toast.LENGTH_SHORT).show();
                            }
                        });

                        holder.ivSecondtDel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String PathText = getRef(position).getKey().toString();

                                mGroupRef.child(groupToken).child("Messenger").child(PathText).removeValue();
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

                        holder.ivSecondtSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String pdfURL = model.getDoc().toString();

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

                                Toast.makeText(ChatGroupActivity.this, "Download PDF Success", Toast.LENGTH_SHORT).show();
                            }
                        });

                        holder.ivSecondtDel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String PathText = getRef(position).getKey().toString();

                                mGroupRef.child(groupToken).child("Messenger").child(PathText).removeValue();
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
                        });

                        holder.ivSecondtDel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String PathText = getRef(position).getKey().toString();
                                mGroupRef.child(groupToken).child("Messenger").child(PathText).removeValue();
                            }
                        });


                    } else {
                        Toast.makeText(ChatGroupActivity.this, "Load File Fail", Toast.LENGTH_SHORT).show();
                    }

                    Picasso.get().load(UserProfile).into(holder.SecondUserImage);

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
                                    btScollToDown.setVisibility(View.VISIBLE);
                                    super.onScrolled(recyclerView, dx, dy);
                                }
                            });
                        }
                    }, 1000);


                    btScollToDown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recyclerView.scrollToPosition(ScollingNum);
                            btScollToDown.setVisibility(View.GONE);

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

                        holder.FirstTextUser.setOnLongClickListener(new View.OnLongClickListener() {
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

                        holder.ivFirstSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(ChatGroupActivity.this, "Can't Download Text", Toast.LENGTH_SHORT).show();
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
                        } );

                        holder.ivFirstSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(ChatGroupActivity.this, "Can't Download Image", Toast.LENGTH_SHORT).show();
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

                        MediaController mediaController = new MediaController(ChatGroupActivity.this);
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
                                Toast.makeText(ChatGroupActivity.this, "Can't Download Video", Toast.LENGTH_SHORT).show();
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

                                Toast.makeText(ChatGroupActivity.this, "Download Mp3 Success", Toast.LENGTH_SHORT).show();
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
                        } );

                        holder.ivFirstSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String pdfURL = model.getDoc().toString();

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

                                Toast.makeText(ChatGroupActivity.this, "Download PDF Success", Toast.LENGTH_SHORT).show();
                            }
                        });




                        //Show Activity
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

                                mGroupRef.child(groupToken).child("Messenger").child(PathText).removeValue();
                            }
                        });


                    } else {
                        Toast.makeText(ChatGroupActivity.this, "Load File Fail", Toast.LENGTH_SHORT).show();
                    }


                    mUserRef.child(model.getUser_Id()).child("profileImage").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String Profile = snapshot.getValue().toString();
                                Picasso.get().load(Profile).into(holder.FirstUserImage);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ChatGroupActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });




                }

            }

            @NonNull
            @Override
            public ChatGroupMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_sms,parent,false);
                return new ChatGroupMyViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }






    private void LoadProfileGroup() {
        mGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String profileGroupImage = snapshot.child(groupToken).child("GroupProfileImage").getValue().toString();
                    String  groupNameTitle = snapshot.child(groupToken).child("GroupName").getValue().toString();



                    DataSnapshot color = snapshot.child(groupToken).child("colorWallpaper");
                    DataSnapshot picture = snapshot.child(groupToken).child("groupWallpaper");

                    if(color.exists()){
                        String colorWallpaper = snapshot.child(groupToken).child("colorWallpaper").getValue().toString();
                        int colorInt = Integer.parseInt(colorWallpaper);

                        vBackgroundgroup.setBackgroundColor(colorInt);
                        ivBackgroundgroup.setVisibility(View.GONE);

                    }else if(picture.exists()){
                        String groupWallpaper = snapshot.child(groupToken).child("groupWallpaper").getValue().toString();

                        Picasso.get().load(groupWallpaper).into(ivBackgroundgroup);
                        vBackgroundgroup.setVisibility(View.GONE);

                    }else {

                        Toast.makeText(ChatGroupActivity.this, "ERROR BACKGROUND", Toast.LENGTH_SHORT).show();

                    }


                    Picasso.get().load(profileGroupImage).into(profileGroup);
                    groupName.setText(groupNameTitle);


                }else {
                    Toast.makeText(ChatGroupActivity.this, "Load Profile Group Fail", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatGroupActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }


    private void LoadProfile() {
        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    UserProfile = snapshot.child("profileImage").getValue().toString();
                    UserName = snapshot.child("username").getValue().toString();


                }else {
                    Toast.makeText(ChatGroupActivity.this, "Load Data Not Success", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatGroupActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    ArrayList<String> UserInGroupArry = new ArrayList<String>();

    private void GetDat(String Head,String sms) {
        mGroupRef.child(groupToken).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String GroupName = snapshot.child("GroupName").getValue().toString();


                    for (DataSnapshot child : snapshot.child("Member").getChildren()) {


                        String keyUser = child.getKey();

                        if(keyUser.equals(mUser.getUid())){
//
                        }else {
                            UserInGroupArry.add(keyUser);

                            for (int i = 0; i<UserInGroupArry.size();i++){
                                String UserIdAll = UserInGroupArry.get(i);

                                if(UserIdAll.equals(mUser.getUid())){
                                    //
                                }else {
                                    mGroupRef.child(groupToken).child("Member").child(UserIdAll).child("Notification").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                String NotificationStatus = snapshot.getValue().toString();

                                                if(NotificationStatus.equals("On")){
                                                    Sendnotification(Head,sms,UserIdAll,GroupName);
                                                }else if(NotificationStatus.equals("Off")){

                                                }else {
                                                }
                                            }else {
                                                Sendnotification(Head,sms,UserIdAll,GroupName);
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(ChatGroupActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }



                        }







                        }


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatGroupActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void Sendnotification(String head, String sms, String AnotheruserId, String groupName) {

        String URL = "https://fcm.googleapis.com/fcm/send";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);




        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("to","/topics/"+AnotheruserId);

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("title",groupName);
            jsonObject1.put("body",sms);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null){
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
                StyleableToast.makeText(ChatGroupActivity.this, getString(R.string.WarnTitleFile), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
            } else {
                SendPictureChat();
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
                StyleableToast.makeText(ChatGroupActivity.this, getString(R.string.WarnTitleFile), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
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
                StyleableToast.makeText(ChatGroupActivity.this, getString(R.string.WarnTitleFile), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
            } else {

                SendSoundChat();
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
                StyleableToast.makeText(ChatGroupActivity.this, getString(R.string.WarnTitleFile), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
            } else {
                SendDocChat();
            }


        }
    }
}