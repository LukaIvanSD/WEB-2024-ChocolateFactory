package beans;

import java.time.LocalDateTime;

import beans.CartArticle.DeletionStatus;

public class BlockedUser {
	private int userId;
	private LocalDateTime  blockDate;
	private DeletionStatus deletionStatus;
	public BlockedUser() {}
	public BlockedUser(int userId, LocalDateTime blockDate,DeletionStatus deletionStatus) {
		super();
		this.userId = userId;
		this.blockDate = blockDate;
		this.setDeletionStatus(deletionStatus);
	}
	public BlockedUser(int userId, LocalDateTime blockDate,String deletionStatus) {
		super();
		this.userId = userId;
		this.blockDate = blockDate;
		this.setDeletionStatus(DeletionStatus.valueOf(deletionStatus));
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public LocalDateTime getBlockDate() {
		return blockDate;
	}
	public void setBlockDate(LocalDateTime blockDate) {
		this.blockDate = blockDate;
	}
	public DeletionStatus getDeletionStatus() {
		return deletionStatus;
	}
	public void setDeletionStatus(DeletionStatus deletionStatus) {
		this.deletionStatus = deletionStatus;
	}
	public Boolean IsActive() {
		// TODO Auto-generated method stub
		return deletionStatus==DeletionStatus.Active;
	}
	public Boolean Delete() {
		if(deletionStatus==DeletionStatus.Deleted)
			return false;
		deletionStatus=DeletionStatus.Deleted;
		return true;
	}
	
}

