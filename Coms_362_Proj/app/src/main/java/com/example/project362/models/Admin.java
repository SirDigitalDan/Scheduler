package com.example.project362.models;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class Admin
{
	public static final String COLLECTION = "Admins";

	private static FirebaseFirestore db = FirebaseFirestore.getInstance();

	// adds the admin to the database
	public static Task<Void> create(String email)
	{
		HashMap<String, Object> data = new HashMap<>();
		data.put(Employee.EMAIL, email);

		return db.collection(COLLECTION).document(email).set(data);
	}

	// delete an admin from the database
	public static Task<Void> delete(String email)
	{
		return db.collection(COLLECTION).document(email).delete();
	}

	// get all admins
	public static Task<QuerySnapshot> getAdmins()
	{
		return db.collection(COLLECTION).get();
	}
}
