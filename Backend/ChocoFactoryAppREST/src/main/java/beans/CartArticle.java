package beans;

import beans.CartArticle.DeletionStatus;

public class CartArticle {

	public enum DeletionStatus{Active, Deleted}
	
	private int id;
	private int chocolateItemId;
	private int cartId;
	private int quantity;
	private DeletionStatus deletionStatus;
	
	
	
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
	
	
	public CartArticle() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public CartArticle(int id, int chocolateItemId, int cartId,int quantity ,String deletionStatus) {
		super();
		this.id = id;
		this.quantity=quantity;
		this.chocolateItemId = chocolateItemId;
		this.cartId = cartId;
		this.deletionStatus = DeletionStatus.valueOf(deletionStatus);
	}
	public CartArticle(int chocolateItemId,int quantity)
	{
		this.chocolateItemId=chocolateItemId;
		this.quantity=quantity;
	}
	public CartArticle( int chocolateItemId, int cartId,int quantity ,String deletionStatus) {
		super();
		this.quantity=quantity;
		this.chocolateItemId = chocolateItemId;
		this.cartId = cartId;
		this.deletionStatus = DeletionStatus.valueOf(deletionStatus);
	}


	public CartArticle( int chocolateItemId, int cartId,int quantity ,DeletionStatus deletionStatus) {
		super();
		this.quantity=quantity;
		this.chocolateItemId = chocolateItemId;
		this.cartId = cartId;
		this.deletionStatus = deletionStatus;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getChocolateItemId() {
		return chocolateItemId;
	}
	public void setChocolateItemId(int chocolateItemId) {
		this.chocolateItemId = chocolateItemId;
	}
	public DeletionStatus getDeletionStatus() {
		return deletionStatus;
	}
	public void setDeletionStatus(DeletionStatus deletionStatus) {
		this.deletionStatus = deletionStatus;
	}



	public int getCartId() {
		return cartId;
	}



	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
