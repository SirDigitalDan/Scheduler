package com.example.project362.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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


public class EditInfoActivity extends AppCompatActivity implements View.OnClickListener {

    //all of the variables
    EditText editEmail, editPass, editName, editPass2,userEmail;
    //button to submit the information
    Button editSub;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        //get the instances of the employee user authroization
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //get all of the views
        editName =  findViewById(R.id.editName);
        editEmail =  findViewById(R.id.editEmail);
        editPass =  findViewById(R.id.editPass);
        editPass2 =  findViewById(R.id.editPass2);
        userEmail = findViewById(R.id.deleteUser);

        //get button
        findViewById(R.id.buttonSub).setOnClickListener(EditInfoActivity.this);
        findViewById(R.id.buttonSignout).setOnClickListener(EditInfoActivity.this);
        findViewById(R.id.buttonDelete).setOnClickListener(EditInfoActivity.this);
        findViewById(R.id.buttonAdd).setOnClickListener(EditInfoActivity.this);
        ((EditText) findViewById(R.id.editEmail)).setText(mAuth.getCurrentUser().getEmail());

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
                                                    Toast.makeText(EditInfoActivity.this, "Admin Created", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(EditInfoActivity.this, "Failed to create", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        } else {
                            Toast.makeText(EditInfoActivity.this, "Employee not found in the Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        }




    private void updateUser() {
        //get the input of what the employee wants to edit
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        final String password = editPass.getText().toString().trim();
        String password2 = editPass2.getText().toString().trim();

        //checks to see if there are errors and then records the errors
        if (name.isEmpty()) {
            editName.setError("Name is required");
            editName.requestFocus();

            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Please enter a valid email");
            editEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editPass.setError("Password is required");
            editPass.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editPass.setError("Minimum length of password should be 6");
            editPass.requestFocus();
            return;
        }
        if (password2.isEmpty()) {
            editPass2.setError("Please re-enter password");
            editPass2.requestFocus();
            return;
        }
        if (!password2.equals(password)) {
            editPass2.setError("The passwords do not match");
            editPass2.requestFocus();
            return;
        }
        else
        {
            //authorizes if the email and password is correct
            mAuth.getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(EditInfoActivity.this, "User Email Succsesful", Toast.LENGTH_SHORT).show();
                        mAuth.getCurrentUser().updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(EditInfoActivity.this, "User Pass Succsesful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(EditInfoActivity.this, profileActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    //shows errors
                    else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }


    }

    //button to submit and save edits
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.buttonSub:
                finish();
                updateUser();
                break;
            case R.id.buttonSignout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(EditInfoActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditInfoActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.buttonDelete:
                finish();
                deleteUser();
                break;
            case R.id.buttonAdd:
                finish();
                createUser();
                break;

        }
    }
}
