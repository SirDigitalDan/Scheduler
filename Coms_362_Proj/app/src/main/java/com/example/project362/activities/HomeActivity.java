package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.project362.R;
import com.example.project362.adapters.PendingShiftSwapsAdapter;
import com.example.project362.models.Employee;
import com.example.project362.models.Payment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

public class HomeActivity extends AppCompatActivity
{
	public final String TAG = "HomeActivity";
	private Button allShiftsButton;
	private Button pendingReceivedSwapRequestsButton;
	private Button paymentsButton;
	private Payment payment;


	@Override
	public void onCreate(Bundle savedInstanceBundle)
	{
		super.onCreate(savedInstanceBundle);
		setContentView(R.layout.activity_home);

		allShiftsButton = findViewById(R.id.allShiftsButton);
		pendingReceivedSwapRequestsButton = findViewById(R.id.pendingReceivedSwapRequestsButton);
		paymentsButton = findViewById(R.id.paymentsButton);
	}

	public void onStart()
	{
		super.onStart();
		this.allShiftsButton.setOnClickListener((View v) -> {
			Intent i = new Intent(this, ViewAllShiftsActivity.class);
			startActivity(i);
		});

		this.pendingReceivedSwapRequestsButton.setOnClickListener((View v) -> {
			Intent i = new Intent(this, ViewPendingRequestsToUserActivity.class);
			startActivity(i);
		});

		this.paymentsButton.setOnClickListener((View v) -> {
			FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
			DocumentReference employee = Employee.getEmployeeReferenceByKey(user.getEmail());

			Payment.calculatePayments();

			Toast.makeText(HomeActivity.this, "Payment Created Successfully!", Toast.LENGTH_SHORT).show();


		});


		// ADD LISTENERS HERE
	}


}
