package services;

import javax.ws.rs.core.Response;

import beans.User;
import dao.CustomerDAO;
import dao.CustomerTypeDAO;
import dao.FactoryDAO;
import dao.ManagerDAO;
import dao.UserDAO;
import dao.WorkerDAO;
import dto.AdminProfileDTO;
import dto.CustomerProfileDTO;
import dto.ManagerProfileDTO;
import dto.WorkerProfileDTO;

public class UserProfileService {


	private UserDAO userDao;
	private CustomerDAO customerDao;
	private CustomerTypeDAO customerTypeDao;
	private ManagerDAO managerDao;
	private WorkerDAO workerDao;
	private FactoryDAO factoryDao;
	
	public UserProfileService() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserProfileService(UserDAO userDao, CustomerDAO customerDao, CustomerTypeDAO customerTypeDao,
			ManagerDAO managerDao, WorkerDAO workerDao, FactoryDAO factoryDAO) {
		super();
		this.userDao = userDao;
		this.customerDao = customerDao;
		this.customerTypeDao = customerTypeDao;
		this.managerDao = managerDao;
		this.workerDao = workerDao;
		this.factoryDao = factoryDAO;
	}
	
	public Response getCustomerProfile(User user) {
		
		CustomerProfileDTO dto = new CustomerProfileDTO();
		dto.setUser(user);
		dto.setCustomer(customerDao.GetByUser(user.getId()));
		dto.setCustomerType(customerTypeDao.GetById(dto.getCustomer().getCustomerTypeId()));
		
		
		return Response.status(Response.Status.OK).entity(dto).build();
	}
	
	public Response getAdminProfile(User user) {
			
		AdminProfileDTO dto = new AdminProfileDTO(user);
		
		System.out.println(dto.getUser().getSex());
		return Response.status(Response.Status.OK).entity(dto).build();
	}

	public Response getManagerProfile(User user) {
		ManagerProfileDTO dto;
		if(managerDao.GetByUser(user.getId()).getFactoryId()!=-1)
			 dto = new ManagerProfileDTO(user, factoryDao.GetById(managerDao.GetByUser(user.getId()).getFactoryId()).getName());
		else
			 dto = new ManagerProfileDTO(user,"Not employed");

		return Response.status(Response.Status.OK).entity(dto).build();
	}

	public Response getWorkerProfile(User user) {
		
		WorkerProfileDTO dto = new WorkerProfileDTO(user, factoryDao.GetById(workerDao.GetByUser(user.getId()).getFactoryId()).getName());
		
		
		return Response.status(Response.Status.OK).entity(dto).build();
	}
	
	
	
}
