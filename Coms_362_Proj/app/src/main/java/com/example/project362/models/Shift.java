package com.example.project362.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Shift
{
	private static final String TAG = "com-s-362-shift-project";

	private static final String START_TIME = "startTime";
	private static final String END_TIME = "endTime";
	private static final String EMPLOYEES = "employees";
	private static final String NOTE = "note";

	private static final String COLLECTION = "Shifts";

	private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

	private String id;
	private Date startTime;
	private Date endTime;
	private ArrayList<DocumentReference> employees;
	private String note;

	public Shift(DocumentSnapshot docSnap)
	{
		this.copyFromDocumentSnapshot(docSnap);
	}

	public Task<Void> setStartTime(final Date date)
	{
		Task<Void> t = this.update(START_TIME, date);

		t.addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task)
			{
				if (task.isSuccessful()) Shift.this.startTime = date;
				else
				{
					if (task.getException() != null)
						Log.e(TAG, task.getException().toString());
					throw new Error("Operation unsuccessful");
				}
			}
		});

		return t;
	}

	public Task<Void> setEndTime(final Date date)
	{
		Task<Void> t = this.update(END_TIME, date);

		t.addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task)
			{
				if (task.isSuccessful()) Shift.this.endTime = date;
				else
				{
					if (task.getException() != null)
						Log.e(TAG, task.getException().toString());
					throw new Error("Operation unsuccessful");
				}
			}
		});
		return t;
	}

	public Task<Void> setEmployees(final ArrayList<DocumentReference> employees)
	{
		Task<Void> t = this.update(EMPLOYEES, employees);

		t.addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task)
			{
				if (task.isSuccessful()) Shift.this.employees = employees;
				else
				{
					if (task.getException() != null)
						Log.e(TAG, task.getException().toString());
					throw new Error("Operation unsuccessful");
				}
			}
		});

		return t;
	}

	public Task<Void> addEmployee(final DocumentReference employee)
	{
		final ArrayList<DocumentReference> temp = (ArrayList<DocumentReference>) employees.clone();
		temp.add(employee);

		Task<Void> t = this.update(EMPLOYEES, temp);

		t.addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task)
			{
				if (task.isSuccessful()) Shift.this.employees = temp;
				else
				{
					if (task.getException() != null)
						Log.e(TAG, task.getException().toString());
					throw new Error("Operation unsuccessful");
				}
			}
		});

		return t;
	}

	public Task<Void> removeEmployee(DocumentReference employee)
	{
		final ArrayList<DocumentReference> temp = (ArrayList<DocumentReference>) employees.clone();
		temp.remove(employee);

		Task<Void> t = this.update(EMPLOYEES, temp);

		t.addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task)
			{
				if (task.isSuccessful()) Shift.this.employees = temp;
				else
				{
					if (task.getException() != null)
						Log.e(TAG, task.getException().toString());
					throw new Error("Operation unsuccessful");
				}
			}
		});

		return t;
	}

	public Task<Void> setNote(final String note)
	{
		Task<Void> t = this.update(EMPLOYEES, note);

		t.addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task)
			{
				if (task.isSuccessful()) Shift.this.note = note;
				else
				{
					if (task.getException() != null)
						Log.e(TAG, task.getException().toString());
					throw new Error("Operation unsuccessful");
				}
			}
		});

		return t;
	}

	public String setId(String id)
	{
		return this.id = id;
	}

	public Date getStartTime()
	{
		return this.startTime;
	}

	public Date getEndTime()
	{
		return this.endTime;
	}

	public ArrayList<DocumentReference> getEmployees()
	{
		return this.employees;
	}

	public String getNote()
	{
		return this.note;
	}

	public String getId()
	{
		return this.id;
	}

	/**
	 * Performs a <strong>SHALLOW</strong> copy on the attributes of the given
	 * Shift into the attributes of this shit
	 * @param src - Shift to copy attributes from
	 */
	public void copyFromDocumentSnapshot(DocumentSnapshot src)
	{
		this.id = src.getId();
		this.startTime = (Date) src.get(START_TIME);
		this.endTime = (Date) src.get(END_TIME);
		this.note = (String) src.get(NOTE);
		this.employees = new ArrayList<>();
	}

	// DATABASE LOGIC
	public static Task<DocumentSnapshot> getShiftByKey(String key)
	{
		return db.collection(COLLECTION).document(key).get();
	}

	public static Task<QuerySnapshot> getShifts()
	{
		return db.collection(COLLECTION).get();
	}

	private Task<Void> update(String field, final Object datum)
	{
		Map<String, Object> data = new HashMap<>();
		data.put(field, datum);
		return db.collection(COLLECTION).document(this.id).update(data);
	}
}
