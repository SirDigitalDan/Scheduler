package com.example.project362.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.project362.R;
import com.example.project362.models.Shift;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;

public class ShiftActivity extends AppCompatActivity {

	private static final String TAG = "com-s-362-project-shift";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);
    }

    protected void onStart()
    {
        super.onStart();
        Task<DocumentSnapshot> getShift = Shift.getShiftByKey("xEYLlRf3GbYQDFHKwu8I");
        getShift.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
	        @Override
	        public void onComplete(@NonNull Task<DocumentSnapshot> task)
	        {
	        	if (task.isSuccessful() && task.getResult() != null)
					testShift(new Shift(task.getResult()));
	        	else
			        Toast.makeText(ShiftActivity.this, "Error", Toast.LENGTH_SHORT).show();
	        }
        });
    }

    public void testShift(final Shift s)
    {
		logShift(s);
		s.setStartTime(new Date("3/11/2018")).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task)
			{
				Log.i(TAG, "finished");
				logShift(s);
			}
		});

    }

    public void logShift(Shift s)
    {
	    Log.i(TAG, s.getId());
	    Log.i(TAG, s.getNote());
	    Log.i(TAG, s.getEmployees().toString());
	    Log.i(TAG, s.getEndTime().toString());
	    Log.i(TAG, s.getStartTime().toString());
    }
}
