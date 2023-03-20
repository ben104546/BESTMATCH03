package com.example.a5dayappchat2.Activity.Doing;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.R;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DoingsMain extends AppCompatActivity implements CalandarViewAdapter.OnItemListener{

    TextView tvDoingsMonth;
    ImageButton btDoingNext,btDoingBack;
    RecyclerView reDoingList;
    LocalDate selectDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doings_main);

        tvDoingsMonth = findViewById(R.id.tvDoingsMonth);
        btDoingNext = findViewById(R.id.btDoingNext);
        btDoingBack = findViewById(R.id.btDoingBack);
        reDoingList = findViewById(R.id.reDoingList);
        selectDate = LocalDate.now();

        btDoingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate = selectDate.minusMonths(1);
                setMonthView();

            }
        });

        btDoingNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate = selectDate.plusMonths(1);
                setMonthView();

            }
        });

        setMonthView();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        tvDoingsMonth.setText(monthYearFromDate(selectDate));
        ArrayList<String> dayInMonth = dayInMonthArray(selectDate);

        CalandarViewAdapter calandarViewAdapter = new CalandarViewAdapter(dayInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),7);

        reDoingList.setLayoutManager(layoutManager);
        reDoingList.setAdapter(calandarViewAdapter);

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
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

    }
}