package controllers;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Manager;
import beans.User;
import beans.User.UserType;
import dao.ManagerDAO;
import dao.UserDAO;
import dao.WorkerDAO;
import services.ManagerService;
import services.WorkerService;

@Path("/managers")
public class ManagerController {
	@Context
	ServletContext ctx;
	public ManagerController() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("ManagerDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("ManagerDAO", new ManagerDAO(contextPath));
		}
		if (ctx.getAttribute("UserDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("UserDAO", new UserDAO(contextPath));
		}
	}
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Manager>GetAll()
	{
		ManagerDAO managerDao = (ManagerDAO) ctx.getAttribute("ManagerDAO");
		return  managerDao.GetAll();
	}
	@PUT
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void Update(Manager manager)
	{
		ManagerDAO managerDao = (ManagerDAO) ctx.getAttribute("ManagerDAO");
		managerDao.Update(manager);
	}
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public void Delete(@PathParam("id")Integer id)
	{
		ManagerDAO managerDao = (ManagerDAO) ctx.getAttribute("ManagerDAO");
		managerDao.DeleteById(id);
	}
	 @Path("/rights")
	 @POST
	 @Produces(MediaType.APPLICATION_JSON)
	 public Boolean IsManagerOfFactory(@QueryParam("factoryId") Integer factoryId,@Context HttpServletRequest request)
	 {
		ManagerService managerService=new ManagerService((ManagerDAO)ctx.getAttribute("ManagerDAO"));
		User user=(User)request.getSession().getAttribute("user");
		return managerService.IsManagerOfFactory(user,factoryId);
	 }
	 
	 @Path("/free")
	 @GET
	 @Produces(MediaType.APPLICATION_JSON)
	 public Response GetFreeManagers (@Context HttpServletRequest request){
		 User user=(User)request.getSession().getAttribute("user");
		 if(user==null || !user.IsAdmin())
             return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
		 ManagerService managerService=new ManagerService((ManagerDAO)ctx.getAttribute("ManagerDAO"),(UserDAO)ctx.getAttribute("UserDAO"));
		 return managerService.GetFreeManagers();
		 
	 }
	 
	 @Path("/myFactory")
	 @GET
	 @Produces(MediaType.APPLICATION_JSON)
	 public Integer GetWorkplaceId(@Context HttpServletRequest request)
	 {
		 	User user=(User)request.getSession().getAttribute("user");
		 	if(user==null || user.getUserType()!=UserType.Manager)
		 		return null;
			 ManagerService managerService=new ManagerService((ManagerDAO)ctx.getAttribute("ManagerDAO"));
			return managerService.GetManagerFactoryId(user);
	 }
}
