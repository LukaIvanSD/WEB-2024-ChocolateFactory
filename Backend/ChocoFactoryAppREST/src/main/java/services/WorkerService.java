package services;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.Manager;
import beans.User;
import beans.Worker;
import dao.ManagerDAO;
import dao.UserDAO;
import dao.WorkerDAO;

public class WorkerService {
	private WorkerDAO workerDao;
	private ManagerDAO managerDao;
	private UserDAO userDao;

	public WorkerService(WorkerDAO workerDao) {
		super();
		this.workerDao = workerDao;
	}
	public WorkerService(UserDAO userDao,ManagerDAO managerDao,WorkerDAO workerDao) {
		super();
		this.userDao = userDao;
		this.managerDao=managerDao;
		this.workerDao=workerDao;
	}
	public WorkerService(WorkerDAO workerDao, ManagerDAO managerDao) {
		// TODO Auto-generated constructor stub
		this.workerDao=workerDao;
		this.managerDao=managerDao;
	}
	public Boolean IsWorkerInFactory(User user,int factoryId) {
		// TODO Auto-generated method stub
		if(user==null)
			return false;
		Worker worker=workerDao.GetByUser(user.getId());
		if(worker.getFactoryId()!=factoryId)
			return false;
		return true;
	}
	public Integer GetWorkerFactoryId(User user) {
		
		return workerDao.GetByUser(user.getId()).getFactoryId();
	}
	public Response GetNumberOfWorkersInFactory(User user,int factoryId) {
		Manager manager=managerDao.GetByUser(user.getId());
		if(manager==null || manager.getFactoryId()!=factoryId)
			return Response.status(Status.BAD_REQUEST).build();
		long workers=workerDao.GetAll().stream().filter(worker->worker.getFactoryId()==manager.getFactoryId()).count();
		return Response.status(Status.OK).entity(workers).build();
	}
	public List<User> getAllFactoryWorkers(Integer factoryId, User user) {
		// TODO Auto-generated method stub
		Manager manager=managerDao.GetByUser(user.getId());
		if(factoryId!=manager.getFactoryId())
			return null;
		List<Worker>factoryWorkers=workerDao.GetAll().stream().filter(worker->worker.getFactoryId()==factoryId).collect(Collectors.toList());
		return userDao.GetAll().stream().filter(foundUser->factoryWorkers.stream().anyMatch(worker->worker.getUserId()==foundUser.getId())).collect(Collectors.toList());
	}
	public Response removeWorker(User loggedInUser,int userId) {
		// TODO Auto-generated method stub
		User user=userDao.GetById(userId);
		if(!user.IsWorker())
			return Response.status(Status.BAD_REQUEST).build();
		Worker worker=workerDao.GetByUser(userId);
		Manager manager=managerDao.GetByUser(loggedInUser.getId());
		if(worker.getFactoryId()!=manager.getFactoryId())
			return Response.status(Status.BAD_REQUEST).build();
		workerDao.DeleteById(worker.getId());
		userDao.DeleteById(userId);
		return Response.status(Status.OK).build();
	}

}
