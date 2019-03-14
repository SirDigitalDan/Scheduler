package com.example.project362.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project362.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminHomeActivity extends AppCompatActivity {

    private static final String TAG = "AdminControls";
    Button promote;
    EditText ememail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        ememail = findViewById(R.id.emailtopromote);
        promote = findViewById(R.id.promoteButton);

        promote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                


            }
        });
    }
}
