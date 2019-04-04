package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.project362.R;

import com.example.project362.models.Payment;

public class HomeActivity extends AppCompatActivity
{
	private Button allShiftsButton;
	private Button pendingReceivedSwapRequestsButton;

	private Button paymentsButton;

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

		// go to view all shifts
		this.allShiftsButton.setOnClickListener((View v) -> {
			Intent i = new Intent(this, ViewAllShiftsActivity.class);
			startActivity(i);
		});

		// go to received pending swap requests
		this.pendingReceivedSwapRequestsButton.setOnClickListener((View v) -> {
			Intent i = new Intent(this, ViewPendingRequestsToUserActivity.class);
			startActivity(i);
		});

		////Generate Payment for current user's worked shifts
		this.paymentsButton.setOnClickListener((View v) -> {
			Payment.calculatePayments();
			Toast.makeText(HomeActivity.this, "Payment Created Successfully!", Toast.LENGTH_SHORT).show();
		});
	}
}
