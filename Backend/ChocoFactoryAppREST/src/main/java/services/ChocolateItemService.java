package services;

import javax.ws.rs.core.Response;

import beans.ChocolateItem;
import beans.User;
import dao.ChocolateItemDAO;
import dao.WorkerDAO;
import dto.ChocolateEditQuantityDTO;

public class ChocolateItemService {
	private ChocolateItemDAO chocolateItemDao;
	private WorkerDAO workerDao;

	public ChocolateItemService(ChocolateItemDAO chocolateItemDao,WorkerDAO workerDao) {
		// TODO Auto-generated constructor stub
		this.chocolateItemDao=chocolateItemDao;
		this.workerDao=workerDao;
	}

	public Response UpdateQuantity(ChocolateEditQuantityDTO item, User user) {
		// TODO Auto-generated method stub
		if(item.getQuantity()==null)
			return Response.status(Response.Status.BAD_REQUEST).entity("Invalid input no new quantity").build();
		if(item.getQuantity()<0)
			  return Response.status(Response.Status.BAD_REQUEST).entity("Invalid input: quantity must be non-negative").build();
		if(workerDao.GetByUser(user.getId()).getFactoryId()!=item.getChocolateItem().getFactoryId())
			return Response.status(Response.Status.FORBIDDEN).entity("User does not have management permissions for this chocolate").build();	
		
		ChocolateItem chocolateItem=chocolateItemDao.GetById(item.getChocolateItem().getId());
		if(chocolateItem==null)
			  return Response.status(Response.Status.BAD_REQUEST).entity("Invalid input: item non-existant").build();
		chocolateItem.setQuantity(item.getQuantity());
		chocolateItemDao.Update(chocolateItem);
		return Response.status(Response.Status.OK).entity("Item successfully edited").build();
	}
	

}
