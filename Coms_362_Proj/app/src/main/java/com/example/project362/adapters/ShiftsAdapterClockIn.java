package com.example.project362.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.project362.R;
import com.example.project362.models.Employee;
import com.example.project362.models.Shift;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ShiftsAdapterClockIn extends RecyclerView.Adapter<ShiftsAdapterClockIn.ShiftsViewHolder>  implements View.OnClickListener {


    private ArrayList<Shift> shiftList;
    private DocumentReference currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "ShiftAdapterClockIn";

    static class ShiftsViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView info;
        private final TextView employees;
        private final Button clock;





        ShiftsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.shiftTitle);
            info = itemView.findViewById(R.id.shiftInfo);

            employees = itemView.findViewById(R.id.shiftEmployees);

            clock =  itemView.findViewById(R.id.checkIn);


        }
    }

    public ShiftsAdapterClockIn(ArrayList<Shift> shifts)
    {
        shiftList = shifts;
        currentUser = db.collection("Employees").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }



    @NonNull
    @Override
    public ShiftsAdapterClockIn.ShiftsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shift_card_clockin,
                viewGroup, false);
        ShiftsAdapterClockIn.ShiftsViewHolder svh = new ShiftsAdapterClockIn.ShiftsViewHolder(v);
        return svh;
    }

   @Override
    public void onBindViewHolder(@NonNull final ShiftsAdapterClockIn.ShiftsViewHolder shiftsViewHolder, int i) {
        Employee e;
        final Shift currentShift = shiftList.get(i);
        shiftsViewHolder.title.setText("Shift ID: " + currentShift.getId());
        shiftsViewHolder.info.setText("Description: This shift starts on " + currentShift.getStartTime() + " and ends on "
                + currentShift.getEndTime() + ". ");

        shiftsViewHolder.employees.setText(this.formatEmployees(currentShift.getEmployees()));

       shiftsViewHolder.clock.setOnClickListener((View v) ->
       {
           FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
           DocumentReference emp = Employee.getEmployeeReferenceByKey(user.getEmail());

           //FirebaseFirestore db = FirebaseFirestore.getInstance();



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

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.checkIn:



            }

        }

}
