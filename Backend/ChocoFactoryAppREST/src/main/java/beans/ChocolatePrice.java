package beans;

import beans.CartArticle.DeletionStatus;



public class ChocolatePrice {

	public enum Validity {Valid, Invalid}
	
	private int id;
	private int chocolateItemId;
	private double price;
	private Validity validityStatus;
	private DeletionStatus deletionStatus;
	
	public Boolean IsValid() {
		return validityStatus==Validity.Valid;
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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Validity getValidityStatus() {
		return validityStatus;
	}
	public void setValidityStatus(Validity validityStatus) {
		this.validityStatus = validityStatus;
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
	
	public ChocolatePrice() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Boolean Delete() {
		if(deletionStatus==DeletionStatus.Deleted)
			return false;
		deletionStatus=DeletionStatus.Deleted;
		return true;
	}
	
	public ChocolatePrice(int id, int chocolateItemId, double price, String validityStatus,
			String deletionStatus) {
		super();
		this.id = id;
		this.chocolateItemId = chocolateItemId;
		this.price = price;
		this.validityStatus = Validity.valueOf(validityStatus);
		this.deletionStatus = DeletionStatus.valueOf(deletionStatus);
	}
	public ChocolatePrice( int chocolateItemId, double price) {

		this.chocolateItemId = chocolateItemId;
		this.price = price;
	}
	public ChocolatePrice(ChocolatePrice chocolatePrice)
	{
		this.id=chocolatePrice.id;
		this.chocolateItemId=chocolatePrice.getChocolateItemId();
		this.deletionStatus=chocolatePrice.getDeletionStatus();
		this.price=chocolatePrice.getPrice();
		this.validityStatus=chocolatePrice.getValidityStatus();
		
	}
	public void Invalidate() {
		this.validityStatus=Validity.Invalid;
		// TODO Auto-generated method stub
		
	}
	public void Validate() {
		// TODO Auto-generated method stub
		this.validityStatus=Validity.Valid;

		
	}
	
	
	
}
