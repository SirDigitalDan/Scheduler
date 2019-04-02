package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity implements OnClickListener
{
	FirebaseAuth mAuth;
	EditText editTextEmail, editTextPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mAuth = FirebaseAuth.getInstance();
		findViewById(R.id.button_send).setOnClickListener(MainActivity.this);
		findViewById(R.id.button_send1).setOnClickListener(MainActivity.this);

		Button btnClickMe = (Button) findViewById(R.id.button_send);
		btnClickMe.setOnClickListener(MainActivity.this);
		findViewById(R.id.buttonLogin).setOnClickListener(this);
		editTextEmail = findViewById(R.id.editTextEmail);
		editTextPassword = findViewById(R.id.editTextPassword);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
	}

	private void userLogin()
	{

		String email = editTextEmail.getText().toString();
		String password = editTextPassword.getText().toString();
		if (email.isEmpty())
		{
			editTextEmail.setError("Email is required");
			editTextEmail.requestFocus();
			return;
		}
		if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
		{
			editTextEmail.setError("please enter valid email");
			editTextEmail.requestFocus();
			return;
		}
		if (password.isEmpty())
		{
			editTextPassword.setError("password is required");
			editTextPassword.requestFocus();
			return;
		}
		if (password.length() < 6)
		{
			editTextPassword.setError("Enter password that is atleast 6 characters long");
			editTextPassword.requestFocus();
			return;
		}

		Employee.getEmployeeByEmail(email).addOnCompleteListener((Task<DocumentSnapshot> checkEmp) -> {
			if (checkEmp.isSuccessful() && checkEmp.getResult() != null && checkEmp.getResult().exists())
			{
				mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Task<AuthResult> task) ->
				{
					if (task.isSuccessful())
					{
						FirebaseFirestore db = FirebaseFirestore.getInstance();

						db.collection(Admin.COLLECTION).get()
								.addOnCompleteListener((Task<QuerySnapshot> t) -> {
									if (t.isSuccessful())
									{
										boolean isAdmin = false;
										for (QueryDocumentSnapshot document : t.getResult())
											if (document.getId().equals(mAuth.getCurrentUser().getEmail()))
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
			else if (checkEmp.isSuccessful() && checkEmp.getResult() != null && !checkEmp.getResult().exists())
				Toast.makeText(getApplicationContext(), "email and/or password incorrect",
						Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getApplicationContext(), "something went wrong!",
						Toast.LENGTH_SHORT).show();
		});

	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.button_send:
				finish();
				Intent i = new Intent(MainActivity.this, SignUpActivity.class);
				startActivity(i);
				break;

			case R.id.buttonLogin:
				userLogin();
				break;
			case R.id.button_send1:
				finish();
				Intent in = new Intent(MainActivity.this, ProfileActivity.class);
				startActivity(in);
				break;

		}
	}
}
