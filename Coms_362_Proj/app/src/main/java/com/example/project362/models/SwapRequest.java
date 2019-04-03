package com.example.project362.models;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SwapRequest
{
	public enum Status {
		PENDING("PENDING", 0), ACCEPTED("ACCEPTED", 1), REJECTED("REJECTED", 2);

		private final int value;
		private final String desc;

		Status(String desc, int value)
		{
			this.desc = desc;
			this.value = value;
		}

		public int getValue()
		{
			return this.value;
		}

		public String toString()
		{
			return this.desc;
		}

		public static Status getStatus(int i)
		{
			return Status.values()[i];
		}
	}

	private static final String SHIFT = "shiftId";
	private static final String FROM = "fromId";
	private static final String TO = "toId";
	private static final String STATUS = "status";

	private static final String COLLECTION = "SwapRequests";

	private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

	// the shift the request pertains to
	private DocumentReference shift;

	// the employee making the request
	private DocumentReference from;

	// the employee requested to swap
	private DocumentReference to;

	// the status of this swap request: 0 if pending, 1 if accepted, 2 if rejected
	private int status;

	private String key;

	public SwapRequest(DocumentSnapshot docSnap)
	{
		this.copyFromDocumentSnapshot(docSnap);
	}

	public SwapRequest(DocumentReference shift, DocumentReference from, DocumentReference to,
	                   int status)
	{
		this.shift = shift;
		this.from = from;
		this.to = to;
		this.status = status;
	}

	public SwapRequest(DocumentReference shift, DocumentReference from, DocumentReference to)
	{
		this.shift = shift;
		this.from = from;
		this.to = to;
		this.status = Status.PENDING.getValue();
	}

	public SwapRequest(String shiftId, String fromId, String toId)
	{
		this.shift = Shift.getShiftReferenceByKey(shiftId);
		this.from = Employee.getEmployeeReferenceByKey(fromId);
		this.to = Employee.getEmployeeReferenceByKey(toId);
	}


	public DocumentReference getFrom()
	{
		return this.from;
	}

	public DocumentReference getTo()
	{
		return this.to;
	}

	public int getStatus()
	{
		return status;
	}

	public DocumentReference getShift()
	{
		return this.shift;
	}

	public Task<DocumentSnapshot> accept()
	{
		return Shift.swapEmployees(this.shift, this.from, this.to).addOnCompleteListener((Task<DocumentSnapshot> t) -> {
			this.status = Status.ACCEPTED.getValue();
			this.update(STATUS, this.status);
		});
	}

	public Task<Void> reject()
	{
		this.status = Status.REJECTED.getValue();
		return this.update(STATUS, this.status);
	}

	/**
	 * Ensures that the shift being swapped is valid
	 * @return true if the shift swap request is a valid request, false otherwise
	 */
	public static boolean isValid(Shift s, DocumentReference from, DocumentReference to)
	{
		// make sure that
		// 1. the from employee is assigned to the shift
		// 2. the to employee is not already assigned to the shift
		return (s.getEmployees().contains(from) && !s.getEmployees().contains(to));
	}

	private void copyFromDocumentSnapshot(DocumentSnapshot src)
	{
		this.key = src.getId();
		this.shift = src.getDocumentReference(SHIFT);
		this.from = src.getDocumentReference(FROM);
		this.to = src.getDocumentReference(TO);
		this.status = (int) (long) src.get(STATUS);
	}

	public static Task<QuerySnapshot> getSwapRequests()
	{
		return db.collection(COLLECTION).get();
	}

	public static Task<DocumentSnapshot> getSwapRequestByKey(String key)
	{
		return db.collection(COLLECTION).document(key).get();
	}

	public static DocumentReference getSwapRequestReferenceByKey(String key)
	{
		return db.collection(COLLECTION).document(key);
	}

	public static Task<Void> delete(String key)
	{
		return db.collection(COLLECTION).document(key).delete();
	}

	public Task<DocumentReference> create()
	{
		HashMap<String, Object> h = new HashMap<>();
		h.put(SHIFT, this.shift);
		h.put(FROM, this.from);
		h.put(TO, this.to);
		h.put(STATUS, this.status);
		return db.collection(COLLECTION).add(h);
	}

	private Task<Void> update(String field, final Object datum)
	{
		Map<String, Object> data = new HashMap<>();
		data.put(field, datum);
		return db.collection(COLLECTION).document(this.key).update(data);
	}

	public void temp()
	{
	}
}
