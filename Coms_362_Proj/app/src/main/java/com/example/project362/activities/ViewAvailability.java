package com.example.project362.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.project362.R;
import com.example.project362.models.Employee;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;

public class ViewAvailability extends AppCompatActivity implements View.OnClickListener
{
    private String currentUser;
    private ListView lv;
    private ArrayList<String> availArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_availability);
        findViewById(R.id.viewAvail).setOnClickListener(ViewAvailability.this);

        lv=findViewById(R.id.listAvails);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        availArr = new ArrayList<>();
    }
    private void showAvailability()
    {
    	CollectionReference employees = Employee.getEmployees();

    	// get the desired employee
        Employee.getEmployeeByEmail(currentUser).addOnCompleteListener((task) -> {
            if(task.isSuccessful())
            {
            	// build employee from the database record
                Employee e = new Employee(task.getResult());
                // extract the employees availability information
                availArr = e.getAvailability();
	            lister();
            }
        });
    }

    public void lister()
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
