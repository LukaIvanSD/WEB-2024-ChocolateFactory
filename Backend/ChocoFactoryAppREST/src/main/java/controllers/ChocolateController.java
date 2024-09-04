package controllers;

import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Chocolate;
import beans.ChocolateItem;
import beans.ChocolatePrice;
import beans.User;
import beans.User.UserType;
import dao.ChocolateDAO;
import dao.ChocolateItemDAO;
import dao.ChocolatePriceDAO;
import dao.ManagerDAO;
import dao.WorkerDAO;
import dto.ChocolateDTO;
import dto.ChocolateEditRequestDTO;
import dto.FiltersDTO;
import services.ChocolateService;

@Path("/chocolates")
public class ChocolateController {
	@Context
	ServletContext ctx;
	public ChocolateController() {}
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("ChocolateDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("ChocolateDAO", new ChocolateDAO(contextPath));
		}
		if(ctx.getAttribute("ChocolatePriceDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("ChocolatePriceDAO", new ChocolatePriceDAO(contextPath));
		}
		if(ctx.getAttribute("ChocolateItemDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("ChocolateItemDAO", new ChocolateItemDAO(contextPath));
		}
		if(ctx.getAttribute("ManagerDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("ManagerDAO", new ManagerDAO(contextPath));
		}
		if(ctx.getAttribute("WorkerDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("WorkerDAO", new WorkerDAO(contextPath));
		}
	}
	
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<ChocolateDTO> GetChocolatesForFactory(@PathParam("id") Integer factoryId,@Context HttpServletRequest request){
		User user=(User)request.getSession().getAttribute("user");
		ChocolateService chocolateService=new ChocolateService((ChocolateDAO)ctx.getAttribute("ChocolateDAO"),(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"),(ChocolatePriceDAO)ctx.getAttribute("ChocolatePriceDAO"),(ManagerDAO)ctx.getAttribute("ManagerDAO"),(WorkerDAO)ctx.getAttribute("WorkerDAO"));
		return chocolateService.GetFactoryChocolates(factoryId,user);
	}
	 @Path("/")
	 @POST
	 @Produces(MediaType.APPLICATION_JSON)
	 @Consumes(MediaType.APPLICATION_JSON)
	 public Response CreateChocolate(@QueryParam("price") int price,@QueryParam("factoryId") int factoryId,Chocolate chocolate,@Context HttpServletRequest request) {
		User user=(User)request.getSession().getAttribute("user");
		
		 if (user == null || user.getUserType() != UserType.Manager)
             return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
		ChocolateService chocolateService=new ChocolateService((ChocolateDAO)ctx.getAttribute("ChocolateDAO"),(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"),(ChocolatePriceDAO)ctx.getAttribute("ChocolatePriceDAO"),(ManagerDAO)ctx.getAttribute("ManagerDAO"));
		return chocolateService.CreateChocolate(chocolate,price,factoryId,user);
	}
	 @Path("/")
	 @DELETE
	 @Produces(MediaType.APPLICATION_JSON)
	 @Consumes(MediaType.APPLICATION_JSON)
	 public Response DeleteChocolate(ChocolateItem item,@Context HttpServletRequest request ) {
		 User user=(User)request.getSession().getAttribute("user");
			 if (user == null || user.getUserType() != UserType.Manager)
	             return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
			ChocolateService chocolateService=new ChocolateService((ChocolateDAO)ctx.getAttribute("ChocolateDAO"),(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"),(ChocolatePriceDAO)ctx.getAttribute("ChocolatePriceDAO"),(ManagerDAO)ctx.getAttribute("ManagerDAO"));
			return chocolateService.DeleteChocolate(item,user);
	 }
	 
	 @Path("/{id}")
	 @PATCH
	 @Produces(MediaType.APPLICATION_JSON)
	 @Consumes(MediaType.APPLICATION_JSON)
	 public Response EditChocolate(@PathParam("id") int id, Map<String, Object> updates, @Context HttpServletRequest request) {
	        User user = (User) request.getSession().getAttribute("user");
	        if (user == null || user.getUserType() != UserType.Manager) {
	            return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
	        }
			ChocolateService chocolateService=new ChocolateService((ChocolateDAO)ctx.getAttribute("ChocolateDAO"),(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"),(ChocolatePriceDAO)ctx.getAttribute("ChocolatePriceDAO"),(ManagerDAO)ctx.getAttribute("ManagerDAO"));
	        return chocolateService.EditChocolate(id,updates,user);
	 }
	 @Path("/")
	 @PATCH
	 @Produces(MediaType.APPLICATION_JSON)
	 @Consumes(MediaType.APPLICATION_JSON)
	 public Response EditChocolate(ChocolateEditRequestDTO chocolateRequestDto, @Context HttpServletRequest request) {
		 User user = (User) request.getSession().getAttribute("user");
	        if (user == null || user.getUserType() != UserType.Manager) {
	            return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
	        }	
		 ChocolateService chocolateService=new ChocolateService((ChocolateDAO)ctx.getAttribute("ChocolateDAO"),(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"),(ChocolatePriceDAO)ctx.getAttribute("ChocolatePriceDAO"),(ManagerDAO)ctx.getAttribute("ManagerDAO"));

		 return chocolateService.EditChocolate(chocolateRequestDto, user);
	 }
	 @Path("/filters")
	 @GET
	 @Produces(MediaType.APPLICATION_JSON)
	 public FiltersDTO GetChocolateFilters()
	 {
		 ChocolateService chocolateService=new ChocolateService((ChocolateDAO)ctx.getAttribute("ChocolateDAO"),(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"));
	        return chocolateService.GetFilters();
	 }
	 
}
