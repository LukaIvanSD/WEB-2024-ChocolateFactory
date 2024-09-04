package dto;

import java.time.LocalDate;

import beans.Comment;
import beans.Comment.Status;
import beans.Customer;
import beans.User;

public class CommentDTO {
	private int id;
	private int creatorId;
	private String text;
	private double rating;
	private Status status;
	private LocalDate dateCreated;
	private String creatorName;
	public CommentDTO() {}
	
	public CommentDTO(Comment comment,User creator) {
		this.id=comment.getId();
		text=comment.getText();
		rating=comment.getRating();
		status=comment.getStatus();
		dateCreated=comment.getDateCreated();
		this.creatorName=creator.getFirstName()+" "+creator.getLastName();
		this.creatorId=creator.getId();
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public LocalDate getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public int getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}
	
}
