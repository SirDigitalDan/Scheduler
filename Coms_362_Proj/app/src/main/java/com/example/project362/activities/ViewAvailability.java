package com.example.project362.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.project362.R;
import com.example.project362.models.Employee;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ViewAvailability extends AppCompatActivity
{
    private String currentUser;
    private ListView lv;
    private ArrayList<String> availArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_availability);
        findViewById(R.id.viewAvail).setOnClickListener(v -> showAvailability());

        lv = findViewById(R.id.listAvails);

        // get the current users email
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        availArr = new ArrayList<>();
    }
    private void showAvailability()
    {
    	// get the desired employee by email
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
        //This Method lists the availability array
	    lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
			    availArr));
    }
}
