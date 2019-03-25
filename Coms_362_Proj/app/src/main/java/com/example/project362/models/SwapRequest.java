package com.example.project362.models;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SwapRequest
{
	private static final String SHIFT = "shiftId";
	private static final String FROM = "fromId";
	private static final String TO = "toId";

	private static final String COLLECTION = "SwapRequests";

	private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

	private DocumentReference shift;
	private DocumentReference from;
	private DocumentReference to;

	public SwapRequest(DocumentSnapshot docSnap)
	{
		this.copyFromDocumentSnapshot(docSnap);
	}

	public SwapRequest(DocumentReference shift, DocumentReference from, DocumentReference to)
	{
		this.shift = shift;
		this.from = from;
		this.to = to;
	}

	public SwapRequest(String shiftId, String fromId, String toId)
	{
		this.shift = Shift.getShiftReferenceByKey(shiftId);
		this.from = Employee.getEmployeeReferenceByKey(fromId);
		this.to = Employee.getEmployeeReferenceByKey((toId);
	}

	public boolean isValid()
	{
		return false;
	}

	private void copyFromDocumentSnapshot(DocumentSnapshot doc)
	{
		this.shift = doc.getDocumentReference(SHIFT);
		this.from = doc.getDocumentReference(FROM);
		this.to = doc.getDocumentReference(TO);
	}
}
