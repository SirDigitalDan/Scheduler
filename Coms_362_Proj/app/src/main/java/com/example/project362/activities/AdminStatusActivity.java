package com.example.project362.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project362.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AdminStatusActivity extends AppCompatActivity implements View.OnClickListener {
    EditText userEmail;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_status);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        userEmail = findViewById(R.id.employeeEmail);

        findViewById(R.id.grantAdmin).setOnClickListener(AdminStatusActivity.this);
        findViewById(R.id.deleteEmployee).setOnClickListener(AdminStatusActivity.this);
    }
    private void deleteUser(){

        String em = userEmail.getText().toString().trim();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference ref = db.collection("Employees");
        DocumentReference em1 = ref.document(em);

        em1.delete();


    }
    private void createUser()
    {
        final String ema = userEmail.getText().toString().trim();
        final FirebaseFirestore db1 = FirebaseFirestore.getInstance().getInstance();
        final CollectionReference ref = db1.collection("Employees");
        DocumentReference em1 = ref.document(ema);

        db1.collection("Employees")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId().equals(ema))
                                {
                                    Map<String, Object> city = new HashMap<>();
                                    city.put("email", ema);


                                    db1.collection("Admins").document(ema)
                                            .set(city)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(AdminStatusActivity.this, "Admin Created", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(AdminStatusActivity.this, "Failed to create", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        } else {
                            Toast.makeText(AdminStatusActivity.this, "Employee not found in the Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.deleteEmployee:
                finish();
                deleteUser();
                break;
            case R.id.grantAdmin:
                finish();
                createUser();
                break;



        }


    }
}
