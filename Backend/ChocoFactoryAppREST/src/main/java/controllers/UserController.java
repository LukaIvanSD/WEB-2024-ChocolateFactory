package controllers;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.Customer;
import beans.User;
import dao.CancelledPurchaseDAO;
import dao.CustomerDAO;
import dao.PurchaseDAO;
import dao.UserDAO;
import services.UserService;

@Path("/users")
public class UserController {
	@Context
	ServletContext ctx;
	
	public UserController() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("UserDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("UserDAO", new UserDAO(contextPath));
		}
		if (ctx.getAttribute("CancelledPurchaseDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("CancelledPurchaseDAO", new CancelledPurchaseDAO(contextPath));
		}
		if (ctx.getAttribute("PurchaseDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("PurchaseDAO", new PurchaseDAO(contextPath));
		}
		if (ctx.getAttribute("CustomerDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("CustomerDAO", new CustomerDAO(contextPath));
		}
	}
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User>GetAll()
	{
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		return  userDao.GetAll();
	}
	
	@GET
	@Path("/allCustomers")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Customer>GetAllCustomers()
	{
		CustomerDAO customerDAO = (CustomerDAO) ctx.getAttribute("CustomerDAO");
		return  customerDAO.GetAll();
	}
	
	
	
	@PUT
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response Update(User user)
	{
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		if(userDao.Update(user))
			return Response.status(200).entity("OK").build();
		return Response.status(404).entity("User not found").build();
	}
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response Delete(@PathParam("id") Integer id)
	{
		UserDAO userDao = (UserDAO) ctx.getAttribute("UserDAO");
		if(userDao.DeleteById(id))
			return Response.status(200).entity("OK").build();
		return Response.status(404).entity("User not found").build();		
 
	}
	@GET
	@Path("/suspicious")
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetSuspiciousUsers(@Context HttpServletRequest request) {
		System.out.println("ide gas");
		User user=(User)request.getSession().getAttribute("user");
		if(user==null || !user.IsAdmin())
			return Response.status(Status.FORBIDDEN).entity("No permission").build();
		UserService userService =new UserService((UserDAO)ctx.getAttribute("UserDAO"),(CancelledPurchaseDAO)ctx.getAttribute("CancelledPurchaseDAO"),(PurchaseDAO)ctx.getAttribute("PurchaseDAO"));
		return Response.status(Status.OK).entity(userService.GetSuspiciousUsers()).build();
	}
	

	
	
	
	
	
}
