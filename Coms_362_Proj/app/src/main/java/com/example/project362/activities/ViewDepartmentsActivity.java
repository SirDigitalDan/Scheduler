package com.example.project362.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.adapters.DepartmentsAdapter;
import com.example.project362.models.Department;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewDepartmentsActivity extends AppCompatActivity
{
	RecyclerView recycler;
	public ArrayList<Department> departments = new ArrayList<>();

	TextView departmentNameInput;
	Button createDepartmentButton;
	DepartmentsAdapter adapter;

	@Override
	protected void onCreate(Bundle saveInstanceState)
	{
		super.onCreate(saveInstanceState);
		setContentView(R.layout.activity_view_departments);

		departmentNameInput = findViewById(R.id.department_name_input);
		createDepartmentButton = findViewById(R.id.create_department_btn);

		recycler.setHasFixedSize(true);

		// use a linear layout manager
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
		recycler.setLayoutManager(layoutManager);

		// get list of all the departments and add to the recycler view
		Department.getDepartments().addOnCompleteListener((Task<QuerySnapshot> task) -> {
			if (task.isSuccessful())
			{
				for (QueryDocumentSnapshot depDoc : task.getResult())
					departments.add(new Department(depDoc));
				adapter = new DepartmentsAdapter(departments);
				recycler.setAdapter(adapter);
			}
			else
				Toast.makeText(ViewDepartmentsActivity.this, "No Departments At This Time!",
						Toast.LENGTH_SHORT).show();
		});
	}

	@Override
	public void onStart()
	{
		super.onStart();
		createDepartmentButton.setOnClickListener(v -> {
			String name = departmentNameInput.getText().toString();

			// create an instance of the new department
			Department d = new Department(name);
			// add to the database, then update the list if it is successful
			d.create().addOnCompleteListener(t -> {
				if (t.isSuccessful() && t.getResult() != null)
				{
					t.getResult().get().addOnCompleteListener(getDepTask -> {
						if (getDepTask.isSuccessful() && getDepTask.getResult() != null)
						{
							Department created = new Department(getDepTask.getResult());
							departments.add(created);
							adapter.notifyDataSetChanged();
							Toast.makeText(ViewDepartmentsActivity.this, "Department created!",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
		});
	}
}
