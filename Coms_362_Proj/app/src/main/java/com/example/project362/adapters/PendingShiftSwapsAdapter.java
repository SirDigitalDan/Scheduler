package com.example.project362.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.project362.R;
import com.example.project362.models.Shift;
import com.example.project362.models.SwapRequest;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;


public class PendingShiftSwapsAdapter extends RecyclerView.Adapter<PendingShiftSwapsAdapter.SwapViewHolder>
{
	ArrayList<SwapRequest> swapList;

	public PendingShiftSwapsAdapter(ArrayList<SwapRequest> swapList)
	{
		this.swapList = swapList;
	}

	@NonNull
	@Override
	public SwapViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
	{
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.swap_card, viewGroup,
				false);
		return new SwapViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull SwapViewHolder swapViewHolder, int i)
	{
		final SwapRequest swap = this.swapList.get(i);

		swap.getShift().get().addOnCompleteListener((Task<DocumentSnapshot> t) -> {
			if (t.isSuccessful())
			{
				Shift s = new Shift(t.getResult());
				swapViewHolder.shiftDesc.setText("Shift ID: " + s.getId() + "\nTimes: " + s.getStartTime() + " - " + s.getEndTime());
			}
		});

		swapViewHolder.acceptButton.setOnClickListener((View v) ->
			swap.accept().addOnCompleteListener((Task<DocumentSnapshot> t) -> this.removeSwapRequest(swap)));

		swapViewHolder.rejectButton.setOnClickListener((View v) ->
			swap.reject().addOnCompleteListener((Task<Void> t) -> this.removeSwapRequest(swap)));
	}

	private void removeSwapRequest(SwapRequest req)
	{
		this.swapList.remove(req);
		this.notifyDataSetChanged();
	}

	@Override
	public int getItemCount()
	{
		return swapList.size();
	}

	static class SwapViewHolder extends RecyclerView.ViewHolder
	{
		private TextView shiftDesc;
		private Button acceptButton;
		private Button rejectButton;

		public SwapViewHolder(@NonNull View itemView)
		{
			super(itemView);
			this.shiftDesc = itemView.findViewById(R.id.shiftDescription);
			this.acceptButton = itemView.findViewById(R.id.acceptButton);
			this.rejectButton = itemView.findViewById(R.id.rejectButton);
		}
	}
}
