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

public class Payment {

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

    private static final int EMPLOYEEPAY = 10;
    private static final int ADMINPAY = 20;

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

    public Payment(DocumentSnapshot docSnap)
    {
        this.copyFromDocumentSnapshot(docSnap);
    }

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
        this.employee = src.getDocumentReference("employee");

    }

    private Task<Void> update(String field, final Object datum)
    {
        Map<String, Object> data = new HashMap<>();
        data.put(field, datum);
        return db.collection(COLLECTION).document(this.key).update(data);
    }

    public static Task<DocumentReference> addToFirestore(DocumentReference employee, int amount, int status)
    {
        HashMap<String, Object> h = new HashMap<>();
        h.put(EMPLOYEE, employee);
        h.put(AMOUNT, amount);
        h.put(STATUS, status);
        return db.collection(COLLECTION).add(h);
    }

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


    public Task<Void> accept()
    {

        this.status = Status.ACCEPTED.getValue();
        return this.update(STATUS, this.status);

    }

    public Task<Void> reject()
    {
        this.status = Status.REJECTED.getValue();
        return this.update(STATUS, this.status);
    }

    public static void calculatePayments(){

        int pay = 0;
        db.collection("Shifts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    List<DocumentReference> products = (List<DocumentReference>) document.get("employees");
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DocumentReference employee = db.collection("Employees").document(user.getEmail());

                    if(products.contains(employee)){
                        Timestamp start = document.getTimestamp("startTime");
                        Timestamp end = document.getTimestamp("endTime");

                        Date startDate = start.toDate();
                        Date endDate = end.toDate();

                        //HH converts hour in 24 hours format (0-23), day calculation
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                        long diff = endDate.getTime() - startDate.getTime();


                        long diffMinutes = diff / (60 * 1000) % 60;
                        long diffHours = diff / (60 * 60 * 1000) % 24;
                        long diffDays = diff / (24 * 60 * 60 * 1000);



                        int moneyEarned = (int) diffMinutes / 60;
                        moneyEarned += diffHours;
                        moneyEarned += diffDays * 24;

                        moneyEarned = moneyEarned * 10;

                        addToFirestore(employee, moneyEarned, 0);



                    }
                    else{

                    }


                }
            }
        });



    }

}
