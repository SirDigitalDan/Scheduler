package com.example.project362.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.models.Admin;
import com.example.project362.models.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AdminStatusActivity extends AppCompatActivity implements View.OnClickListener
{
	EditText userEmail;

	private FirebaseAuth mAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_status);
		mAuth = FirebaseAuth.getInstance();
		final FirebaseFirestore db1 = FirebaseFirestore.getInstance();
		userEmail = findViewById(R.id.employeeEmail);

		findViewById(R.id.grantAdmin).setOnClickListener(AdminStatusActivity.this);
		findViewById(R.id.deleteEmployee).setOnClickListener(AdminStatusActivity.this);
	}

	private void deleteUser()
	{
		String email = userEmail.getText().toString().trim();

		Employee.delete(email).addOnCompleteListener((Task<Void> t) -> {
			if (t.isSuccessful())
				Toast.makeText(AdminStatusActivity.this, "Employee deleted", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(AdminStatusActivity.this, "Failed to delete employee",
						Toast.LENGTH_SHORT).show();
		});
	}

	private void createAdmin()
	{
		final String email = userEmail.getText().toString().trim();
		final FirebaseFirestore db = FirebaseFirestore.getInstance().getInstance();
		final CollectionReference ref = db.collection(Employee.COLLECTION);

		db.collection(Employee.COLLECTION).document(email).get()
				.addOnCompleteListener((Task<DocumentSnapshot> task) -> {
					if (task.isSuccessful())
					{
						Admin.create(email)
								.addOnCompleteListener((Task<Void> t) -> {
									if (t.isSuccessful())
										Toast.makeText(AdminStatusActivity.this, "Admin " +
												"created", Toast.LENGTH_SHORT).show();
									else
										Toast.makeText(AdminStatusActivity.this, "Failed " +
												"to create", Toast.LENGTH_SHORT).show();
								});
					}
					else
						Toast.makeText(AdminStatusActivity.this, "Employee not found in the " +
								"Database", Toast.LENGTH_SHORT).show();
				});

	}

	@Override
	public void onClick(View view)
	{

		switch (view.getId())
		{
			case R.id.deleteEmployee:
				finish();
				deleteUser();
				break;
			case R.id.grantAdmin:
				finish();
				createAdmin();
				break;


		}


	}
}
