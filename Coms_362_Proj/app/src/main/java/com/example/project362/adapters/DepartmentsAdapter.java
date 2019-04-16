package com.example.project362.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.project362.R;
import com.example.project362.models.Department;

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
	}

	@Override
	public int getItemCount()
	{
		return departmentsList.size();
	}

	static class DepartmentViewHolder extends RecyclerView.ViewHolder
	{
		TextView name;
		DepartmentViewHolder(@NonNull View itemView)
		{
			super(itemView);
			name = itemView.findViewById(R.id.department_name);
		}
	}
}
