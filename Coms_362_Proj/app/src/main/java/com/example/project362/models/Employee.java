package com.example.project362.models;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Employee
{
	private static final String TAG = "com-s-362-shift-project";

	private static final String COLLECTION = "Employees";

	public static final String EMP_ID = "empId";
	public static final String EMAIL = "email";
	public static final String NAME = "name";
	public static final String STATUS = "status";

	private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

	private String id;
	private String empId;
	private String email;
	private String name;
	private String status;

	public Employee(DocumentSnapshot doc)
	{
		this.copyFromDocumentSnapshot(doc);
	}

	public Employee(String id, String empId, String email, String name, String status)
	{
		this.id = id;
		this.empId = empId;
		this.email = email;
		this.name = name;
		this.status = status;
	}

	public Task<Void> setEmpId(final String empId)
	{
		return this.update(EMP_ID, empId).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task)
			{
				if (task.isSuccessful())
					Employee.this.empId = empId;
			}
		});
	}

	public Task<Void> setEmail(String email)
	{
		return this.update(EMAIL, email).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task)
			{
				if (task.isSuccessful())
					Employee.this.email = empId;
			}
		});
	}

	public Task<Void> setName(final String name)
	{
		return this.update(NAME, name).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task)
			{
				if (task.isSuccessful())
					Employee.this.name = name;
			}
		});
	}

	public Task<Void> setStatus(final String status)
	{
		return this.update(STATUS, status).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task)
			{
				if (task.isSuccessful())
					Employee.this.status = status;
			}
		});
	}

	public Task<Void> insertIntoDatabase()
	{
		HashMap<String, Object> data = new HashMap<>();

		data.put(Employee.EMAIL, this.email);
		data.put(Employee.STATUS, this.status);
		data.put(Employee.EMP_ID, this.empId);
		data.put(Employee.NAME, this.name);

		return db.collection(COLLECTION).document(this.email).set(data);
	}

	public String getEmpId()
	{
		return this.empId;
	}

	public String getName()
	{
		return this.name;
	}

	public String getEmail()
	{
		return this.email;
	}

	public String getStatus()
	{
		return this.status;
	}

	// DATABASE LOGIC
	public static Task<DocumentSnapshot> getEmployeeByEmail(String email)
	{
		return db.collection(COLLECTION).document(email).get();
	}

	public void copyFromDocumentSnapshot(DocumentSnapshot src)
	{
		this.id = src.getId();
		this.empId = (String) src.get(EMP_ID);
		this.email = (String) src.get(EMAIL);
		this.name = (String) src.get(NAME);
		this.status = (String) src.get(STATUS);
	}

	private Task<Void> update(String field, final Object datum)
	{
		Map<String, Object> data = new HashMap<>();
		data.put(field, datum);
		return db.collection(COLLECTION).document(this.id).update(data);
	}

	public Task<Void> update(HashMap<String, Object> data)
	{
		return db.collection(COLLECTION).document(this.id).update(data);
	}

	public static Task<Void> create(String id, HashMap<String, Object> data)
	{
		return db.collection(COLLECTION).document(id).set(data);
	}
}
