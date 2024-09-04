package beans;

import beans.CartArticle.DeletionStatus;

public class Worker {
	private int id;
	private int userId;
	private int factoryId;
	private DeletionStatus deletionStatus;
	
	
	public Worker(int id,int userId, int factoryId, String deletionStatus) {
		super();
		this.userId=userId;
		this.id = id;
		this.factoryId = factoryId;
		this.deletionStatus =DeletionStatus.valueOf(deletionStatus);
	}

	
	
	public Worker() {}
	
	
	public Worker(int userId, int factoryId) {
		this.userId=userId;
		this.factoryId=factoryId;
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
	public int getFactoryId() {
		return factoryId;
	}
	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}
	public DeletionStatus getDeletionStatus() {
		return deletionStatus;
	}
	public void setDeletionStatus(DeletionStatus deletionStatus) {
		this.deletionStatus = deletionStatus;
	}



	public int getUserId() {
		return userId;
	}



	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
