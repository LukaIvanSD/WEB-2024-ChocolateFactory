package controllers;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PATCH;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.ChocolateItem;
import beans.User;
import beans.User.UserType;
import dao.ChocolateItemDAO;
import dao.WorkerDAO;
import dto.ChocolateEditQuantityDTO;
import services.ChocolateItemService;

@Path("/chocolateItems")
public class ChocolateItemController {
	@Context
	ServletContext ctx;
	
	public ChocolateItemController() {
		
	}
	@PostConstruct
	public void init()
	{
		if(ctx.getAttribute("ChocolateItemDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("ChocolateItemDAO", new ChocolateItemDAO(context));
		}
		if(ctx.getAttribute("WorkerDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("WorkerDAO", new WorkerDAO(context));
		}
	}
	@Path("")
	@PATCH
	@Produces(MediaType.APPLICATION_JSON)
	public Response UpdateQuantity(ChocolateEditQuantityDTO item,@Context HttpServletRequest request) {
		User user=(User)request.getSession().getAttribute("user");
	        if (user == null || user.getUserType() != UserType.Worker) {
	            return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
	        }
	        ChocolateItemService chocolateItemService=new ChocolateItemService((ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"),(WorkerDAO)ctx.getAttribute("WorkerDAO"));
		return chocolateItemService.UpdateQuantity(item,user);
	}

}
