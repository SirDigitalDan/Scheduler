package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.models.Admin;
import com.example.project362.models.Employee;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity
{
	FirebaseAuth mAuth;
	EditText editTextEmail, editTextPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mAuth = FirebaseAuth.getInstance();

		// go to sign up page
		findViewById(R.id.buttonSignUp).setOnClickListener((v) -> {
			finish();
			Intent i = new Intent(MainActivity.this, SignUpActivity.class);
			startActivity(i);
		});

		// go to edit account page
		findViewById(R.id.buttonEditAccount).setOnClickListener((v) -> {
			finish();
			Intent in = new Intent(MainActivity.this, EditInfoActivity.class);
			startActivity(in);
		});

		// login
		findViewById(R.id.buttonLogin).setOnClickListener((v) -> userLogin());

		editTextEmail = findViewById(R.id.editTextEmail);
		editTextPassword = findViewById(R.id.editTextPassword);
	}

	private void userLogin()
	{
		String email = editTextEmail.getText().toString();
		String password = editTextPassword.getText().toString();
		// if email is empty, notify user
		if (email.isEmpty())
		{
			editTextEmail.setError("Email is required");
			editTextEmail.requestFocus();
			return;
		}
		// if email is not in the form of an email, notify user
		if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
		{
			editTextEmail.setError("please enter valid email");
			editTextEmail.requestFocus();
			return;
		}
		// if password is empty, notify user
		if (password.isEmpty())
		{
			editTextPassword.setError("password is required");
			editTextPassword.requestFocus();
			return;
		}
		// if password is shorter than 6 characters notify user
		if (password.length() < 6)
		{
			editTextPassword.setError("Enter password that is atleast 6 characters long");
			editTextPassword.requestFocus();
			return;
		}

		// get the employee reference for the logging in user
		Employee.getEmployeeByEmail(email).addOnCompleteListener((Task<DocumentSnapshot> checkEmp) -> {
			// if employee exists
			if (checkEmp.isSuccessful() && checkEmp.getResult() != null && checkEmp.getResult().exists())
			{
				// sign in with the auth database
				mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Task<AuthResult> task) ->
				{
					if (task.isSuccessful())
					{

						// check if admin
						Admin.getAdmins().addOnCompleteListener((Task<QuerySnapshot> t) -> {
							if (t.isSuccessful())
							{
								String currentEmail = mAuth.getCurrentUser().getEmail();
								boolean isAdmin = false;

								// iterate over all admins and see if the current admin is in there
								for (QueryDocumentSnapshot document : t.getResult())
									if (document.getId().equals(currentEmail))
										isAdmin = true;

								if (isAdmin)
								{
									Toast.makeText(MainActivity.this, "Admin Sign In " +
											"Succsesful", Toast.LENGTH_SHORT).show();
									Intent intent = new Intent(MainActivity.this,
											AdminHomeActivity.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(intent);
								}
								else
								{
									Toast.makeText(MainActivity.this, "Employee Sign In " +
											"Succsesful", Toast.LENGTH_SHORT).show();
									Intent intent = new Intent(MainActivity.this,
											HomeActivity.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(intent);
								}
							}
						});
					}
					else
						Toast.makeText(getApplicationContext(), task.getException().getMessage(),
								Toast.LENGTH_SHORT).show();
				});
			}
			// if password was wrong
			else if (checkEmp.isSuccessful() && checkEmp.getResult() != null && !checkEmp.getResult().exists())
				Toast.makeText(getApplicationContext(), "email and/or password incorrect",
						Toast.LENGTH_SHORT).show();
			// if the earth has stopped turning and life as we know it has ceased to exist.
			//
			// or we couldn't connect to the database
			else
				Toast.makeText(getApplicationContext(), "something went wrong!",
						Toast.LENGTH_SHORT).show();
		});
	}
}
