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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class EditInfoActivity extends AppCompatActivity implements View.OnClickListener {

    //all of the variables
    EditText editEmail, editPass, editName, editPass2;
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

        //get all of the views
        editName =  findViewById(R.id.editName);
        editEmail =  findViewById(R.id.editEmail);
        editPass =  findViewById(R.id.editPass);
        editPass2 =  findViewById(R.id.editPass2);

        //get button
        findViewById(R.id.buttonSub).setOnClickListener(EditInfoActivity.this);

        //get the email that the user used to register to edit it
        ((EditText) findViewById(R.id.editEmail)).setText(mAuth.getCurrentUser().getEmail());

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

        }
    }
}
