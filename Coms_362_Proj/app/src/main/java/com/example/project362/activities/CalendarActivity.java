package com.example.project362.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.project362.R;
import com.example.project362.models.Employee;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class CalendarActivity extends AppCompatActivity {
    CalendarView calendarView;
            TextView myDate;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mAuth = FirebaseAuth.getInstance();
        calendarView =  findViewById(R.id.calendarView);
        myDate = findViewById(R.id.myDate);
        String id = mAuth.getCurrentUser().getEmail();
        calendarView.setOnDateChangeListener((calendarView, i, i1, i2) -> {
            String date = (i1 + 1) +"/" + i2 +"/"+ i;
            myDate.setText(date);
            Employee.getEmployeeByEmail(id).addOnCompleteListener((Task<DocumentSnapshot> t) -> {
               if (t.isSuccessful() && t.getResult() != null)
               {
                   Employee e = new Employee(t.getResult());
                   e.addAvailability(date);
               }
            });

        });
    }
}
