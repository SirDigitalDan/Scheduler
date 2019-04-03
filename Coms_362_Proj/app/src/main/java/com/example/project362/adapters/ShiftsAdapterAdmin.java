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
import com.example.project362.models.Employee;
import com.example.project362.models.Shift;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ShiftsAdapterAdmin extends RecyclerView.Adapter<ShiftsAdapterAdmin.ShiftsViewHolder> {

    private ArrayList<Shift> shiftList;
    private DocumentReference currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "ShiftsAdapterAdmin";

    static class ShiftsViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView info;
        private final TextView note;
        private final EditText noteAdd;
        private final Button noteButton;
        private final TextView employees;
        private final Button pickUpShiftButton;
        private final Button dropShiftButton;

        private final Button deleteShift;
        private final Button addEmployee;
        private final TextView addEmployeeText;

        private final Button removeEmployee;
        private final Button lockShift;
        private final TextView lockStatus;

        ShiftsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.shiftTitle);
            info = itemView.findViewById(R.id.shiftInfo);

            employees = itemView.findViewById(R.id.shiftEmployees);
            pickUpShiftButton = itemView.findViewById(R.id.pickUpShiftButton);
            dropShiftButton = itemView.findViewById(R.id.dropShiftButton);

            noteAdd = itemView.findViewById(R.id.editTextShiftNote);
            note = itemView.findViewById(R.id.shiftNote);
            noteButton = itemView.findViewById(R.id.noteButton);

            deleteShift = itemView.findViewById(R.id.deleteShift);
            addEmployee = itemView.findViewById(R.id.addEmployee);
            addEmployeeText = itemView.findViewById(R.id.addEmployeeText);

            removeEmployee = itemView.findViewById(R.id.removeEmployee);
            lockShift = itemView.findViewById(R.id.lockShift);
            lockStatus = itemView.findViewById(R.id.lockStatus);
        }
    }

    public ShiftsAdapterAdmin(ArrayList<Shift> shifts)
    {
        shiftList = shifts;
        currentUser = db.collection("Employees").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    @NonNull
    @Override
    public ShiftsAdapterAdmin.ShiftsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shift_card_admin,
                viewGroup, false);
        ShiftsAdapterAdmin.ShiftsViewHolder svh = new ShiftsAdapterAdmin.ShiftsViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ShiftsAdapterAdmin.ShiftsViewHolder shiftsViewHolder, int i) {
        Employee e;
        final Shift currentShift = shiftList.get(i);
        shiftsViewHolder.title.setText("Shift ID: " + currentShift.getId());
        shiftsViewHolder.info.setText("Description: This shift starts on " + currentShift.getStartTime() + " and ends on "
                + currentShift.getEndTime() + ". ");

        if(currentShift.getStatus() == 1) {
            shiftsViewHolder.lockStatus.setText("UNLOCKED");
        } else if(currentShift.getStatus() == 0) {
            shiftsViewHolder.lockStatus.setText("LOCKED");
        }

        shiftsViewHolder.employees.setText(this.formatEmployees(currentShift.getEmployees()));
        shiftsViewHolder.note.setText(currentShift.getNote());

        shiftsViewHolder.noteButton.setOnClickListener((View v) ->
        {
            // Code here executes on main thread after user presses button
            String text = shiftsViewHolder.noteAdd.getText().toString();
            String n = shiftsViewHolder.note.getText().toString();
            String note = n + "\n" + text;

            currentShift.setNote(note).addOnCompleteListener((Task<Void> task) ->
            {
                if (task.isSuccessful())
                    shiftsViewHolder.note.setText(currentShift.getNote());
            });

            shiftsViewHolder.noteAdd.setText("");
        });

        shiftsViewHolder.dropShiftButton.setOnClickListener((final View v) ->
        {
            currentShift.removeEmployee(currentUser).addOnCompleteListener((Task<Void> task) ->
            {
                if (task.isSuccessful())
                {
                    shiftsViewHolder.employees.setText(ShiftsAdapterAdmin.this.formatEmployees(currentShift.getEmployees()));
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

        shiftsViewHolder.pickUpShiftButton.setOnClickListener((final View v) ->
        {
            currentShift.addEmployee(currentUser).addOnCompleteListener((Task<Void> task) ->
            {
                if (task.isSuccessful())
                {
                    shiftsViewHolder.employees.setText(ShiftsAdapterAdmin.this.formatEmployees(currentShift.getEmployees()));
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
            });
        });

        shiftsViewHolder.addEmployee.setOnClickListener((final View v) -> {
            String addingEmployee = shiftsViewHolder.addEmployeeText.getText().toString();
            DocumentReference ref = Employee.getEmployeeReferenceByKey(addingEmployee);
            // DocumentReference ref = db.collection(Employee.COLLECTION).document(addingEmployee);

            currentShift.addEmployee(ref).addOnCompleteListener((Task<Void> task) ->
            {
                if (task.isSuccessful())
                {
                    shiftsViewHolder.employees.setText(ShiftsAdapterAdmin.this.formatEmployees(currentShift.getEmployees()));
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
            });
        });

        shiftsViewHolder.deleteShift.setOnClickListener((final View v) -> {
            Shift.delete(currentShift.getId()).addOnCompleteListener((Task<Void> task) -> {
                if (task.isSuccessful()) {
                    this.shiftList.remove(currentShift);
                    this.notifyDataSetChanged();
                }
            });
        });

        shiftsViewHolder.removeEmployee.setOnClickListener((final View v) -> {
            String removingEmployee = shiftsViewHolder.addEmployeeText.getText().toString();
            DocumentReference ref = db.collection(Employee.COLLECTION).document(removingEmployee);

            currentShift.removeEmployee(ref).addOnCompleteListener((Task<Void> task) ->
            {
                if (task.isSuccessful())
                {
                    shiftsViewHolder.employees.setText(ShiftsAdapterAdmin.this.formatEmployees(currentShift.getEmployees()));
                    Toast.makeText(v.getContext(), "Employee removal successful!",
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

        shiftsViewHolder.lockShift.setOnClickListener((final View v) -> {
            currentShift.toggleStatus().addOnCompleteListener((Task<Void> task) ->
            {
                if (task.isSuccessful())
                {
                    if(currentShift.getStatus() == 1) {
                        shiftsViewHolder.lockStatus.setText("UNLOCKED");
                        Toast.makeText(v.getContext(), "Toggle lock status successful!",
                                Toast.LENGTH_SHORT).show();
                    } else if(currentShift.getStatus() == 0) {
                        shiftsViewHolder.lockStatus.setText("LOCKED");
                        Toast.makeText(v.getContext(), "Toggle lock status successful!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), "Something went wrong!",
                                Toast.LENGTH_SHORT).show();
                    }
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
    }

    String formatEmployees(ArrayList<DocumentReference> employees)
    {
        StringBuilder employeesSb = new StringBuilder();
        for (DocumentReference ref : employees)
            employeesSb.append(ref.getId()).append("\n");
        return employeesSb.toString();
    }

    @Override
    public int getItemCount() {
        return shiftList.size();
    }
}
