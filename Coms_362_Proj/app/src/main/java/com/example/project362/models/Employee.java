package com.example.project362.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Employee
{
	private static final String TAG = "com-s-362-shift-project";

	public static final String COLLECTION = "Employees";

	public static final String EMP_ID = "empId";
	public static final String EMAIL = "email";
	public static final String NAME = "name";
	public static final String STATUS = "status";
	public static final String AVAILABILITY= "availability";

	private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

	private String id;
	private String empId;
	private String email;
	private String name;
	private String status;
	private ArrayList<String> availability;
	public Employee(DocumentSnapshot doc)
	{
		this.copyFromDocumentSnapshot(doc);
	}

	public Employee(String id, String empId, String email, String name, String status,ArrayList<String> availability)
	{
		this.id = id;
		this.empId = empId;
		this.email = email;
		this.name = name;
		this.status = status;
		this.availability = availability;
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
	public Task<Void> addAvailability(final String availability)
	{
	    //Creates an availability array which is composed of dates that the employee selects
		final ArrayList<String> temp = new ArrayList<>(this.availability);
		temp.add(availability);
        //This function will be called within a different class and lets the user add a date in the format mm/dd/yyyy to our database
		return this.update(AVAILABILITY, temp).addOnCompleteListener((Task<Void> t) ->
		{
			if (t.isSuccessful()) Employee.this.availability = temp;
			else
			{
				if (t.getException() != null)
					Log.e(TAG, t.getException().toString());
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

	public String getId() { return this.id; }

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
		this.availability = (ArrayList<String>) src.get(AVAILABILITY);
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

	public Task<Void> create()
	{
		HashMap<String, Object> h = new HashMap<>();
		h.put(EMAIL, this.email);
		h.put(EMP_ID, this.empId);
		h.put(STATUS, this.status);
		h.put(NAME, this.name);
		h.put(AVAILABILITY, this.availability);
		return db.collection(COLLECTION).document(this.email).set(h);
	}

	public static Task<Void> create(String id, HashMap<String, Object> data)
	{
		return db.collection(COLLECTION).document(id).set(data);
	}

	public static Task<Void> delete(String id)
	{
		return db.collection(COLLECTION).document(id).delete();
	}
}
