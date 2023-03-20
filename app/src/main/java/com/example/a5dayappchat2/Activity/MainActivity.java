package com.example.a5dayappchat2.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.a5dayappchat2.Activity.Sticker_Sotre.MenuStickerMain;
import com.example.a5dayappchat2.Activity.Utills.AdsList;
import com.example.a5dayappchat2.Activity.Utills.Comment;
import com.example.a5dayappchat2.Activity.Utills.Post;
import com.example.a5dayappchat2.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final int REQUEST_CODE = 101;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef;
    DatabaseReference postRef;
    DatabaseReference likeRef;
    DatabaseReference commentRef;
    String profileImageUrlV,userNameV;
    CircleImageView profileImageHeader;
    TextView userNameHeader;
    ImageView addImagePost,sendImagePost,ivNotification,ivChooseedImage;
    EditText inputPoseDes;
    Uri imageUri;
    ProgressDialog mLoadingBar;
    StorageReference postImageRef;
    FirebaseRecyclerAdapter<Post, MyViewHolder>adapter;
    FirebaseRecyclerOptions<Post>options;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Comment>commentOption;
    FirebaseRecyclerAdapter<Comment, CommentViewHolder>commentAdapter;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    String myLg ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        
        

        if (mUser != null) {
            String uid = mUser.getUid();
            // Do something with the UID

            FirebaseMessaging.getInstance().subscribeToTopic(mUser.getUid());

            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
            postRef = FirebaseDatabase.getInstance().getReference().child("Post");
            likeRef = FirebaseDatabase.getInstance().getReference().child("Like");
            postImageRef = FirebaseStorage.getInstance().getReference().child("PostImages");
            commentRef = FirebaseDatabase.getInstance().getReference().child("Comment");

            drawerLayout = findViewById(R.id.drawerLayout);
            navigationView = findViewById(R.id.nvgView);

            View view = navigationView.inflateHeaderView(R.layout.drawer_header);

            profileImageHeader = view.findViewById(R.id.profileImage_Header);
            userNameHeader = view.findViewById(R.id.userName_Header);

            addImagePost = findViewById(R.id.add_image_post);
            ivChooseedImage = findViewById(R.id.ivChooseedImage);
            sendImagePost = findViewById(R.id.send_post_image_view);
            inputPoseDes = findViewById(R.id.etAddPost);



            mLoadingBar = new ProgressDialog(this);

            recyclerView =  findViewById(R.id.recycleViewGroup);


            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);

            recyclerView.setLayoutManager(layoutManager);

            navigationView.setNavigationItemSelectedListener(this);

            sendImagePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddPost();
                }
            });


            addImagePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent,REQUEST_CODE);
                }
            });

            //LoadMyLg();
            LoadPost();


            GetDeviceToken();


            showAds();


        } else {
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        
        




    }


    String deviceToken;

    private void GetDeviceToken() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast

                        Log.d("ADADADADD",token);


                    }
                });




    }



/*
    private void LoadMyLg() {
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String LG = snapshot.child(mUser.getUid()).child("language").getValue().toString();
                setLocale(LG);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLocale(String lg) {
        Resources resource = getResources();
        DisplayMetrics metrics = resource.getDisplayMetrics();
        Configuration configuration = resource.getConfiguration();
        configuration.locale = new Locale(lg);
        resource.updateConfiguration(configuration,metrics);
        onConfigurationChanged(configuration);
    }

 */

ImageView AdsForOne;
    private void showAds() {
        Dialog dialog = new Dialog(this);
        final Drawable drawable = new ColorDrawable(android.graphics.Color.TRANSPARENT);
        dialog.getWindow().setBackgroundDrawable(drawable);
        dialog.setContentView(R.layout.pop_up_ads_2_0);
        dialog.show();

        ImageSlider imageSlider = dialog.findViewById(R.id.image_slider);

        RecyclerView recyclerViewAdsList = dialog.findViewById(R.id.recyclerViewAdsList);
        FloatingActionButton floatingActionButtonClosePopupAds = dialog.findViewById(R.id.floatingActionButtonClosePopupAds);
        ImageView AdsForOne = dialog.findViewById(R.id.AdsForOne);


        recyclerViewAdsList.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        DatabaseReference mAdsRef = FirebaseDatabase.getInstance().getReference().child("Ads");
        FirebaseRecyclerOptions<AdsList>options;
        FirebaseRecyclerAdapter<AdsList,FindGroupViewHolder>adapter;

        Query query = mAdsRef;
        options = new FirebaseRecyclerOptions.Builder<AdsList>().setQuery(query, AdsList.class).build();
        adapter = new FirebaseRecyclerAdapter<AdsList, FindGroupViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull FindGroupViewHolder holder, int position, @NonNull AdsList model) {


                if(model.isAdsStatus() == true){

                    Picasso.get().load(model.getAdsImage()).into(holder.circleImageProfileGroup);
                    slideModels.add(new SlideModel(model.getAdsImage().toString(),ScaleTypes.FIT));

                    imageSlider.setVisibility(View.VISIBLE);
                    AdsForOne.setVisibility(View.GONE);
                    imageSlider.setImageList(slideModels,ScaleTypes.FIT);

                    /*
                    if(slideModels.size() == 1){
                        AdsForOne.setVisibility(View.VISIBLE);
                        Picasso.get().load(model.getAdsImage().toString()).into(AdsForOne);
                        imageSlider.setVisibility(View.GONE);

                    }else {

                    }


                     */





                }else {
                    //
                }
/*
                if(slideModels.isEmpty()){

                }else {
//
                }

 */




            }

            @NonNull
            @Override
            public FindGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_group_cell, parent, false);
                return new FindGroupViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerViewAdsList.setAdapter(adapter);

        floatingActionButtonClosePopupAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



    }




    private void LoadPost() {
        options = new FirebaseRecyclerOptions.Builder<Post>().setQuery(postRef, Post.class).build();
        adapter = new FirebaseRecyclerAdapter<Post, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Post model) {
                String postKey = getRef(position).getKey();
                String timeAgo = calculateTimeAgo(model.getDatePost());
                holder.timeAgo.setText(timeAgo);
                holder.username.setText(model.getUsername());

                if(!model.getPostDesc().isEmpty()&&model.getPostImage().isEmpty() ){
                    holder.postDes.setVisibility(View.VISIBLE);
                    holder.postDes.setText(model.getPostDesc());
                    holder.postImage.setVisibility(View.GONE);

                }else if(model.getPostDesc().isEmpty()&&!model.getPostImage().isEmpty() ){
                    holder.postImage.setVisibility(View.VISIBLE);
                    holder.postDes.setVisibility(View.GONE);
                    Picasso.get().load(model.getPostImage()).into(holder.postImage);
                }else {
                    holder.postDes.setVisibility(View.VISIBLE);
                    holder.postImage.setVisibility(View.VISIBLE);
                    holder.postDes.setText(model.getPostDesc());
                    Picasso.get().load(model.getPostImage()).into(holder.postImage);
                }

                Picasso.get().load(model.getUserProfileImageUrl()).into(holder.profileImage);
                holder.countLike(postKey,mUser.getUid(),likeRef);
                holder.countComment(postKey,mUser.getUid(),commentRef);



                holder.likeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        likeRef.child(postKey).child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    likeRef.child(postKey).child(mUser.getUid()).removeValue();


                                }else {
                                    likeRef.child(postKey).child(mUser.getUid()).setValue("like");

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MainActivity.this,""+error.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });
                holder.commentSends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String comment = holder.inputComments.getText().toString();
                        if(comment.isEmpty()){
                            Toast.makeText(MainActivity.this,"Please Write A Comment",Toast.LENGTH_SHORT).show();

                        }else {
                            AddComment(holder,postKey,commentRef,mUser.getUid(),comment);
                        }
                    }
                });

                LoadComment(postKey);
                holder.postImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this,ImageViewActivity.class);
                        intent.putExtra("url",model.getPostImage());
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_post,parent,false);
                return new MyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void LoadComment(String postKey) {
        MyViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        Query query = commentRef.child(postKey);

        commentOption = new FirebaseRecyclerOptions.Builder<Comment>().setQuery(query,Comment.class).build();
        commentAdapter = new FirebaseRecyclerAdapter<Comment,CommentViewHolder> (commentOption) {
            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Comment model) {

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){
                            Picasso.get().load(model.getUserProfileImageUrl()).into(holder.profileImage);
                            holder.username.setText(model.getUsername());
                            holder.comment.setText(model.getComment());
                        }else {
                            //
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




            }

            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_comment,parent,false);
                return new CommentViewHolder(view);
            }
        };
        commentAdapter.startListening();
        MyViewHolder.recyclerView.setAdapter(commentAdapter);

    }



    private void AddComment(MyViewHolder holder, String postKey, DatabaseReference commentRef, String uid, String comment) {

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss");
        String strDate = formatter.format(date);

        HashMap hashMap = new HashMap();
        hashMap.put("username",userNameV);
        hashMap.put("userProfileImageUrl",profileImageUrlV);
        hashMap.put("comment",comment);



        commentRef.child(postKey).push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<StorageMetadata>() {
            @Override
            public void onComplete(@NonNull Task<StorageMetadata> task) {
                if (task.isSuccessful()){

                    adapter.notifyDataSetChanged();
                    holder.inputComments.setText(null);

                }else {
                    Toast.makeText(MainActivity.this,""+task.getException().toString(),Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private String calculateTimeAgo(String datePost) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy H:mm:ss");
        try {
            long time = sdf.parse(datePost).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            return ago+"";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null){
            imageUri=data.getData();
            ivChooseedImage.setVisibility(View.VISIBLE);
            ivChooseedImage.setImageURI(imageUri);


        }
    }

    private void AddPost() {
        String postDesc = inputPoseDes.getText().toString();

        if(postDesc.isEmpty() && imageUri == null){
            inputPoseDes.setError("please Enter In Post");

        }else if(postDesc.isEmpty() || postDesc.length()<3){

            mLoadingBar.setTitle("Adding Post");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss");
            String strDate = formatter.format(date);

            postImageRef.child(mUser.getUid()+strDate).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        postImageRef.child(mUser.getUid()+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                HashMap hashMap = new HashMap();
                                hashMap.put("datePost",strDate);
                                hashMap.put("postImage",uri.toString());
                                hashMap.put("postDesc","");
                                hashMap.put("userProfileImageUrl",profileImageUrlV);
                                hashMap.put("username",userNameV);

                                postRef.push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()){
                                            mLoadingBar.dismiss();

                                            addImagePost.setImageResource(R.drawable.ic_add_post_image);
                                            inputPoseDes.setText("");
                                            ivChooseedImage.setVisibility(View.GONE);

                                        }else {
                                            mLoadingBar.dismiss();
                                            Toast.makeText(MainActivity.this,"" + task.getException().toString(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }else {
                        mLoadingBar.dismiss();
                        Toast.makeText(MainActivity.this,"" + task.getException().toString(),Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }else if(imageUri==null){
            Toast.makeText(MainActivity.this,"Please select an image",Toast.LENGTH_SHORT).show();

            mLoadingBar.setTitle("Adding Post");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss");
            String strDate = formatter.format(date);

            HashMap hashMap = new HashMap();
            hashMap.put("datePost",strDate);
            hashMap.put("postDesc",postDesc);
            hashMap.put("postImage","");
            hashMap.put("userProfileImageUrl",profileImageUrlV);
            hashMap.put("username",userNameV);
            postRef.push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        mLoadingBar.dismiss();

                        addImagePost.setImageResource(R.drawable.ic_add_post_image);
                        inputPoseDes.setText("");
                        ivChooseedImage.setVisibility(View.GONE);

                    }else {
                        mLoadingBar.dismiss();
                        Toast.makeText(MainActivity.this,"" + task.getException().toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else {
            mLoadingBar.setTitle("Adding Post");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss");
            String strDate = formatter.format(date);

            postImageRef.child(mUser.getUid()+strDate).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        postImageRef.child(mUser.getUid()+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                HashMap hashMap = new HashMap();
                                hashMap.put("datePost",strDate);
                                hashMap.put("postImage",uri.toString());
                                hashMap.put("postDesc",postDesc);
                                hashMap.put("userProfileImageUrl",profileImageUrlV);
                                hashMap.put("username",userNameV);
                                postRef.push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()){
                                            mLoadingBar.dismiss();

                                            addImagePost.setImageResource(R.drawable.ic_add_post_image);
                                            inputPoseDes.setText("");
                                            ivChooseedImage.setVisibility(View.GONE);

                                        }else {
                                            mLoadingBar.dismiss();
                                            Toast.makeText(MainActivity.this,"" + task.getException().toString(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }else {
                        mLoadingBar.dismiss();
                        Toast.makeText(MainActivity.this,"" + task.getException().toString(),Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mUser == null){
            SendUserToLoginActivity();
        }else {
            mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        profileImageUrlV = snapshot.child("profileImage").getValue().toString();
                        userNameV = snapshot.child("username").getValue().toString();
                        Picasso.get().load(profileImageUrlV).into(profileImageHeader);
                        userNameHeader.setText(userNameV);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void SendUserToLoginActivity() {
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.profile:
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                break;

            case R.id.friends:
               startActivity(new Intent(MainActivity.this,FriendActivity.class));
                break;

            case R.id.findFriends:
                startActivity(new Intent(MainActivity.this,FindFriendActivity.class));
                break;

            case R.id.chat:
                startActivity(new Intent(MainActivity.this,ChatUserActivity.class));
                break;

            case R.id.activity:
                startActivity(new Intent(MainActivity.this,ActivitySummarize.class));
                break;

            case R.id.group:
                startActivity(new Intent(MainActivity.this,GroupActivity.class));
                break;

            case R.id.find_group:
                startActivity(new Intent(MainActivity.this,FindGroupActivity.class));
                break;

            case R.id.invited_group:
                startActivity(new Intent(MainActivity.this,ReceivedGroupActivity.class));
                break;

            case R.id.store:
                startActivity(new Intent(MainActivity.this, MenuStickerMain.class));
                break;

            case R.id.logout:
              mAuth.signOut();
              Intent intent = new Intent(MainActivity.this,LoginActivity.class);
              startActivity(intent);
              finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return true;
    }






    @Override
    protected void onDestroy() {
        super.onDestroy();

    }




    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });



}