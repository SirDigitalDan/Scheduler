package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.project362.R;

public class HomeActivity extends AppCompatActivity
{
	private Button allShiftsButton;

	@Override
	public void onCreate(Bundle savedInstanceBundle)
	{
		super.onCreate(savedInstanceBundle);
		setContentView(R.layout.activity_home);

		allShiftsButton = findViewById(R.id.allShiftsButton);
	}

	public void onStart()
	{
		super.onStart();
		this.allShiftsButton.setOnClickListener((View v) -> {
			Intent i = new Intent(this, ViewAllShiftsActivity.class);
			startActivity(i);
		});



		// ADD LISTENERS HERE
	}
}
