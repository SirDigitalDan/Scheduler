package com.example.project362.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.models.Employee;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class EmployeePerformance extends AppCompatActivity {
    private static final String TAG ="" ;
    FirebaseAuth mAuth;
    EditText editTextAttendance, editTextLeadership, editTextTechnology, editTextTeamwork,editTextEmployee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_performance);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.button_calculateEvaluation).setOnClickListener((v) -> calculateEvalation());
        editTextAttendance = findViewById(R.id.CriteriaAttendance);
        editTextLeadership = findViewById(R.id.CriteriaLeadership);
        editTextTechnology = findViewById(R.id.CriteriaTechnology);
        editTextTeamwork = findViewById(R.id.CriteriaTeamwork);
        editTextEmployee= findViewById(R.id.enterEmployee);

    }

    private Task<DocumentSnapshot> calculateEvalation() {
        final String email = editTextEmployee.getText().toString().trim();
        final String attendance = editTextAttendance.getText().toString().trim();
        final String leadership = editTextLeadership.getText().toString().trim();
        final String technology = editTextTechnology.getText().toString().trim();
        final String teamwork = editTextTeamwork.getText().toString().trim();
        final int attendanceInt= Integer.parseInt(attendance);
        final int leadershipInt= Integer.parseInt(leadership);
        final int technologyInt= Integer.parseInt(technology);
        final int teamworkInt = Integer.parseInt(teamwork);
        System.out.println(attendanceInt + "att " + leadershipInt + " lead" + technologyInt + "tech " + teamworkInt );
        //sum up all of the evaluation criteria and divide it by number of criteria to get an avg
        double evaluation = (attendanceInt + leadershipInt + technologyInt + teamworkInt) / 4.0;

        // get current employee reference
        return Employee.getEmployeeByEmail(email).addOnCompleteListener((Task<DocumentSnapshot> t) -> {
            if (t.isSuccessful())
            {
                // get the current employee
                Employee e = new Employee(t.getResult());
                // update name in the database
                e.setEval(evaluation).addOnFailureListener((Exception exception) -> {

                    Toast.makeText(EmployeePerformance.this, "edit user performance failed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, exception.toString());
                });
            }
            else {
                // Toast.makeText(EditInfoActivity.this, "edit user info failed", Toast.LENGTH_SHORT).show();
            }
        });

    }



}
