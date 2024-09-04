package services;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.CartArticle;
import beans.User;
import beans.CartArticle.DeletionStatus;
import beans.ChocolateItem;
import dao.CartArticleDAO;
import dao.CartDAO;
import dao.ChocolateItemDAO;
import dao.CustomerDAO;
import dto.CartArticleDTO;
import dto.CartArticleRequestDTO;

public class CartArticleService {
	private CartArticleDAO cartArticleDao;
	private CartDAO cartDao;
	private CustomerDAO customerDao;
	private ChocolateItemDAO chocolateItemDao;

	public CartArticleService(CartArticleDAO cartArticleDao,CartDAO cartDao,CustomerDAO customerDao,ChocolateItemDAO chocolateItemDao) {
		super();
		this.cartArticleDao = cartArticleDao;
		this.cartDao=cartDao;
		this.customerDao=customerDao;
		this.chocolateItemDao=chocolateItemDao;
	}

	public Response SaveArticle(CartArticleRequestDTO cartArticleRequestDto, User user) {
		// TODO Auto-generated method stub
		if(!IsRequestValid(cartArticleRequestDto))
			return Response.status(Status.BAD_REQUEST).build();
		int customerId=customerDao.GetByUser(user.getId()).getId();	
		int cartId=cartDao.GetByCustomerId(customerId).getId();
		ChocolateItem chocolateItem=chocolateItemDao.GetById(cartArticleRequestDto.getChocolateItemId());
		if(chocolateItem==null || !chocolateItem.IsAvailable())
			return Response.status(Status.BAD_REQUEST).entity("Item not available").build();
		Response response=IsArticleAlreadyInCart(cartId,cartArticleRequestDto,chocolateItem.getQuantity());
		if(response!=null)
			return response;
		CartArticle cartArticle=new CartArticle(chocolateItem.getId(),cartId,(int)cartArticleRequestDto.getQuantity(),DeletionStatus.Active);
		cartArticle=cartArticleDao.Save(cartArticle);
		return Response.status(Status.OK).entity(cartArticle).build();
	}

	private Response IsArticleAlreadyInCart(int cartId,CartArticleRequestDTO cartArticleRequestDto,int availableQuantity) {
		// TODO Auto-generated method stub
		Collection<CartArticle> userArticles=cartArticleDao.GetByCartId(cartId);
		for(CartArticle cartArticle:userArticles)
			if(cartArticle.getChocolateItemId()==(int)cartArticleRequestDto.getChocolateItemId())
			{
				int currentQuantity=cartArticle.getQuantity();
				int additionalQuantity=cartArticleRequestDto.getQuantity();
				if(currentQuantity+additionalQuantity>availableQuantity)
					return Response.status(Status.CONFLICT).entity("Wanted quantity exceeded available quantity").build();
				cartArticle.setQuantity(currentQuantity+additionalQuantity);
				cartArticleDao.Update(cartArticle);
				return Response.status(Status.OK).entity("ArticleQuantity updated").build();
			}
		return null;
	}

	private boolean IsRequestValid(CartArticleRequestDTO cartArticleRequestDto) {
		return cartArticleRequestDto.getChocolateItemId()!=null && cartArticleRequestDto.getQuantity()>0;
	}
	
}
