package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.models.Shift;

import java.util.Date;

public class CreateShiftActivity extends AppCompatActivity
{
	TextView shiftNameInput;
	TextView shiftStartInput;
	TextView shiftEndInput;
	Button makeShiftBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		shiftNameInput = findViewById(R.id.shift_name_input);
		shiftStartInput = findViewById(R.id.shift_start_input);
		shiftEndInput = findViewById(R.id.shift_end_input);

		makeShiftBtn = findViewById(R.id.make_shift_btn);

		makeShiftBtn.setOnClickListener(v -> {
			String name = shiftNameInput.getText().toString();
			Date start = new Date(shiftStartInput.getText().toString());
			Date end = new Date(shiftEndInput.getText().toString());

			Shift s = new Shift(name, start, end);
			s.create().addOnCompleteListener(t -> {
				if (t.isSuccessful())
				{
					Toast.makeText(CreateShiftActivity.this, "Shift created!",
							Toast.LENGTH_SHORT).show();
					Intent i = new Intent(this, AdminHomeActivity.class);
					startActivity(i);
				}
				else
					Toast.makeText(CreateShiftActivity.this, "Something went wrong!",
							Toast.LENGTH_SHORT).show();
			});
		});
	}
}
