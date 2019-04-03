package com.example.project362.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.project362.R;
import com.example.project362.models.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewAvailability extends AppCompatActivity implements View.OnClickListener{
    private String currentUser;
    private ListView lv;
    FirebaseAuth mAuth;
    private ArrayList<String> availArr;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_availability);
        findViewById(R.id.viewAvail).setOnClickListener(ViewAvailability.this);
        mAuth = FirebaseAuth.getInstance();
        lv=findViewById(R.id.listAvails);
        currentUser = mAuth.getCurrentUser().getEmail();
        availArr= new ArrayList<>();
    }
    private void showAvailability()
    {
        CollectionReference employeeCollect= db.collection("Employees");
        //Goes through all of the shifts
        Query employeeQuery = employeeCollect.whereEqualTo("Email",currentUser);

        employeeQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Employee e = new Employee(document);
                        availArr = e.getAvailability();
                    }

                }
                else
                {

                }

            }
        });
        lister1();

    }
    public void lister1()
    {
        //This Method lists the upComingShifts array
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                availArr );

        lv.setAdapter(arrayAdapter);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewAvail:
                showAvailability();
                break;

        }

    }
}
