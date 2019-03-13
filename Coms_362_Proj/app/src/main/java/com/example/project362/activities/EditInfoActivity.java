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

    EditText editEmail, editPass, editName, editPass2;
    Button editSub;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        mAuth = FirebaseAuth.getInstance();
        editName =  findViewById(R.id.editName);
        editEmail =  findViewById(R.id.editEmail);
        editPass =  findViewById(R.id.editPass);
        editPass2 =  findViewById(R.id.editPass2);
        findViewById(R.id.buttonSub).setOnClickListener(EditInfoActivity.this);

        ((EditText) findViewById(R.id.editEmail)).setText(mAuth.getCurrentUser().getEmail());

    }

    private void updateUser() {
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        final String password = editPass.getText().toString().trim();
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
        else
        {
            //mAuth.getCurrentUser().updateEmail(email);
            //mAuth.getCurrentUser().updatePassword(password);
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
                    else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
            /*
            mAuth.getCurrentUser().updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(EditInfoActivity.this, "User Pass Succsesful", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
            */
        }


    }

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
