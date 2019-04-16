package com.example.project362.models;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.project362.activities.AdminHomeActivity;
import com.example.project362.activities.HomeActivity;
import com.example.project362.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    The Payment Class represents an amount of money owed to an employee for working their shifts

    @param employee     The employee that is owed the payment amount
    @param amount       The amount of money
    @param status       The status of the Payment - Pending, Approved, Rejected

 */

public class Payment {

    /*
        the Status enum is an attribute of a Payment.  It always starts as Pending once it is created
        and can be approved or rejected by an Admin account.
     */

    public enum Status{
        PENDING("PENDING", 0), ACCEPTED("ACCEPTED", 1), REJECTED("REJECTED", 2);

        private final int value;
        private final String desc;

        Status(String desc, int value)
        {
            this.desc = desc;
            this.value = value;
        }

        public int getValue()
        {
            return this.value;
        }

        public String toString()
        {
            return this.desc;
        }

        public static Status getStatus(int i)
        {
            return Status.values()[i];
        }
    }

    /*
        Currently, all employees make 10 dollars per hour
     */
    private static final int EMPLOYEEPAY = 10;


    private static final String EMPLOYEE = "employee";
    private static final String AMOUNT = "amount";
    private static final String STATUS = "status";

    private static final String COLLECTION = "Payments";



    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // the employee that is receiving the payment if approved
    private DocumentReference employee;

    // the amount of money
    private int amount;

    // the status of this payment approval: 0 if pending, 1 if accepted, 2 if rejected
    private int status;

    private String key;

    //Create new Payment object from a Firebase snapshot
    public Payment(DocumentSnapshot docSnap)
    {
        this.copyFromDocumentSnapshot(docSnap);
    }

    //Create new Payment Object
    public Payment(DocumentReference employee, int amount, int status)
    {
        this.employee = employee;
        this.amount = amount;
        this.status = status;
    }

    public DocumentReference getEmployee(){
        return this.employee;
    }

    public int getAmount(){
        return this.amount;
    }

    public int getStatus(){
        return this.status;
    }


    private void copyFromDocumentSnapshot(DocumentSnapshot src)
    {
        this.key = src.getId();
        this.amount = (int) (long) src.getLong(AMOUNT);
        this.status = (int) (long) src.getLong(STATUS);

        this.employee = src.getDocumentReference(EMPLOYEE);

    }
    // Update Payment in Database
    private Task<Void> update(String field, final Object datum)
    {
        Map<String, Object> data = new HashMap<>();
        data.put(field, datum);
        return db.collection(COLLECTION).document(this.key).update(data);
    }

    //Create new payment in the database
    public static Task<DocumentReference> addToFirestore(DocumentReference employee, int amount, int status)
    {
        HashMap<String, Object> h = new HashMap<>();
        h.put(EMPLOYEE, employee);
        h.put(AMOUNT, amount);
        h.put(STATUS, status);
        return db.collection(COLLECTION).add(h);
    }

    ///Add current Employee in Database
    public Task<DocumentReference> create()
    {
        HashMap<String, Object> h = new HashMap<>();
        h.put(EMPLOYEE, this.employee);
        h.put(AMOUNT, this.amount);
        h.put(STATUS, this.status);
        return db.collection(COLLECTION).add(h);
    }


    public static Task<Void> delete(String key)
    {
        return db.collection(COLLECTION).document(key).delete();
    }

    // return all payments in DB
    public static Task<QuerySnapshot> getPayments()
    {
        return db.collection(COLLECTION).get();
    }

    public static Task<DocumentSnapshot> getPaymentByKey(String key)
    {
        return db.collection(COLLECTION).document(key).get();
    }

    public static DocumentReference getPaymentReferenceByKey(String key)
    {
        return db.collection(COLLECTION).document(key);
    }

    //Changes the status from 0 to 1
    public Task<Void> accept()
    {

        this.status = Status.ACCEPTED.getValue();
        return this.update(STATUS, this.status);

    }

    //Changes the status from 0 to 2
    public Task<Void> reject()
    {
        this.status = Status.REJECTED.getValue();
        return this.update(STATUS, this.status);
    }

    /*
        calculatePayments let an employee generate a Payment statement for the shifts that they have worked.

        The method works by iterating through all of the shifts and find which shifts have (1) occured already and
        (2) have included the current user as an employee

        The method will calculate the hours that the employee has worked and multiply it by the hourly wage of
        an employee ($10).  It will then add the Payment to the database.
     */
    public static void calculatePayments()
    {
        Shift.getShifts().addOnCompleteListener((@NonNull Task<QuerySnapshot> task) ->
        {
            ////Iterate through all shifts
            for (DocumentSnapshot shift : task.getResult())
            {
                List<DocumentReference> emps = (List<DocumentReference>) shift.get("employees");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DocumentReference employee = db.collection("Employees").document(user.getEmail());

                ///check if shift has occured yet
                Date today = new Date();
                Timestamp t = shift.getTimestamp("endTime");
                Date shiftEndDate = t.toDate();

                long dateCheck = today.getTime() - shiftEndDate.getTime();

                ///check if employee is included in the shift and it has occured
                if(emps.contains(employee) && dateCheck > 0)
                {
                    ////Calculate hours worked
                    Timestamp start = shift.getTimestamp("startTime");
                    Timestamp end = shift.getTimestamp("endTime");

                    Date startDate = start.toDate();
                    Date endDate = end.toDate();

                    long diff = endDate.getTime() - startDate.getTime();

                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);


                    int moneyEarned = (int) diffMinutes / 60;
                    moneyEarned += diffHours;
                    moneyEarned += diffDays * 24;

                    moneyEarned = moneyEarned * EMPLOYEEPAY;

                    /// Add new payment to database
                    addToFirestore(employee, moneyEarned, 0);
                }
            }
        });
    }
}
