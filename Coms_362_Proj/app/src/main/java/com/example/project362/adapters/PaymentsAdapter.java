package com.example.project362.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.models.Employee;
import com.example.project362.models.Payment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/*
    The PaymentsAdapter Class creates a scrollable list of Payments for the Admin to see and interact with
 */
public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.PaymentsViewHolder>
{
    private ArrayList<Payment> paymentsList;

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
    }

    @NonNull
    @Override
    public PaymentsAdapter.PaymentsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.payment_card,
                viewGroup, false);
        return new PaymentsViewHolder(v);
    }


    // Iterate through all Payments and create a card for each pending Payment
    // The payment card includes the employee email, the amount of money generated for the payment
    // and two buttons, one for accepting and one for rejecting the Payment
    @Override
    public void onBindViewHolder(@NonNull final PaymentsViewHolder paymentsViewHolder, int i)
    {
        // get the current payment
        Payment currentPayment = paymentsList.get(i);

        // get the employee for the current payment
        currentPayment.getEmployee().get().addOnCompleteListener((Task<DocumentSnapshot> task) -> {
            if (task.isSuccessful()) {
                Employee e = new Employee(task.getResult());
                // set the email Text view to the email of the employee for the current payment
                paymentsViewHolder.email.setText("Employee: " + e.getEmail());
            }
        });

        // set the ammount
        paymentsViewHolder.amount.setText("Money Requested: " + currentPayment.getAmount());

        // button to approve the payment
        paymentsViewHolder.acceptButton.setOnClickListener((View v) ->
        {
            currentPayment.accept().addOnCompleteListener((Task<Void> task) ->
            {
                if (task.isSuccessful())
                    Toast.makeText(v.getContext(), "Payment Approved!",
                            Toast.LENGTH_SHORT).show();
            });
        });

        // button to reject the payment
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
