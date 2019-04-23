package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.adapters.ProjectsAdapter;
import com.example.project362.adapters.ShiftsAdapter;
import com.example.project362.models.Project;
import com.example.project362.models.Shift;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewProjectsActivity extends AppCompatActivity implements View.OnClickListener
{
    private RecyclerView recyclerView;

    public ArrayList<Project> projects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_projects);
        recyclerView = findViewById(R.id.projectsRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // get all of the Projects
        Project.getProjects().addOnCompleteListener((Task<QuerySnapshot> task) -> {
            if (task.isSuccessful())
            {
                // add each Project to the Projects arraylist
                for (QueryDocumentSnapshot projectDoc : task.getResult())
                    projects.add(new Project(projectDoc));
                // update the display with the new Projects
                recyclerView.setAdapter(new ProjectsAdapter(projects));
            }
            else
                Toast.makeText(ViewProjectsActivity.this, "No Shifts At This Time!",
                        Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onClick(View view){

    }
}
