package com.example.project362.models;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class Shift
{
    private static final String TAG = "com-s-362-shift-project";
    private static final String COLLECTION = "Shifts";
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String id;
    private Date startTime;
    private Date endTime;
    private Object[] employees;
    private String note;

    public Shift() {}

    public Shift(DocumentSnapshot doc)
    {
        this.id = doc.getId();
        this.copyFrom(doc.toObject(Shift.class));
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
        return this.startTime = d;
    }

    public Date setEndTime(Date d)
    {
        return this.endTime = d;
    }

    public Object[] setEmployees(Object[] employees)
    {
        return this.employees = employees;
    }

    public String setNote(String note)
    {
        return this.note = note;
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

    public Object[] getEmployees()
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

    // DATABASE LOGIC
    public static Task<DocumentSnapshot> getShiftByKey(String key)
    {
        return db.collection(COLLECTION).document(key).get();
    }

    public void copyFrom(Shift src)
    {
        this.startTime = src.startTime;
        this.endTime = src.endTime;
        this.employees = src.employees;
        this.note = src.note;
    }
}
