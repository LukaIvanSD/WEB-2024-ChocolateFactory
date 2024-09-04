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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.Chocolate;
import beans.Factory;
import beans.User;
import beans.User.UserType;
import dao.CartArticleDAO;
import dao.ChocolateDAO;
import dao.ChocolateItemDAO;
import dao.ChocolatePriceDAO;
import dao.FactoryDAO;
import dao.LocationDAO;
import dao.ManagerDAO;
import dao.UserDAO;
import dao.WorkerDAO;
import dto.FactoryDTO;
import dto.FactoryMainDTO;
import dto.FilterRequestDTO;
import dto.SearchDataDTO;
import services.ChocolateService;
import services.FactoryService;
import services.SearchService;

@Path("/factories")
public class FactoryController {
	@Context
	ServletContext ctx;
	public FactoryController() {
		
	}
	@PostConstruct
	public void init(){
		if(ctx.getAttribute("FactoryDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("FactoryDAO", new FactoryDAO(contextPath));
		}
		if(ctx.getAttribute("LocationDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("LocationDAO", new LocationDAO(contextPath));
		}
		if(ctx.getAttribute("ChocolateItemDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("ChocolateItemDAO", new ChocolateItemDAO(contextPath));
		}
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
		if(ctx.getAttribute("CartArticleDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("CartArticleDAO", new CartArticleDAO(contextPath));
		}
		if(ctx.getAttribute("WorkerDAO")==null)
		{
			String contextPath=ctx.getRealPath("");
			ctx.setAttribute("WorkerDAO", new WorkerDAO(contextPath));
		}
		
	}
	
	@Path("/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Factory> GetAll() 
	{
		FactoryDAO factoryDao=(FactoryDAO )ctx.getAttribute("FactoryDAO");
		return factoryDao.GetAll();
	}
	@Path("/sort")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<FactoryDTO> GetSorted() 
	{
		
		FactoryService factoryService=new FactoryService((FactoryDAO )ctx.getAttribute("FactoryDAO"),(LocationDAO )ctx.getAttribute("LocationDAO"));
		return factoryService.GetSortedFactories();
	}
	
	@Path("/search1")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<FactoryDTO> GetSorted(SearchDataDTO searchDataDTO) 
	{   List<String> selectedFactories = searchDataDTO.getSelectedFactories();
	 	if(selectedFactories.isEmpty())
	 		selectedFactories.add("");
    	List<Chocolate> selectedChocolates = searchDataDTO.getSelectedChocolates();
    	List<String> locations = searchDataDTO.getLocations();
    	double rating = searchDataDTO.getRating();
    	if(locations.isEmpty())
    		locations.add("");
		FactoryService factoryService=new FactoryService((FactoryDAO )ctx.getAttribute("FactoryDAO"),(LocationDAO )ctx.getAttribute("LocationDAO"),(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"));
		return factoryService.SearchFactories(selectedFactories,selectedChocolates,locations,rating);
	}
	@Path("/search")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<FactoryDTO> GetSorted(@QueryParam("parameter")String searchParameter) 
	{
		if(searchParameter.isBlank())
			return GetSorted();
		SearchService searchService=new SearchService((FactoryDAO )ctx.getAttribute("FactoryDAO"),(LocationDAO )ctx.getAttribute("LocationDAO"),(ChocolateDAO)ctx.getAttribute("ChocolateDAO"),(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"));
		return searchService.SearchFactories(searchParameter);
	}
	@Path("/show/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public FactoryMainDTO getFactory(@PathParam("id")Integer id ) {
		FactoryService factoryService=new FactoryService((FactoryDAO )ctx.getAttribute("FactoryDAO"),(LocationDAO )ctx.getAttribute("LocationDAO"));
		return factoryService.getFactory(id);
	}
	@Path("/filter")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public List<FactoryDTO> GetFilteredFactories(FilterRequestDTO filters)
	{
		FactoryService factoryService=new FactoryService((ChocolateDAO)ctx.getAttribute("ChocolateDAO"),(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"),(FactoryDAO)ctx.getAttribute("FactoryDAO"),(LocationDAO)ctx.getAttribute("LocationDAO"));
		return factoryService.GetFilteredFactories(filters.getFactories(),filters.getTypes(),filters.getCategories(),filters.getOnlyOpen());

	}
	@Path("/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeFactory(@PathParam ("id")Integer factoryId,@Context HttpServletRequest request) {
		User user=(User)request.getSession().getAttribute("user");
		if(user ==null || !user.IsAdmin()|| factoryId==null)
			return Response.status(Status.BAD_REQUEST).build();
		FactoryService factoryService=new FactoryService((FactoryDAO)ctx.getAttribute("FactoryDAO"),(LocationDAO)ctx.getAttribute("LocationDAO"),(ManagerDAO)ctx.getAttribute("ManagerDAO"),(WorkerDAO)ctx.getAttribute("WorkerDAO"),(ChocolateItemDAO)ctx.getAttribute("ChocolateItemDAO"),(CartArticleDAO)ctx.getAttribute("CartArticleDAO"),(ChocolateDAO)ctx.getAttribute("ChocolateDAO"),(ChocolatePriceDAO)ctx.getAttribute("ChocolatePriceDAO"),(UserDAO)ctx.getAttribute("UserDAO"));
		return factoryService.deleteFactory(factoryId);
	}
}
