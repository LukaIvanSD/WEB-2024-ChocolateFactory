package beans;

import beans.CartArticle.DeletionStatus;

public class Manager{
	private int id;
	private int userId;
	private int factoryId;
	private DeletionStatus deletionStatus;
	
	public Manager()
	{}
	public Manager(int id, int userId, int factoryId) {
		this.id=id;
		this.userId=userId;
		this.factoryId=factoryId;
		deletionStatus=DeletionStatus.Active;
	}
	
	public Manager(int id, int userId, int factoryId,String deletionStatus) {
		this.id=id;
		this.userId=userId;
		this.factoryId=factoryId;
		this.deletionStatus=DeletionStatus.valueOf(deletionStatus);
	}
	
	public Manager(int userId, int factoryId) {
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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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
	
	
}
