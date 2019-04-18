package com.example.project362.models;

import android.annotation.SuppressLint;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class Department
{
	private static final String COLLECTION = "Departments";

	private static final String NAME = "name";

	@SuppressLint("StaticFieldLeak")
	private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
	private String id;
	private String name;


	public Department(String name)
	{
		this.name = name;
	}

	public Department(DocumentSnapshot docSnap)
	{
		this.copyFromDocumentSnapshot(docSnap);
	}

	private void copyFromDocumentSnapshot(DocumentSnapshot docSnap)
	{
		this.id = docSnap.getId();
		this.name = (String) docSnap.get(NAME);
	}

	public String getName()
	{
		return this.name;
	}

	public Task<QuerySnapshot> getEmployees()
	{
		// return a list of all employees assigned to this Dept
		DocumentReference self = db.collection(COLLECTION).document(this.id);
		return Employee.getEmployeesByDepartment(self);
	}

	// adds this dept to the database
	public Task<DocumentReference> create()
	{
		// hashmap containing key value pairs of instance variables and their values
		HashMap<String, Object> data = new HashMap<>();

		data.put(NAME, this.name);
		return db.collection(COLLECTION).add(data)
			.addOnCompleteListener(t -> {
				if (t.isSuccessful() && t.getResult() != null)
					// update id if insert was successful
					this.id = t.getResult().getId();
			});
	}

	public static Task<QuerySnapshot> getDepartments()
	{
		return db.collection(COLLECTION).get();
	}

	public static Task<QuerySnapshot> getDepartmentsByName(String name)
	{
		// fetch where department name is equal to this department
		return db.collection(COLLECTION).whereEqualTo(NAME, name).get();
	}
}
