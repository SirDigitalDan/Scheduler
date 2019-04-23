package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project362.R;

import com.example.project362.models.Payment;
import com.example.project362.models.Project;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class CreateProjectActivity extends AppCompatActivity
{
    private EditText projectName;
    private EditText projectBudget;
    private CalendarView deadline;
    private Button submit;

    private Date date;
    private Calendar cal;

    @Override
    public void onCreate(Bundle savedInstanceBundle)
    {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_create_project);

        projectName = findViewById(R.id.editTextProjectName);
        projectBudget = findViewById(R.id.editTextProjectBudget);
        deadline = findViewById(R.id.projectDeadlineCalendar);
        submit = findViewById(R.id.createProjectSubmit);

        cal = Calendar.getInstance();
    }

    public void onStart()
    {
        super.onStart();

        this.submit.setOnClickListener((View v) -> {
            String name = projectName.getText().toString();

            String budget = projectBudget.getText().toString();
            String[] values = budget.split(",");

            Map<String, Long> budgetMap = new HashMap<>();
            int i = 0;
            int j = 1;
            long total = 0;
            while(j < values.length){
                long l = Long.parseLong(values[j].trim());
                budgetMap.put(values[i], l);
                i += 2;
                j += 2;
                total += l;
            }

            budgetMap.put("total", total);

            Project p = new Project(name, date, budgetMap);

            p.create().addOnCompleteListener(t -> {
                if (t.isSuccessful())
                {
                    Toast.makeText(CreateProjectActivity.this, "Project created!",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, AdminViewProjectsActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(CreateProjectActivity.this, "Something went wrong!",
                            Toast.LENGTH_SHORT).show();
            });

        });

        deadline.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                cal.set(year, month, dayOfMonth, 23, 59);
                date = cal.getTime();
            }
        });

    }


}
