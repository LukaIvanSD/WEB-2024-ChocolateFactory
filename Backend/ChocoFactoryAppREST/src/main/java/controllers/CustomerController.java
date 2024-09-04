package controllers;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Customer;
import dao.CustomerDAO;
@Path("/customers")
public class CustomerController {
	@Context
	ServletContext ctx;
	public CustomerController() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("CustomerDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("CustomerDAO", new CustomerDAO(contextPath));
		}
	}
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Customer>GetAll()
	{
		CustomerDAO customerDao = (CustomerDAO) ctx.getAttribute("CustomerDAO");
		return  customerDao.GetAll();
	}
	@PUT
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response Update(Customer customer)
	{
		CustomerDAO customerDao = (CustomerDAO) ctx.getAttribute("CustomerDAO");
		if(customerDao.Update(customer))
			return Response.status(200).entity("OK").build();
		return Response.status(404).entity("User not found").build();
	}
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response Delete(@PathParam("id")Integer id)
	{
		CustomerDAO customerDao = (CustomerDAO) ctx.getAttribute("CustomerDAO");
		if(customerDao.DeleteById(id))
			return Response.status(200).entity("OK").build();
		return Response.status(404).entity("User not found").build();
	}
}
