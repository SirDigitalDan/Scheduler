package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.project362.R;
import com.example.project362.models.Employee;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener{
    CalendarView calendarView;
            TextView myDate;
    private FirebaseAuth mAuth;
    private ArrayList<String> availability;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        findViewById(R.id.save_avail).setOnClickListener(CalendarActivity.this);
        findViewById(R.id.view_avail).setOnClickListener(CalendarActivity.this);
        mAuth = FirebaseAuth.getInstance();
        calendarView =  findViewById(R.id.calendarView);
        myDate = findViewById(R.id.myDate);
        String id = mAuth.getCurrentUser().getEmail();
        availability= new ArrayList<>();

        calendarView.setOnDateChangeListener((calendarView, i, i1, i2) -> {
            String date = (i1 + 1) +"/" + i2 +"/"+ i;
            myDate.setText(date);
           availability.add(date);

        });
    }

    public void addAvailabilities (){
        String id = mAuth.getCurrentUser().getEmail();
        Employee.getEmployeeByEmail(id).addOnCompleteListener((Task<DocumentSnapshot> t) -> {
            if (t.isSuccessful() && t.getResult() != null)
            {
                //creates new employee Object
                Employee e = new Employee(t.getResult());
                //Goes through and gets all of the dates in the Availability array
                for(int i=0; i <availability.size();i++) {
                    String curr = availability.get(i);
                    //e.addAvailability(curr);
                }
                e.addAvailability(availability);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_avail:
                    addAvailabilities();

                break;
            case R.id.view_avail:
                finish();
                Intent in = new Intent(CalendarActivity.this, ViewAvailability.class);
                startActivity(in);
        }

    }

}
