package controllers;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.User;
import dao.CartArticleDAO;
import dao.CartDAO;
import dao.ChocolateItemDAO;
import dao.CustomerDAO;
import dto.CartArticleDTO;
import dto.CartArticleRequestDTO;
import services.CartArticleService;

@Path("/cartArticle")
public class CartArticleController {
	
	@Context
	ServletContext ctx;
	public CartArticleController() {}
	
	@PostConstruct
	public void init() {
		if(ctx.getAttribute("CartArticleDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("CartArticleDAO", new CartArticleDAO(context));
		}
		if(ctx.getAttribute("CartDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("CartDAO", new CartDAO(context));
		}
		if(ctx.getAttribute("CustomerDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("CustomerDAO", new CustomerDAO(context));
		}
		if(ctx.getAttribute("ChocolateItemDAO")==null)
		{
			String context=ctx.getRealPath("");
			ctx.setAttribute("ChocolateItemDAO", new ChocolateItemDAO(context));
		}
	}
	@Path("/")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response SaveArticleToCart(CartArticleRequestDTO cartArticleRequestDto, @Context HttpServletRequest request) {
		User user=(User)request.getSession().getAttribute("user");
		if(user==null || !user.IsCustomer())
			return Response.status(Status.FORBIDDEN).entity("No permission").build();
		CartArticleService cartArticleService=new CartArticleService((CartArticleDAO)ctx.getAttribute("CartArticleDAO"),(CartDAO)ctx.getAttribute("CartDAO"),(CustomerDAO)ctx.getAttribute("CustomerDAO"),(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"));
		return cartArticleService.SaveArticle(cartArticleRequestDto,user);
	}
}
