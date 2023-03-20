package com.example.a5dayappchat2.Activity;


import static com.example.a5dayappchat2.Activity.Utills.CalendarUtils.daysInWeekArray;
import static com.example.a5dayappchat2.Activity.Utills.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.Activity.Preferences.PreferencesDateOfActivity;
import com.example.a5dayappchat2.Activity.Utills.CalendarUtils;
import com.example.a5dayappchat2.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.ArrayList;


public class WeeklyActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {



    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    Button btBack,btNext;
    RecyclerView DoingsOfDay;
    TextView tvNameDoingsList,tvTimeDoings;
    DatabaseReference mGroupRef;

    PreferencesDateOfActivity preferencesDateOfActivity;

    String GroupToken;
    String message;





    FirebaseRecyclerOptions<CalendarList> options;
    FirebaseRecyclerAdapter<CalendarList,CalendarListViewHolder> adapter;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly);


        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        btBack = findViewById(R.id.btBack);
        btNext = findViewById(R.id.btNext);


        DoingsOfDay = findViewById(R.id.DoingsOfDay);
        DoingsOfDay.setLayoutManager(new LinearLayoutManager(this));



        tvNameDoingsList = findViewById(R.id.tvNameDoingsList);
        tvTimeDoings = findViewById(R.id.tvTimeDoings);

        preferencesDateOfActivity = new PreferencesDateOfActivity(getApplicationContext());

        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");


        GroupToken = getIntent().getStringExtra("groupTokenForWeekly");

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousWeekAction();
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextWeekAction();
            }
        });

        setWeekView();

        LoadDoingAllDay();


    }

    private void LoadDoingAllDay() {
        options = new FirebaseRecyclerOptions.Builder<CalendarList>().setQuery(mGroupRef.child(GroupToken).child("Activity").orderByChild("DoingsDate"), CalendarList.class).build();
        adapter = new FirebaseRecyclerAdapter<CalendarList, CalendarListViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull CalendarListViewHolder holder, int position, @NonNull CalendarList model) {
                getRef(position);
                Picasso.get().load(model.getDoingsImage()).into(holder.firstUserProfileImage);
                holder.tvNameDoingsList.setText(model.getDoingsName());
                holder.tvTimeDoings.setText(model.getDoingsDate());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(WeeklyActivity.this, ShowDayDoing.class);
                        intent.putExtra("GroupToken",GroupToken);
                        intent.putExtra("DoingToken",getRef(position).getKey().toString());
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
        DoingsOfDay.setAdapter(adapter);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousWeekAction()
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextWeekAction()
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date) {

        message = date.getDayOfMonth() + " " + monthYearFromDate(CalendarUtils.selectedDate);

        CalendarUtils.selectedDate = date;
        setWeekView();
        
        
        LoadDoingForDay("");

    }

    private void LoadDoingForDay(String s) {
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
                            Intent intent = new Intent(WeeklyActivity.this, ShowDayDoing.class);
                            intent.putExtra("GroupToken",GroupToken);
                            intent.putExtra("DoingToken",getRef(position).getKey().toString());
                            startActivity(intent);

                        }
                    });

                    Toast.makeText(WeeklyActivity.this, "Load Activity Success", Toast.LENGTH_SHORT).show();

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
        DoingsOfDay.setAdapter(adapter);

    }

}