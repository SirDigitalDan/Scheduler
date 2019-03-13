package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.adapters.ShiftsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public ArrayList<Object> shifts = new ArrayList<Shift>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = (RecyclerView) findViewById(R.id.shiftsList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        //get onclikc for floating action button
        findViewById(R.id.floatingActionButtonEdit).setOnClickListener(HomeActivity.this);


        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Shifts");


        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(HomeActivity.this, "Firebase Query Success!", Toast.LENGTH_SHORT).show();
                            for (QueryDocumentSnapshot shiftDoc : task.getResult()) {
                                Shift s = new Shift(shiftDoc.getId(), shiftDoc.getTimestamp("startTime").toDate(), shiftDoc.getTimestamp("endTime").toDate(), shiftDoc.getString("note"));


                                shifts.add(s);

                            }

                            Toast.makeText(HomeActivity.this, "Query Size " + Integer.toString(shifts.size()), Toast.LENGTH_SHORT).show();
                            mAdapter = new ShiftsAdapter(shifts);
                            recyclerView.setAdapter(mAdapter);
                        } else {

                            Toast.makeText(HomeActivity.this, "No Shifts At This Time!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });



    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.floatingActionButtonEdit:
                finish();
                Intent i = new Intent(this, EditInfoActivity.class);
                startActivity(i);
                break;
        }
    }
}
