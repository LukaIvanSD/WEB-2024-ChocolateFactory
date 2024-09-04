package services;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.Customer;
import beans.Factory;
import beans.Manager;
import beans.User;
import beans.Cart;
import beans.CartArticle.DeletionStatus;
import beans.User.UserType;
import beans.Worker;
import dao.CartDAO;
import dao.CustomerDAO;
import dao.ManagerDAO;
import dao.UserDAO;
import dao.WorkerDAO;
import dto.FactoryManagerDTO;

public class RegistrationService {

	private UserDAO userDao;
	private ManagerDAO managerDao;
	private CustomerDAO customerDao;
	private WorkerDAO workerDao;
	private CartDAO cartDao;
	
	public RegistrationService(UserDAO userDao,ManagerDAO managerDao,CustomerDAO customerDao,WorkerDAO workerDao,CartDAO cartDao)
	{
		this.userDao=userDao;
		this.managerDao=managerDao;
		this.customerDao=customerDao;	
		this.workerDao=workerDao;
		this.cartDao=cartDao;
	}
    

	public Response CreateManager(Factory factory,User user) {
			user.setUserType(UserType.Manager);
			user=RegisterUser(user);
	        Response response=RegisterManager(user.getId(),factory.getId());
        return response;
    }
    public Response UpdateManager(Factory factory,Manager manager) {
    	
    		Manager newManager=managerDao.GetById(manager.getId());
    		newManager.setFactoryId(factory.getId());
    		managerDao.Update(newManager);
    		return Response.status(200).entity("OK").build();
    }
    public Response CreateManager(User user) {
    	Response response=CheckRegistration(user);
		if(response.getStatus() == 200)
		{
	        user.setUserType(UserType.Manager);
	        user=RegisterUser(user);
	        RegisterManager(user.getId());
		}
			
        return response;
    	
    }
    public Response CreateCustomer(User user) {
        Response response=CheckRegistration(user);
   		if(response.getStatus() == 200)
   		{
   		    user.setUserType(UserType.Customer);
	        user=RegisterUser(user);
	        Customer customer=RegisterCustomer(user.getId());
	        CreateCart(customer.getId());
   		}
   			
   		return response;
    }
	public Response CreateWorker(User user,int factoryId) {
        Response response=CheckRegistration(user);
   		if(response.getStatus() == 200)
   		{
   		    user.setUserType(UserType.Worker);
   		    user=RegisterUser(user);
	        RegisterWorker(user.getId(),factoryId);
   		}
   			
   		return response;
    }
    
    
    
    public Response CheckRegistration(User user) {
        if (!CheckForm(user)) {
            return Response.status(400).entity("Invalid form").build();
        }
        if (userDao == null) {
            return Response.status(500).entity("Internal server error: UserDAO not found").build();
        }
        Boolean exists = userDao.Exists(user.getUsername());
        if (exists) {
            return Response.status(400).entity("Username already exists").build();
        }
        return Response.status(200).build();
    }
    
	public Boolean CheckForm(User user)
	{
		 if (user.getUsername() == null || user.getUsername().isBlank() || 
		    		user.getPassword() == null || user.getPassword().isBlank() || 
		            user.getFirstName() == null || user.getFirstName().isBlank() || 
		            user.getLastName() == null || user.getLastName().isBlank() || 
		            user.getDateOfBirth() == null || user.getDateOfBirth().toString().isBlank()) {
		            return false;
		        }
		 return true;
	}
	
	private Customer RegisterCustomer(int userId) {
		Customer customer=new Customer(userId,0,0);
		customer.setDeletionStatus(DeletionStatus.Active);
		return customerDao.Save(customer);
		
	}
    private void CreateCart(int customerId) {
		Cart cart=new Cart(customerId);
		cartDao.Save(cart);
	}

	
	private Response RegisterManager(int userId,int factoryId) {
		if(managerDao.GetAll().stream().anyMatch(manager->manager.getFactoryId()==factoryId))
			return Response.status(Status.BAD_REQUEST).entity("factory already has a manager").build();
		Manager manager=new Manager(userId,factoryId);
		manager.setDeletionStatus(DeletionStatus.Active);
		managerDao.Save(manager);
		return Response.status(200).entity("OK").build();
		
	}
	private void RegisterManager(int userId) {
		Manager manager=new Manager(userId,-1);
		manager.setDeletionStatus(DeletionStatus.Active);
		managerDao.Save(manager);
		
	}
	
	private User RegisterUser(User user) {
		user.setDeletionStatus(DeletionStatus.Active);
		userDao.Save(user);
		return user;
	}
	private void RegisterWorker(int userId, int factoryId){
		Worker worker=new Worker(userId,factoryId);
		worker.setDeletionStatus(DeletionStatus.Active);
		workerDao.Save(worker);
	}
	
	
}
