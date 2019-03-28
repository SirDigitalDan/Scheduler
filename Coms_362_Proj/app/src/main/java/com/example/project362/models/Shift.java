package com.example.project362.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
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

	public static final String COLLECTION = "Shifts";

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
		return this.update(START_TIME, date).addOnCompleteListener((Task<Void> task) ->
		{
			if (task.isSuccessful()) Shift.this.startTime = date;
			else
			{
				if (task.getException() != null)
					Log.e(TAG, task.getException().toString());
				throw new Error("Operation unsuccessful");
			}
		});
	}

	public Task<Void> setEndTime(final Date date)
	{
		return this.update(END_TIME, date).addOnCompleteListener((Task<Void> task) ->
		{
			if (task.isSuccessful()) Shift.this.endTime = date;
			else
			{
				if (task.getException() != null)
					Log.e(TAG, task.getException().toString());
				throw new Error("Operation unsuccessful");
			}
		});
	}

	public Task<Void> setEmployees(final ArrayList<DocumentReference> employees)
	{
		return this.update(EMPLOYEES, employees).addOnCompleteListener((Task<Void> t) ->
		{
			if (t.isSuccessful()) Shift.this.employees = employees;
			else
			{
				if (t.getException() != null)
					Log.e(TAG, t.getException().toString());
			}
		});
	}

	public Task<Void> addEmployee(final DocumentReference employee)
	{
		final ArrayList<DocumentReference> temp = new ArrayList<>(employees);

		boolean contained = false;
		for (int i = 0; i < temp.size(); i++)
		{
			if (temp.get(i).getId().equals(employee.getId()))
			{
				contained = true;
				break;
			}
		}

		if (contained)
			return Tasks.forException(new Exception("That employee is not assigned to the selected" +
					" " +
					"shift"));

		temp.add(employee);

		return this.update(EMPLOYEES, temp).addOnCompleteListener((Task<Void> t) ->
		{
			if (t.isSuccessful()) Shift.this.employees = temp;
			else
			{
				if (t.getException() != null)
					Log.e(TAG, t.getException().toString());
			}
		});
	}

	public Task<Void> removeEmployee(DocumentReference employee)
	{
		final ArrayList<DocumentReference> temp = new ArrayList<>(employees);

		boolean contained = false;
		for (int i = 0; i < temp.size(); i++)
		{
			if (temp.get(i).getId().equals(employee.getId()))
			{
				temp.remove(i);
				contained = true;
				break;
			}
		}

		if (!contained)
			return Tasks.forException(new Exception("That employee is not assigned to the selected" +
					" " +
					"shift"));

		return this.update(EMPLOYEES, temp).addOnCompleteListener((Task<Void> task) ->
		{
			if (task.isSuccessful()) Shift.this.employees = temp;
			else
			{
				if (task.getException() != null)
					Log.e(TAG, task.getException().toString());
			}
		});
	}

	public Task<Void> removeEmployee(int i)
	{
		if (i >= this.employees.size())
			return Tasks.forException(new Exception("That employee does not exist"));

		final ArrayList<DocumentReference> temp = new ArrayList<>(this.employees);

		return this.update(EMPLOYEES, temp).addOnCompleteListener((Task<Void> t) -> {
			if (t.isSuccessful()) Shift.this.employees = temp;
			else
			{
				if (t.getException() != null)
					Log.e(TAG, t.getException().toString());
			}
		});
	}

	public Task<Void> removeEmployee(String id)
	{
		for (int i = 0; i < this.employees.size(); i++)
			if (this.employees.get(i).getId().equals(id))
				return this.removeEmployee(i);

		return Tasks.forException(new Exception("Employee not found"));
	}

	public Task<Void> setNote(final String note)
	{
		return this.update(NOTE, note).addOnCompleteListener((Task<Void> t) ->
		{
			if (t.isSuccessful()) Shift.this.note = note;
			else
			{
				if (t.getException() != null)
					Log.e(TAG, t.getException().toString());
			}
		});
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

	public DocumentReference getReference()
	{
		return db.collection(COLLECTION).document(this.id);
	}

	/**
	 * Performs a <strong>SHALLOW</strong> copy on the attributes of the given
	 * Shift into the attributes of this shit
	 *
	 * @param src - Shift to copy attributes from
	 */
	public void copyFromDocumentSnapshot(DocumentSnapshot src)
	{
		this.id = src.getId();
		this.startTime = (Date) src.get(START_TIME);
		this.endTime = (Date) src.get(END_TIME);
		this.note = (String) src.get(NOTE);
		this.employees = (ArrayList<DocumentReference>) src.get(EMPLOYEES);
	}

	// DATABASE LOGIC
	public static Task<DocumentSnapshot> getShiftByKey(String key)
	{
		return Shift.getShiftReferenceByKey(key).get();
	}

	public static DocumentReference getShiftReferenceByKey(String key)
	{
		return db.collection(COLLECTION).document(key);
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

	public static Task<Void> delete(String id)
	{
		return db.collection(COLLECTION).document(id).delete();
	}
}
