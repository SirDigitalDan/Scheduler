package com.example.project362.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.models.Employee;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;


public class EditInfoActivity extends AppCompatActivity
{

	private static final String TAG = "com-s-362-project";

	private FirebaseAuth mAuth;

	EditText editPassword, editName, editVerifyPassword, userEmail;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//get content
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_info);

		//get the instances of the employee user authroization
		mAuth = FirebaseAuth.getInstance();

		//get all of the views
		editName = findViewById(R.id.editName);
		editPassword = findViewById(R.id.editPass);
		editVerifyPassword = findViewById(R.id.editPass2);

		findViewById(R.id.buttonSub).setOnClickListener((View view) -> {
			updateUser();
			Intent intent = new Intent(EditInfoActivity.this, MainActivity.class);
			startActivity(intent);
		});

		findViewById(R.id.buttonSignout).setOnClickListener((View view) -> {
			FirebaseAuth.getInstance().signOut();
			Toast.makeText(EditInfoActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(EditInfoActivity.this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		});
	}

	private void updateUser()
	{
		String name = editName.getText().toString().trim();
		final String password = editPassword.getText().toString().trim();
		String vPassword = editVerifyPassword.getText().toString().trim();

		String id = mAuth.getCurrentUser().getEmail();

		if (!password.isEmpty() || !vPassword.isEmpty())
		{
			if ((!password.isEmpty() && vPassword.isEmpty()))
			{
				editVerifyPassword.setError("Please confirm your password");
				editVerifyPassword.requestFocus();
				return;
			}
			if (password.isEmpty() && !vPassword.isEmpty())
			{
				editPassword.setError("This field is required");
				editPassword.requestFocus();
				return;
			}
			if (!vPassword.equals(password))
			{
				editVerifyPassword.setError("Your passwords do not match");
				editVerifyPassword.requestFocus();
				return;
			}
			if (password.length() < 6)
			{
				editPassword.setError("Password is too short");
				editPassword.requestFocus();
				return;
			}

			this.updateAuthPassword(password);
		}

		this.updateEmployee(id, name);
	}

	public Task<Void> updateAuthPassword(String password)
	{
		return mAuth.getCurrentUser().updatePassword(password).addOnFailureListener((Exception e) ->
				Toast.makeText(EditInfoActivity.this, "edit password failed", Toast.LENGTH_SHORT).show());
	}

	public Task<DocumentSnapshot> updateEmployee(String id, String name)
	{
		Log.d(TAG, id);
		final HashMap<String, Object> data = new HashMap<>();

		if (name != null && !name.isEmpty())
			data.put(Employee.NAME, name);

		return Employee.getEmployeeByEmail(id).addOnCompleteListener((Task<DocumentSnapshot> t) -> {
			if (t.isSuccessful())
			{
				Employee e = new Employee(t.getResult());
				e.update(data).addOnFailureListener((Exception exception) -> {
					Toast.makeText(EditInfoActivity.this, "edit user info failed",
							Toast.LENGTH_SHORT).show();
					Log.d(TAG, exception.toString());
				});
			}
			else
				Toast.makeText(EditInfoActivity.this, "edit user info failed", Toast.LENGTH_SHORT).show();
		});
	}
}