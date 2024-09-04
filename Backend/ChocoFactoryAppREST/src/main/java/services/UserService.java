package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import beans.CancelledPurchase;
import beans.Purchase;
import beans.User;
import dao.CancelledPurchaseDAO;
import dao.PurchaseDAO;
import dao.UserDAO;

public class UserService {
	private UserDAO userDao;
	private CancelledPurchaseDAO cancelledPurchaseDao;
	private PurchaseDAO purchaseDao;
	
	public UserService(UserDAO userDao, CancelledPurchaseDAO cancelledPurchaseDao,PurchaseDAO purchaseDao) {
		super();
		this.userDao = userDao;
		this.cancelledPurchaseDao = cancelledPurchaseDao;
		this.purchaseDao=purchaseDao;
	}
	public List<User> GetSuspiciousUsers() {
		List<User> suspiciousUsers=new ArrayList<>();
		List<CancelledPurchase>cancelledPurchases=GetCanncelledPurchasesInLastMonth();
		HashMap<User,Integer>userCancellationCounts=new HashMap<>();
		for(CancelledPurchase cancelledPurchase : cancelledPurchases)
		{
			Purchase purchase=purchaseDao.GetById(cancelledPurchase.getPurchaseId());
			User buyer=userDao.GetById(purchase.getBuyerId());
			if(!userCancellationCounts.containsKey(buyer))
				userCancellationCounts.put(buyer, 1);
			else
			{	
				int updateCounter = userCancellationCounts.get(buyer)+1;
				userCancellationCounts.put(buyer,updateCounter);
			}
		}
		for(Map.Entry<User, Integer> entry : userCancellationCounts.entrySet())
		{
			if(entry.getValue()>=5)
				suspiciousUsers.add(entry.getKey());
		}
		return suspiciousUsers;
	}
	private List<CancelledPurchase> GetCanncelledPurchasesInLastMonth() {
		return cancelledPurchaseDao.GetAll().stream().filter(cancelledPurchase->cancelledPurchase.IsCancelledInLastMonth()).collect(Collectors.toList());
	}
	

	
}
