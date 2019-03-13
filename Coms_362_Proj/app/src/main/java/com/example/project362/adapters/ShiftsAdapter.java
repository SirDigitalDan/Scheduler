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

import com.example.project362.R;
import com.example.project362.models.Shift;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class ShiftsAdapter extends RecyclerView.Adapter<ShiftsAdapter.ShiftsViewHolder> {


    private ArrayList<Shift> shiftList = new ArrayList<Shift>();


    public static class ShiftsViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView info;
        public TextView note;
        public EditText noteAdd;
        public Button noteButton;


        public ShiftsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.shiftTitle);
            info = itemView.findViewById(R.id.shiftInfo);
            noteAdd = itemView.findViewById(R.id.editTextShiftNote);
            note = itemView.findViewById(R.id.shiftNote);
            noteButton = itemView.findViewById(R.id.noteButton);

        }
    }

    public ShiftsAdapter(ArrayList<Shift> shifts){

        shiftList = shifts;
    }

    @NonNull
    @Override
    public ShiftsAdapter.ShiftsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shift_card, viewGroup, false );
        ShiftsViewHolder svh = new ShiftsViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ShiftsViewHolder shiftsViewHolder, int i) {
        Shift currentShift = shiftList.get(i);
        shiftsViewHolder.title.setText("Shift ID: " + currentShift.getId());
        shiftsViewHolder.info.setText("Description: This shift starts on " + currentShift.getStartTime() + " and ends on "
        + currentShift.getEndTime() + ". ");
        shiftsViewHolder.note.setText(currentShift.getNote());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference ref = db.collection("Shifts").document(currentShift.getId());

        shiftsViewHolder.noteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String text = shiftsViewHolder.noteAdd.getText().toString();
                String n = shiftsViewHolder.note.getText().toString();
                String string = n + "\n" + text;

                ref.update("note", string);

                shiftsViewHolder.noteAdd.setText("");

            }
        });




        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {

                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    shiftsViewHolder.note.setText(snapshot.getString("note"));
                } else {

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return shiftList.size();
    }
}
