package com.example.project362.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.adapters.PendingShiftSwapsAdapter;
import com.example.project362.models.SwapRequest;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewPendingRequestsToUserActivity extends AppCompatActivity
{
	private RecyclerView recyclerView;
	private RecyclerView.LayoutManager layoutManager;
	private ArrayList<SwapRequest> swapRequests = new ArrayList<>();

	@Override
	public void onCreate(Bundle savedInstanceBundle)
	{
		super.onCreate(savedInstanceBundle);
		setContentView(R.layout.activity_view_pending_requests_to_user);

		FirebaseAuth auth = FirebaseAuth.getInstance();
		final String currentEmail;

		if (auth.getCurrentUser() != null) currentEmail = auth.getCurrentUser().getEmail();
		else
		{
			Toast.makeText(ViewPendingRequestsToUserActivity.this, "Intruder alert! The " +
							"robots will arrive shortly to remove you... permanently.",
					Toast.LENGTH_SHORT).show();
			return;
		}

		recyclerView = findViewById(R.id.pendingSwapRequestsList);
		recyclerView.setHasFixedSize(true);


		layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);

		// get the swap requests
		SwapRequest.getSwapRequests().addOnCompleteListener((Task<QuerySnapshot> t) -> {
			if (t.isSuccessful() && t.getResult() != null)
			{
				// add all pending swap requests to the list of swap requests for the current user
				for (DocumentSnapshot ds : t.getResult().getDocuments())
				{
					// convert DocumentSnapshot to swap request
					SwapRequest request = new SwapRequest(ds);
					// if the swap request is to the current user and hasn't been accepted or
					// rejected, add to the list
					if (request.getTo().getId().equals(currentEmail) && request.getStatus() ==
							(SwapRequest.Status.PENDING.getValue()))
						swapRequests.add(request);
				}

				recyclerView.setAdapter(new PendingShiftSwapsAdapter(swapRequests));
			}
			else
				Toast.makeText(ViewPendingRequestsToUserActivity.this, "Could not load swap " +
						"requests", Toast.LENGTH_SHORT).show();
		});
	}
}
