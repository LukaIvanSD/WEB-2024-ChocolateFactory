package dto;

import beans.User;

public class ManagerProfileDTO {
	
	private User user;
	private String factoryName;
	public ManagerProfileDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ManagerProfileDTO(User user, String factoryName) {
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
