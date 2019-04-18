package com.example.project362.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.activities.AdminViewProjectsActivity;
import com.example.project362.activities.CreateProjectActivity;
import com.example.project362.activities.ViewProjectsActivity;
import com.example.project362.models.Admin;
import com.example.project362.models.Employee;
import com.example.project362.models.Project;
import com.example.project362.models.Shift;

import com.example.project362.models.SwapRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectsViewHolder>
{
    private ArrayList<Project> projectList;
    private DocumentReference currentUser;

    private static final String TAG = "com-s-362-project";

    static class ProjectsViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView name;
        private final TextView deadline;
        private final TextView budget;
        private final TextView employees;

        private final Button budgetButton;
        private final Button deadlineButton;
        private final FloatingActionButton deleteButton;

        ProjectsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.projectName);
            employees = itemView.findViewById(R.id.projectEmployees);
            budget = itemView.findViewById(R.id.projectBudget);
            deadline = itemView.findViewById(R.id.projectDeadline);

            deadlineButton = itemView.findViewById(R.id.deadlineButton);
            budgetButton = itemView.findViewById(R.id.budgetButton);
            deleteButton = itemView.findViewById(R.id.deleteProjectButton);

        }
    }

    public ProjectsAdapter(ArrayList<Project> projects)
    {
        projectList = projects;
        currentUser =
                Employee.getEmployeeReferenceByKey(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    @NonNull
    @Override
    public ProjectsAdapter.ProjectsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.project_card,
                viewGroup, false);
        return new ProjectsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProjectsViewHolder projectsViewHolder, int i)
    {
        final Project currentProject = projectList.get(i);
        projectsViewHolder.name.setText(currentProject.getName());

        String s = DateFormat.getDateInstance().format(currentProject.getDeadline());

        projectsViewHolder.deadline.setText(s);

        projectsViewHolder.employees.setText(this.formatEmployees(currentProject.getEmployees()));
        projectsViewHolder.budget.setText("$" + currentProject.getBudget().get("total").toString());



        projectsViewHolder.deadlineButton.setVisibility(View.GONE);
        projectsViewHolder.budgetButton.setVisibility(View.GONE);
        projectsViewHolder.deleteButton.hide();


        Admin.getAdmins().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getId().equals(currentUser.getId())){
                            projectsViewHolder.deadlineButton.setVisibility(View.VISIBLE);
                            projectsViewHolder.budgetButton.setVisibility(View.VISIBLE);
                            projectsViewHolder.deleteButton.show();
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        projectsViewHolder.deadlineButton.setOnClickListener((final View v) -> {

        });

        projectsViewHolder.deleteButton.setOnClickListener((final View v) -> {
            Project.delete(currentProject.getId());
            projectList.remove(i);
            notifyItemRemoved(i);
        });


        projectsViewHolder.budgetButton.setOnClickListener((final View v) -> {

        });


    }

    //This method will format the list specified so that it displays vertically
    private String formatEmployees(ArrayList<DocumentReference> employees)
    {
        StringBuilder employeesSb = new StringBuilder();
        for (DocumentReference ref : employees)
            employeesSb.append(ref.getId()).append("\n");
        return employeesSb.toString();
    }

    @Override
    public int getItemCount()
    {
        return projectList.size();
    }
}
