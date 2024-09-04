package controllers;

import java.util.List;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.ChocolateItem;
import beans.User;
import dao.CartArticleDAO;
import dao.CartDAO;
import dao.ChocolateDAO;
import dao.ChocolateItemDAO;
import dao.ChocolatePriceDAO;
import dao.CustomerDAO;
import dao.CustomerTypeDAO;
import dao.FactoryDAO;
import dao.PurchaseArticleDAO;
import dao.PurchaseDAO;
import dto.CartDTO;
import dto.GhostItemsDTO;
import services.CartService;

@Path("/cart")
public class CartController {

	@Context
	ServletContext ctx;
	public CartController() {}
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("CartDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("CartDAO", new CartDAO(context));
		}
		if(ctx.getAttribute("CartArticleDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("CartArticleDAO", new CartArticleDAO(context));
		}
		if(ctx.getAttribute("CustomerDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("CustomerDAO", new CustomerDAO(context));
		}
		if(ctx.getAttribute("ChocolateDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("ChocolateDAO", new ChocolateDAO(context));
		}
		if(ctx.getAttribute("FactoryDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("FactoryDAO", new FactoryDAO(context));
		}
		if(ctx.getAttribute("ChocolateItemDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("ChocolateItemDAO", new ChocolateItemDAO(context));
		}
		if(ctx.getAttribute("ChocolatePriceDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("ChocolatePriceDAO", new ChocolatePriceDAO(context));
		}
		if(ctx.getAttribute("CustomerTypeDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("CustomerTypeDAO", new CustomerTypeDAO(context));
		}
		if(ctx.getAttribute("PurchaseDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("PurchaseDAO", new PurchaseDAO(context));
		}
		if(ctx.getAttribute("PurchaseArticleDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("PurchaseArticleDAO", new PurchaseArticleDAO(context));
		}
	}
	
	
	@Path("/numberOfCartAricles")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response numberOfCartArticles(@Context HttpServletRequest request) {
		
		User user=(User)request.getSession().getAttribute("user");
		
		if(user == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
		}
		
		if(user.IsCustomer()) {
			return Response.status(Response.Status.OK).entity(((CartArticleDAO)ctx.getAttribute("CartArticleDAO")).GetByCartId(((CartDAO)ctx.getAttribute("CartDAO")).GetByCustomerId(((CustomerDAO)ctx.getAttribute("CustomerDAO")).GetByUser(user.getId()).getId()).getId())).build();
		}
		
		return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
	}
	
	
	@Path("/getCart")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCart(@Context HttpServletRequest request) {
		User user=(User)request.getSession().getAttribute("user");
		
		if(user == null) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
		}
		
		if(user.IsCustomer()) {
			int customerId = ((CustomerDAO)ctx.getAttribute("CustomerDAO")).GetByUser(user.getId()).getId();
			CartService service = new CartService((ChocolateDAO)ctx.getAttribute("ChocolateDAO"),
					(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"), (ChocolatePriceDAO)ctx.getAttribute("ChocolatePriceDAO"),
					(CartArticleDAO)ctx.getAttribute("CartArticleDAO"), (CartDAO)ctx.getAttribute("CartDAO"),
					(FactoryDAO)ctx.getAttribute("FactoryDAO"),(CustomerDAO)ctx.getAttribute("CustomerDAO"));
			
			return Response.status(Response.Status.OK).entity(service.getCustomerCart(customerId)).build();
		}	
		return Response.status(Response.Status.FORBIDDEN).entity("Access Denied You Are Not a Customer").build();
	}
	@Path("/getGhostCart")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getGhostCart(List<GhostItemsDTO> ghostItemsDto, @Context HttpServletRequest request) {
		
		User user=(User)request.getSession().getAttribute("user");
		if(user!=null)
			return Response.status(Response.Status.FORBIDDEN).entity("You are loggedIn").build();
		CartService cartService = new CartService((ChocolateDAO)ctx.getAttribute("ChocolateDAO"),
				(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"), (ChocolatePriceDAO)ctx.getAttribute("ChocolatePriceDAO"),
				(CartArticleDAO)ctx.getAttribute("CartArticleDAO"), (CartDAO)ctx.getAttribute("CartDAO"),
				(FactoryDAO)ctx.getAttribute("FactoryDAO"),(CustomerDAO)ctx.getAttribute("CustomerDAO"));
		
		return cartService.getGhostCart(ghostItemsDto);
	}
	
	
	@Path("/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeCartArticle(@PathParam("id")Integer cartArticleId, @Context HttpServletRequest request) {
		
		User user=(User)request.getSession().getAttribute("user");
		
		CartArticleDAO articleDao = (CartArticleDAO)ctx.getAttribute("CartArticleDAO");
		CartDAO cartDao = (CartDAO)ctx.getAttribute("CartDAO");
		
		if(user != null && !user.IsCustomer()) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
		}
		
		
		int customerId = ((CustomerDAO)ctx.getAttribute("CustomerDAO")).GetByUser(user.getId()).getId();
		
		if(cartDao.GetById(articleDao.GetById(cartArticleId).getCartId()).getCustomerId() != customerId) {
			Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
		}
		
		if(articleDao.DeleteById(cartArticleId))
			return Response.status(Response.Status.OK).entity("Article Removed Successfully.").build();
		
		return Response.status(Response.Status.EXPECTATION_FAILED).entity("Error with removing.").build();
	}
	
	@Path("/decrease{id}")
	@PATCH
	@Produces(MediaType.APPLICATION_JSON)
	public Response descreaseArticleQuantity(@PathParam("id")Integer cartArticleId, @Context HttpServletRequest request) {
		
		User user=(User)request.getSession().getAttribute("user");
		
		CartArticleDAO articleDao = (CartArticleDAO)ctx.getAttribute("CartArticleDAO");
		CartDAO cartDao = (CartDAO)ctx.getAttribute("CartDAO");
		
		if(user != null && !user.IsCustomer()) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
		}
				
		int customerId = ((CustomerDAO)ctx.getAttribute("CustomerDAO")).GetByUser(user.getId()).getId();
		
		if(cartDao.GetById(articleDao.GetById(cartArticleId).getCartId()).getCustomerId() != customerId) {
			Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
		}
		
		CartService service = new CartService((ChocolateDAO)ctx.getAttribute("ChocolateDAO"),
				(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"), (ChocolatePriceDAO)ctx.getAttribute("ChocolatePriceDAO"),
				(CartArticleDAO)ctx.getAttribute("CartArticleDAO"), (CartDAO)ctx.getAttribute("CartDAO"),
				(FactoryDAO)ctx.getAttribute("FactoryDAO"),(CustomerDAO)ctx.getAttribute("CustomerDAO"));
				
		return service.decreaseArticleQuantity(cartArticleId);
	}
	
	@Path("/increase{id}")
	@PATCH
	@Produces(MediaType.APPLICATION_JSON)
	public Response increaseArticleQuantity(@PathParam("id")Integer cartArticleId, @Context HttpServletRequest request) {
		
		User user=(User)request.getSession().getAttribute("user");
		
		CartArticleDAO articleDao = (CartArticleDAO)ctx.getAttribute("CartArticleDAO");
		CartDAO cartDao = (CartDAO)ctx.getAttribute("CartDAO");
		
		if(user != null && !user.IsCustomer()) {
			return Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
		}
				
		int customerId = ((CustomerDAO)ctx.getAttribute("CustomerDAO")).GetByUser(user.getId()).getId();
		
		if(cartDao.GetById(articleDao.GetById(cartArticleId).getCartId()).getCustomerId() != customerId) {
			Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
		}
		
		CartService service = new CartService((ChocolateDAO)ctx.getAttribute("ChocolateDAO"),
				(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"), (ChocolatePriceDAO)ctx.getAttribute("ChocolatePriceDAO"),
				(CartArticleDAO)ctx.getAttribute("CartArticleDAO"), (CartDAO)ctx.getAttribute("CartDAO"),
				(FactoryDAO)ctx.getAttribute("FactoryDAO"),(CustomerDAO)ctx.getAttribute("CustomerDAO"));
				
		return service.increaseArticleQuantity(cartArticleId);
	}
	
	@Path("/getDiscount")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response increaseArticleQuantity(@Context HttpServletRequest request) {
		
		User user=(User)request.getSession().getAttribute("user");
		
		
		if(user == null) {
			return Response.status(Response.Status.OK).entity(0.00).build();
		}
		
		if(!user.IsCustomer()) {
			Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
		}
		
		int customerId = ((CustomerDAO)ctx.getAttribute("CustomerDAO")).GetByUser(user.getId()).getId();
		
		CartService service = new CartService((ChocolateDAO)ctx.getAttribute("ChocolateDAO"),
				(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"), (ChocolatePriceDAO)ctx.getAttribute("ChocolatePriceDAO"),
				(CartArticleDAO)ctx.getAttribute("CartArticleDAO"), (CartDAO)ctx.getAttribute("CartDAO"),
				(FactoryDAO)ctx.getAttribute("FactoryDAO"),(CustomerDAO)ctx.getAttribute("CustomerDAO"),
				(CustomerTypeDAO)ctx.getAttribute("CustomerTypeDAO"));
		
		return service.getDiscount(customerId);
	}
	
	@Path("/makePurchase")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response makePurchase(CartDTO cart,@Context HttpServletRequest request) {
		
		User user=(User)request.getSession().getAttribute("user");
		
		if(user == null || !user.IsCustomer()) {
			Response.status(Response.Status.FORBIDDEN).entity("Access Denied").build();
		}
		
		CartService service = new CartService((ChocolateDAO)ctx.getAttribute("ChocolateDAO"),
				(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"), (ChocolatePriceDAO)ctx.getAttribute("ChocolatePriceDAO"),
				(CartArticleDAO)ctx.getAttribute("CartArticleDAO"), (CartDAO)ctx.getAttribute("CartDAO"),
				(FactoryDAO)ctx.getAttribute("FactoryDAO"),(CustomerDAO)ctx.getAttribute("CustomerDAO"),
				(CustomerTypeDAO)ctx.getAttribute("CustomerTypeDAO"), (PurchaseDAO)ctx.getAttribute("PurchaseDAO"),
				(PurchaseArticleDAO)ctx.getAttribute("PurchaseArticleDAO"));
		
		
		
		return service.makePurchase(cart, user);
	}
	@Path("/save")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response SaveGhostItems(List<GhostItemsDTO> ghostItemsDto, @Context HttpServletRequest request) {
		
		User user=(User)request.getSession().getAttribute("user");
		if(user==null || !user.IsCustomer())
			return Response.status(Status.FORBIDDEN).build();
		CartService cartService=new CartService((CartDAO)ctx.getAttribute("CartDAO"),(CartArticleDAO)ctx.getAttribute("CartArticleDAO"),(CustomerDAO)ctx.getAttribute("CustomerDAO"));
		return cartService.SaveGhostItems(ghostItemsDto,user);
	}
}