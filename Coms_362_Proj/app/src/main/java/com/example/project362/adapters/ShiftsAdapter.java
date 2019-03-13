package com.example.project362.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;

import com.example.project362.R;
import com.example.project362.models.Shift;

import java.util.ArrayList;

public class ShiftsAdapter extends RecyclerView.Adapter<ShiftsAdapter.ShiftsViewHolder> {


    private ArrayList<Shift> shiftList = new ArrayList<Shift>();


    public static class ShiftsViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView info;
        public EditText note;


        public ShiftsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.shiftTitle);
            info = itemView.findViewById(R.id.shiftInfo);
            note = itemView.findViewById(R.id.editTextShiftNote);
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
    public void onBindViewHolder(@NonNull ShiftsViewHolder shiftsViewHolder, int i) {
        Shift currentShift = shiftList.get(i);
        shiftsViewHolder.title.setText("Shift ID: " + currentShift.getId());
        shiftsViewHolder.info.setText("Description: This shift starts on " + currentShift.getStartTime() + " and ends on "
        + currentShift.getEndTime() + ". ");

    }

    @Override
    public int getItemCount() {
        return shiftList.size();
    }
}
