package com.example.project362.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.activities.ViewAllShiftsActivity;
import com.example.project362.models.Employee;
import com.example.project362.models.Shift;

import com.example.project362.models.SwapRequest;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShiftsAdapter extends RecyclerView.Adapter<ShiftsAdapter.ShiftsViewHolder>
{
	private ArrayList<Shift> shiftList;
	private DocumentReference currentUser;
	private FirebaseFirestore db = FirebaseFirestore.getInstance();

	private static final String TAG = "com-s-362-project";

	static class ShiftsViewHolder extends RecyclerView.ViewHolder
	{
		private final TextView title;
		private final TextView info;
		private final TextView note;
		private final TextView swapWith;
		private final EditText noteAdd;
		private final Button noteButton;
		private final TextView employees;
		private final Button pickUpShiftButton;
		private final Button dropShiftButton;
		private final Button swapButton;
		private final Button clockInButton;

		private final TextView lockStatus2;

		ShiftsViewHolder(@NonNull View itemView)
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

			swapWith = itemView.findViewById(R.id.swapWith);
			swapButton = itemView.findViewById(R.id.swapButton);

			lockStatus2 = itemView.findViewById(R.id.lockStatus2);

			clockInButton = itemView.findViewById(R.id.clockInButton);
		}
	}

	public ShiftsAdapter(ArrayList<Shift> shifts)
	{
		shiftList = shifts;
		currentUser =
				Employee.getEmployeeReferenceByKey(FirebaseAuth.getInstance().getCurrentUser().getEmail());
	}

	@NonNull
	@Override
	public ShiftsAdapter.ShiftsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
	{
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shift_card,
				viewGroup, false);
		return new ShiftsViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull final ShiftsViewHolder shiftsViewHolder, int i)
	{
		final Shift currentShift = shiftList.get(i);
		shiftsViewHolder.title.setText("Shift ID: " + currentShift.getId());
		shiftsViewHolder.info
				.setText("Start Time: " + currentShift.getStartTime() + "\nEnd Time:" + currentShift.getEndTime());

		shiftsViewHolder.employees.setText(this.formatEmployees(currentShift.getEmployees()));
		shiftsViewHolder.note.setText(currentShift.getNote());

		if(currentShift.getStatus() == 1) {
			shiftsViewHolder.lockStatus2.setText("UNLOCKED");
		} else if(currentShift.getStatus() == 0) {
			shiftsViewHolder.lockStatus2.setText("LOCKED");
		}

		shiftsViewHolder.noteButton.setOnClickListener((View v) ->
		{
			// Code here executes on main thread after user presses button
			String text = shiftsViewHolder.noteAdd.getText().toString();
			String n = shiftsViewHolder.note.getText().toString();
			String note = n + "\n" + text;

			currentShift.setNote(note).addOnCompleteListener((Task<Void> task) -> {
				if (task.isSuccessful())
					shiftsViewHolder.note.setText(currentShift.getNote());
			});

			shiftsViewHolder.noteAdd.setText("");
		});


		/// Allows the employee to drop the shift if they are included
		shiftsViewHolder.dropShiftButton.setOnClickListener((final View v) ->
		{
			currentShift.removeEmployee(currentUser).addOnCompleteListener((Task<Void> task) ->
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
			});
		});


		/// Allows the employee to pick up the shift if they have not already
		shiftsViewHolder.pickUpShiftButton.setOnClickListener((final View v) ->
			currentShift.addEmployee(currentUser).addOnCompleteListener((Task<Void> task) ->
			{
				if (task.isSuccessful())
				{
					shiftsViewHolder.employees.setText(ShiftsAdapter.this.formatEmployees(currentShift.getEmployees()));
					Toast.makeText(v.getContext(), "Shift pick up successful!",
							Toast.LENGTH_SHORT).show();
				}
				else
				{
					if (task.getException() != null) {
						Toast.makeText(v.getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(v.getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
					}
				}
			}));

		/// Allows the employee to request a swap of a shift
		shiftsViewHolder.swapButton.setOnClickListener((final View v) -> {
			FirebaseAuth auth = FirebaseAuth.getInstance();

			if (auth.getCurrentUser() == null) return;

			DocumentReference from =
					Employee.getEmployeeReferenceByKey(auth.getCurrentUser().getEmail());
			DocumentReference to =
					Employee.getEmployeeReferenceByKey(shiftsViewHolder.swapWith.getText().toString());

			DocumentReference s = currentShift.getReference();

			if (!SwapRequest.isValid(currentShift, from, to))
				Toast.makeText(v.getContext(), "That swap request is invalid",
						Toast.LENGTH_SHORT).show();

			SwapRequest request = new SwapRequest(s, from, to);
			request.create();
		});

		/*
			By clicking the clock in button, the system checks to verfiy that the current user is
			included in the shift, and then checks them in as "attended" to that shift
		 */
		shiftsViewHolder.clockInButton.setOnClickListener((View v) ->
		{
			FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser(); //get current user
			String key = currentUser.getEmail();
			DocumentReference ref = Employee.getEmployeeReferenceByKey(key);  //get employee reference

			currentShift.checkInEmployee(ref).addOnCompleteListener((Task<Void> task) ->
			{
				if (task.isSuccessful()){

				}
				else
				{
					if (task.getException() != null)

						throw new Error("Operation unsuccessful");
				}
			});

		});
	}

	//This method will format the list specified so that it displays vertically
	private String formatEmployees(ArrayList<DocumentReference> employees)
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
