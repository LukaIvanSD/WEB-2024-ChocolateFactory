package beans;

import java.time.LocalDate;
import java.time.LocalDateTime;

import beans.CartArticle.DeletionStatus;

public class Comment {
	public enum Status{
		Approved,
		Declined,
		InProcess
	}
	private int Id;
	private int factoryId;
	private int purchaseId;
	private String text;
	private int rating;
	private Status status;
	private LocalDate dateCreated;
	private DeletionStatus deletionStatus;
	
	public Comment() {}
	
	public Comment(int id,int factoryId,int purchaseId, String text, int rating,LocalDate dateCreated, String status,
			String deletionStatus) {
		super();
		this.setDateCreated(dateCreated);
		Id = id;
		this.factoryId=factoryId;
		this.purchaseId=purchaseId;
		this.text = text;
		this.rating = rating;
		this.status = Status.valueOf(status);
		this.deletionStatus = DeletionStatus.valueOf(deletionStatus);
	}
	public Comment(int factoryId,int purchaseId, String text, int rating,LocalDate dateCreated, Status status,
			DeletionStatus deletionStatus) {
		super();
		this.setDateCreated(dateCreated);
		this.factoryId=factoryId;
		this.purchaseId=purchaseId;
		this.text = text;
		this.rating = rating;
		this.status = status;
		this.deletionStatus = deletionStatus;
	}

	public Boolean IsActive()
	{
		return deletionStatus==DeletionStatus.Active;
	}
	
	public Boolean Delete() {
		if(deletionStatus==DeletionStatus.Deleted)
			return false;
		deletionStatus=DeletionStatus.Deleted;
		return true;
	}
	
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public DeletionStatus getDeletionStatus() {
		return deletionStatus;
	}
	public void setDeletionStatus(DeletionStatus deletionStatus) {
		this.deletionStatus = deletionStatus;
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}

	public int getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}

	public int getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}

	public Boolean IsApproved() {
		// TODO Auto-generated method stub
		return status==Status.Approved;
	}
	
}
