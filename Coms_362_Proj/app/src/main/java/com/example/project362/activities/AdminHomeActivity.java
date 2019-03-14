package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.adapters.ShiftsAdapterAdmin;
import com.example.project362.models.Shift;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminHomeActivity extends AppCompatActivity {

    private static final String TAG = "AdminControls";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public ArrayList<Shift> shifts = new ArrayList<Shift>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Button adminStatusButton = findViewById(R.id.adminStatusButton);

        recyclerView = (RecyclerView) findViewById(R.id.shiftsList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Shifts");

        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(AdminHomeActivity.this, "Firebase Query Success!",
                            Toast.LENGTH_SHORT).show();
                    for (QueryDocumentSnapshot shiftDoc : task.getResult())
                    {
                        Shift s = new Shift(shiftDoc);
                        shifts.add(s);
                    }

                    Toast.makeText(AdminHomeActivity.this,
                            "Query Size " + Integer.toString(shifts.size()), Toast.LENGTH_SHORT).show();
                    mAdapter = new ShiftsAdapterAdmin(shifts);
                    recyclerView.setAdapter(mAdapter);
                }
                else
                {
                    Toast.makeText(AdminHomeActivity.this, "No Shifts At This Time!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adminStatusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                Intent i = new Intent(AdminHomeActivity.this, AdminStatusActivity.class);
                startActivity(i);
            }
        });
    }
}
