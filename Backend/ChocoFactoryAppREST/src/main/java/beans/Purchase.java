package beans;

import java.time.LocalDateTime;

import beans.CartArticle.DeletionStatus;

public class Purchase {
	
	public enum PurchaseStatus{
		Inprocess,
		Approved,
		Rejected,
		Cancelled
	}
	
	private int id;
	private LocalDateTime  purchaseDate;
	private double price;
	private int buyerId;
	private PurchaseStatus purchaseStatus;
	private DeletionStatus deletionStatus;
	
	
	
	public Purchase() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Purchase(int id, LocalDateTime purchaseDate, double price, int buyerId, String purchaseStatus,
			String deletionStatus) {
		super();
		this.id = id;
		this.purchaseDate = purchaseDate;
		this.price = price;
		this.buyerId = buyerId;
		this.purchaseStatus = PurchaseStatus.valueOf(purchaseStatus);
		this.deletionStatus = DeletionStatus.valueOf(deletionStatus);
	}
	
	public Purchase(LocalDateTime purchaseDate, double price, int buyerId, PurchaseStatus purchaseStatus,
			DeletionStatus deletionStatus) {
		super();
		this.purchaseDate = purchaseDate;
		this.price = price;
		this.buyerId = buyerId;
		this.purchaseStatus = purchaseStatus;
		this.deletionStatus = deletionStatus;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDateTime getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(LocalDateTime purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(int buyerId) {
		this.buyerId = buyerId;
	}
	public PurchaseStatus getPurchaseStatus() {
		return purchaseStatus;
	}
	public void setPurchaseStatus(PurchaseStatus purchaseStatus) {
		this.purchaseStatus = purchaseStatus;
	}
	public DeletionStatus getDeletionStatus() {
		return deletionStatus;
	}
	public void setDeletionStatus(DeletionStatus deletionStatus) {
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
	public boolean IsApproved() {
		// TODO Auto-generated method stub
		return purchaseStatus==PurchaseStatus.Approved;
	}
	public Boolean Approve() {
		// TODO Auto-generated method stub
		if(purchaseStatus!=PurchaseStatus.Inprocess)
			return false;
		purchaseStatus=PurchaseStatus.Approved;
		return true;
			
		
	}
	public Boolean Reject() {
		if(purchaseStatus!=PurchaseStatus.Inprocess)
			return false;
		purchaseStatus=PurchaseStatus.Rejected;
		return true;
	}
	
}
