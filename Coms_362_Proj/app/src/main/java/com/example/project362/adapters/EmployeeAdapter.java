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
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
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

		employeeViewHolder.name.setText(e.getName());
		employeeViewHolder.status.setText(e.getStatus());
		employeeViewHolder.updateWageInput.setText(String.format(Locale.getDefault(), "%.2f",
				e.getWage()));
		employeeViewHolder.departmentInput.setText("Loading...");

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
		else
			employeeViewHolder.departmentInput.setText("Unassigned");

		employeeViewHolder.updateWageBtn.setOnClickListener(v ->
			e.setWage(Double.parseDouble(employeeViewHolder.updateWageInput.getText().toString()))
				.addOnCompleteListener(t -> {
					if (t.isSuccessful())
						Toast.makeText(v.getContext(), "Successfully updated wage",
								Toast.LENGTH_SHORT).show();
				}));

		employeeViewHolder.updateDepartmentBtn.setOnClickListener(v -> {
			String depName = employeeViewHolder.departmentInput.getText().toString();
			Department.getDepartmentsByName(depName).addOnCompleteListener(t -> {
				if (t.isSuccessful() && t.getResult() != null && t.getResult().size() >= 1)
				{
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

		public EmployeeViewHolder(@NonNull View itemView)
		{
			super(itemView);

			this.name = itemView.findViewById(R.id.employee_name);
			this.status = itemView.findViewById(R.id.employee_status);
			this.updateWageInput = itemView.findViewById(R.id.update_wage_input);
			this.updateWageBtn = itemView.findViewById(R.id.update_wage_btn);
			this.departmentInput = itemView.findViewById(R.id.employee_department_input);
			this.updateDepartmentBtn = itemView.findViewById(R.id.update_department_btn);
		}
	}
}
