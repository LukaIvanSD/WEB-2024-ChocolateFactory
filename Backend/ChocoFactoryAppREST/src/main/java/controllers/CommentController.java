package controllers;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.User;
import dao.CommentDAO;
import dao.FactoryDAO;
import dao.ManagerDAO;
import dao.PurchaseArticleDAO;
import dao.PurchaseDAO;
import dao.RejectedPurchaseDAO;
import dao.UserDAO;
import dao.WorkerDAO;
import dto.CommentDTO;
import dto.CommentRequestDTO;
import services.CommentService;

@Path("/comments")
public class CommentController {
	@Context
	ServletContext ctx;
	public CommentController()
	{
		
	}
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("CommentDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("CommentDAO", new CommentDAO(contextPath));
		}
		if(ctx.getAttribute("UserDAO")==null)
		{		
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("UserDAO", new UserDAO(contextPath));
		}
		if(ctx.getAttribute("WorkerDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("WorkerDAO", new WorkerDAO(contextPath));
		}
		if(ctx.getAttribute("ManagerDAO")==null)
		{		
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("ManagerDAO", new ManagerDAO(contextPath));
		}
		if(ctx.getAttribute("PurchaseDAO")==null)
		{		
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("PurchaseDAO", new PurchaseDAO(contextPath));
		}
		if(ctx.getAttribute("PurchaseArticleDAO")==null)
		{		
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("PurchaseArticleDAO", new PurchaseArticleDAO(contextPath));
		}
		if(ctx.getAttribute("FactoryDAO")==null)
		{		
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("FactoryDAO", new FactoryDAO(contextPath));
		}
		if(ctx.getAttribute("RejectedPurchaseDAO")==null)
		{		
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("RejectedPurchaseDAO", new RejectedPurchaseDAO(contextPath));
		}
			
	}
	
	@Path("/")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response CreateComment(CommentRequestDTO commentDto,@Context HttpServletRequest request) {
		User user=(User)request.getSession().getAttribute("user");
		if(user==null || !user.IsCustomer())
			return Response.status(Status.FORBIDDEN).entity("No permission").build();
		CommentService commentService=new CommentService((CommentDAO)ctx.getAttribute("CommentDAO"),(UserDAO)ctx.getAttribute("UserDAO"),(ManagerDAO)ctx.getAttribute("ManagerDAO"),(WorkerDAO)ctx.getAttribute("WorkerDAO"),(PurchaseDAO)ctx.getAttribute("PurchaseDAO"),(PurchaseArticleDAO)ctx.getAttribute("PurchaseArticleDAO"));
		return commentService.CreateComment(commentDto,user);
	}
	
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<CommentDTO> getCommentsForFactory(@PathParam("id") Integer id ,@Context HttpServletRequest request)
	{
		User user=(User) request.getSession().getAttribute("user");
		CommentService commentService=new CommentService((CommentDAO)ctx.getAttribute("CommentDAO"),(UserDAO)ctx.getAttribute("UserDAO"),(ManagerDAO)ctx.getAttribute("ManagerDAO"),(WorkerDAO)ctx.getAttribute("WorkerDAO"),(PurchaseDAO)ctx.getAttribute("PurchaseDAO"),(PurchaseArticleDAO)ctx.getAttribute("PurchaseArticleDAO"));
		return commentService.getComments(id,user);
	}
	
	@Path("/approve/{id}")
	@PUT
	@Produces (MediaType.APPLICATION_JSON)
	public Response ApproveComment(@PathParam("id")int commentId,@Context HttpServletRequest request)
	{
		User user=(User)request.getSession().getAttribute("user");
		if(user==null || !user.IsManager())
			return Response.status(Status.FORBIDDEN).entity("No permission").build();
		CommentService commentService=new CommentService((CommentDAO)ctx.getAttribute("CommentDAO"),(ManagerDAO)ctx.getAttribute("ManagerDAO"),(FactoryDAO)ctx.getAttribute("FactoryDAO"));
		return commentService.ApproveComment(commentId,user);
	}
	@Path("/decline/{id}")
	@PUT
	@Produces (MediaType.APPLICATION_JSON)
	public Response DeclineComment(@PathParam("id")int commentId,@Context HttpServletRequest request)
	{
		User user=(User)request.getSession().getAttribute("user");
		if(user==null || !user.IsManager())
			return Response.status(Status.FORBIDDEN).entity("No permission").build();
		CommentService commentService=new CommentService((CommentDAO)ctx.getAttribute("CommentDAO"),(ManagerDAO)ctx.getAttribute("ManagerDAO"),(FactoryDAO)ctx.getAttribute("FactoryDAO"));
		return commentService.DeclineComment(commentId,user);
	}
	
	@Path("/rejectionReason/{id}")
	@GET
	@Produces (MediaType.APPLICATION_JSON)
	public Response GetRejectionreason(@PathParam("id")Integer purchaseId,@Context HttpServletRequest request) {
		User user=(User)request.getSession().getAttribute("user");
		if(user==null || !user.IsCustomer())
			return Response.status(Status.FORBIDDEN).entity("No permission").build();
		CommentService commentService=new CommentService((RejectedPurchaseDAO)ctx.getAttribute("RejectedPurchaseDAO"));
		return commentService.GetRejectionReason(purchaseId,user);
	}
	
	
	
	
	@Path("/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response DeleteComment(@PathParam("id")Integer commentId,@Context HttpServletRequest request) {
		
		User user=(User)request.getSession().getAttribute("user");
		if(user==null || !user.IsCustomer())
			return Response.status(Status.FORBIDDEN).entity("No permission").build();
		
		CommentDAO dao = (CommentDAO)ctx.getAttribute("CommentDAO");
		
		if(dao.DeleteById(commentId))
			return Response.status(Response.Status.OK).entity("Successfully deleted!").build();
		
		return Response.status(Response.Status.OK).entity("Failed to delete.").build();
	}
	
	
}
