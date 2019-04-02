package com.example.project362.adapters;

import android.support.annotation.NonNull;
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
import com.example.project362.models.Employee;
import com.example.project362.models.Payment;
import com.example.project362.models.Shift;

import com.example.project362.models.SwapRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.PaymentsViewHolder>
{
    private ArrayList<Payment> paymentsList;
    private DocumentReference currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "com-s-362-project";

    static class PaymentsViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView amount;
        private final TextView email;

        private final Button acceptButton;
        private final Button rejectButton;

        PaymentsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            amount = itemView.findViewById(R.id.paymentAmount);
            email = itemView.findViewById(R.id.paymentEmployeeEmail);

            acceptButton = itemView.findViewById(R.id.acceptPaymentButton);
            rejectButton = itemView.findViewById(R.id.rejectPaymentButton);
        }
    }

    public PaymentsAdapter(ArrayList<Payment> payments)
    {
        paymentsList = payments;

        currentUser = Employee.getEmployeeReferenceByKey(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    @NonNull
    @Override
    public PaymentsAdapter.PaymentsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.payment_card,
                viewGroup, false);
        return new PaymentsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PaymentsViewHolder paymentsViewHolder, int i)
    {


        Payment currentPayment = paymentsList.get(i);

        currentPayment.getEmployee().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    paymentsViewHolder.email.setText("Employee: " + document.getString("email"));
                }
            }
        });

        paymentsViewHolder.amount.setText("Money Requested: " + currentPayment.getAmount());


        paymentsViewHolder.acceptButton.setOnClickListener((View v) ->
        {

            currentPayment.accept().addOnCompleteListener((Task<Void> task) ->
            {
                if (task.isSuccessful())
                    Toast.makeText(v.getContext(), "Payment Approved!",
                            Toast.LENGTH_SHORT).show();
            });


        });
        paymentsViewHolder.rejectButton.setOnClickListener((View v) ->
        {

            currentPayment.reject().addOnCompleteListener((Task<Void> task) ->
            {
                if (task.isSuccessful())
                    Toast.makeText(v.getContext(), "Payment Approved!",
                            Toast.LENGTH_SHORT).show();
            });


        });





    }



    @Override
    public int getItemCount()
    {
        return paymentsList.size();
    }
}
