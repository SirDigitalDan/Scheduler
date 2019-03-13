package com.example.project362.models;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;


public class Employee
{
    private static final String TAG = "com-s-362-shift-project";
    private static final String COLLECTION = "Employees";
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String empId;
    private String email;
    private String name;
    private String status;


    public Employee() {}

    public Employee(DocumentSnapshot doc)
    {
        this.empId = doc.getId();
        this.copyFrom(doc.toObject(Employee.class));
    }

    public Employee(String empId, String email, String name, String status)
    {
        this.empId = empId;
        this.email = email;
        this.name = name;
        this.status = status;
    }

    public String setempId(String s)
    {
        return this.empId = s;
    }

    public String email(String e)
    {
        return this.email = e;
    }

    public String setName(String n)
    {
        return this.name = n;
    }

    public String setStatus(String s)
    {
        return this.status = s;
    }

    public String getempId()
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

    public void copyFrom(Employee src)
    {
        this.empId = src.empId;
        this.email = src.email;
        this.name = src.name;
        this.status = src.status;
    }
}
