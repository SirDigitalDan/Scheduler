package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.project362.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private DocumentReference currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Goes to the page which will let the Employee set their availability
        findViewById(R.id.button_send2).setOnClickListener(ProfileActivity.this);


        currentUser =
                db.collection("Employees").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    private void getAllUsers() {
        // [START get_all_Shifts]
        db.collection("Shifts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                             //   Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {

                           // Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        // [END get_all_users]
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_send2:
                finish();
                Intent in1 = new Intent(ProfileActivity.this, CalendarActivity.class);
                startActivity(in1);
                break;
        }
    }
}
