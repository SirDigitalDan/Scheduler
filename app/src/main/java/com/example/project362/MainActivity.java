package com.example.project362;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements OnClickListener{
    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth= FirebaseAuth.getInstance();
        findViewById(R.id.button_send).setOnClickListener(MainActivity.this);
        Button btnClickMe = (Button) findViewById(R.id.button_send);
        btnClickMe.setOnClickListener(MainActivity.this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword=  findViewById(R.id.editTextPassword);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    private void userLogin(){

        String email= editTextEmail.getText().toString();
        String password= editTextPassword.getText().toString();
        if(email.isEmpty())
        {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("please enter valid email");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty())
        {
            editTextPassword.setError("password is required");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError("Enter password that is atleast 6 characters long");
            editTextPassword.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(getApplicationContext(),"User Register Succsesful"), Toast.LENGTH_SHORT);
                    Toast.makeText(MainActivity.this, "User Register Succsesful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                Toast.makeText(MainActivity.this, "User Register Succsesful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, profileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    });

    }

        @Override
        public void onClick (View view ){

        switch(view.getId()){
            case R.id.button_send:
                finish();
                // Intent i = new Intent(this, SignUpActivity.class);
                //startActivity(i);
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
                //startActivity(new Intent(this, SignUpActivity.class));
                break;


           case R.id.buttonLogin:
                userLogin();
                break;

            }



    }
}
