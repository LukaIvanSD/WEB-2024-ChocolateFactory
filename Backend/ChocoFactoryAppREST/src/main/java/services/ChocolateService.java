package services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.Chocolate;
import beans.ChocolateItem;
import beans.ChocolateItem.Availability;
import beans.ChocolatePrice;
import beans.User;
import beans.CartArticle.DeletionStatus;
import beans.User.UserType;
import dao.ChocolateDAO;
import dao.ChocolateItemDAO;
import dao.ChocolatePriceDAO;
import dao.ManagerDAO;
import dao.WorkerDAO;
import dto.ChocolateDTO;
import dto.ChocolateEditRequestDTO;
import dto.FiltersDTO;

public class ChocolateService {
	private ChocolateDAO chocolateDao;
	private ChocolateItemDAO chocolateItemDao;
	private ChocolatePriceDAO chocolatePriceDao;
	private ManagerDAO managerDao;
	private WorkerDAO workerDao;
	public ChocolateService(ChocolateDAO chocolateDao,ChocolateItemDAO chocolateItemDao,ChocolatePriceDAO chocolatePriceDao,ManagerDAO managerDao,WorkerDAO workerDao) {
		this.chocolateDao=chocolateDao;
		this.chocolateItemDao=chocolateItemDao;
		this.chocolatePriceDao=chocolatePriceDao;
		this.workerDao=workerDao;
		this.managerDao=managerDao;
	}
	


	public ChocolateService(ChocolateDAO chocolateDao, ChocolateItemDAO chocolateItemDao, ChocolatePriceDAO chocolatePriceDao, ManagerDAO managerDao) {
		this.chocolateDao=chocolateDao;
		this.chocolateItemDao=chocolateItemDao;
		this.chocolatePriceDao=chocolatePriceDao;
		this.managerDao=managerDao;
	}




	public ChocolateService(ChocolateDAO chocolateDao, ChocolateItemDAO chocolateItemDao) {
		// TODO Auto-generated constructor stub
		this.chocolateDao=chocolateDao;
		this.chocolateItemDao=chocolateItemDao;
	}



	public Collection<ChocolateDTO> GetFactoryChocolates(int factoryId,User user){
		Collection<ChocolateDTO> chocolatesDto=new ArrayList<>();
		if(user==null || user.getUserType()==UserType.Customer || (user.getUserType()==UserType.Manager && managerDao.GetByUser(user.getId()).getFactoryId()!=factoryId )|| (user.getUserType()==UserType.Worker && workerDao.GetByUser(user.getId()).getFactoryId()!=factoryId))
		{
			for(ChocolateItem chocolateItem : chocolateItemDao.GetAll())
				if(chocolateItem.getFactoryId()==factoryId && chocolateItem.getAvailability()==Availability.Available)
				{
					chocolatesDto.add(new ChocolateDTO(chocolateDao.GetById(chocolateItem.getChocolateId()),chocolateItem,chocolatePriceDao.getValid(chocolateItem.getId())));
				}
				
		}
		else
			for(ChocolateItem chocolateItem : chocolateItemDao.GetAll())
				if(chocolateItem.getFactoryId()==factoryId)
				{
					chocolatesDto.add(new ChocolateDTO(chocolateDao.GetById(chocolateItem.getChocolateId()),chocolateItem,chocolatePriceDao.getValid(chocolateItem.getId())));
				}
		return chocolatesDto;
	}



	public Response CreateChocolate(Chocolate chocolate, int price,int factoryId, User user) {
		// TODO Auto-generated method stub
		if(managerDao.GetByUser(user.getId()).getFactoryId()!=factoryId)
			return Response.status(Response.Status.FORBIDDEN).entity("User does not have management permissions for this factory").build();	
		if (!CheckForm(chocolate,price)) {
            return Response.status(400).entity("Invalid form").build();
        }
		chocolate.setFactoryCreatorId(factoryId);
		chocolate.setDeletionStatus(DeletionStatus.Active);
		chocolate=chocolateDao.Save(chocolate);
		ChocolateItem chocolateItem=new ChocolateItem(chocolate.getId(),0,factoryId);
		chocolateItem.setDeletionStatus(DeletionStatus.Active);
		chocolateItem=chocolateItemDao.Save(chocolateItem);
		ChocolatePrice chocolatePrice=new ChocolatePrice(chocolateItem.getId(),price);
		chocolatePrice.setDeletionStatus(DeletionStatus.Active);
		chocolatePrice=chocolatePriceDao.Save(chocolatePrice);
		return Response.status(Response.Status.OK).entity(new ChocolateDTO(chocolate,chocolateItem,chocolatePrice)).build();
		
	}
	public Boolean CheckForm(Chocolate chocolate,int price)
	{
	    if (chocolate.getName() == null || chocolate.getName().isBlank() || price<= 0 || 
	        chocolate.getCategory() == null || chocolate.getCategory().isBlank() || 
	        chocolate.getType() == null || chocolate.getType().isBlank() ||  chocolate.getWeight() <= 0 || 
	        chocolate.getDescription() == null || chocolate.getDescription().isBlank() || 
	        chocolate.getImagePath() == null || chocolate.getImagePath().isBlank()||
	        chocolate.getWeight()<=0) {
	        return false;
	    }
	    return true;
	}



	public Response DeleteChocolate(ChocolateItem item, User user) {
		// TODO Auto-generated method stub
		if(managerDao.GetByUser(user.getId()).getFactoryId()!=item.getFactoryId())
			return Response.status(Response.Status.FORBIDDEN).entity("User does not have management permissions for this factory").build();	
		if(!chocolateItemDao.DeleteById(item.getId()))
			return Response.status(404).entity("Chocolate not found").build();
		if(!chocolatePriceDao.InvalidatePrice(item.getId()))
			return Response.status(404).entity("ChocolateItem not found").build();
		return Response.status(200).entity("Chocolate successfully deleted").build();
	}



	public Response EditChocolate(int chocolateItemId, Map<String, Object> updates,User user) {
		// TODO Auto-generated method stub
		ChocolateItem chocolateItem=chocolateItemDao.GetById(chocolateItemId);
		if(chocolateItem==null)
			return Response.status(404).entity("Chocolate not found").build();
		if(managerDao.GetByUser(user.getId()).getFactoryId()!=chocolateItem.getFactoryId())
			return Response.status(Response.Status.FORBIDDEN).entity("User does not have management permissions for this factory").build();	
		Chocolate chocolate=chocolateDao.GetById(chocolateItem.getChocolateId());
		ChocolatePrice chocolatePrice=new ChocolatePrice(chocolatePriceDao.GetByChocolateItemId(chocolateItemId));
		AtomicBoolean  isPriceUpdated= new AtomicBoolean (false);
		updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                	chocolate.setName((String)value);
                    break;
                case "type":
                	chocolate.setType((String) value);
                    break;
                case "category":
                	chocolate.setCategory((String) value);
                    break;
                case "weight":
                	chocolate.setWeight(Double.valueOf(value.toString()));
                    break;
                case "description":
                	chocolate.setDescription((String) value);
                    break;
                case "imagePath":
                	chocolate.setImagePath((String) value);
                    break;
                case "price":
                	chocolatePrice.setPrice(Double.valueOf(value.toString()));
                	isPriceUpdated.set(true);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid attribute: " + key);
            }
        });
		chocolateDao.Update(chocolate);
		if(isPriceUpdated.get())
		chocolatePriceDao.Update(chocolatePrice);
		return Response.status(200).entity("Chocolate successfully edited").build();
	}



	public FiltersDTO GetFilters() {
		// TODO Auto-generated method stub
		FiltersDTO filters=new FiltersDTO();
		for(Chocolate chocolate : chocolateDao.GetAll())
		{
			if(IsChocolateInAnyFactory(chocolate))
			{
				if(!filters.getCategories().contains(chocolate.getCategory()))
					filters.getCategories().add(chocolate.getCategory());
				if(!filters.getTypes().contains(chocolate.getType()))
					filters.getTypes().add(chocolate.getType());
			}
		}
		return filters;
	}



	public List<Chocolate> GetFactoryChocolates(int factoryId) {
		// TODO Auto-generated method stub
		Collection<ChocolateItem>chocolateItems=chocolateItemDao.GetByFactoryId(factoryId);
		List<Chocolate>chocolates=new ArrayList<>();
		for(ChocolateItem chocolateItem : chocolateItems) {
			chocolates.add(chocolateDao.GetById(chocolateItem.getChocolateId()));
		}
		return chocolates;
	}



	public Response EditChocolate(ChocolateEditRequestDTO chocolateRequestDto, User user) {
		// TODO Auto-generated method stub
		double price=chocolateRequestDto.getPrice();
		Chocolate editedChocolate=chocolateRequestDto.getUpdatedChocolate();
		ChocolateDTO chocolateDto=chocolateRequestDto.getItem();
		
		if(managerDao.GetByUser(user.getId()).getFactoryId()!=chocolateDto.getChocolateItem().getFactoryId())
			return Response.status(Response.Status.FORBIDDEN).entity("User does not have management permissions for this factory").build();	
		if(chocolateDto.getChocolate().getId()!=chocolateDto.getChocolateItem().getChocolateId() || chocolateDto.getChocolateItem().getId()!=chocolateDto.getChocolatePrice().getChocolateItemId())
			return Response.status(404).entity("Invalid request").build();
		if(price>0)
		{
			chocolateDto.getChocolatePrice().setPrice(price);
			chocolatePriceDao.Update(chocolateDto.getChocolatePrice());
		}
		if(chocolateDto.getChocolate().getFactoryCreatorId()==chocolateDto.getChocolateItem().getFactoryId())
			chocolateDao.Update(editedChocolate);
		return Response.status(Status.OK).entity("Chocolate successfully edited").build();
	}
	private Boolean IsChocolateInAnyFactory(Chocolate chocolate)
	{
		Collection<ChocolateItem> item=chocolateItemDao.GetByChoclateId(chocolate.getId());
		return item.size()!=0 && item.stream().allMatch(chocoItem->chocoItem.IsAvailable());
	}
}
