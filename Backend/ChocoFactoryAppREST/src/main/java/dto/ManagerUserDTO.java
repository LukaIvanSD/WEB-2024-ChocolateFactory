package dto;

import beans.Manager;
import beans.User;

public class ManagerUserDTO {
	private User user;
	private Manager manager;
	
	public ManagerUserDTO() {}
	public ManagerUserDTO(User user, Manager manager) {
		super();
		this.user = user;
		this.manager = manager;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Manager getManager() {
		return manager;
	}
	public void setManager(Manager manager) {
		this.manager = manager;
	}
	
}
