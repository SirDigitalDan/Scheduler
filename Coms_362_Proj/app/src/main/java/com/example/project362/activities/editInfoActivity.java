package com.example.project362.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
public class editInfoActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editEmail, editPass, editName, editPass2;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = (EditText) findViewById(R.id.editName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPass = (EditText) findViewById(R.id.editPass);
        editPass2 = (EditText) findViewById(R.id.editPass2);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.editEmail)
        mAuth.auth();


    }

    private void updateUser() {
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPass.getText().toString().trim();
        String password2 = editPass2.getText().toString().trim();

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


    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.buttonSignUp:
                updateUser();User();
                break;

        }
    }
}
