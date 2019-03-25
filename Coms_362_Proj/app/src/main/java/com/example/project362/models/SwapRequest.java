package com.example.project362.models;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class SwapRequest
{
	private static final String SHIFT_ID = "shift";
	private static final String FROM_ID = "from";
	private static final String TO_ID = "to";

	private static final String COLLECTION = "SwapRequests";

	private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

	private String shiftId;
	private String fromId;
	private String toId;

	public SwapRequest(DocumentSnapshot docSnap)
	{
		this.copyFromDocumentSnapshot(docSnap);
	}

	private void copyFromDocumentSnapshot(DocumentSnapshot doc)
	{

	}
}
