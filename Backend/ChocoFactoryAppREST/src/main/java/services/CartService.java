package services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.Cart;
import beans.CartArticle;
import beans.CartArticle.DeletionStatus;
import beans.Chocolate;
import beans.ChocolateItem;
import beans.Customer;
import beans.CustomerType;
import beans.Purchase;
import beans.Purchase.PurchaseStatus;
import beans.PurchaseArticle;
import beans.User;
import dao.CartArticleDAO;
import dao.CartDAO;
import dao.ChocolateDAO;
import dao.ChocolateItemDAO;
import dao.ChocolatePriceDAO;
import dao.CustomerDAO;
import dao.CustomerTypeDAO;
import dao.FactoryDAO;
import dao.PurchaseArticleDAO;
import dao.PurchaseDAO;
import dto.CartArticleDTO;
import dto.CartDTO;
import dto.CartShipmentDTO;
import dto.ChocolateDTO;
import dto.GhostItemsDTO;

public class CartService {

	private ChocolateDAO chocolateDao;
	private ChocolateItemDAO chocolateItemDao;
	private ChocolatePriceDAO chocolatePriceDao;
	private CartArticleDAO cartArticleDao;
	private CartDAO cartDao;
	private FactoryDAO factoryDao;
	private CustomerDAO customerDao;
	private CustomerTypeDAO customerTypeDao;
	private PurchaseDAO purchaseDao;
	private PurchaseArticleDAO purchaseArticleDao;
	public CartService() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CartService(ChocolateDAO chocolateDao, ChocolateItemDAO chocolateItemDao,
			ChocolatePriceDAO chocolatePriceDao, CartArticleDAO cartArticleDao, CartDAO cartDao,
			FactoryDAO factoryDao, CustomerDAO customerDao) {
		super();
		this.chocolateDao = chocolateDao;
		this.chocolateItemDao = chocolateItemDao;
		this.chocolatePriceDao = chocolatePriceDao;
		this.cartArticleDao = cartArticleDao;
		this.cartDao = cartDao;
		this.factoryDao = factoryDao;
		this.customerDao = customerDao;
	}
	
	
	
	public CartService(ChocolateDAO chocolateDao, ChocolateItemDAO chocolateItemDao,
			ChocolatePriceDAO chocolatePriceDao, CartArticleDAO cartArticleDao, CartDAO cartDao, FactoryDAO factoryDao,
			CustomerDAO customerDao, CustomerTypeDAO customerTypeDao) {
		super();
		this.chocolateDao = chocolateDao;
		this.chocolateItemDao = chocolateItemDao;
		this.chocolatePriceDao = chocolatePriceDao;
		this.cartArticleDao = cartArticleDao;
		this.cartDao = cartDao;
		this.factoryDao = factoryDao;
		this.customerDao = customerDao;
		this.customerTypeDao = customerTypeDao;
	}
	
	
	
	public CartService(ChocolateDAO chocolateDao, ChocolateItemDAO chocolateItemDao,
			ChocolatePriceDAO chocolatePriceDao, CartArticleDAO cartArticleDao, CartDAO cartDao, FactoryDAO factoryDao,
			CustomerDAO customerDao, CustomerTypeDAO customerTypeDao, PurchaseDAO purchaseDao,
			PurchaseArticleDAO purchaseArticleDao) {
		super();
		this.chocolateDao = chocolateDao;
		this.chocolateItemDao = chocolateItemDao;
		this.chocolatePriceDao = chocolatePriceDao;
		this.cartArticleDao = cartArticleDao;
		this.cartDao = cartDao;
		this.factoryDao = factoryDao;
		this.customerDao = customerDao;
		this.customerTypeDao = customerTypeDao;
		this.purchaseDao = purchaseDao;
		this.purchaseArticleDao = purchaseArticleDao;
	}
	public CartService(CartDAO cartDao, CartArticleDAO cartArticleDao, CustomerDAO customerDao) {
		this.cartDao=cartDao;
		this.cartArticleDao=cartArticleDao;
		this.customerDao=customerDao;
		
	}
	public CartDTO getCustomerCart(int customerId) {
		CartDTO cart = new CartDTO();
		ArrayList<CartShipmentDTO> shipments = new ArrayList<CartShipmentDTO>();
		cart.setCart(cartDao.GetByCustomerId(customerId));
		int numberOfArticles = 0;
		
		for(CartArticle article : cartArticleDao.GetByCartId(cart.getCart().getId())) {
			if(chocolateItemDao.GetById(article.getChocolateItemId()).IsAvailable()) {
				boolean isArticleAdded = false;
				for(CartShipmentDTO shipment : shipments) {
					if(shipment.getFactory().getId() == chocolateItemDao.GetById(article.getChocolateItemId()).getFactoryId()) {
						shipment.getCartArticles().add(transformCartArticleToDTO(article));
						shipment.setTotalPrice(shipment.getTotalPrice() + transformCartArticleToDTO(article).getTotalPrice());
						isArticleAdded = true;
						numberOfArticles++;
						break;
					}
				}
				if(!isArticleAdded) {
					CartShipmentDTO shipment = new CartShipmentDTO();
					ArrayList<CartArticleDTO> articles = new ArrayList<CartArticleDTO>();
					articles.add(transformCartArticleToDTO(article));
					shipment.setCartArticles(articles);
					shipment.setFactory(factoryDao.GetById(chocolateItemDao.GetById(article.getChocolateItemId()).getFactoryId()));
					shipment.setTotalPrice(transformCartArticleToDTO(article).getTotalPrice());
					numberOfArticles++;
					shipments.add(shipment);
				}
			}
			else {
				cartArticleDao.DeleteById(article.getId());				
			}
			
		}
		
		cart.setShipments(shipments);
		cart.setNumberOfArticles(numberOfArticles);
		
		return cart;
	}
	
	
	public Response decreaseArticleQuantity(int cartArticleId) {
		
		CartArticle updatedArticle =  cartArticleDao.GetById(cartArticleId);
		updatedArticle.setQuantity((updatedArticle.getQuantity() - 1));
		
		if(chocolateItemDao.GetById(updatedArticle.getChocolateItemId()).getQuantity() <  updatedArticle.getQuantity()) {
			updatedArticle.setQuantity(chocolateItemDao.GetById(updatedArticle.getChocolateItemId()).getQuantity());
			cartArticleDao.Update(updatedArticle);
			return Response.status(Response.Status.OK).entity("Not enough product in stock.").build();
		}
		
		cartArticleDao.Update(updatedArticle);
		
		return Response.status(Response.Status.OK).entity("Quantity changed successfully.").build();
	}
	
	public Response increaseArticleQuantity(int cartArticleId) {
		
		CartArticle updatedArticle =  cartArticleDao.GetById(cartArticleId);
		updatedArticle.setQuantity((updatedArticle.getQuantity() + 1));
		
		if(chocolateItemDao.GetById(updatedArticle.getChocolateItemId()).getQuantity() <  updatedArticle.getQuantity()) {
			updatedArticle.setQuantity(chocolateItemDao.GetById(updatedArticle.getChocolateItemId()).getQuantity());
			cartArticleDao.Update(updatedArticle);
			return Response.status(Response.Status.OK).entity("Not enough product in stock.").build();
		}
		
		cartArticleDao.Update(updatedArticle);
		
		return Response.status(Response.Status.OK).entity("Quantity changed successfully.").build();
	}
	
	public CartArticleDTO transformCartArticleToDTO(CartArticle article) {
		
		CartArticleDTO dto = new CartArticleDTO();
		
		dto.setCartArticle(article);
		dto.setTotalPrice(article.getQuantity()*chocolatePriceDao.GetByChocolateItemId(article.getChocolateItemId()).getPrice());
		dto.setValidationMessage("");
		ChocolateDTO chocolate = new ChocolateDTO();
		chocolate.setChocolate(chocolateDao.GetById(chocolateItemDao.GetById(article.getChocolateItemId()).getChocolateId()));
		chocolate.setChocolateItem(chocolateItemDao.GetById(article.getChocolateItemId()));
		chocolate.setChocolatePrice(chocolatePriceDao.GetByChocolateItemId(article.getChocolateItemId()));
		dto.setChocolate(chocolate);
		
		return dto;
	}
	
	public Response getDiscount(int customerId) {
				
		return Response.status(Response.Status.OK).entity(customerTypeDao.GetById(customerDao.GetById(customerId).getCustomerTypeId()).getDiscount()).build(); 
	}
	
	public Response makePurchase(CartDTO cart, User user) {
		
		Validate(cart);
		
		if(!isValid(cart)) {
			return Response.status(Response.Status.OK).entity(cart).build();
		}
		
		
		
		SavePurchases(cart, user);
		
		
		EmptyCart(cart.getCart());
		
		return Response.status(Response.Status.OK).entity("Purchase is made.").build(); 
	}
	
	public void EmptyCart(Cart cart) {
		for(CartArticle article : cartArticleDao.GetByCartId(cart.getId())) {
			cartArticleDao.DeleteById(article.getId());
		}
	}
	
	public void SavePurchases(CartDTO cartDto, User user) {
		
		double discount =  customerTypeDao.GetById(customerDao.GetByUser(user.getId()).getCustomerTypeId()).getDiscount();
		
		Customer customer = customerDao.GetByUser(user.getId());
		double total = 0.00;
		
		for(CartShipmentDTO shipmentDto : cartDto.getShipments()) {
			
			Purchase purchase = new Purchase(LocalDateTime.now(), shipmentDto.getTotalPrice() * (1.00 - discount), user.getId(), PurchaseStatus.Inprocess, DeletionStatus.Active);
			
			total += purchase.getPrice();
			purchase = purchaseDao.Save(purchase);
			
			
			for(CartArticleDTO articleDTO : shipmentDto.getCartArticles()) {
				
				PurchaseArticle article = new PurchaseArticle(purchase.getId(), articleDTO.getChocolate().getChocolatePrice().getId(), articleDTO.getCartArticle().getQuantity(), DeletionStatus.Active);				
				article = purchaseArticleDao.Save(article);
				
				ChocolateItem item = articleDTO.getChocolate().getChocolateItem();
				item.setQuantity(item.getQuantity() - article.getQuantity());
				chocolateItemDao.Update(item);
			}
		}
		
		GivePointsToCustomer(customer, total);
	}
	
	
	public void GivePointsToCustomer(Customer customer, Double purchasePrice) {
		
		customer.setBonusPoints(customer.getBonusPoints() + (int)((purchasePrice/1000) * 133));
		
		for(CustomerType type : customerTypeDao.GetAll()) {
			if(customer.getBonusPoints() >= type.getPointsForUpgrade()) {
				customer.setCustomerTypeId(type.getId());
			}		
		}
		
		customerDao.Update(customer);
	}
	
	
	public void Validate(CartDTO cart) {
		
		for(CartShipmentDTO shipment : cart.getShipments()) {
			for(CartArticleDTO article : shipment.getCartArticles()) {
				if(article.getCartArticle().getQuantity() > chocolateItemDao.GetById(article.getCartArticle().getChocolateItemId()).getQuantity()) {
					System.out.println("mitrovicc122");					
					article.setValidationMessage("On stock: " + chocolateItemDao.GetById(article.getCartArticle().getChocolateItemId()).getQuantity());
				}
				else {
					article.setValidationMessage("");
				}
			}
		}
		
	}
	public Response getGhostCart(List<GhostItemsDTO> ghostItemsDto) {
		ArrayList<CartArticle>wantedArticles=GetWantedArticles(ghostItemsDto);
		CartDTO cart=getCartDTO(wantedArticles);
		return Response.status(Status.OK).entity(cart).build();
	}
	private ArrayList<CartArticle> GetWantedArticles(List<GhostItemsDTO> ghostItemsDto) {
		ArrayList<CartArticle>cartArticles=new ArrayList<>();
		for (GhostItemsDTO ghostItemDto : ghostItemsDto) {
		    Integer itemId = ghostItemDto.getChocolateItemId();
		    Integer wantedQuantity = ghostItemDto.getQuantity();
		    if(chocolateItemDao.GetById(itemId)!=null);
		    	cartArticles.add(new CartArticle(itemId,wantedQuantity));
		}
		return cartArticles;
	}
	private CartDTO getCartDTO(List<CartArticle>wantedArticles) {
		CartDTO cart = new CartDTO();
		ArrayList<CartShipmentDTO> shipments = new ArrayList<CartShipmentDTO>();
		int numberOfArticles = 0;
		for(CartArticle article : wantedArticles) {
			
			if(chocolateItemDao.GetById(article.getChocolateItemId()).IsAvailable()) {
				boolean isArticleAdded = false;
				for(CartShipmentDTO shipment : shipments) {
					if(shipment.getFactory().getId() == chocolateItemDao.GetById(article.getChocolateItemId()).getFactoryId()) {
						shipment.getCartArticles().add(transformCartArticleToDTO(article));
						shipment.setTotalPrice(shipment.getTotalPrice() + transformCartArticleToDTO(article).getTotalPrice());
						isArticleAdded = true;
						numberOfArticles++;
						break;
					}
				}
				if(!isArticleAdded) {
					CartShipmentDTO shipment = new CartShipmentDTO();
					ArrayList<CartArticleDTO> articles = new ArrayList<CartArticleDTO>();
					articles.add(transformCartArticleToDTO(article));
					shipment.setCartArticles(articles);
					shipment.setFactory(factoryDao.GetById(chocolateItemDao.GetById(article.getChocolateItemId()).getFactoryId()));
					shipment.setTotalPrice(transformCartArticleToDTO(article).getTotalPrice());
					numberOfArticles++;
					shipments.add(shipment);
				}
			}
			else {
				cartArticleDao.DeleteById(article.getId());
			}
			
		}
		
		cart.setShipments(shipments);
		cart.setNumberOfArticles(numberOfArticles);
		return cart;
	}
	public Response SaveGhostItems(List<GhostItemsDTO> ghostItemsDto, User user) {
		// TODO Auto-generated method stub
		if(ghostItemsDto.size()==0)
			return Response.status(Status.OK).entity("No items to save").build();
		Cart customerCart=cartDao.GetByCustomerId(customerDao.GetByUser(user.getId()).getId());
		Collection<CartArticle>customerArticles=cartArticleDao.GetByCartId(customerCart.getId());
		for(GhostItemsDTO ghostItemDto : ghostItemsDto)
		{
			CartArticle cartArticle=new CartArticle(ghostItemDto.getChocolateItemId(),customerCart.getId(),ghostItemDto.getQuantity(),DeletionStatus.Active);
			CartArticle foundArticle=null;
			for(CartArticle customerArticle : customerArticles)
				if(customerArticle.getChocolateItemId()==ghostItemDto.getChocolateItemId())
				{
					foundArticle=customerArticle;
					break;
				}
			if(foundArticle!=null)
			{	
				int quantityBefore=foundArticle.getQuantity();
				foundArticle.setQuantity(quantityBefore+ghostItemDto.getQuantity());
				cartArticleDao.Update(foundArticle);
			}
			else
				cartArticleDao.Save(cartArticle);
		}
		return Response.status(Status.OK).entity("Items successfully saved in cart").build();
	}
	public boolean isValid(CartDTO cart) {
		
		boolean isValid = true;
		
		for(CartShipmentDTO shipment : cart.getShipments()) {
			for(CartArticleDTO article : shipment.getCartArticles()) {
				if(!article.getValidationMessage().isBlank()) {
					isValid = false;
					break;
				}
			}
		}
		
		
		return isValid;
	}
	
}
