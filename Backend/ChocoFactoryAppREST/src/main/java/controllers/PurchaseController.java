package controllers;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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

import beans.User;
import dao.CancelledPurchaseDAO;
import dao.ChocolateDAO;
import dao.ChocolateItemDAO;
import dao.ChocolatePriceDAO;
import dao.CommentDAO;
import dao.CustomerDAO;
import dao.CustomerTypeDAO;
import dao.FactoryDAO;
import dao.ManagerDAO;
import dao.PurchaseArticleDAO;
import dao.PurchaseDAO;
import dao.RejectedPurchaseDAO;
import dao.UserDAO;
import dto.PurchaseDTO;
import dto.RejectPurchaseDTO;
import dto.SearchPurchasesParamsDTO;
import services.PurchaseService;
import services.SearchService;

@Path("/purchases")
public class PurchaseController {

	@Context
	ServletContext ctx;
	
	public PurchaseController() {	
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("UserDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("UserDAO", new UserDAO(contextPath));
		}
		if(ctx.getAttribute("ChocolateDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("ChocolateDAO", new ChocolateDAO(contextPath));
		}
		if(ctx.getAttribute("ChocolateItemDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("ChocolateItemDAO", new ChocolateItemDAO(contextPath));
		}
		if(ctx.getAttribute("ChocolatePriceDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("ChocolatePriceDAO", new ChocolatePriceDAO(contextPath));
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
		if(ctx.getAttribute("ManagerDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("ManagerDAO", new ManagerDAO(contextPath));
		}
		if(ctx.getAttribute("CommentDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("CommentDAO", new CommentDAO(contextPath));
		}
		if(ctx.getAttribute("CancelledPurchaseDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("CancelledPurchaseDAO", new CancelledPurchaseDAO(contextPath));
		}
		if(ctx.getAttribute("CustomerTypeDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("CustomerTypeDAO", new CustomerTypeDAO(contextPath));
		}
		if(ctx.getAttribute("CustomerDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("CustomerDAO", new CustomerDAO(contextPath));
		}
		if(ctx.getAttribute("RejectedPurchaseDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("RejectedPurchaseDAO", new RejectedPurchaseDAO(contextPath));
		}
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetPurchases(@Context HttpServletRequest request) {
		
		 User user = (User) request.getSession().getAttribute("user");
		 
		 if(user == null || !user.IsCustomer()) {
			 return Response.status(Response.Status.FORBIDDEN).entity("Access Denied You Are Not A Customer").build();
		 }
		
		 PurchaseService service = new PurchaseService((PurchaseArticleDAO )ctx.getAttribute("PurchaseArticleDAO"), (PurchaseDAO )ctx.getAttribute("PurchaseDAO"), (ChocolateDAO )ctx.getAttribute("ChocolateDAO"), (ChocolateItemDAO )ctx.getAttribute("ChocolateItemDAO"), (ChocolatePriceDAO )ctx.getAttribute("ChocolatePriceDAO"), (FactoryDAO )ctx.getAttribute("FactoryDAO"), (ManagerDAO )ctx.getAttribute("ManagerDAO"), (UserDAO )ctx.getAttribute("UserDAO"),(CommentDAO)ctx.getAttribute("CommentDAO"));
		
		 
		 
		return service.getPurchasesForCustomer(user);
	}
	
	@GET
	@Path("/allFactoryPurchases")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response GetPurchasesForManager(@Context HttpServletRequest request) {
		
		 User user = (User) request.getSession().getAttribute("user");
		 
		 if(user == null || !user.IsManager()) {
			 return Response.status(Response.Status.FORBIDDEN).entity("Access Denied You Are Not A Manager").build();
		 }
		
		 PurchaseService service = new PurchaseService((PurchaseArticleDAO )ctx.getAttribute("PurchaseArticleDAO"), (PurchaseDAO )ctx.getAttribute("PurchaseDAO"), (ChocolateDAO )ctx.getAttribute("ChocolateDAO"), (ChocolateItemDAO )ctx.getAttribute("ChocolateItemDAO"), (ChocolatePriceDAO )ctx.getAttribute("ChocolatePriceDAO"), (FactoryDAO )ctx.getAttribute("FactoryDAO"), (ManagerDAO )ctx.getAttribute("ManagerDAO"), (UserDAO )ctx.getAttribute("UserDAO"),(CommentDAO)ctx.getAttribute("CommentDAO"));
		
		 
		 
		return service.getPurchasesForManager(user);
	}
	
	@POST
	@Path("/searchPurchases")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<PurchaseDTO> searchPurchases(@Context HttpServletRequest request, SearchPurchasesParamsDTO params){
		
		
		
		User user = (User) request.getSession().getAttribute("user");
		
		SearchService service = new SearchService((PurchaseArticleDAO )ctx.getAttribute("PurchaseArticleDAO"), (PurchaseDAO )ctx.getAttribute("PurchaseDAO"), (ChocolateDAO )ctx.getAttribute("ChocolateDAO"), (ChocolateItemDAO )ctx.getAttribute("ChocolateItemDAO"), (ChocolatePriceDAO )ctx.getAttribute("ChocolatePriceDAO"), (FactoryDAO )ctx.getAttribute("FactoryDAO"), (ManagerDAO )ctx.getAttribute("ManagerDAO"),(CommentDAO)ctx.getAttribute("CommentDAO") );
		
		if(user != null) {
			if(user.IsCustomer())
				return service.SearchPurchases(params, user);
		}
		
		
		return null;
	}
	
	@PATCH
	@Path("/reject")
	@Produces(MediaType.APPLICATION_JSON)
	public Response Reject(RejectPurchaseDTO purchaseDto,@Context HttpServletRequest request) {
	 User user = (User) request.getSession().getAttribute("user");
		 
		 if(user == null || !user.IsManager()) {
			 return Response.status(Response.Status.FORBIDDEN).entity("Access Denied You Are Not A Manager").build();
		 }
		 PurchaseService purchaseService =new PurchaseService((ManagerDAO)ctx.getAttribute("ManagerDAO"),(PurchaseDAO)ctx.getAttribute("PurchaseDAO"),(PurchaseArticleDAO)ctx.getAttribute("PurchaseArticleDAO"),(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"),(ChocolatePriceDAO)ctx.getAttribute("ChocolatePriceDAO"),(RejectedPurchaseDAO)ctx.getAttribute("RejectedPurchaseDAO"));
		 return purchaseService.RejectPurchase(user,purchaseDto);
	}
	
	@PATCH
	@Path("/approve")
	@Produces(MediaType.APPLICATION_JSON)
	public Response Approve(@QueryParam("id")Integer purchaseId,@Context HttpServletRequest request) {
	 User user = (User) request.getSession().getAttribute("user");
		 
		 if(user == null || !user.IsManager()) {
			 return Response.status(Response.Status.FORBIDDEN).entity("Access Denied You Are Not A Manager").build();
		 }
		 PurchaseService purchaseService =new PurchaseService((ManagerDAO)ctx.getAttribute("ManagerDAO"),(PurchaseDAO)ctx.getAttribute("PurchaseDAO"),(PurchaseArticleDAO)ctx.getAttribute("PurchaseArticleDAO"),(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"),(ChocolatePriceDAO)ctx.getAttribute("ChocolatePriceDAO"));
		 return purchaseService.ApprovePurchase(user,purchaseId);
	}
	
	
	@Path("/cancel/{id}")
	@PATCH
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancelPurchase(@PathParam("id")Integer purchaseId, @Context HttpServletRequest request) {
		
		User user=(User)request.getSession().getAttribute("user");
		
		if(user == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
		}
		
		
		if(!user.IsCustomer()) {
			return Response.status(Response.Status.FORBIDDEN).entity("You are not a Customer!").build();
		}
		
		System.out.println("najsuvlji gas");
		PurchaseService service = new PurchaseService((PurchaseArticleDAO )ctx.getAttribute("PurchaseArticleDAO"), 
				(PurchaseDAO )ctx.getAttribute("PurchaseDAO"), (ChocolateDAO )ctx.getAttribute("ChocolateDAO"), 
				(ChocolateItemDAO )ctx.getAttribute("ChocolateItemDAO"), (ChocolatePriceDAO )ctx.getAttribute("ChocolatePriceDAO"), 
				(FactoryDAO )ctx.getAttribute("FactoryDAO"), (ManagerDAO )ctx.getAttribute("ManagerDAO"),(UserDAO)ctx.getAttribute("UserDAO"), (CommentDAO)ctx.getAttribute("CommentDAO"),
				(CustomerDAO)ctx.getAttribute("CustomerDAO"),(CustomerTypeDAO)ctx.getAttribute("CustomerTypeDAO"),(CancelledPurchaseDAO)ctx.getAttribute("CancelledPurchaseDAO"));
		
		return service.cancelPurchase(purchaseId, user);
	}
	
	
	
	
	
}
