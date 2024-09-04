package controllers;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.Manager;
import beans.User;
import beans.User.UserType;
import beans.Worker;
import dao.ManagerDAO;
import dao.UserDAO;
import dao.WorkerDAO;
import services.ManagerService;
import services.WorkerService;

@Path("/workers")
public class WorkerController {
		@Context
		ServletContext ctx;
		public WorkerController() {
			
		}
		
		@PostConstruct
		public void init() {
			if (ctx.getAttribute("WorkerDAO") == null) {
		    	String contextPath = ctx.getRealPath("");
				ctx.setAttribute("WorkerDAO", new WorkerDAO(contextPath));
			}
			if (ctx.getAttribute("ManagerDAO") == null) {
		    	String contextPath = ctx.getRealPath("");
				ctx.setAttribute("ManagerDAO", new ManagerDAO(contextPath));
			}
			if (ctx.getAttribute("UserDAO") == null) {
		    	String contextPath = ctx.getRealPath("");
				ctx.setAttribute("UserDAO", new UserDAO(contextPath));
			}
		}
		 @Path("/rights")
		 @POST
		 @Produces(MediaType.APPLICATION_JSON)
		 public Boolean IsWorkerInFactory(@QueryParam("factoryId") Integer factoryId,@Context HttpServletRequest request)
		 {
			WorkerService workerService=new WorkerService((WorkerDAO)ctx.getAttribute("WorkerDAO"));
			User user=(User)request.getSession().getAttribute("user");
			return workerService.IsWorkerInFactory(user,factoryId);
		 }
		 
		 @Path("/myFactory")
		 @GET
		 @Produces(MediaType.APPLICATION_JSON)
		 public Integer GetWorkplaceId(@Context HttpServletRequest request)
		 {
			 	User user=(User)request.getSession().getAttribute("user");
			 	if(user==null || user.getUserType()!=UserType.Worker)
			 		return null;
				WorkerService workerService=new WorkerService((WorkerDAO)ctx.getAttribute("WorkerDAO"));
				return workerService.GetWorkerFactoryId(user);
		 }
		 @Path("/number")
		 @GET
		 @Produces(MediaType.APPLICATION_JSON)
		 public Response GetNumberOfWorkersInFactory(@QueryParam("factoryId") int factoryId, @Context HttpServletRequest request) {
		     User user = (User) request.getSession().getAttribute("user");
		     if (user.getUserType() == null || !user.IsManager()) {
		         return Response.status(Status.FORBIDDEN).entity("You don't have permission!").build();
		     }
		     WorkerService workerService = new WorkerService((WorkerDAO) ctx.getAttribute("WorkerDAO"), (ManagerDAO) ctx.getAttribute("ManagerDAO"));
		     return workerService.GetNumberOfWorkersInFactory(user, factoryId);
		 }
		 @Path("/{id}")
		 @GET
		 @Produces(MediaType.APPLICATION_JSON)
		 public Collection<User> getAllFactoryWorkers(@PathParam ("id")Integer factoryId,@Context HttpServletRequest request){
			 if (factoryId==null)
				 return null;
			 User user=(User)request.getSession().getAttribute("user");
			   if (user.getUserType() == null || !user.IsManager()) {
			         return null;
			     }
			 WorkerService workerService=new WorkerService((UserDAO)ctx.getAttribute("UserDAO"),(ManagerDAO)ctx.getAttribute("ManagerDAO"),(WorkerDAO)ctx.getAttribute("WorkerDAO"));
			 return workerService.getAllFactoryWorkers(factoryId,user);
		 }
		 @Path("/{id}")
		 @DELETE
		 @Produces(MediaType.APPLICATION_JSON)
		 public Response removeWorker(@PathParam ("id")Integer userId,@Context HttpServletRequest request) {
			 if(userId==null)
				 return Response.status(Status.BAD_REQUEST).build();
			 User user=(User)request.getSession().getAttribute("user");
			 WorkerService workerService = new WorkerService((UserDAO)ctx.getAttribute("UserDAO"),(ManagerDAO)ctx.getAttribute("ManagerDAO"),(WorkerDAO)ctx.getAttribute("WorkerDAO"));
			 return workerService.removeWorker(user,userId);
		 }

}
