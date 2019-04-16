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
import com.example.project362.models.Employee;

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

		employeeViewHolder.updateWageBtn.setOnClickListener(v ->
			e.setWage(Double.parseDouble(employeeViewHolder.updateWageInput.getText().toString()))
				.addOnCompleteListener(t -> {
					if (t.isSuccessful())
						Toast.makeText(v.getContext(), "Successfully updated wage",
								Toast.LENGTH_SHORT).show();
				}));
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
		TextView department;
		TextView updateWageInput;
		Button updateWageBtn;

		public EmployeeViewHolder(@NonNull View itemView)
		{
			super(itemView);

			this.name = itemView.findViewById(R.id.employee_name);
			this.status = itemView.findViewById(R.id.employee_status);
			this.updateWageInput = itemView.findViewById(R.id.update_wage_input);
			this.updateWageBtn = itemView.findViewById(R.id.update_wage_btn);
			this.department = itemView.findViewById(R.id.department_name_input);
		}
	}
}
