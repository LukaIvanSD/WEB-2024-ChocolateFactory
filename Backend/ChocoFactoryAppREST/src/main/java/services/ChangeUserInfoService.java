package services;

import javax.ws.rs.core.Response;

import beans.User;
import dao.UserDAO;
import dto.ChangeUserInfoDTO;

public class ChangeUserInfoService {

	private UserDAO userDao;
	
	
	
	public ChangeUserInfoService() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ChangeUserInfoService(UserDAO userDao) {
		super();
		this.userDao = userDao;
	}



	public Response ChangeInfo(User user, ChangeUserInfoDTO dto) {
		
		User updatedUser = user;
		
		if(dto.getPassword().isBlank()) {
			updatedUser.setDateOfBirth(dto.getDateOfBirth());
			updatedUser.setFirstName(dto.getFirstName());
			updatedUser.setLastName(dto.getLastName());
			updatedUser.setSex(dto.getSex());
		}
		if(!dto.getPassword().isBlank())
		 updatedUser.setPassword(dto.getPassword());
		
		Boolean isUpdated = userDao.Update(userDao.GetById(updatedUser.getId()));
	
		return Response.status(Response.Status.OK).entity(isUpdated).build();
	}
}
