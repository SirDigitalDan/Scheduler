package com.example.project362.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.models.Department;
import com.example.project362.models.Employee;
import com.example.project362.models.Shift;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>
{
	ArrayList<Employee> employees;

	public EmployeeAdapter(ArrayList<Employee> employees)
	{
		this.employees = employees;
	}

	@NonNull
	@Override
	public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
	{
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employee_card,
				viewGroup, false);
		return new EmployeeAdapter.EmployeeViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull EmployeeViewHolder employeeViewHolder, int i)
	{
		final Employee e = employees.get(i);

		final DocumentReference employee = Employee.getEmployeeReferenceByKey(e.getEmail());

		employeeViewHolder.name.setText(e.getName());
		employeeViewHolder.status.setText(e.getStatus());
		employeeViewHolder.updateWageInput.setText(String.format(Locale.getDefault(), "%.2f",
				e.getWage()));
		employeeViewHolder.departmentInput.setText("Loading...");


///This calculates the worked hours of the employee and then sets it the their employee card
		Shift.getShifts().addOnCompleteListener((@NonNull Task<QuerySnapshot> task) ->
		{
			int hours = 0;
			////Iterate through all shifts
			for (DocumentSnapshot shift : task.getResult())
			{
				List<DocumentReference> emps = (List<DocumentReference>) shift.get("employees");

				///check if shift has occured yet
				Date today = new Date();
				Timestamp t = shift.getTimestamp("endTime");
				Date shiftEndDate = t.toDate();

				long dateCheck = today.getTime() - shiftEndDate.getTime();



				///check if employee is included in the shift and it has occured
				if(emps.contains(employee) && dateCheck > 0)
				{
					////Calculate hours worked
					Timestamp start = shift.getTimestamp("startTime");
					Timestamp end = shift.getTimestamp("endTime");

					Date startDate = start.toDate();
					Date endDate = end.toDate();

					long diff = endDate.getTime() - startDate.getTime();

					long diffMinutes = diff / (60 * 1000) % 60;
					long diffHours = diff / (60 * 60 * 1000) % 24;
					long diffDays = diff / (24 * 60 * 60 * 1000);

					hours = (int) diffMinutes / 60;
					hours += diffHours;
					hours += diffDays * 24;
				}
			}

			employeeViewHolder.workedHours.setText("Worked Hours: " + hours);

		});

		// if the employee is assigned to an apartment, fetch department name from the database
		if (e.getDepartment() != null)
		{
			e.getDepartment().get().addOnCompleteListener(t -> {
				if (t.isSuccessful() && t.getResult() != null)
				{
					Department d = new Department(t.getResult());
					employeeViewHolder.departmentInput.setText(d.getName());
				}
			});
		}
		// else set as unassigned
		else
			employeeViewHolder.departmentInput.setText("Unassigned");

		// updates the wage of the selected employee
		employeeViewHolder.updateWageBtn.setOnClickListener(v -> {
			double newWage =
					Double.parseDouble(employeeViewHolder.updateWageInput.getText().toString());
			if (newWage < 0)
			{
				Toast.makeText(v.getContext(), "Wage cannot be less than 0!", Toast.LENGTH_SHORT).show();
				return;
			}

			e.setWage(Double.parseDouble(employeeViewHolder.updateWageInput.getText().toString()))
					.addOnCompleteListener(t -> {
						if (t.isSuccessful())
							Toast.makeText(v.getContext(), "Successfully updated wage",
									Toast.LENGTH_SHORT).show();
					});
		});

		// updates the department of the selected employee
		employeeViewHolder.updateDepartmentBtn.setOnClickListener(v -> {
			// get name of the department
			String depName = employeeViewHolder.departmentInput.getText().toString();
			Department.getDepartmentsByName(depName).addOnCompleteListener(t -> {
				// check if the department exists
				if (t.isSuccessful() && t.getResult() != null && t.getResult().size() >= 1)
				{
					// get the department, then set the employees department
					DocumentSnapshot ds = t.getResult().getDocuments().get(0);
					e.setDepartment(ds.getReference())
						.addOnCompleteListener(update -> {
							if (update.isSuccessful())
								Toast.makeText(v.getContext(), "Success!", Toast.LENGTH_SHORT).show();
							else
								Toast.makeText(v.getContext(), "Failed!", Toast.LENGTH_SHORT).show();
						});
				}
				else
					Toast.makeText(v.getContext(), "That department could not be found",
							Toast.LENGTH_SHORT).show();
			});
		});
	}

	@Override
	public int getItemCount()
	{
		return employees.size();
	}

	public class EmployeeViewHolder extends RecyclerView.ViewHolder
	{
		TextView name;
		TextView status;
		TextView departmentInput;
		TextView updateWageInput;
		Button updateWageBtn;
		Button updateDepartmentBtn;
		TextView workedHours;

		public EmployeeViewHolder(@NonNull View itemView)
		{
			super(itemView);

			this.name = itemView.findViewById(R.id.employee_name);
			this.status = itemView.findViewById(R.id.employee_status);
			this.updateWageInput = itemView.findViewById(R.id.update_wage_input);
			this.updateWageBtn = itemView.findViewById(R.id.update_wage_btn);
			this.departmentInput = itemView.findViewById(R.id.employee_department_input);
			this.updateDepartmentBtn = itemView.findViewById(R.id.update_department_btn);
			this.workedHours = itemView.findViewById(R.id.workedHoursTextView);
		}
	}


}
