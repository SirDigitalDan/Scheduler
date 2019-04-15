package com.example.project362.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.adapters.EmployeeWageAdapter;
import com.example.project362.adapters.ShiftsAdapter;
import com.example.project362.models.Employee;
import com.example.project362.models.Shift;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdminViewEmployeesActivity extends AppCompatActivity
{
	RecyclerView recycler;
	ArrayList<Employee> employees;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_employees);

		recycler = findViewById(R.id.employees_list);
		recycler.setHasFixedSize(true);

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
		recycler.setLayoutManager(layoutManager);

		Employee.getEmployees().addOnCompleteListener(t -> {
			if (t.isSuccessful() && t.getResult() != null)
			{
				// add each shift to the shifts arraylist
				for (QueryDocumentSnapshot empDoc : t.getResult())
				{
					Employee e = new Employee(empDoc);
					employees.add(e);
				}
				// update the display with the new shifts
				recycler.setAdapter(new EmployeeWageAdapter(employees));
			}
			else
				Toast.makeText(AdminViewEmployeesActivity.this, "No Employees At This Time!",
						Toast.LENGTH_SHORT).show();
		});
	}

	@Override
	protected void onStart()
	{
		super.onStart();
	}
}
