package controllers;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.User;
import dao.BlockedUserDAO;
import dao.UserDAO;
import services.LoginService;

@Path("")
public class LoginController {
	@Context
	ServletContext ctx;
	
	public LoginController() {
		
	}
	
	@PostConstruct
	// ctx polje je null u konstruktoru, mora se pozvati nakon konstruktora (@PostConstruct anotacija)
	public void init() {
		// Ovaj objekat se instancira vise puta u toku rada aplikacije
		// Inicijalizacija treba da se obavi samo jednom
		if (ctx.getAttribute("UserDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("UserDAO", new UserDAO(contextPath));
		}
		if (ctx.getAttribute("BlockedUserDAO") == null) {
	    	String contextPath = ctx.getRealPath("");
			ctx.setAttribute("BlockedUserDAO", new BlockedUserDAO(contextPath));
		}
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User user, @Context HttpServletRequest request) {
		System.out.println("GAS");
		 if ((User)request.getSession().getAttribute("user") != null)
			 return Response.status(400).entity("Already loggedIn").build();
		LoginService loginService=new LoginService((UserDAO) ctx.getAttribute("UserDAO"),(BlockedUserDAO)ctx.getAttribute("BlockedUserDAO"));
		return loginService.Login(user,request);
		
	}
	
	
	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(@Context HttpServletRequest request) {
        if ((User)request.getSession().getAttribute("user") != null) {
        	request.getSession().invalidate();
            return Response.status(200).entity("OK").build();
        }
        return Response.status(400).entity("Not loggedIn").build();
		
	}
	
	@GET
	@Path("/currentUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User login(@Context HttpServletRequest request) {
		return (User) request.getSession().getAttribute("user");
	}
}
