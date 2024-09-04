package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import beans.Chocolate;
import beans.ChocolateItem;
import beans.Factory;
import beans.Purchase;
import beans.PurchaseArticle;
import beans.User;
import dao.ChocolateDAO;
import dao.ChocolateItemDAO;
import dao.ChocolatePriceDAO;
import dao.CommentDAO;
import dao.FactoryDAO;
import dao.LocationDAO;
import dao.ManagerDAO;
import dao.PurchaseArticleDAO;
import dao.PurchaseDAO;
import dto.FactoryDTO;
import dto.PurchaseArticleDTO;
import dto.PurchaseDTO;
import dto.SearchPurchasesParamsDTO;

public class SearchService {
	private FactoryDAO factoryDao;
	private LocationDAO locationDao;
	private ChocolateDAO chocolateDao;
	private ChocolateItemDAO chocolateItemDao;
	private PurchaseArticleDAO purchaseArticleDAO;
	private PurchaseDAO purchaseDAO;
	private ChocolatePriceDAO chocolatePriceDAO;
	private ManagerDAO managerDAO;
	private CommentDAO commentDao;
	
	public SearchService(FactoryDAO factoryDao,LocationDAO locationDao,ChocolateDAO chocolateDao,ChocolateItemDAO chocolateItemDao) {
	this.factoryDao=factoryDao;
	this.locationDao=locationDao;
	this.chocolateDao=chocolateDao;
	this.chocolateItemDao=chocolateItemDao;
	}
	
	public SearchService(PurchaseArticleDAO purchaseArticleDAO,PurchaseDAO purchaseDAO, ChocolateDAO chocolateDAO, ChocolateItemDAO chocolateItemDAO, ChocolatePriceDAO chocolatePriceDAO, FactoryDAO factoryDAO,  ManagerDAO managerDAO, CommentDAO commentDAO) 
	{
		this.purchaseArticleDAO=purchaseArticleDAO;
		this.purchaseDAO=purchaseDAO;
		this.chocolateDao = chocolateDAO;
		this.chocolateItemDao = chocolateItemDAO;
		this.chocolatePriceDAO = chocolatePriceDAO;
		this.factoryDao = factoryDAO;
		this.managerDAO = managerDAO;
		this.commentDao=commentDAO;
	}
	
	public Collection<FactoryDTO> SearchFactories(String searchParameter) {
		Collection<FactoryDTO>factoriesDto=new ArrayList<>();
		double rating = tryParseDouble(searchParameter);
		factoryDao.GetAll().stream().filter(factory->factory.getName().equals(searchParameter)|| locationDao.GetById(factory.getLocationId()).getAddress().equals(searchParameter)||locationDao.GetById(factory.getLocationId()).getCity().equals(searchParameter)|| factory.getRating()==rating)
		.sorted((t1, t2) -> t1.getStatus().compareTo(t2.getStatus()))
		.collect(Collectors.toList()).forEach(factory-> factoriesDto.add(new FactoryDTO(factory,locationDao.GetById(factory.getLocationId()))));
		if(factoriesDto.size()==0)
		{
			List<ChocolateItem>chocolateItems=GetChocolateItems(searchParameter);
			factoryDao.GetAll().stream().filter(
					factory->chocolateItems.stream().anyMatch(chocolateItem->chocolateItem.getFactoryId()==factory.getId())).sorted((t1, t2) -> t1.getStatus().compareTo(t2.getStatus())).forEach(f->factoriesDto.add(new FactoryDTO(f,locationDao.GetById(f.getLocationId()))));
		}
		return  factoriesDto;
	}
	
	public Collection<PurchaseDTO> SearchPurchases(SearchPurchasesParamsDTO params, User user){
		
		ArrayList<PurchaseDTO> purchases = new ArrayList<PurchaseDTO>();
		for (Purchase purchase : purchaseDAO.GetByBuyerId(user.getId())) {
			for(PurchaseArticle article : purchaseArticleDAO.GetByPurchaseId(purchase.getId())){
				 System.out.println(purchase.getId() + "av");
				 System.out.println(article.getId() + "maju");
                 System.out.println(factoryDao.GetByIdIncludingDeleted(chocolateItemDao.GetByIdIncludingDeleted(chocolatePriceDAO.GetById(article.getChocolatePriceId()).getChocolateItemId()).getFactoryId()).getName());
                
			}
		}
		
		
		for (Purchase purchase : purchaseDAO.GetByBuyerId(user.getId())) {
            boolean matches = true;
            Collection<PurchaseArticle> articles = purchaseArticleDAO.GetByPurchaseId(purchase.getId());
            List<PurchaseArticleDTO> articleDtos = new ArrayList<PurchaseArticleDTO>();
            Factory factory = null;
            

            if (matches && params.getPurchaseRangeFrom() != null && purchase.getPurchaseDate().toLocalDate().isBefore(params.getPurchaseRangeFrom())) {
                matches = false;
            }

            if (matches && params.getPurchaseRangeTo() != null && purchase.getPurchaseDate().toLocalDate().isAfter(params.getPurchaseRangeTo())) {
                matches = false;
            }

            if (matches && params.getPriceFrom() > 0 && purchase.getPrice() < params.getPriceFrom()) {
                matches = false;;
            }

            if (matches && params.getPriceTo() > 0 && purchase.getPrice() > params.getPriceTo()) {
                matches = false;
            }

            if(matches) {
            	for(PurchaseArticle article : articles) {
            		if (!params.getFactoryName().isEmpty() && !params.getFactoryName().equalsIgnoreCase(factoryDao.GetByIdIncludingDeleted(chocolateItemDao.GetByIdIncludingDeleted(chocolatePriceDAO.GetById(article.getChocolatePriceId()).getChocolateItemId()).getFactoryId()).getName()) ) {
                        matches = false;
                        break;
                    }	
            		articleDtos.add(new PurchaseArticleDTO(chocolateDao.GetByIdIncludingDeleted(chocolateItemDao.GetByIdIncludingDeleted(chocolatePriceDAO.GetById(article.getChocolatePriceId()).getChocolateItemId()).getChocolateId()), chocolatePriceDAO.GetById(article.getChocolatePriceId()).getPrice(),article.getQuantity()));
            		factory = factoryDao.GetByIdIncludingDeleted(chocolateItemDao.GetByIdIncludingDeleted(chocolatePriceDAO.GetById(article.getChocolatePriceId()).getChocolateItemId()).getFactoryId());
    				
            	}
            }
            
            if (matches) {
                purchases.add(new PurchaseDTO(purchase, articleDtos,factory.getName(),factory.getId(),commentDao.GetByPurchaseId(purchase.getId())));
            }
            
        }
		return purchases;
	}
	
	 public static double tryParseDouble(String str) {
	        try {
	            return Double.parseDouble(str);
	        } catch (NumberFormatException e) {
	            return -1;
	        }
	    }
		public List<ChocolateItem>GetChocolateItems(String selectedChocolates){
			 List<Chocolate> chocolates = chocolateDao.GetAll().stream()
			            .filter(chocolate -> selectedChocolates.contains(chocolate.getName()))
			            .collect(Collectors.toList());
			 List<ChocolateItem>chocolateItems=chocolateItemDao.GetAll().stream().filter(chocolateItem->chocolates.stream().anyMatch(c->c.getId()==chocolateItem.getChocolateId()&& chocolateItem.IsAvailable())).collect(Collectors.toList());;
			return chocolateItems;
			
		}
		

}
