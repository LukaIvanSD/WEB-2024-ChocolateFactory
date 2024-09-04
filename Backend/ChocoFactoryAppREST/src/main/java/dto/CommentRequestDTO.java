package dto;

public class CommentRequestDTO {
	private Integer factoryId;
	private Integer purchaseId;
	private String comment;
	private Integer rating;
	
	public CommentRequestDTO() {}
	public CommentRequestDTO(int factoryId, int purchaseId, String comment, int rating) {
		super();
		this.factoryId = factoryId;
		this.purchaseId = purchaseId;
		this.comment = comment;
		this.rating = rating;
	}
	public Integer getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}
	public Integer getPurchaseId() {
		return purchaseId;
	}
	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
}
