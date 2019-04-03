package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.models.Employee;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity
{
    CalendarView calendarView;
    TextView myDate;

    private ArrayList<String> availability;
    String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

	    // the calendar view
	    calendarView = findViewById(R.id.calendarView);

	    // the date entered from the calendar
	    myDate = findViewById(R.id.myDate);

        findViewById(R.id.save_avail).setOnClickListener((v) -> addAvailability(myDate.getText().toString()));
        findViewById(R.id.view_avail).setOnClickListener((v) -> {
	        finish();
	        Intent in = new Intent(CalendarActivity.this, ViewAvailability.class);
	        startActivity(in);
        });

        currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        availability = new ArrayList<>();

        calendarView.setOnDateChangeListener((calendarView, year, month, day) -> {
            String date = (month + 1) +"/" + day + "/" + year;
            myDate.setText(date);
            availability.add(date);
        });
    }

    public void addAvailability(String date)
    {
    	if (date.isEmpty())
    		Toast.makeText(CalendarActivity.this, "Date is empty",
			    Toast.LENGTH_SHORT).show();

    	// get the current user's employee object
        Employee.getEmployeeByEmail(currentEmail).addOnCompleteListener((Task<DocumentSnapshot> t) -> {
            if (t.isSuccessful() && t.getResult() != null)
            {
                //creates new employee Object
                Employee e = new Employee(t.getResult());
                e.addAvailability(date);
            }
        });
    }
}
