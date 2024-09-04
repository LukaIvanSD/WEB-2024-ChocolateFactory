package beans;

import beans.CartArticle.DeletionStatus;

public class ChocolateItem {
	
	public enum Availability{
		Available,
		NotAvailable
	}
	
	
	private int id;
	private int chocolateId;
	private Availability availability;
	private int quantity;
	private int factoryId;
	private DeletionStatus deletionStatus;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getChocolateId() {
		return chocolateId;
	}
	public void setChocolateId(int chocolateId) {
		this.chocolateId = chocolateId;
	}
	public DeletionStatus getDeletionStatus() {
		return deletionStatus;
	}
	public void setDeletionStatus(DeletionStatus deletionStatus) {
		this.deletionStatus = deletionStatus;
	}
	public void setAvailability(Availability availability) {
		this.availability = availability;
	}
	public Availability getAvailability() {
	     return this.availability = (quantity > 0) ? Availability.Available : Availability.NotAvailable;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}
	
	public ChocolateItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ChocolateItem(int id, int chocolateId, int quantity, int factoryId, String deletionStatus) {
		super();
		this.id = id;
		this.availability = (quantity > 0) ? Availability.Available : Availability.NotAvailable;
		this.chocolateId = chocolateId;
		this.quantity = quantity;
		this.factoryId = factoryId;
		this.deletionStatus = DeletionStatus.valueOf(deletionStatus);
	}
	
	public ChocolateItem(int chocolateId, int quantity, int factoryId) {
		this.availability = (quantity > 0) ? Availability.Available : Availability.NotAvailable;
		this.chocolateId = chocolateId;
		this.quantity = quantity;
		this.factoryId = factoryId;
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
	public boolean IsAvailable() {
		return getAvailability()==Availability.Available;
	}
	
	
}
