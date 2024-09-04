package services;

import java.time.LocalDateTime;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.BlockedUser;
import beans.User;
import dao.BlockedUserDAO;
import dao.UserDAO;

public class BlockedUserService {
	private BlockedUserDAO blockedUserDao;
	private UserDAO userDao;
	
	public BlockedUserService(BlockedUserDAO blockedUserDao,UserDAO userDao) {
		this.blockedUserDao=blockedUserDao;
		this.userDao=userDao;
	}
	public Response BlockUser(int userId) {
		User user=userDao.GetById(userId);
		Response response=IsRequestInvalid(userId);
		if(response!=null)
			return response;
		
		if(blockedUserDao.GetByUserId(userId)!=null)
			return Response.status(Status.BAD_REQUEST).entity("User already blocked").build();
		BlockedUser blockedUser=new BlockedUser();
		blockedUser.setUserId(userId);
		blockedUser.setBlockDate(LocalDateTime.now());
		return Response.status(Status.OK).entity(blockedUserDao.Save(blockedUser)).build();
		 

	}
	public Response UnBlockUser(Integer userId) {
		Response response=IsRequestInvalid(userId);
		if(response!=null)
			return response;
		
		BlockedUser blockedUser=blockedUserDao.GetByUserId(userId);
		if(blockedUser==null)
			return Response.status(Status.BAD_REQUEST).entity("User isnt blocked").build();
		blockedUserDao.DeleteByUserId(userId);
		return Response.status(Status.OK).entity("OK").build();
	}
	
	private Response IsRequestInvalid(Integer userId) {
		if(userId==null)
			return Response.status(Status.BAD_REQUEST).entity("No user sent").build();
		User user=userDao.GetById(userId);
		if(user==null)
			return Response.status(Status.BAD_REQUEST).entity("Non existant user").build();
		if(user.IsAdmin())
			return Response.status(Status.BAD_REQUEST).entity("Invalid operation").build();
		return null;
	}
}
