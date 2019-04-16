package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.models.Employee;
import com.example.project362.models.Shift;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private String currentUser;
    private ListView lv;
    private ListView listEval;
    private EditText getEvals;
    FirebaseAuth mAuth;
    ArrayList<String> upcomingShifts,evaluations;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        lv=findViewById(R.id.listShifts);

        //Goes to the page which will let the Employee set their availability
        findViewById(R.id.button_send2).setOnClickListener(ProfileActivity.this);
        findViewById(R.id.button_send3).setOnClickListener(ProfileActivity.this);
        findViewById(R.id.button_Eval).setOnClickListener(ProfileActivity.this);
        findViewById(R.id.button_Message).setOnClickListener(ProfileActivity.this);
        getEvals= findViewById(R.id.editTextEval);
        upcomingShifts = new ArrayList<>();
        evaluations = new ArrayList<>();
        currentUser = mAuth.getCurrentUser().getEmail();

    }
    private Task<DocumentSnapshot>  getEval(){

        // get current employee reference
        return Employee.getEmployeeByEmail(currentUser).addOnCompleteListener((Task<DocumentSnapshot> t) -> {
            if (t.isSuccessful())
            {
                // get the current employee
                Employee e = new Employee(t.getResult());
                // update name in the database
                double evals = e.getEval();
                evaluations.add(evals+" ");
                String str = evals + " ";
                getEvals.setText(str);
            }
            else {
                Toast.makeText(ProfileActivity.this, "edit user info failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getAllUsers() {
        // [START get_all_Shifts]

        Shift.getShifts().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    //Parses the Documents of the Shifts
                    for (QueryDocumentSnapshot document: task.getResult()){
                        //Creates Shift Document
                        Shift s = new Shift(document);
                        ArrayList<DocumentReference> emps = s.getEmployees();
                        //Checks to see if the current signed in user matches any of the shifts
                        for (int i=0; i <emps.size(); i++)
                        {
                            //If the User matches they get added to the upcomingShifts database
                            if(currentUser.equals(emps.get(i).getId()))
                            {
                                String ids = s.getName();
                                upcomingShifts.add(ids);
                            }
                        }
                    }
                }
                else
                {
                    //Lets the User no the user doesn't have any upcoming shifts
                    Toast.makeText(getApplicationContext(),"The current user " +currentUser+ " doesn't have any upcoming shifts",Toast.LENGTH_SHORT).show();
                }
            }
        });
        lister();
    }
    public void lister()
    {
        //This Method lists the upComingShifts array
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                upcomingShifts );

        lv.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_send2:
                finish();
                Intent in1 = new Intent(ProfileActivity.this, CalendarActivity.class);
                startActivity(in1);
                break;
            case R.id.button_Message:
                finish();
                Intent i = new Intent(ProfileActivity.this, ViewMessages.class);
                startActivity(i);
                break;
            case R.id.button_send3:
                getAllUsers();
                break;
            case R.id.button_Eval:
                getEval();
                break;
        }
    }
}
