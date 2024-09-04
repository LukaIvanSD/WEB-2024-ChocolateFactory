package beans;

import beans.CartArticle.DeletionStatus;

public class Customer {
	private int id;
	private int userId;
	private int bonusPoints;
	private int customerTypeId;
	private DeletionStatus deletionStatus;

	public Customer()
	{
		
	}
	public Customer(int id, int userId, int bonusPoints, int customerTypeId) {
		this.id = id;
		this.userId = userId;
		this.bonusPoints = bonusPoints;
		this.customerTypeId = customerTypeId;
		deletionStatus=DeletionStatus.Active;
	}
	public Customer(int id, int userId, int bonusPoints, int customerTypeId,String deletionStatus) {
		this.id = id;
		this.userId = userId;
		this.bonusPoints = bonusPoints;
		this.customerTypeId = customerTypeId;
		this.deletionStatus=DeletionStatus.valueOf(deletionStatus);
	}
	
	public Customer( int userId, int bonusPoints, int customerTypeId) {
		this.userId=userId;
		this.bonusPoints=bonusPoints;
		this.customerTypeId=customerTypeId;
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
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBonusPoints() {
		return bonusPoints;
	}
	public void setBonusPoints(int bonusPoints) {
		this.bonusPoints = bonusPoints;
	}
	public int getCustomerTypeId() {
		return customerTypeId;
	}
	public void setCustomerTypeId(int customerTypeId) {
		this.customerTypeId = customerTypeId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public DeletionStatus getDeletionStatus() {
		return deletionStatus;
	}
	public void setDeletionStatus(DeletionStatus deletionStatus) {
		this.deletionStatus = deletionStatus;
	}
	
}
