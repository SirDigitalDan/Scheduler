package com.example.project362.models;

import com.google.firebase.firestore.FirebaseFirestore;

public class Admin
{
	private static final String COLLECTION = "Admins";

	private static FirebaseFirestore db = FirebaseFirestore.getInstance();

	public static boolean isAdmin(String email)
	{
		return db.collection(COLLECTION).document(email).get().getResult() == null;
	}
}
