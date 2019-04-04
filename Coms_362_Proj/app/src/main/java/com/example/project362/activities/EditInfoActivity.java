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
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;


public class EditInfoActivity extends AppCompatActivity
{
	private static final String TAG = "com-s-362-project";

	private FirebaseAuth mAuth;

	EditText editPassword, editName, editVerifyPassword;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//get content
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_info);

		// get the instances of the employee user authorization
		mAuth = FirebaseAuth.getInstance();

		editName = findViewById(R.id.editName);
		editPassword = findViewById(R.id.editPass);
		editVerifyPassword = findViewById(R.id.editPass2);

		findViewById(R.id.buttonSub).setOnClickListener((View view) -> {
			updateUser();
			finish();
			// switch to main activity
			Intent intent = new Intent(EditInfoActivity.this, MainActivity.class);
			startActivity(intent);
		});

		findViewById(R.id.profileActivity).setOnClickListener((View view) -> {
			finish();
			// switch to edit profile activity
			Intent intent = new Intent(EditInfoActivity.this, ProfileActivity.class);
			startActivity(intent);
		});

		findViewById(R.id.buttonSignout).setOnClickListener((View view) -> {
			FirebaseAuth.getInstance().signOut();
			Toast.makeText(EditInfoActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
			// switch to main activity
			Intent intent = new Intent(EditInfoActivity.this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		});
	}

	private void updateUser()
	{
		String name = editName.getText().toString().trim();
		final String password = editPassword.getText().toString().trim();
		// verification password (check if they match)
		String vPassword = editVerifyPassword.getText().toString().trim();

		FirebaseUser curUser = mAuth.getCurrentUser();
		if (curUser == null)
			return;

		String id = mAuth.getCurrentUser().getEmail();

		// if one of the password fields is not empty
		if (!password.isEmpty() || !vPassword.isEmpty())
		{
			// if the other password field is empty, then ask user to enter
			if ((!password.isEmpty() && vPassword.isEmpty()))
			{
				editVerifyPassword.setError("Please confirm your password");
				editVerifyPassword.requestFocus();
				return;
			}
			if (password.isEmpty())
			{
				editPassword.setError("This field is required");
				editPassword.requestFocus();
				return;
			}
			// check if passwords are equal
			if (!vPassword.equals(password))
			{
				editVerifyPassword.setError("Your passwords do not match");
				editVerifyPassword.requestFocus();
				return;
			}
			// password length must be >= 6
			if (password.length() < 6)
			{
				editPassword.setError("Password is too short");
				editPassword.requestFocus();
				return;
			}

			// update in the auth database
			this.updateAuthPassword(password);
		}

		// update the employee's name
		this.updateEmployee(id, name);
	}

	public Task<Void> updateAuthPassword(String password)
	{
		FirebaseUser curUser = mAuth.getCurrentUser();
		if (curUser == null) return Tasks.forException(new Exception("an error occurred"));

		// update the password in the auth database
		return mAuth.getCurrentUser().updatePassword(password).addOnFailureListener((Exception e) ->
				Toast.makeText(EditInfoActivity.this, "edit password failed", Toast.LENGTH_SHORT).show());
	}

	public Task<DocumentSnapshot> updateEmployee(String id, String name)
	{
		// get current employee reference
		return Employee.getEmployeeByEmail(id).addOnCompleteListener((Task<DocumentSnapshot> t) -> {
			if (t.isSuccessful())
			{
				// get the current employee
				Employee e = new Employee(t.getResult());
				// update name in the database
				e.setName(name).addOnFailureListener((Exception exception) -> {
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