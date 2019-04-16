package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.adapters.ShiftsAdapterAdmin;
import com.example.project362.models.Shift;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminHomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    public ArrayList<Shift> shifts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        // button to navigate to page to grant employee to admin status
        Button adminStatusButton = findViewById(R.id.adminStatusButton);

        // button to navigate to page to review pending payments
        Button paymentButton = findViewById(R.id.reviewPaymentsButton);

        // button to navigate to page to show attendance for each shift
        Button adminAttendanceButton = findViewById(R.id.adminAttendance);

        // button to navigate to the page for shift creation
        Button createShift = findViewById(R.id.createShift);

        Button viewEmployees = findViewById(R.id.view_employees_nav_btn);

        Button viewDepartments = findViewById(R.id.view_departments);

        recyclerView = findViewById(R.id.shiftsList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // get all shifts to display
        Shift.getShifts().addOnCompleteListener((Task<QuerySnapshot> task) -> {
            if (task.isSuccessful())
            {
                for (QueryDocumentSnapshot shiftDoc : task.getResult())
                {
                	// convert the snapshots to Shift objects
                    Shift s = new Shift(shiftDoc);
                    // add to shifts to display
                    shifts.add(s);
                }

                Toast.makeText(AdminHomeActivity.this,
                        "Query Size " + Integer.toString(shifts.size()), Toast.LENGTH_SHORT).show();
                mAdapter = new ShiftsAdapterAdmin(shifts);
                recyclerView.setAdapter(mAdapter);
            }
            else
                Toast.makeText(AdminHomeActivity.this, "No Shifts At This Time!", Toast.LENGTH_SHORT).show();
        });

        adminStatusButton.setOnClickListener((View v) -> {
            finish();
            // go to admin status page
            Intent i = new Intent(AdminHomeActivity.this, AdminStatusActivity.class);
            startActivity(i);
        });

        paymentButton.setOnClickListener((View v) -> {
            finish();
            // go to payment review page
            Intent i = new Intent(AdminHomeActivity.this, PaymentReviewActivity.class);
            startActivity(i);
        });

        adminAttendanceButton.setOnClickListener((View v) -> {
        	finish();
        	// go to attendance page
            Intent i = new Intent(this, ViewAttendanceActivity.class);
            startActivity(i);
        });

        createShift.setOnClickListener((View v) -> {
            finish();
            // go to the create shift page
            Intent i = new Intent(this, CreateShiftActivity.class);
            startActivity(i);
        });

        viewEmployees.setOnClickListener(v -> {
        	finish();
        	// go to the view employees page
	        Intent i = new Intent(this, AdminViewEmployeesActivity.class);
	        startActivity(i);
        });

        viewDepartments.setOnClickListener(v -> {
        	finish();
        	// go to the departments list
	        Intent i = new Intent(this, ViewDepartmentsActivity.class);
	        startActivity(i);
        });
    }
}
