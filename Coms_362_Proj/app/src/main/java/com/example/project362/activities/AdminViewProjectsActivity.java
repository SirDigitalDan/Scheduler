package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.example.project362.R;
import com.example.project362.adapters.ProjectsAdapter;
import com.example.project362.models.Project;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class AdminViewProjectsActivity extends AppCompatActivity implements View.OnClickListener
{
    private RecyclerView recyclerView;
    private FloatingActionButton createButton;
    public ArrayList<Project> projects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_projects);
        recyclerView = findViewById(R.id.adminProjectsList);
        createButton = findViewById(R.id.createProjectButton);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // get all of the Projects and put them in the view
        Project.getProjects().addOnCompleteListener((Task<QuerySnapshot> task) -> {
            if (task.isSuccessful())
            {
                // add each Project to the Project arraylist
                for (QueryDocumentSnapshot projectDoc : task.getResult())
                    projects.add(new Project(projectDoc));
                // update the display with the new Projects

                recyclerView.setAdapter(new ProjectsAdapter(projects));
            }
            else
                Toast.makeText(AdminViewProjectsActivity.this, "No Shifts At This Time!",
                        Toast.LENGTH_SHORT).show();
        });


    }

    public void onStart()
    {
        super.onStart();

        // This will bring you to a new activity allowing you to Create a Project
        //Floating Action Button
        this.createButton.setOnClickListener((View v) -> {
            Intent i = new Intent(this, CreateProjectActivity.class);
            startActivity(i);
        });


    }

    @Override
    public void onClick(View view){

    }
}

