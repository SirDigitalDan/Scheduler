package com.example.project362.models;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Shift
{
	private static final String TAG = "com-s-362-shift-project";

	private static final String ID = "id";
	private static final String START_TIME = "startTime";
	private static final String END_TIME = "endTime";
	private static final String EMPLOYEES = "employees";
	private static final String NOTE = "note";

	private static final String COLLECTION = "Shifts";

	private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

	private String id;
	private Date startTime;
	private Date endTime;
	private ArrayList<Object> employees;
	private String note;

	public Shift() {}

	public Shift(DocumentSnapshot doc)
	{
		this.id = doc.getId();
		this.copyFromDocumentSnapshot(doc);
	}

	public Shift(String id, Date startTime, Date endTime, String note)
	{
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.note = note;
	}

	public Date setStartTime(Date d)
	{
		this.startTime = d;
		Shift.update(this.id, START_TIME, this.startTime);
		return this.startTime;
	}

	public Date setEndTime(Date d)
	{
		this.endTime = d;
		Shift.update(this.id, END_TIME, this.endTime);
		return this.endTime;
	}

	public ArrayList<Object> setEmployees(ArrayList<Object> employees)
	{
		this.employees = employees;
		Shift.update(this.id, EMPLOYEES, this.employees);
		return this.employees;
	}

	public ArrayList<Object> addEmployee(Object employee)
	{
		this.employees.add(employee);
		Shift.update(this.id, EMPLOYEES, this.employees);
		return this.employees;
	}

	public ArrayList<Object> removeEmployee(Object employee)
	{
		this.employees.remove(employee);
		Shift.update(this.id, EMPLOYEES, this.employees);
		return this.employees;
	}

	public String setNote(String note)
	{
		this.note = note;
		Shift.update(this.id, NOTE, this.note);
		return this.note;
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

	public ArrayList<Object> getEmployees()
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
		this.startTime = (Date) src.get(START_TIME);
		this.endTime = (Date) src.get(END_TIME);
		this.employees = (ArrayList<Object>) src.get(EMPLOYEES);
		this.note = (String) src.get(NOTE);
	}

	// DATABASE LOGIC
	public static Task<DocumentSnapshot> getShiftByKey(String key)
	{
		return db.collection(COLLECTION).document(key).get();
	}

	private static void update(String key, final String field, final Object datum)
	{
		Map<String, Object> data = new HashMap<>();
		data.put(field, datum);
		db.collection(COLLECTION).document(key).update(data);
	}
}
