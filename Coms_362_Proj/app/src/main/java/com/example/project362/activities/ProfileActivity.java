package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.project362.R;
import com.example.project362.models.Shift;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private String currentUser;
    private ListView lv;
    FirebaseAuth mAuth;
    //TextView testID;
    EditText testID;
    ArrayList<String> upcomingShifts;
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
        testID  =   findViewById(R.id.editID);
        upcomingShifts= new ArrayList<>();
        currentUser = mAuth.getCurrentUser().getEmail();

    }

    private void getAllUsers() {
        // [START get_all_Shifts]


        CollectionReference shiftCollection= db.collection("Shifts");
        Query shiftQuery = shiftCollection.whereEqualTo("ID", "Shift2");

        shiftQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document: task.getResult()){

                        Shift s= new Shift(document);
                        //s.copyFromDocumentSnapshot(document);
                        //Shift s = document.toObject(Shift.class);
                        ArrayList<DocumentReference> emps= s.getEmployees();

                        for (int i=0; i <emps.size(); i++)
                        {
                            if(currentUser.equals(emps.get(i).getId()))
                            {
                                String ids = s.getId();
                                upcomingShifts.add(ids);

                            }

                        }
                        //String ids = s.getId();
                        //testID.setText(ids);
                        if(upcomingShifts.size()>0) {
                            String lol = upcomingShifts.get(0);
                            testID.setText(lol);
                        }
                    }
                }
                else
                {
                    //No error if nothing is found
                }
            }


        });
        lister();
        //Query shiftsQuery = shiftCollection.whereEqualTo("ok");
        //Query shiftQuery = shiftCollection.where
    }
    public void lister()
    {
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
            case R.id.button_send3:
                getAllUsers();
                //lister();
                break;
        }
    }
}
