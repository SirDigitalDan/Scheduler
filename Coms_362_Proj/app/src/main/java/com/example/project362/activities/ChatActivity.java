package com.example.project362.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.project362.R;
import com.example.project362.models.Employee;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText userEmail;
    private String currentUser;
        private EditText userMessage;
    FirebaseAuth mAuth;
    private Button sendMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        userEmail = findViewById(R.id.editTextUser);
        userMessage = findViewById(R.id.editTextMessage);
        sendMessage= findViewById(R.id.sendMessage);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.sendMessage).setOnClickListener(ChatActivity.this);
        currentUser = mAuth.getCurrentUser().getEmail();
    }

    public void sendMessage()
    {
        String emails = userEmail.getText().toString();
        String message = userMessage.getText().toString();
        // get the current user's employee object
        Employee.getEmployeeByEmail(emails).addOnCompleteListener((Task<DocumentSnapshot> t) -> {
            if (t.isSuccessful() && t.getResult() != null)
            {
                //creates new employee Object
                Employee e = new Employee(t.getResult());
                String messageEdit = "From: " + currentUser + " Message content - " + message;
                // add date to the availability
                e.addMessage(messageEdit);
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.sendMessage:
                sendMessage();
                break;

        }
    }
}
