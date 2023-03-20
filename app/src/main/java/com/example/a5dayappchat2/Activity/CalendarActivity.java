package com.example.a5dayappchat2.Activity;

import static com.example.a5dayappchat2.Activity.Utills.CalendarUtils.daysInMonthArray;
import static com.example.a5dayappchat2.Activity.Utills.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.Activity.Utills.CalendarUtils;
import com.example.a5dayappchat2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    String  groupToken ;

    FloatingActionButton btGotoCreateDoings;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calendar_tree);
        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();
        groupToken = getIntent().getStringExtra("GroupTokenForCalendarGroup");
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        btGotoCreateDoings = findViewById(R.id.btGotoCreateDoings);

        btGotoCreateDoings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoDoingsActivity();
            }
        });
    }

    private void GotoDoingsActivity() {
        Intent intent = new Intent(CalendarActivity.this,CreateDoingsActivity.class);
        intent.putExtra("groupTokenForCreateDoings",groupToken);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date) {

        if(date != null)
        {

            Intent intent = new Intent(CalendarActivity.this,WeeklyActivity.class);
            intent.putExtra("groupTokenForWeekly",groupToken);
            startActivity(intent);

            CalendarUtils.selectedDate = date;
            setMonthView();


        }

    }
}








