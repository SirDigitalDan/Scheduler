package com.example.project362.models;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Admin
{
	public static final String COLLECTION = "Admins";

	private static FirebaseFirestore db = FirebaseFirestore.getInstance();

	public static Task<Void> create(String email)
	{
		HashMap<String, Object> data = new HashMap<>();
		data.put(Employee.EMAIL, email);

		return db.collection(Admin.COLLECTION).document(email).set(data);
	}
}
