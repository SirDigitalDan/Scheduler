package com.example.project362.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.project362.R;
import com.example.project362.models.Employee;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ViewMessages extends AppCompatActivity {

    private String currentUser;
    private ListView lv;
    private ArrayList<String> messageArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);
        findViewById(R.id.viewMessages).setOnClickListener(v -> showMessages());

        lv = findViewById(R.id.listMessages);

        // get the current users email
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        messageArr = new ArrayList<>();
    }

    private void showMessages()
    {
        // get the desired employee by email
        Employee.getEmployeeByEmail(currentUser).addOnCompleteListener((task) -> {
            if(task.isSuccessful())
            {
                // build employee from the database record
                Employee e = new Employee(task.getResult());
                // extract the employees message information
                messageArr = e.getMessage();
                listerMessage();
            }
        });
    }

    public void listerMessage()
    {
        //This Method lists the message array
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                messageArr));
    }

}
