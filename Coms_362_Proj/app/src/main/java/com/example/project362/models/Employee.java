package com.example.project362.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
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
	public static final String AVAILABILITY = "availability";
	public static final String CHECKED = "checked";


	private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

	private String id;
	private String empId;
	private String email;
	private String name;
	private String status;
	private ArrayList<String> availability;
	private String checked;

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
		this.availability = new ArrayList<>();
		this.checked = "";
	}

	// set the employee id
	public Task<Void> setEmpId(final String empId)
	{
		// update the employee id in the database
		return this.update(EMP_ID, empId).addOnCompleteListener((Task<Void> task) ->
		{
			if (task.isSuccessful())
				Employee.this.empId = empId;
		});
	}

	// set the employees email
	public Task<Void> setEmail(String email)
	{
		// update the email in the database
		return this.update(EMAIL, email).addOnCompleteListener((Task<Void> task) ->
		{
			if (task.isSuccessful())
				Employee.this.email = empId;
		});
	}

	// set the employees name
	public Task<Void> setName(final String name)
	{
		// update the name in the database
		return this.update(NAME, name).addOnCompleteListener((Task<Void> task) ->
		{
			if (task.isSuccessful())
				Employee.this.name = name;
		});
	}

	// add availability to this employee
	public Task<Void> addAvailability(final String date)
	{
		// make sure the date is not already in the availability list
		if (this.availability.contains(date)) return Tasks.forException(new Exception("That date " +
				"is already added"));

	    //Creates an availability array which is composed of dates that the employee selects
		final ArrayList<String> temp = new ArrayList<>(this.availability);
		temp.add(date);

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

	// set the status of the employee
	public Task<Void> setStatus(final String status)
	{
		return this.update(STATUS, status).addOnCompleteListener((Task<Void> task) ->
		{
			if (task.isSuccessful())
				Employee.this.status = status;
		});
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

	public ArrayList<String> getAvailability()
	{
		return this.availability;
	}

	public String getId() { return this.id; }

	// DATABASE LOGIC
	public static Task<DocumentSnapshot> getEmployeeByEmail(String email)
	{
		// fetch the employee reference by an email, then turn into a task of document snapshot
		return Employee.getEmployeeReferenceByKey(email).get();
	}

	// given a document snapshot, copies values into this employee object
	public void copyFromDocumentSnapshot(DocumentSnapshot src)
	{
		this.id = src.getId();
		this.empId = (String) src.get(EMP_ID);
		this.email = (String) src.get(EMAIL);
		this.name = (String) src.get(NAME);
		this.status = (String) src.get(STATUS);
		this.availability = (ArrayList<String>) src.get(AVAILABILITY);
	}

	// update this employees values in a database
	private Task<Void> update(String field, final Object datum)
	{
		Map<String, Object> data = new HashMap<>();
		data.put(field, datum);
		return db.collection(COLLECTION).document(this.id).update(data);
	}

	// update in the database
	public Task<Void> update(HashMap<String, Object> data)
	{
		return db.collection(COLLECTION).document(this.id).update(data);
	}

	// create this employee object in the database
	public Task<Void> create()
	{
		HashMap<String, Object> h = new HashMap<>();
		h.put(EMAIL, this.email);
		h.put(EMP_ID, this.empId);
		h.put(STATUS, this.status);
		h.put(NAME, this.name);
		h.put(AVAILABILITY, this.availability);
		h.put(CHECKED, this.checked);

		return db.collection(COLLECTION).document(this.email).set(h);
	}

	// delete this employee from the database
	public static Task<Void> delete(String id)
	{
		return db.collection(COLLECTION).document(id).delete();
	}

	// get reference for employee
	public static DocumentReference getEmployeeReferenceByKey(String key)
	{
		return db.collection(COLLECTION).document(key);
	}
}
