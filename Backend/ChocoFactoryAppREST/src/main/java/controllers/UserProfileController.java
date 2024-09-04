package controllers;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.User;
import dao.ChocolateDAO;
import dao.ChocolateItemDAO;
import dao.ChocolatePriceDAO;
import dao.CustomerDAO;
import dao.CustomerTypeDAO;
import dao.FactoryDAO;
import dao.ManagerDAO;
import dao.PurchaseArticleDAO;
import dao.PurchaseDAO;
import dao.UserDAO;
import dao.WorkerDAO;
import services.PurchaseService;
import services.UserProfileService;

@Path("/userProfile")
public class UserProfileController {

	@Context
	ServletContext ctx;
	
	public UserProfileController() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("UserDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("UserDAO", new UserDAO(contextPath));
		}
		if(ctx.getAttribute("ManagerDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("ManagerDAO", new ManagerDAO(contextPath));
		}
		if(ctx.getAttribute("CustomerDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("CustomerDAO", new CustomerDAO(contextPath));
		}
		if(ctx.getAttribute("CustomerTypeDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("CustomerTypeDAO", new CustomerTypeDAO(contextPath));
		}
		if(ctx.getAttribute("WorkerDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("WorkerDAO", new WorkerDAO(contextPath));
		}
		if(ctx.getAttribute("FactoryDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("FactoryDAO", new FactoryDAO(contextPath));
		}
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetUser(@Context HttpServletRequest request) {
		
		 User user = (User) request.getSession().getAttribute("user");
		 
		 if(user == null) {
			 return Response.status(Response.Status.FORBIDDEN).entity("Access Denied You Are Not A User").build();
		 }
		
		 UserProfileService service = new UserProfileService((UserDAO )ctx.getAttribute("UserDAO"), (CustomerDAO )ctx.getAttribute("CustomerDAO"),
				 (CustomerTypeDAO )ctx.getAttribute("CustomerTypeDAO"), (ManagerDAO )ctx.getAttribute("ManagerDAO"),
				 (WorkerDAO )ctx.getAttribute("WorkerDAO"), (FactoryDAO )ctx.getAttribute("FactoryDAO"));
		 
		 
		 if(user.IsCustomer())
			 return service.getCustomerProfile(user);
		 else if(user.IsAdmin())
			 return service.getAdminProfile(user);
		 else if(user.IsManager())
			 return service.getManagerProfile(user);
		 else if(user.IsWorker())
			 return service.getWorkerProfile(user);
		 
		 
		 return Response.status(Response.Status.FORBIDDEN).entity("User Not Loaded Correctly").build();
	}
	
	
	
	
}
