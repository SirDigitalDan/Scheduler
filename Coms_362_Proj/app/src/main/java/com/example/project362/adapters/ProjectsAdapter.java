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
import java.util.HashMap;
import java.util.Map;

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

        private final EditText budgetChange;

        private final Button budgetButton;
        private final Button deadlineButton;
        private final FloatingActionButton deleteButton;

        ProjectsViewHolder(@NonNull View itemView)
        {
            /**
             * Initialize all items in the Project Card view
             */
            super(itemView);
            name = itemView.findViewById(R.id.projectName);
            employees = itemView.findViewById(R.id.projectEmployees);
            budget = itemView.findViewById(R.id.projectBudget);
            deadline = itemView.findViewById(R.id.projectDeadline);
            deadlineButton = itemView.findViewById(R.id.deadlineButton);
            budgetButton = itemView.findViewById(R.id.budgetButton);
            deleteButton = itemView.findViewById(R.id.deleteProjectButton);
            budgetChange = itemView.findViewById(R.id.editTextBudgetDetails);

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
        /**
         * Set up view for each Project card
         */
        final Project currentProject = projectList.get(i);
        projectsViewHolder.name.setText(currentProject.getName());
        String s = DateFormat.getDateInstance().format(currentProject.getDeadline());
        projectsViewHolder.deadline.setText(s);
        projectsViewHolder.employees.setText(this.formatEmployees(currentProject.getEmployees()));
        projectsViewHolder.budget.setText("$" + currentProject.getBudget().get("total").toString());

        /**
         * Hide some buttons that allow admin functions
         */
        projectsViewHolder.deadlineButton.setVisibility(View.GONE);
        projectsViewHolder.budgetButton.setVisibility(View.GONE);
        projectsViewHolder.budgetChange.setVisibility(View.GONE);
        projectsViewHolder.deleteButton.hide();

        /**
         * The Project card will only show admin functions if you are logged in as
         * an Admin, because of this, the system needs to check if you are an Admin.
         *
         * If you are an admin, the system will show buttons that are normally invisible to normal employees
         */
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

        /**
         * Allows the user to change the deadline for the Project.
         */
        projectsViewHolder.deadlineButton.setOnClickListener((final View v) -> {

        });

        /**
         * Delete the current project
         */
        projectsViewHolder.deleteButton.setOnClickListener((final View v) -> {
            Project.delete(currentProject.getId());
            projectList.remove(i);
            notifyItemRemoved(i);
        });

        /**
         * This reveals an input for the user to edit the budget details for the project
         *
         * The input is initially hidden, but gets revealed on push of the button.
         * If the button is clicked while the view is visible, it will take the text in the
         * input and use it to caluclate the new project budget.  It will then update
         * the Database with the new information.
         */
        projectsViewHolder.budgetButton.setOnClickListener((final View v) -> {
            if(projectsViewHolder.budgetChange.getVisibility() == View.GONE){
                projectsViewHolder.budgetChange.setVisibility(View.VISIBLE);
            }
            else{

                String budget = projectsViewHolder.budgetChange.getText().toString();
                String[] values = budget.split(",");

                Map<String, Long> budgetMap = new HashMap<>();
                int k = 0;
                int j = 1;
                long total = 0;
                while(j < values.length){
                    long l = Long.parseLong(values[j].trim());
                    budgetMap.put(values[k], l);
                    k += 2;
                    j += 2;
                    total += l;
                }

                budgetMap.put("total", total);
                currentProject.setBudget(budgetMap);
                projectsViewHolder.budgetChange.setVisibility(View.GONE);

            }
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
