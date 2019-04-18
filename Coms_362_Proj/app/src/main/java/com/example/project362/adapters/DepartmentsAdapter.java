package com.example.project362.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.models.Department;
import com.example.project362.models.Employee;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.DepartmentViewHolder>
{
	private ArrayList<Department> departmentsList;

	public DepartmentsAdapter(ArrayList<Department> departments)
	{
		departmentsList = departments;
	}

	@NonNull
	@Override
	public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
	{
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.department_card,
				viewGroup, false);
		return new DepartmentViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull final DepartmentViewHolder viewHolder, int i)
	{
		Department d = departmentsList.get(i);

		viewHolder.name.setText(d.getName());
		d.getEmployees().addOnCompleteListener(t -> {
			if (t.isSuccessful() && t.getResult() != null)
			{
				ArrayList<String> employees = new ArrayList<>();
				for (DocumentSnapshot docSnap : t.getResult().getDocuments())
					employees.add(new Employee(docSnap).toString());
				ArrayAdapter<String> adapter = new ArrayAdapter<>(
						viewHolder.v,
						android.R.layout.simple_list_item_1,
						employees);
				viewHolder.list.setAdapter(adapter);
			}
			else
				Toast.makeText(viewHolder.v, "Something went wrong!", Toast.LENGTH_SHORT).show();
		});
	}

	@Override
	public int getItemCount()
	{
		return departmentsList.size();
	}

	static class DepartmentViewHolder extends RecyclerView.ViewHolder
	{
		TextView name;
		ListView list;
		Context v;
		DepartmentViewHolder(@NonNull View itemView)
		{
			super(itemView);
			name = itemView.findViewById(R.id.department_name);
			list = itemView.findViewById(R.id.department_employees_list);
			v = itemView.getContext();
		}
	}
}
