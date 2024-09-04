package dto;

import beans.User;

public class AdminProfileDTO {

	private User user;

	public AdminProfileDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminProfileDTO(User user) {
		super();
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	
}
