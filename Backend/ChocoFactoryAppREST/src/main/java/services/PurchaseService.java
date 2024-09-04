package services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.CancelledPurchase;
import beans.ChocolateItem;
import beans.Customer;
import beans.CustomerType;
import beans.Factory;
import beans.Manager;
import beans.Purchase;
import beans.Purchase.PurchaseStatus;
import beans.PurchaseArticle;
import beans.RejectedPurchase;
import beans.User;
import dao.CancelledPurchaseDAO;
import dao.ChocolateDAO;
import dao.ChocolateItemDAO;
import dao.ChocolatePriceDAO;
import dao.CommentDAO;
import dao.CustomerDAO;
import dao.CustomerTypeDAO;
import dao.FactoryDAO;
import dao.ManagerDAO;
import dao.PurchaseArticleDAO;
import dao.PurchaseDAO;
import dao.RejectedPurchaseDAO;
import dao.UserDAO;
import dto.PurchaseArticleDTO;
import dto.PurchaseDTO;
import dto.RejectPurchaseDTO;

public class PurchaseService {
	
	private PurchaseArticleDAO purchaseArticleDAO;
	private PurchaseDAO purchaseDAO;
	private ChocolateDAO chocolateDAO;
	private ChocolateItemDAO chocolateItemDAO;
	private ChocolatePriceDAO chocolatePriceDAO;
	private FactoryDAO factoryDAO;
	private ManagerDAO managerDAO;
	private UserDAO userDAO;
	private CommentDAO commentDao;
	private CustomerDAO customerDao;
	private CustomerTypeDAO customerTypeDao;
	private CancelledPurchaseDAO cancelledPurchaseDAO;
	private RejectedPurchaseDAO rejectedPurchaseDao;
	
	
	
	public PurchaseService(PurchaseArticleDAO purchaseArticleDAO, PurchaseDAO purchaseDAO, ChocolateDAO chocolateDAO,
			ChocolateItemDAO chocolateItemDAO, ChocolatePriceDAO chocolatePriceDAO, FactoryDAO factoryDAO,
			ManagerDAO managerDAO, UserDAO userDAO, CommentDAO commentDao, CustomerDAO customerDao,
			CustomerTypeDAO customerTypeDao, CancelledPurchaseDAO cancelledPurchaseDAO) {
		super();
		this.purchaseArticleDAO = purchaseArticleDAO;
		this.purchaseDAO = purchaseDAO;
		this.chocolateDAO = chocolateDAO;
		this.chocolateItemDAO = chocolateItemDAO;
		this.chocolatePriceDAO = chocolatePriceDAO;
		this.factoryDAO = factoryDAO;
		this.managerDAO = managerDAO;
		this.userDAO = userDAO;
		this.commentDao = commentDao;
		this.customerDao = customerDao;
		this.customerTypeDao = customerTypeDao;
		this.cancelledPurchaseDAO = cancelledPurchaseDAO;
	}


	public PurchaseService(PurchaseArticleDAO purchaseArticleDAO, PurchaseDAO purchaseDAO, ChocolateDAO chocolateDAO,
			ChocolateItemDAO chocolateItemDAO, ChocolatePriceDAO chocolatePriceDAO, FactoryDAO factoryDAO,
			ManagerDAO managerDAO, UserDAO userDAO,CommentDAO commentDao) {
		super();
		this.purchaseArticleDAO = purchaseArticleDAO;
		this.purchaseDAO = purchaseDAO;
		this.chocolateDAO = chocolateDAO;
		this.chocolateItemDAO = chocolateItemDAO;
		this.chocolatePriceDAO = chocolatePriceDAO;
		this.factoryDAO = factoryDAO;
		this.managerDAO = managerDAO;
		this.userDAO = userDAO;
		this.commentDao=commentDao;
	}


	public PurchaseService(PurchaseArticleDAO purchaseArticleDAO,PurchaseDAO purchaseDAO, ChocolateDAO chocolateDAO, ChocolateItemDAO chocolateItemDAO, ChocolatePriceDAO chocolatePriceDAO, FactoryDAO factoryDAO,  ManagerDAO managerDAO) {
		// TODO Auto-generated constructor stub
		this.purchaseArticleDAO=purchaseArticleDAO;
		this.purchaseDAO=purchaseDAO;
		this.chocolateDAO = chocolateDAO;
		this.chocolateItemDAO = chocolateItemDAO;
		this.chocolatePriceDAO = chocolatePriceDAO;
		this.factoryDAO = factoryDAO;
		this.managerDAO = managerDAO;
	}
	
	
	public PurchaseService(ManagerDAO managerDao, PurchaseDAO purchaseDao, PurchaseArticleDAO purchaseArticleDao,
			ChocolateItemDAO chocolateItemDao, ChocolatePriceDAO chocolatePriceDao) {
		// TODO Auto-generated constructor stub
		this.managerDAO=managerDao;
		this.purchaseArticleDAO=purchaseArticleDao;
		this.chocolateItemDAO=chocolateItemDao;
		this.chocolatePriceDAO=chocolatePriceDao;
		this.purchaseDAO=purchaseDao;
	}


	public PurchaseService(ManagerDAO managerDao, PurchaseDAO purchaseDao, PurchaseArticleDAO purchaseArticleDao,
			ChocolateItemDAO chocolateItemDao, ChocolatePriceDAO chocolatePriceDao,RejectedPurchaseDAO rejectedPurchaseDao) {
		// TODO Auto-generated constructor stub
		this.managerDAO=managerDao;
		this.purchaseArticleDAO=purchaseArticleDao;
		this.chocolateItemDAO=chocolateItemDao;
		this.chocolatePriceDAO=chocolatePriceDao;
		this.purchaseDAO=purchaseDao;
		this.rejectedPurchaseDao=rejectedPurchaseDao;
	}


	public Response getPurchasesForCustomer(User user){
		
		List<PurchaseDTO> purchases = new ArrayList<PurchaseDTO>();
		
		for(Purchase purchase : purchaseDAO.GetByBuyerId(user.getId())){
			Factory factory = null;
			List<PurchaseArticleDTO> articles = new ArrayList<PurchaseArticleDTO>();
			for(PurchaseArticle article : purchaseArticleDAO.GetByPurchaseId(purchase.getId())) {
				System.out.println(article.getId());
				System.out.println(article.getChocolatePriceId());
				articles.add(new PurchaseArticleDTO(chocolateDAO.GetByIdIncludingDeleted(chocolateItemDAO.GetByIdIncludingDeleted(chocolatePriceDAO.GetById(article.getChocolatePriceId()).getChocolateItemId()).getChocolateId()), chocolatePriceDAO.GetById(article.getChocolatePriceId()).getPrice(),article.getQuantity()));
				factory = factoryDAO.GetByIdIncludingDeleted(chocolateItemDAO.GetByIdIncludingDeleted(chocolatePriceDAO.GetById(article.getChocolatePriceId()).getChocolateItemId()).getFactoryId());
					
			}
			purchases.add(new PurchaseDTO(purchase, articles,factory.getName(),factory.getId(),commentDao.GetByPurchaseId(purchase.getId())));
		}
		
		return Response.status(Response.Status.OK).entity(purchases).build();
	}
	
	public Response getPurchasesForManager(User user) {
		List<PurchaseDTO> purchases = new ArrayList<PurchaseDTO>();
		User buyer = new User();
		
		for(Purchase purchase : purchaseDAO.GetAll()) {
			Factory factory = null;
			List<PurchaseArticleDTO> articles = new ArrayList<PurchaseArticleDTO>();
			for(PurchaseArticle article : purchaseArticleDAO.GetByPurchaseId(purchase.getId())) {
				if(chocolateItemDAO.GetByIdIncludingDeleted(chocolatePriceDAO.GetById(article.getChocolatePriceId()).getChocolateItemId()).getFactoryId() == managerDAO.GetByUser(user.getId()).getFactoryId()) {
					
					factory = factoryDAO.GetByIdIncludingDeleted(chocolateItemDAO.GetByIdIncludingDeleted(chocolatePriceDAO.GetById(article.getChocolatePriceId()).getChocolateItemId()).getFactoryId());
					articles.add(new PurchaseArticleDTO(chocolateDAO.GetByIdIncludingDeleted(chocolateItemDAO.GetByIdIncludingDeleted(chocolatePriceDAO.GetById(article.getChocolatePriceId()).getChocolateItemId()).getChocolateId()), chocolatePriceDAO.GetById(article.getChocolatePriceId()).getPrice(),article.getQuantity()));					
				}
				else
					break;
			}
			if(articles.size() > 0) {
				buyer = userDAO.GetById(purchase.getBuyerId());
				purchases.add(new PurchaseDTO(purchase, articles,factory.getName(),factory.getId(), buyer,commentDao.GetByPurchaseId(purchase.getId())));				
			}
		}
		
		
		return Response.status(Response.Status.OK).entity(purchases).build();
	}


	public Response ApprovePurchase(User user, Integer purchaseId) {
		Manager manager=managerDAO.GetByUser(user.getId());
		if(!IsPurchaseForManagerFactory(purchaseId,manager.getFactoryId()))
			return Response.status(Status.BAD_REQUEST).entity("Manager does not have sent puchase").build();
		Purchase purchase=purchaseDAO.GetById(purchaseId);
		if(!purchase.Approve())
			return Response.status(Status.BAD_REQUEST).entity("Purchase state is not in process").build();
		purchaseDAO.Update(purchase);
		return Response.status(Status.OK).entity("Ok").build();
	}


	public Response RejectPurchase(User user, RejectPurchaseDTO purchaseDto) {
		// TODO Auto-generated method stub
		if(IsRequestInvalid(purchaseDto))
			return Response.status(Status.BAD_REQUEST).entity("Request not valid").build();
		Manager manager=managerDAO.GetByUser(user.getId());
		if(!IsPurchaseForManagerFactory(purchaseDto.getPurchaseId(),manager.getFactoryId()))
			return Response.status(Status.BAD_REQUEST).entity("Manager does not have sent puchase").build();
		Purchase purchase=purchaseDAO.GetById(purchaseDto.getPurchaseId());
		if(!purchase.Reject())
			return Response.status(Status.BAD_REQUEST).entity("Purchase state is not in process").build();
		purchase=purchaseDAO.Update(purchase);
		RejectedPurchase rejectedPurchase=new RejectedPurchase(purchase.getId(),purchaseDto.getReason());
		rejectedPurchaseDao.Save(rejectedPurchase);
		return Response.status(Status.OK).entity("Ok").build();
	}


	private boolean IsRequestInvalid(RejectPurchaseDTO purchaseDto) {
		// TODO Auto-generated method stub
		
		return purchaseDto.getPurchaseId()==null || purchaseDto.getReason().isBlank();
	}


	private boolean IsPurchaseForManagerFactory(Integer purchaseId, int factoryId) {
		// TODO Auto-generated method stub
		Collection<PurchaseArticle>articles=purchaseArticleDAO.GetByPurchaseId(purchaseId);
		PurchaseArticle firstArticle = articles.iterator().next();
		int articleFactoryId=chocolateItemDAO.GetById(chocolatePriceDAO.GetById(firstArticle.getChocolatePriceId()).getChocolateItemId()).getFactoryId();
		return articleFactoryId==factoryId;
	}
	
	public Response cancelPurchase(int purchaseId, User user) {
		
		
		
		Purchase purchase = purchaseDAO.GetById(purchaseId);
		if(user.getId() !=  purchase.getBuyerId())
			return Response.status(Response.Status.FORBIDDEN).entity("This is not your purchase!").build();
		
		
		Customer customer = customerDao.GetByUser(user.getId());
		
		for(PurchaseArticle article : purchaseArticleDAO.GetByPurchaseId(purchaseId)) {
			ChocolateItem item = chocolateItemDAO.GetByIdIncludingDeleted(chocolatePriceDAO.GetById(article.getChocolatePriceId()).getChocolateItemId());
			if(item != null && item.IsActive()) {
				item.setQuantity(item.getQuantity() + article.getQuantity());
				chocolateItemDAO.Update(item);
			}
		}
		
		customer.setBonusPoints(customer.getBonusPoints() -  (int)((purchase.getPrice()/1000) * 133 * 4));
		
		if(customer.getBonusPoints() < 0)
			customer.setBonusPoints(0);
		
		for(CustomerType type : customerTypeDao.GetAll()) {
			if(customer.getBonusPoints() >= type.getPointsForUpgrade()) {
				customer.setCustomerTypeId(type.getId());
			}		
		}
		
		customerDao.Update(customer);
		
		cancelledPurchaseDAO.Save(new CancelledPurchase(purchase.getId(), LocalDate.now()));
		
		purchase.setPurchaseStatus(PurchaseStatus.Cancelled);
		purchaseDAO.Update(purchase);
		
		return Response.status(Response.Status.OK).entity("Purchase is cancelled.").build();
	}
	
	
}
