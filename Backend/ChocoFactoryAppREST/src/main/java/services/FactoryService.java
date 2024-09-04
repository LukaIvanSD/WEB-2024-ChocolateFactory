package services;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import beans.Chocolate;
import beans.ChocolateItem;
import beans.Comment;
import beans.Comment.Status;
import beans.Factory;
import beans.Factory.FactoryStatus;
import beans.Location;
import beans.Manager;
import beans.User;
import beans.Worker;
import beans.CartArticle;
import beans.CartArticle.DeletionStatus;
import beans.User.UserType;
import dao.CartArticleDAO;
import dao.ChocolateDAO;
import dao.ChocolateItemDAO;
import dao.ChocolatePriceDAO;
import dao.CommentDAO;
import dao.FactoryDAO;
import dao.LocationDAO;
import dao.ManagerDAO;
import dao.UserDAO;
import dao.WorkerDAO;
import dto.CommentDTO;
import dto.FactoryDTO;
import dto.FactoryMainDTO;
import dto.FactoryManagerDTO;

public class FactoryService {
	private FactoryDAO factoryDao;
	private LocationDAO locationDao;
	private ChocolateItemDAO chocolateItemDao;
	private ChocolateDAO chocolateDao;
	private ManagerDAO managerDao;
	private WorkerDAO workerDao;
	private UserDAO userDao;
	private ChocolatePriceDAO chocolatePriceDao;
	private CartArticleDAO cartArticleDao;
	private CommentDAO commentDao;

	
	public FactoryService(FactoryDAO factoryDao,LocationDAO locationDao, ChocolateItemDAO chocolateItemDao) {
	this.factoryDao=factoryDao;
	this.locationDao=locationDao;
	this.chocolateItemDao=chocolateItemDao;
	}
	public FactoryService(FactoryDAO factoryDao,LocationDAO locationDao) {
		this.factoryDao=factoryDao;
		this.locationDao=locationDao;
		}
	
	public FactoryService(ChocolateDAO chocolateDao,ChocolateItemDAO chocolateItemDao,FactoryDAO factoryDao,LocationDAO locationDao) {
		// TODO Auto-generated constructor stub
		this.chocolateDao=chocolateDao;
		this.chocolateItemDao=chocolateItemDao;
		this.factoryDao=factoryDao;
		this.locationDao=locationDao;
	}
	public FactoryService(FactoryDAO factoryDao, LocationDAO locationDao, ManagerDAO managerDao,
			WorkerDAO workerDao, ChocolateItemDAO chocolateItemDao,CartArticleDAO cartArticleDao,
			ChocolateDAO chocolateDao, ChocolatePriceDAO chocolatePriceDao,UserDAO userDao) {
		// TODO Auto-generated constructor stub
		this.factoryDao=factoryDao;
		this.locationDao=locationDao;
		this.managerDao=managerDao;
		this.workerDao=workerDao;
		this.chocolateItemDao=chocolateItemDao;
		this.cartArticleDao=cartArticleDao;
		this.chocolateDao=chocolateDao;
		this.chocolatePriceDao=chocolatePriceDao;
		this.userDao=userDao;
	}

	public Collection<FactoryDTO> GetSortedFactories(){
		Collection<FactoryDTO> factoriesDto = new ArrayList<>();
		factoryDao.GetAll().stream()
		.sorted((t1, t2) -> t1.getStatus().compareTo(t2.getStatus()))
		.collect(Collectors.toList()).forEach(factory-> factoriesDto.add(new FactoryDTO(factory,locationDao.GetById(factory.getLocationId()))));
		
		return  factoriesDto;
	}
	
	
	public Collection<FactoryDTO>SearchFactories(List<String> selectedFactories,List<Chocolate> selectedChocolates,List<String> locations,double rating){
		Collection<FactoryDTO> factoriesDto = new ArrayList<>();
		List<ChocolateItem>chocolateItems=GetChocolateItems(selectedChocolates);
		
		if(chocolateItems.size()==0)
		for(Factory factory : factoryDao.GetAll())
		{
			if(selectedFactories.stream().anyMatch(selectedFactory->factory.getName().equals(selectedFactory)) && locations.stream().anyMatch(location->locationDao.GetById(factory.getLocationId()).getAddress().equals(location) /*&& factory.getRating()==rating*/))
				factoriesDto.add(new FactoryDTO(factory,locationDao.GetById(factory.getLocationId())));
		}
		else
		{
			for(Factory factory : factoryDao.GetAll())
			{
				if(selectedFactories.stream().anyMatch(selectedFactory->factory.getName().equals(selectedFactory)) && locations.stream().anyMatch(location->locationDao.GetById(factory.getLocationId()).getAddress().equals(location) /*&& factory.getRating()==rating)*/ && chocolateItems.stream().anyMatch(chocolateItem->chocolateItem.getFactoryId()==factory.getId())))
					factoriesDto.add(new FactoryDTO(factory,locationDao.GetById(factory.getLocationId())));
			}
		}
		return factoriesDto;
	}
	
	public List<ChocolateItem>GetChocolateItems(List<Chocolate> selectedChocolates){
		List<ChocolateItem>chocolateItems=new ArrayList<>();
		for(ChocolateItem chocolateItem : chocolateItemDao.GetAll())
			if(selectedChocolates.stream().anyMatch(chocolate->chocolate.getId()==chocolateItem.getChocolateId()))	
				chocolateItems.add(chocolateItem);
		return chocolateItems;
		
	}
	public FactoryMainDTO getFactory(int id) {
		Factory factory=factoryDao.GetById(id);
		if(factory==null)
			return null;
			return new FactoryMainDTO(factory,locationDao.GetById(factory.getLocationId()));
		
	}
	public Factory CreateFactory(Factory factory,Location location) {
		// TODO Auto-generated method stub
		location.setDeletionStatus(DeletionStatus.Active);
		locationDao.Save(location);
		factory.setLocationId(location.getId());
		factory.setDeletionStatus(DeletionStatus.Active);
		factory.setRating(0);
		factoryDao.Save(factory);
		return factory;
	}
	
	public Boolean CheckFactoryForm(Factory factory)
	{
		 if (factory.getLogoPath() == null || factory.getLogoPath().isBlank() || 
			 factory.getName() == null || factory.getName().isBlank()|| factory.getOpenFrom()==null || factory.getOpenTo()==null)
		           return false;
		 
		 return true;
	}
	public Boolean CheckLocationForm(Location location)
	{
		 if (location.getAddress() == null || location.getAddress().isBlank() ||
			 location.getCity()==null || location.getAddress().isBlank() || 
			 location.getPostcode()==null||location.getPostcode().isBlank()||
			 location.getLatitude() == 0 ||
			 location.getLongitude() == 0) 
		            return false;
		 return true;
	}
	public Response AreFormsValid(Factory factory,Location location) 
	{
		if(	!CheckFactoryForm(factory))
		return Response.status(400).entity("Factory form not valid").build();
		if(!CheckLocationForm(location))
		return Response.status(400).entity("Location form not valid").build();
		return Response.status(200).build();
	}
	public List<FactoryDTO> GetFilteredFactories(List<FactoryDTO> factories, List<String> types, List<String> categories,Boolean onlyOpen) {
		// TODO Auto-generated method stub
		if(factories==null)
			factories=(List<FactoryDTO>) GetSortedFactories();
		if(onlyOpen)
		factories.removeIf(factory->factoryDao.GetById(factory.getId()).IsClosed());
		List<FactoryDTO>filteredFactories=new ArrayList<>();
		ChocolateService chocolateService=new ChocolateService(chocolateDao,chocolateItemDao);
		for(FactoryDTO factory : factories)
		{
			List<Chocolate>chocolates=chocolateService.GetFactoryChocolates(factory.getId());
			Boolean hasType=true;
			Boolean hasCategory=true;
			if(types.size()!=0)
				hasType=chocolates.stream().anyMatch(chocolate->types.stream().anyMatch(type->type.equals(chocolate.getType())));
			if(categories.size()!=0)
				hasCategory=chocolates.stream().anyMatch(chocolate->categories.stream().anyMatch(category->category.equals(chocolate.getCategory())));
			if(hasType && hasCategory)
				filteredFactories.add(factory);
		}
		return filteredFactories;
	}
	public Response deleteFactory(Integer factoryId) {
		// TODO Auto-generated method stub
		Factory factory=factoryDao.GetById(factoryId);
		if(factory==null)
			return Response.status(Response.Status.BAD_REQUEST).build();
		Location location=locationDao.GetById(factory.getLocationId());
		if(location==null)
			return Response.status(Response.Status.BAD_REQUEST).build();
		Manager manager=null;
		for(Manager m:managerDao.GetAll())
		{
			if(m.getFactoryId()==factoryId)
			{
				manager=m;
				break;
			}
		}
		if(manager==null)
			return Response.status(Response.Status.BAD_REQUEST).build();
		for(Worker worker:workerDao.GetAll())
		{
			if(worker.getFactoryId()==factoryId)
			{
				userDao.DeleteById(worker.getUserId());
				workerDao.DeleteById(worker.getId());
			}
		}
		for(ChocolateItem chocolateItem:chocolateItemDao.GetByFactoryId(factoryId))
		{
			for(CartArticle cartArticle : cartArticleDao.GetAll())
			{
				if(cartArticle.getChocolateItemId()==chocolateItem.getId())
					cartArticleDao.DeleteById(cartArticle.getId());
			}
			chocolatePriceDao.InvalidatePrice(chocolateItem.getId());
			chocolateDao.DeleteById(chocolateItem.getChocolateId());
			chocolateItemDao.DeleteById(chocolateItem.getId());
			
		}
		manager.setFactoryId(-1);
		managerDao.Update(manager);
		locationDao.DeleteById(location.getId());
		factoryDao.DeleteById(factory.getId());
		return Response.status(Response.Status.OK).build();
	}
	
}
