package com.example.a5dayappchat2.Activity.ActivityDetailAndSetting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.Activity.ActivityDeailAndSetting;
import com.example.a5dayappchat2.Activity.Doing.ActivityDoingsMainReal;
import com.example.a5dayappchat2.Activity.Doing.DoingsMain;
import com.example.a5dayappchat2.Activity.MainActivity;
import com.example.a5dayappchat2.Activity.ShowDayDoing;
import com.example.a5dayappchat2.Activity.Utills.ActivityDetailImage;
import com.example.a5dayappchat2.Activity.ViewHolderActivityImage;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.FragmentMainActivityDetailBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainActivityDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivityDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainActivityDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainActivityDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainActivityDetailFragment newInstance(String param1, String param2) {
        MainActivityDetailFragment fragment = new MainActivityDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    DatabaseReference mGroupRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    private FragmentMainActivityDetailBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ActivityDeailAndSetting activityDeailAndSetting = (ActivityDeailAndSetting) getActivity();


        String GroupToken = activityDeailAndSetting.getDataGroupToken();
        String DoingToken = activityDeailAndSetting.getDataDoingToken();



        binding = FragmentMainActivityDetailBinding.inflate(inflater, container, false);


        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();



        LoadActivityDetail(GroupToken,DoingToken);
        LoadImageActivity(GroupToken,DoingToken);
        HideORShowButtonJoinActivity(GroupToken,DoingToken);



        binding.include.tvDetailActivity.setText("Details Activity");

        binding.ivDelActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mGroupRef.child(GroupToken).child("Activity").child(DoingToken).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(getActivity(), ActivityDoingsMainReal.class);
                        startActivity(intent);

                    }
                });


            }
        });

        binding.layoutJoinActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap hashMap = new HashMap();
                hashMap.put("Doing_position","Doing_Member");
                hashMap.put("Notification",true);

                mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("PeopleOfActivity").child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {

                        Intent intent = new Intent(getActivity(),MainActivityDetailFragment.class);
                        startActivity(intent);
                    }
                });

            }
        });

        return binding.getRoot();


    }



    private void HideORShowButtonJoinActivity(String groupToken, String doingToken) {
        mGroupRef.child(groupToken).child("Activity").child(doingToken).child("PeopleOfActivity").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    binding.layoutJoinActivity.setVisibility(View.GONE);

                }else {
                    binding.layoutJoinActivity.setVisibility(View.VISIBLE);
                }


                if(snapshot.child("Doing_position").exists()){
                    String Position = snapshot.child("Doing_position").getValue().toString();
                    if(Position.equals("Doing_Header")){
                        binding.ivDelActivity.setVisibility(View.VISIBLE);
                    }else {
                        binding.ivDelActivity.setVisibility(View.GONE);
                    }
                }else {
                    binding.ivDelActivity.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    FirebaseRecyclerOptions<ActivityDetailImage>ActivityOptions;
    FirebaseRecyclerAdapter<ActivityDetailImage, ViewHolderActivityImage>ActivityAdapter;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    ImageView ivPictureActivtyOFF;
    ImageView ivFullPicture;

    private void LoadImageActivity(String groupToken, String doingToken) {

        mGroupRef.child(groupToken).child("Activity").child(doingToken).child("ImageActivity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int CountPIcture = (int) snapshot.getChildrenCount();


                    if(CountPIcture <= 1){

                        layoutManager = new LinearLayoutManager(getActivity());


                    }else {
                        layoutManager = new GridLayoutManager(getActivity(),2);
                    }

                    binding.recyclerViewActvity.setLayoutManager(layoutManager);

                    Toast.makeText(getActivity(), String.valueOf(CountPIcture), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), ""+Constant.ERROR_TEXT_NOT_EXISTS, Toast.LENGTH_SHORT).show();
            }
        });


        Query query = mGroupRef.child(groupToken).child("Activity").child(doingToken).child("ImageActivity").orderByChild("ImageLink");
        ActivityOptions = new FirebaseRecyclerOptions.Builder<ActivityDetailImage>().setQuery(query, ActivityDetailImage.class).build();
        ActivityAdapter = new FirebaseRecyclerAdapter<ActivityDetailImage, ViewHolderActivityImage>(ActivityOptions) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderActivityImage holder, int position, @NonNull ActivityDetailImage model) {
                getRef(position);
                Picasso.get().load(model.getImageLink()).into(holder.ivPictureDoings);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        dialogBuilder = new AlertDialog.Builder(getActivity());
                        final  View contarctPopView = getLayoutInflater().inflate(R.layout.single_view_picture_activity_full,null);


                        ivPictureActivtyOFF = contarctPopView.findViewById(R.id.ivPictureActivtyOFF);
                        ivFullPicture = contarctPopView.findViewById(R.id.ivFullPicture);

                        Picasso.get().load(model.getImageLink()).into(ivFullPicture);



                        ivPictureActivtyOFF.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        dialogBuilder.setView(contarctPopView);
                        dialog = dialogBuilder.create();
                        dialog.show();
                    }
                });

            }

            @NonNull
            @Override
            public ViewHolderActivityImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_picture_doings, parent, false);
                return new ViewHolderActivityImage(view);
            }
        };

        ActivityAdapter.startListening();
        binding.recyclerViewActvity.setAdapter(ActivityAdapter);

    }

    RecyclerView.LayoutManager layoutManager;

    private void LoadActivityDetail(String groupToken, String doingToken) {
        mGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    
                    String ActivityImage = snapshot.child(groupToken).child("Activity").child(doingToken).child("DoingsImage").getValue().toString();
                    String ActivityName = snapshot.child(groupToken).child("Activity").child(doingToken).child("DoingsName").getValue().toString();
                    String ActivityDescription = snapshot.child(groupToken).child("Activity").child(doingToken).child("DoingsName").getValue().toString();
                    String ActivityDate = snapshot.child(groupToken).child("Activity").child(doingToken).child("DoingsDate").getValue().toString();
                    String ActivityTime = snapshot.child(groupToken).child("Activity").child(doingToken).child("DoingsTime").getValue().toString();

                    String SumDate = ActivityDate+" "+ActivityTime;
                    String currentDate = new SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault()).format(new Date());

                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");

                    try {
                        Date date = sdf.parse(SumDate);
                        Date date2 = sdf.parse(currentDate);

                        long currentTime = date2.getTime();
                        long newYearDate = date.getTime();

                        long countDownToNewYear = newYearDate - currentTime;

                        binding.myCountdownActivity.start(countDownToNewYear);

                    } catch (ParseException e){
                        e.printStackTrace();
                    }





                    Picasso.get().load(ActivityImage).resize(1920, 1080).centerInside().into(binding.ivActivity);
                    binding.tvActivityName.setText(ActivityName);
                    binding.tvACtivityDate.setText(ActivityDate);
                    binding.tvActivityDescrip.setText(ActivityDescription);

                }else {
                    Toast.makeText(getActivity(), ""+ Constant.ERROR_TEXT_NOT_EXISTS, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}