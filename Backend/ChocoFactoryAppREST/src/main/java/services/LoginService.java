package services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import beans.BlockedUser;
import beans.User;
import dao.BlockedUserDAO;
import dao.UserDAO;

public class LoginService {
	private UserDAO userDao;
	private BlockedUserDAO blockedUserDao;
	public LoginService(UserDAO userDao,BlockedUserDAO blockedUserDao) {
		this.userDao=userDao;
		this.blockedUserDao=blockedUserDao;
	}
	public Response Login(User user,HttpServletRequest request)
	{
		User loggedUser = userDao.GetByCredentials(user.getUsername(), user.getPassword());
		if (loggedUser == null) {
			return Response.status(400).entity("Invalid username and/or password").build();
		}
		BlockedUser blockedUser= blockedUserDao.GetByUserId(loggedUser.getId());
		if(blockedUser!=null)
			return Response.status(Response.Status.FORBIDDEN).entity("Your account has been blocked at "+blockedUser.getBlockDate()).build();
		
		request.getSession().setAttribute("user", loggedUser);
		return Response.status(200).entity("Login successful").build();
		
	}
}
