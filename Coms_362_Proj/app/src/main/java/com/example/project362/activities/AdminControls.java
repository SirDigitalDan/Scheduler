package com.example.project362.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project362.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminControls extends AppCompatActivity {

    private static final String TAG = "AdminControls";
    Button promote;
    EditText ememail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_controls);

        ememail = findViewById(R.id.emailtopromote);
        promote = findViewById(R.id.promoteButton);

        promote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String employeeEmail = ememail.getText().toString();

                try {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();




                } catch (Exception e) {
                    Toast.makeText(AdminControls.this,
                            "Doesn't look like that employee exists, please try again.",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
