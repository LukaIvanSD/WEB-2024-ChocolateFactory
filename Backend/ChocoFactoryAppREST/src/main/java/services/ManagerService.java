package services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.Manager;
import beans.User;
import dao.ManagerDAO;
import dao.UserDAO;
import dto.ManagerUserDTO;

public class ManagerService {
	private ManagerDAO managerDao;
	private UserDAO userDao;

	public ManagerService(ManagerDAO managerDao) {
		super();
		this.managerDao = managerDao;
	}
	public ManagerService(ManagerDAO managerDao,UserDAO userDao) {
		super();
		this.managerDao = managerDao;
		this.userDao=userDao;
	}
	public Boolean IsManagerOfFactory(User user,int factoryId) {
		// TODO Auto-generated method stub
		if(user==null)
			return false;
		Manager manager=managerDao.GetByUser(user.getId());
		if(manager.getFactoryId()!=factoryId)
			return false;
		return true;
	}
	public Response GetFreeManagers() {
		// TODO Auto-generated method stub
		List<Manager>freeManagers=managerDao.GetAll().stream().filter(manager->manager.getFactoryId()==-1) .collect(Collectors.toList());
		List<ManagerUserDTO> managerUserDtos=new ArrayList<>();
		for(Manager manager :freeManagers)
		{
			managerUserDtos.add(new ManagerUserDTO(userDao.GetById(manager.getUserId()),manager));
		}
		return Response.status(Status.OK).entity(managerUserDtos).build();
	}
	public Integer GetManagerFactoryId(User user) {
		return managerDao.GetByUser(user.getId()).getFactoryId();

	}
	
}
