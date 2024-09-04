package dto;

import beans.User;

public class WorkerProfileDTO {

	private User user;
	private String factoryName;
	public WorkerProfileDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WorkerProfileDTO(User user, String factoryName) {
		super();
		this.user = user;
		this.factoryName = factoryName;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	
	
	
}
