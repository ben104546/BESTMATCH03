package com.example.a5dayappchat2.Activity.Doing;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.Activity.ActivityDeailAndSetting;
import com.example.a5dayappchat2.Activity.CalendarList;
import com.example.a5dayappchat2.Activity.CalendarListViewHolder;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.ActivityDoingsMainRealBinding;
import com.example.a5dayappchat2.databinding.FragmentDoingsJoinedBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoingsJoined#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoingsJoined extends Fragment implements CalandarViewAdapter.OnItemListener {

    FragmentDoingsJoinedBinding binding;
    ActivityDoingsMainRealBinding bindingMain;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DoingsJoined() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoingsJoined.
     */
    // TODO: Rename and change types and number of parameters
    public static DoingsJoined newInstance(String param1, String param2) {
        DoingsJoined fragment = new DoingsJoined();
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

    LocalDate selectDate;

    DatabaseReference mGroupRef;

    FirebaseAuth mAuth;
    FirebaseUser mUser;


    String GroupToken;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        GroupToken = getActivity().getIntent().getStringExtra("GroupKey");

        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        binding = FragmentDoingsJoinedBinding.inflate(inflater, container, false);
        bindingMain = ActivityDoingsMainRealBinding.inflate(inflater, container, false);

        selectDate = LocalDate.now();

        binding.btDoingBack.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                selectDate = selectDate.minusMonths(1);
                setMonthView();

            }
        });

        binding.btDoingNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                selectDate = selectDate.plusMonths(1);
                setMonthView();

            }
        });

        binding.include2.ivHideCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.reDoingList.isShown() && binding.btDoingBack.isShown() && binding.btDoingNext.isShown() &&
                        binding.tvDoingsMonth.isShown() && binding.linearLayout.isShown()){
                    binding.reDoingList.setVisibility(View.GONE);
                    binding.btDoingBack.setVisibility(View.GONE);
                    binding.btDoingNext.setVisibility(View.GONE);
                    binding.tvDoingsMonth.setVisibility(View.GONE);
                    binding.linearLayout.setVisibility(View.GONE);
                }else {
                    binding.reDoingList.setVisibility(View.VISIBLE);
                    binding.btDoingBack.setVisibility(View.VISIBLE);
                    binding.btDoingNext.setVisibility(View.VISIBLE);
                    binding.tvDoingsMonth.setVisibility(View.VISIBLE);
                    binding.linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });



        binding.JoinedDoings.setLayoutManager(new LinearLayoutManager(getActivity()));

        LoadGroupAppBar();
        setMonthView();
        LoadHaveDoings(GroupToken);




        return binding.getRoot();
    }

    private void LoadGroupAppBar() {

        mGroupRef.child(GroupToken).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String GroupName = snapshot.child("GroupName").getValue().toString();
                    String GroupProfileImage = snapshot.child("GroupProfileImage").getValue().toString();

                    Picasso.get().load(GroupProfileImage).into(binding.include2.cimprofilegroup);
                    binding.include2.tvnamegroup.setText(GroupName);



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



    FirebaseRecyclerOptions<CalendarList> options;
    FirebaseRecyclerAdapter<CalendarList, CalendarListViewHolder> adapter;




    private void LoadHaveDoings(String groupToken) {

        options = new FirebaseRecyclerOptions.Builder<CalendarList>().setQuery(mGroupRef.child(groupToken).child("Activity").orderByChild("DoingsDate"), CalendarList.class).build();
        adapter = new FirebaseRecyclerAdapter<CalendarList, CalendarListViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull CalendarListViewHolder holder, int position, @NonNull CalendarList model) {
                getRef(position);

                String DoingToken = getRef(position).getKey().toString();

                mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("PeopleOfActivity").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){

                            boolean Notification = (boolean) snapshot.child("Notification").getValue();

                            Picasso.get().load(model.getDoingsImage()).into(holder.firstUserProfileImage);
                            holder.tvNameDoingsList.setText(model.getDoingsName());
                            holder.tvTimeDoings.setText(model.getDoingsDate());

                            if(Notification){
                                holder.ivNotificOn.setVisibility(View.VISIBLE);
                                holder.ivNotificOff.setVisibility(View.GONE);
                            }else {
                                holder.ivNotificOn.setVisibility(View.GONE);
                                holder.ivNotificOff.setVisibility(View.VISIBLE);
                            }

                            holder.ivNotificOn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("Notification",false);

                                    mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("PeopleOfActivity").child(mUser.getUid()).updateChildren(hashMap);

                                }
                            });


                            holder.ivNotificOff.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("Notification",true);

                                    mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("PeopleOfActivity").child(mUser.getUid()).updateChildren(hashMap);
                                }
                            });

                        }else {
                            holder.itemView.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    }
                });



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getActivity(), ActivityDeailAndSetting.class);
                        intent.putExtra("GroupKey",groupToken);
                        intent.putExtra("DoingKey",getRef(position).getKey().toString());

                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public CalendarListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_doings_chat_cell,parent,false);
                return new CalendarListViewHolder(view);
            }
        };

        adapter.startListening();
        binding.JoinedDoings.setAdapter(adapter);


    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {


        binding.tvDoingsMonth.setText(monthYearFromDate(selectDate));
        ArrayList<String> dayInMonth = dayInMonthArray(selectDate);

        CalandarViewAdapter calandarViewAdapter = new CalandarViewAdapter(dayInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(),7);

        binding.reDoingList.setLayoutManager(layoutManager);
        binding.reDoingList.setAdapter(calandarViewAdapter);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> dayInMonthArray(LocalDate selectDate) {
        ArrayList<String>dayInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(selectDate);

        int dayInMonth = yearMonth.lengthOfMonth();

        LocalDate firstDatInMonth = selectDate.withDayOfMonth(1);

        int dayOfWeek = firstDatInMonth.getDayOfWeek().getValue();

        for (int i = 1 ; i <= 42 ; i++){

            if(i <= dayOfWeek || i > dayInMonth + dayOfWeek){
                dayInMonthArray.add("");
            }else {
                dayInMonthArray.add(String.valueOf(i - dayOfWeek));
            }

        }

        return dayInMonthArray;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, String dayText) {

        if(!dayText.equals("")){
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectDate);
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }

    }
}