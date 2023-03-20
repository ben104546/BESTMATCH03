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
import com.example.a5dayappchat2.Activity.ShowDayDoing;
import com.example.a5dayappchat2.Activity.WeeklyActivity;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.ActivityDoingsMainRealBinding;
import com.example.a5dayappchat2.databinding.FragmentAllDoingsBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllDoings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllDoings extends Fragment implements CalandarViewAdapter.OnItemListener {

    FragmentAllDoingsBinding binding;
    ActivityDoingsMainRealBinding bindingMain;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllDoings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllDoings.
     */
    // TODO: Rename and change types and number of parameters
    public static AllDoings newInstance(String param1, String param2) {
        AllDoings fragment = new AllDoings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    String GroupToken;
    String message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);



        }else {

        }
    }


    LocalDate selectDate;

    DatabaseReference mGroupRef;


    FirebaseRecyclerOptions<CalendarList> options;
    FirebaseRecyclerAdapter<CalendarList, CalendarListViewHolder> adapter;




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        GroupToken = getActivity().getIntent().getStringExtra("GroupKey");


        binding = FragmentAllDoingsBinding.inflate(inflater, container, false);

        bindingMain = ActivityDoingsMainRealBinding.inflate(inflater, container, false);

        selectDate = LocalDate.now();
        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");


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



        binding.recyclerViewAllDoings.setLayoutManager(new LinearLayoutManager(getActivity()));

        LoadDoingAllDay(GroupToken);
        LoadGroupAppBar();

        setMonthView();

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

   private void LoadDoingAllDay(String groupToken) {


        options = new FirebaseRecyclerOptions.Builder<CalendarList>().setQuery(mGroupRef.child(groupToken).child("Activity").orderByChild("DoingsDate"), CalendarList.class).build();
        adapter = new FirebaseRecyclerAdapter<CalendarList, CalendarListViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull CalendarListViewHolder holder, int position, @NonNull CalendarList model) {
                getRef(position);


                String Date = model.getDoingsDate();

                Picasso.get().load(model.getDoingsImage()).into(holder.firstUserProfileImage);
                holder.tvNameDoingsList.setText(model.getDoingsName());
                holder.tvTimeDoings.setText(model.getDoingsDate());

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
        binding.recyclerViewAllDoings.setAdapter(adapter);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        binding.tvDoingsMonth.setText(monthYearFromDate(selectDate));
        ArrayList<String> dayInMonth = dayInMonthArray(selectDate);

        CalandarViewAdapter calandarViewAdapter = new CalandarViewAdapter(dayInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(),7);


        binding.tvDoingsMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadDoingAllDay(GroupToken);
                binding.tvDoingsMonth.setText(monthYearFromDate(selectDate));
            }
        });




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
            message = dayText + " " + monthYearFromDate(selectDate);
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

            LoadDoingForDay(message);
            binding.tvDoingsMonth.setText(message);
        }

    }

    private void LoadDoingForDay(String message) {
        options = new FirebaseRecyclerOptions.Builder<CalendarList>().setQuery(mGroupRef.child(GroupToken).child("Activity").orderByChild("DoingsTime"), CalendarList.class).build();
        adapter = new FirebaseRecyclerAdapter<CalendarList, CalendarListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CalendarListViewHolder holder, int position, @NonNull CalendarList model) {

                if(message.equals(model.getDoingsDate())){
                    getRef(position);
                    Picasso.get().load(model.getDoingsImage()).into(holder.firstUserProfileImage);
                    holder.tvNameDoingsList.setText(model.getDoingsName());
                    holder.tvTimeDoings.setText(model.getDoingsTime());

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), ShowDayDoing.class);
                            intent.putExtra("GroupToken",GroupToken);
                            intent.putExtra("DoingToken",getRef(position).getKey().toString());
                            startActivity(intent);

                        }
                    });

                    Toast.makeText(getActivity(), "Load Activity Success", Toast.LENGTH_SHORT).show();

                }else {
                    getRef(position);
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }

            }

            @NonNull
            @Override
            public CalendarListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_doings_chat_cell,parent,false);
                return new CalendarListViewHolder(view);
            }
        };

        adapter.startListening();
        binding.recyclerViewAllDoings.setAdapter(adapter);


    }
}