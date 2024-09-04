package beans;

import beans.CartArticle.DeletionStatus;

public class PurchaseArticle {
	private int id;
	private int purchaseId;
	private int chocolatePriceId;
	private int quantity;
	private DeletionStatus deletionStatus;
	
	public PurchaseArticle(int id, int purchaseId, int chocolatePriceId, int quantity, String deletionStatus) {
		super();
		this.id = id;
		this.purchaseId = purchaseId;
		this.chocolatePriceId = chocolatePriceId;
		this.quantity = quantity;
		this.deletionStatus = DeletionStatus.valueOf(deletionStatus);
	}
	
	
	
	public PurchaseArticle(int purchaseId, int chocolatePriceId, int quantity, DeletionStatus deletionStatus) {
		super();
		this.purchaseId = purchaseId;
		this.chocolatePriceId = chocolatePriceId;
		this.quantity = quantity;
		this.deletionStatus = deletionStatus;
	}



	public PurchaseArticle() {
		super();
		// TODO Auto-generated constructor stub
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPurchaseId() {
		return purchaseId;
	}
	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}
	public int getChocolatePriceId() {
		return chocolatePriceId;
	}
	public void setChocolatePriceId(int chocolatePriceId) {
		this.chocolatePriceId = chocolatePriceId;
	}
	public DeletionStatus getDeletionStatus() {
		return deletionStatus;
	}
	public void setDeletionStatus(DeletionStatus deletionStatus) {
		this.deletionStatus = deletionStatus;
	}
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
	
}
