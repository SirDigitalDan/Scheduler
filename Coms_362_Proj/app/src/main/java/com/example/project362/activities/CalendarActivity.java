package com.example.project362.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.project362.R;

public class CalendarActivity extends AppCompatActivity {
    CalendarView calendarView;
            TextView myDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView =  findViewById(R.id.calendarView);
        myDate = findViewById(R.id.myDate);

        calendarView.setOnDateChangeListener((calendarView, i, i1, i2) -> {
            String date = (i1 + 1) +"/" + i2 + i;
            myDate.setText(date);
        });
    }
}
