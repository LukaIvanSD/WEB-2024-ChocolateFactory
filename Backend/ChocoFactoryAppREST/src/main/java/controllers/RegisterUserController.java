package controllers;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.Factory;
import beans.Manager;
import beans.User;
import beans.User.UserType;
import dao.CartDAO;
import dao.CustomerDAO;
import dao.FactoryDAO;
import dao.LocationDAO;
import dao.ManagerDAO;
import dao.UserDAO;
import dao.WorkerDAO;
import dto.FactoryManagerDTO;
import services.FactoryService;
import services.RegistrationService;

@Path("/register")
public class RegisterUserController {
	@Context
	ServletContext ctx;
	
	public RegisterUserController() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("UserDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("UserDAO", new UserDAO(contextPath));
		}
		if (ctx.getAttribute("ManagerDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("ManagerDAO", new ManagerDAO(contextPath));
		}
		if (ctx.getAttribute("CustomerDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("CustomerDAO", new CustomerDAO(contextPath));
		}
		if (ctx.getAttribute("WorkerDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("WorkerDAO", new WorkerDAO(contextPath));
		}
		if (ctx.getAttribute("LocationDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("LocationDAO", new LocationDAO(contextPath));
		}
		if (ctx.getAttribute("FactoryDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("FactoryDAO", new FactoryDAO(contextPath));
		}
		if (ctx.getAttribute("CartDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("CartDAO", new CartDAO(contextPath));
		}
	}
	
	@POST
	@Path("/factory")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response RegisterUser(FactoryManagerDTO factoryManagerDto, @Context HttpServletRequest request) {
		 User registrator = (User) request.getSession().getAttribute("user");
		if (registrator==null || !registrator.IsAdmin())
			return Response.status(403).entity("Access Denied: You do not have permission to access this function").build();		
		
	    RegistrationService registrationService=new RegistrationService((UserDAO) ctx.getAttribute("UserDAO"),(ManagerDAO) ctx.getAttribute("ManagerDAO"), (CustomerDAO) ctx.getAttribute("CustomerDAO"),(WorkerDAO)ctx.getAttribute("WorkerDAO"),(CartDAO)ctx.getAttribute("CartDAO"));
		FactoryService factoryService=new FactoryService((FactoryDAO )ctx.getAttribute("FactoryDAO"),(LocationDAO )ctx.getAttribute("LocationDAO"));
		
		//Checks if factory and location forms are valid
		Response response=factoryService.AreFormsValid(factoryManagerDto.getFactory(),factoryManagerDto.getLocation());
		if(response.getStatus()!=200)
			return response;
		//if user creates manager
		if(factoryManagerDto.getManager()==null)
		{
			//Checks if user form is valid
			if(registrationService.CheckRegistration(factoryManagerDto.getUser()).getStatus()!=200)
				return Response.status(Status.BAD_REQUEST).entity("User form not valid").build();
			
			factoryManagerDto.setFactory(factoryService.CreateFactory(factoryManagerDto.getFactory(),factoryManagerDto.getLocation()));
			registrationService.CreateManager(factoryManagerDto.getFactory(),factoryManagerDto.getUser());
			return Response.status(Status.OK).entity("factory, location, manager added").build();
		}
		else if(factoryManagerDto.getUser()==null)
		{	
			ManagerDAO managerDao=(ManagerDAO)ctx.getAttribute("ManagerDAO");
			Manager manager=managerDao.GetById(factoryManagerDto.getManager().getId());
			//Checks if manager is valid
			if(manager==null)
				return Response.status(Status.BAD_REQUEST).entity("Manager does not exist").build();
			if(manager.getFactoryId()!=-1)
				return Response.status(Status.BAD_REQUEST).entity("Manager already has a job").build();
			factoryManagerDto.setFactory(factoryService.CreateFactory(factoryManagerDto.getFactory(),factoryManagerDto.getLocation()));
			registrationService.UpdateManager(factoryManagerDto.getFactory(),factoryManagerDto.getManager());
			return Response.status(Status.OK).entity("factory, location added manager updated").build();
		}
		else
			return Response.status(Status.BAD_REQUEST).entity("You cant register a manager and select already existant manager").build();
	}

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	 public Response RegisterUser(User user, @Context HttpServletRequest request) {
        User registrator = (User) request.getSession().getAttribute("user");
        RegistrationService registrationService=new RegistrationService((UserDAO) ctx.getAttribute("UserDAO"),(ManagerDAO) ctx.getAttribute("ManagerDAO"), (CustomerDAO) ctx.getAttribute("CustomerDAO"),(WorkerDAO)ctx.getAttribute("WorkerDAO"),(CartDAO)ctx.getAttribute("CartDAO"));
        if (registrator != null) {
            if (registrator.IsCustomer() || registrator.IsWorker()) {
                return Response.status(400).entity("Invalid operation").build();
            } else if (registrator.IsAdmin()) {
            	return registrationService.CreateManager(user);
            } else if (registrator.IsManager()) {
                return registrationService.CreateWorker(user,((ManagerDAO) ctx.getAttribute("ManagerDAO")).GetByUser(registrator.getId()).getFactoryId());
            }
        }
        return registrationService.CreateCustomer(user);
    }
	@POST
	@Path("/checkUsername")
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean IsUsernameValid(String username) {
		return !((UserDAO)ctx.getAttribute("UserDAO")).Exists(username);
				
	}
	
}
