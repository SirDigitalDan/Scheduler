package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.project362.R;

public class AdminHomeActivity extends AppCompatActivity implements View.OnClickListener {

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
        findViewById(R.id.adminStatusButton).setOnClickListener(AdminHomeActivity.this);

        promote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                


            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.adminStatusButton:
                finish();
                Intent in = new Intent(AdminHomeActivity.this, AdminStatusActivity.class);
                startActivity(in);
                break;
        }
    }
}
