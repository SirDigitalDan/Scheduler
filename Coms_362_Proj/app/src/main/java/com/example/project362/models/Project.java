package com.example.project362.models;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.BuildConfig;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings({"unchecked", "WeakerAccess"})
public class Project
{
    private static final String TAG = "com-s-362-shift-project";

    private static final String BUDGET = "budget";
    private static final String DEADLINE = "deadline";
    private static final String EMPLOYEES = "employees";
    private static final String NAME="name";
    private static final String COLLECTION = "Projects";

    private	String name;
    private Map<String, Long> budget = new HashMap<String, Long>();
    private Date deadline;
    private ArrayList<DocumentReference> employees;
    private String projectID;

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // make from a document snapshot from the db
    public Project(DocumentSnapshot docSnap)
    {
        this.copyFromDocumentSnapshot(docSnap);
    }

    public Project(String name, Date deadline,  Map<String, Long> budget)
    {
        this.name = name;
        this.deadline = deadline;
        this.budget = budget;
        this.employees = new ArrayList<>();
    }

    // set the deadline
    public Task<Void> setDeadline(final Date date)
    {
        return this.update(DEADLINE, date).addOnCompleteListener((Task<Void> task) ->
        {
            if (task.isSuccessful()) Project.this.deadline = date;
            else
            {
                if (task.getException() != null)
                    Log.e(TAG, task.getException().toString());
                throw new Error("Operation unsuccessful");
            }
        });
    }

    //Set the budget of the project
    public Task<Void> setBudget(final Map<String, Long> budget)
    {
        return this.update(BUDGET, budget).addOnCompleteListener((Task<Void> task) ->
        {
            if (task.isSuccessful()) Project.this.budget = budget;
            else
            {
                if (task.getException() != null)
                    Log.e(TAG, task.getException().toString());
                throw new Error("Operation unsuccessful");
            }
        });
    }

    //Set the employees working on the project
    public Task<Void> setEmployees(final ArrayList<DocumentReference> employees)
    {
        return this.update(EMPLOYEES, employees).addOnCompleteListener((Task<Void> t) ->
        {
            if (t.isSuccessful()) Project.this.employees = employees;
            else
            {
                if (t.getException() != null)
                    Log.e(TAG, t.getException().toString());
            }
        });
    }


    //Add an employee to a project
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
            return Tasks.forException(new Exception("This employee is already assigned to the selected" +
                    " " +
                    "project"));

        temp.add(employee);

        return this.update(EMPLOYEES, temp).addOnCompleteListener((Task<Void> t) ->
        {
            if (t.isSuccessful()) Project.this.employees = temp;
            else
            {
                if (t.getException() != null)
                    Log.e(TAG, t.getException().toString());
            }
        });
    }




    public ArrayList<DocumentReference> getEmployees()
    {
        return this.employees;
    }

    public String getId()
    {
        return this.projectID;
    }

    public String getName()
    {
        return this.name;
    }

    public Date getDeadline() { return this.deadline; }

    public Map<String, Long> getBudget()
    {
        return this.budget;
    }

    /**
     * Performs a <strong>SHALLOW</strong> copy on the attributes of the given
     * Shift into the attributes of this shit
     *
     * @param src - Shift to copy attributes from
     */
    public void copyFromDocumentSnapshot(DocumentSnapshot src)
    {
        this.name = (String) src.get(NAME);
        this.projectID = src.getId();
        this.deadline = ((Timestamp) src.get(DEADLINE)).toDate();
        this.budget = (Map<String, Long>) src.get(BUDGET);
        this.employees = (ArrayList<DocumentReference>) src.get(EMPLOYEES);

    }

    // DATABASE LOGIC
    ///Returns all projects
    public static Task<QuerySnapshot> getProjects()
    {
        return db.collection(COLLECTION).get();
    }

    private Task<Void> update(String field, final Object datum)
    {
        Map<String, Object> data = new HashMap<>();
        data.put(field, datum);
        return db.collection(COLLECTION).document(this.projectID).update(data);
    }

    //delete a project
    public static Task<Void> delete(String id)
    {
        return db.collection(COLLECTION).document(id).delete();
    }


    ///create a project in the DB
    public Task<DocumentReference> create()
    {
        HashMap<String, Object> h = new HashMap<>();
        h.put(NAME, this.name);
        h.put(DEADLINE, this.deadline);
        h.put(EMPLOYEES, this.employees);
        h.put(BUDGET, this.budget);


        return db.collection(COLLECTION).add(h)
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful() && t.getResult() != null)
                        this.projectID = t.getResult().getId();
                });
    }
}
