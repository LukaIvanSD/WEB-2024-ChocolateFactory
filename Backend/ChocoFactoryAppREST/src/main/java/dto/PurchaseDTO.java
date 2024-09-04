package dto;

import java.util.List;

import beans.Comment;
import beans.Purchase;
import beans.User;

public class PurchaseDTO {

	private Purchase purchase;
	private List<PurchaseArticleDTO> items;
	private String factoryName;
	private int factoryId;
	private User buyer;
	private Comment comment;
	
	
	public User getBuyer() {
		return buyer;
	}




	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}




	public PurchaseDTO(Purchase purchase, List<PurchaseArticleDTO> items, String factoryName,int factoryId, User buyer,Comment comment) {
		super();
		this.purchase = purchase;
		this.items = items;
		this.factoryName = factoryName;
		this.factoryId=factoryId;
		this.buyer = buyer;
		this.comment=comment;
	}




	public PurchaseDTO() {
		super();
		// TODO Auto-generated constructor stub
	}


	

	public String getFactoryName() {
		return factoryName;
	}




	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}




	public PurchaseDTO(Purchase purchase, List<PurchaseArticleDTO> items, String factoryName,int factoryId,Comment comment) {
		super();
		this.purchase = purchase;
		this.items = items;
		this.factoryName = factoryName;
		this.factoryId=factoryId;
		this.comment=comment;
	}



	public Purchase getPurchase() {
		return purchase;
	}



	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}



	public List<PurchaseArticleDTO> getItems() {
		return items;
	}



	public void setItems(List<PurchaseArticleDTO> items) {
		this.items = items;
	}




	public int getFactoryId() {
		return factoryId;
	}




	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}




	public Comment getComment() {
		return comment;
	}




	public void setComment(Comment comment) {
		this.comment = comment;
	}

	


}
