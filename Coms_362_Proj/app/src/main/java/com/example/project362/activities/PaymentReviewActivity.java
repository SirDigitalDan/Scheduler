package com.example.project362.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.adapters.PaymentsAdapter;
import com.example.project362.adapters.ShiftsAdapter;
import com.example.project362.models.Payment;
import com.example.project362.models.Shift;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/*
    This activity is a page where an Admin can check all of the Payment statements that are awaiting approval

    It filters out all of the payments and shows only Payments that are pending.

    It displays them in a scrollable list, and allows for approval or rejection
 */

public class PaymentReviewActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;

    public ArrayList<Payment> payments = new ArrayList<Payment>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_review);
        recyclerView = findViewById(R.id.paymentsList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);


        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        /// Get all Payments that are pending and add to an arraylist to pass to an adapter
        Payment.getPayments().addOnCompleteListener((Task<QuerySnapshot> task) -> {
            if (task.isSuccessful())
            {
                for (QueryDocumentSnapshot paymentDoc : task.getResult()){
                    if(paymentDoc.getLong("status") == 0){
                        payments.add(new Payment(paymentDoc));
                    }
                }

                ///Create scrollable list
                recyclerView.setAdapter(new PaymentsAdapter(payments));
            }
            else
                Toast.makeText(PaymentReviewActivity.this, "No Payments To Review!",
                        Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    public void onClick(View view) {

    }
}