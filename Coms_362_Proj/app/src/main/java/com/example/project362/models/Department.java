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
		return Employee.getEmployeesByDepartment(this.id);
	}

	public Task<DocumentReference> create()
	{
		HashMap<String, Object> data = new HashMap<>();

		data.put(NAME, this.name);
		return db.collection(COLLECTION).add(data)
			.addOnCompleteListener(t -> {
				if (t.isSuccessful() && t.getResult() != null)
					this.id = t.getResult().getId();
			});
	}

	public static Task<QuerySnapshot> getDepartments()
	{
		return db.collection(COLLECTION).get();
	}
}
