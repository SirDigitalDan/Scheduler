package com.example.project362.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.models.Employee;
import com.example.project362.models.Shift;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class ShiftsAdapter extends RecyclerView.Adapter<ShiftsAdapter.ShiftsViewHolder>
{
	private ArrayList<Shift> shiftList = new ArrayList<>();
	private DocumentReference currentUser;
	private FirebaseFirestore db = FirebaseFirestore.getInstance();

	private static final String TAG = "com-s-362-project";

	public static class ShiftsViewHolder extends RecyclerView.ViewHolder
	{
		private final TextView title;
		private final TextView info;
		private final TextView note;
		private final EditText noteAdd;
		private final Button noteButton;
		private final TextView employees;
		private final Button pickUpShiftButton;
		private final Button dropShiftButton;


		public ShiftsViewHolder(@NonNull View itemView)
		{
			super(itemView);
			title = itemView.findViewById(R.id.shiftTitle);
			info = itemView.findViewById(R.id.shiftInfo);

			employees = itemView.findViewById(R.id.shiftEmployees);
			pickUpShiftButton = itemView.findViewById(R.id.pickUpShiftButton);
			dropShiftButton = itemView.findViewById(R.id.dropShiftButton);

			noteAdd = itemView.findViewById(R.id.editTextShiftNote);
			note = itemView.findViewById(R.id.shiftNote);
			noteButton = itemView.findViewById(R.id.noteButton);
		}
	}

	public ShiftsAdapter(ArrayList<Shift> shifts)
	{
		shiftList = shifts;

		currentUser =
				db.collection("Employees").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
	}

	@NonNull
	@Override
	public ShiftsAdapter.ShiftsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
	{
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shift_card,
                viewGroup, false);
		ShiftsViewHolder svh = new ShiftsViewHolder(v);
		return svh;
	}

	@Override
	public void onBindViewHolder(@NonNull final ShiftsViewHolder shiftsViewHolder, int i)
	{
		final Shift currentShift = shiftList.get(i);
		shiftsViewHolder.title.setText("Shift ID: " + currentShift.getId());
		shiftsViewHolder.info.setText("Description: This shift starts on " + currentShift.getStartTime() + " and ends on "
				+ currentShift.getEndTime() + ". ");

		shiftsViewHolder.employees.setText(this.formatEmployees(currentShift.getEmployees()));
		shiftsViewHolder.note.setText(currentShift.getNote());

		shiftsViewHolder.noteButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				// Code here executes on main thread after user presses button
				String text = shiftsViewHolder.noteAdd.getText().toString();
				String n = shiftsViewHolder.note.getText().toString();
				String note = n + "\n" + text;

				currentShift.setNote(note).addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task)
					{
						if (task.isSuccessful())
							shiftsViewHolder.note.setText(currentShift.getNote());
					}
				});

				shiftsViewHolder.noteAdd.setText("");
			}
		});

		shiftsViewHolder.dropShiftButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				currentShift.removeEmployee(currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task)
					{
						if (task.isSuccessful())
						{
							shiftsViewHolder.employees.setText(ShiftsAdapter.this.formatEmployees(currentShift.getEmployees()));
							Toast.makeText(v.getContext(), "Shift drop successful!",
									Toast.LENGTH_SHORT).show();
						}
						else
						{
							if (task.getException() != null)
								Log.e(TAG, task.getException().toString());
							Toast.makeText(v.getContext(), "Something went wrong!",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});

		shiftsViewHolder.pickUpShiftButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				currentShift.addEmployee(currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task)
					{
						if (task.isSuccessful())
						{
							shiftsViewHolder.employees.setText(ShiftsAdapter.this.formatEmployees(currentShift.getEmployees()));
							Toast.makeText(v.getContext(), "Shift pick up successful!",
									Toast.LENGTH_SHORT).show();
						}
						else
						{
							if (task.getException() != null)
								Log.e(TAG, task.getException().toString());
							Toast.makeText(v.getContext(), "Something went wrong!",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
	}

	public String formatEmployees(ArrayList<DocumentReference> employees)
	{
		StringBuilder employeesSb = new StringBuilder();
		for (DocumentReference ref : employees)
			employeesSb.append(ref.getId()).append("\n");
		return employeesSb.toString();
	}

	@Override
	public int getItemCount()
	{
		return shiftList.size();
	}
}
