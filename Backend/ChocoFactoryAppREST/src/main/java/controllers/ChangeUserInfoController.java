package controllers;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.User;
import dao.UserDAO;
import dto.ChangeUserInfoDTO;
import services.ChangeUserInfoService;

@Path("/profile")
public class ChangeUserInfoController {

	@Context
	ServletContext ctx;
	
	public ChangeUserInfoController() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("UserDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("UserDAO", new UserDAO(contextPath));
		}
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetPurchases(@Context HttpServletRequest request, ChangeUserInfoDTO userDto) {
		
		 User user = (User) request.getSession().getAttribute("user");
		 
		 if(user == null) {
			 return Response.status(Response.Status.FORBIDDEN).entity("Access Denied, loggin or register").build();
		 }
		
		 ChangeUserInfoService service = new ChangeUserInfoService((UserDAO )ctx.getAttribute("UserDAO"));
		 
		return service.ChangeInfo(user, userDto);
	}
	
}
