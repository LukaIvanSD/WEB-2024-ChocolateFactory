package controllers;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.BlockedUser;
import beans.User;
import dao.BlockedUserDAO;
import dao.UserDAO;
import services.BlockedUserService;
@Path("/blockedUsers")
public class BlockedUserController {

	@Context
	ServletContext ctx;
	public BlockedUserController(){}
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("BlockedUserDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("BlockedUserDAO",new BlockedUserDAO(context));
		}
		if(ctx.getAttribute("UserDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("UserDAO",new UserDAO(context));
		}
	}
	
	@Path("/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetAll(@Context HttpServletRequest request){
		System.out.println("ma ged di");
		User user=(User)request.getSession().getAttribute("user");
		if(user==null || !user.IsAdmin())
			return Response.status(Status.FORBIDDEN).entity("No permission").build();
		
		return Response.status(Status.OK).entity(((BlockedUserDAO)ctx.getAttribute("BlockedUserDAO")).GetAll()).build();		
	}
	@Path("/{id}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response BlockUser(@PathParam("id")Integer userId,@Context HttpServletRequest request) {
		User user=(User)request.getSession().getAttribute("user");
		if(user==null || !user.IsAdmin())
			return Response.status(Status.FORBIDDEN).entity("No permission").build();
		BlockedUserService blockedUserService=new BlockedUserService((BlockedUserDAO)ctx.getAttribute("BlockedUserDAO"),(UserDAO)ctx.getAttribute("UserDAO"));
		return blockedUserService.BlockUser(userId);
	}
	
	@Path("/{id}")
	@PATCH
	@Produces(MediaType.APPLICATION_JSON)
	public Response UnblockUser(@PathParam("id")Integer userId,@Context HttpServletRequest request) {
		User user=(User)request.getSession().getAttribute("user");
		if(user==null || !user.IsAdmin())
			return Response.status(Status.FORBIDDEN).entity("No permission").build();
		BlockedUserService blockedUserService=new BlockedUserService((BlockedUserDAO)ctx.getAttribute("BlockedUserDAO"),(UserDAO)ctx.getAttribute("UserDAO"));
		return blockedUserService.UnBlockUser(userId);
		
	}
}
