package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import beans.User;
import beans.CartArticle.DeletionStatus;
import beans.Comment;
import beans.Comment.Status;
import beans.Factory;
import beans.Manager;
import beans.Purchase;
import beans.PurchaseArticle;
import beans.RejectedPurchase;
import beans.User.UserType;
import dao.CommentDAO;
import dao.FactoryDAO;
import dao.ManagerDAO;
import dao.PurchaseArticleDAO;
import dao.PurchaseDAO;
import dao.RejectedPurchaseDAO;
import dao.UserDAO;
import dao.WorkerDAO;
import dto.CommentDTO;
import dto.CommentRequestDTO;

public class CommentService {
	private CommentDAO commentDao;
	private UserDAO userDao;
	private ManagerDAO managerDao;
	private WorkerDAO workerDao;
	private PurchaseDAO purchaseDao;
	private PurchaseArticleDAO purchaseArticleDao;
	private FactoryDAO factoryDao;
	private RejectedPurchaseDAO rejectedPurchaseDao;
	public CommentService(CommentDAO commentDao,UserDAO userDao,ManagerDAO managerDao,WorkerDAO workerDao,PurchaseDAO purchaseDao,PurchaseArticleDAO purchaseArticleDao) {
		this.userDao=userDao;
		this.commentDao=commentDao;
		this.managerDao=managerDao;
		this.workerDao=workerDao;
		this.purchaseDao=purchaseDao;
		this.purchaseArticleDao=purchaseArticleDao;
	}
	public CommentService(CommentDAO commentDao, ManagerDAO managerDao,FactoryDAO factoryDao) {
		// TODO Auto-generated constructor stub
		this.commentDao=commentDao;
		this.managerDao=managerDao;
		this.factoryDao=factoryDao;
	}
	public CommentService(RejectedPurchaseDAO rejectedPurchaseDao) {
		// TODO Auto-generated constructor stub
		this.rejectedPurchaseDao=rejectedPurchaseDao;
	}
	public Collection<CommentDTO>getComments(int factoryId,User user){
		List<CommentDTO>commentsDto=new ArrayList<>();
		List<Comment>comments=new ArrayList<>();
		for(Comment comment : commentDao.GetAll())
		{	
			if(comment.getFactoryId()==factoryId)
				comments.add(comment);
		}
		if(user==null || user.getUserType()==UserType.Customer || (user.getUserType()==UserType.Manager && managerDao.GetByUser(user.getId()).getFactoryId()!=factoryId )|| (user.getUserType()==UserType.Worker))
		{
			for(Comment comment : comments)
			{	
				if(comment.getStatus()==Status.Approved)
				{
					int creatorId=GetCreatorId(comment);
					commentsDto.add(new CommentDTO(comment,userDao.GetById(creatorId)));
				}
			}
		}
		else
			for(Comment comment : comments)
			{
				int creatorId=GetCreatorId(comment);
				commentsDto.add(new CommentDTO(comment,userDao.GetById(creatorId)));
			}
		return commentsDto;
	}
	private int GetCreatorId(Comment comment) {
		// TODO Auto-generated method stub
		Purchase purchase =purchaseDao.GetById(comment.getPurchaseId());
		return purchaseDao.GetById(comment.getPurchaseId()).getBuyerId();
	}
	
	public Response DeclineComment(int commentId, User user) {
		if(IsInvalidRequest(commentId,user))
			return Response.status(Response.Status.BAD_REQUEST).build();
		Comment comment=commentDao.GetById(commentId);
		if(comment==null || comment.getStatus()!=Status.InProcess)
			return Response.status(Response.Status.BAD_REQUEST).build();
		comment.setStatus(Status.Declined);
		commentDao.Update(comment);
		return Response.status(Response.Status.OK).entity("Comment successfully declined").build();
	}
	
	public Response ApproveComment(int commentId, User user) {
		if(IsInvalidRequest(commentId,user))
			return Response.status(Response.Status.BAD_REQUEST).build();
		Comment comment=commentDao.GetById(commentId);
		if(comment==null || comment.getStatus()!=Status.InProcess)
			return Response.status(Response.Status.BAD_REQUEST).build();
		comment.setStatus(Status.Approved);
		commentDao.Update(comment);
		UpdateFactoryRating(comment.getFactoryId());
		return Response.status(Response.Status.OK).entity("Comment successfully approved").build();
	}
	private void UpdateFactoryRating(int factoryId) {
		// TODO Auto-generated method stub
		List<Comment>factoryComments=commentDao.GetByFactoryId(factoryId).stream().filter(comment->comment.IsApproved()).collect(Collectors.toList());
		Factory factory=factoryDao.GetById(factoryId);
		int numberOfComments=factoryComments.size();
		int ratingSum=CalculateRating(factoryComments);
		double avgRating=ratingSum/(double)numberOfComments;
		factory.setRating(avgRating);
		factoryDao.Update(factory);
		
	}
	private int CalculateRating(List<Comment> factoryComments) {
		// TODO Auto-generated method stub
		int ratingSum=0;
		for(Comment comment : factoryComments)
			ratingSum=ratingSum+comment.getRating();
		return ratingSum;
	}
	public Boolean IsInvalidRequest(int commentId,User user) {
		Manager manager=managerDao.GetByUser(user.getId());
		Comment comment=commentDao.GetById(commentId);
		return manager==null || comment==null|| comment.getFactoryId()!=manager.getFactoryId();
	}
	public Response CreateComment(CommentRequestDTO commentRequestDto,User user) {
		// TODO Auto-generated method stub
		if(IsRequestInValid(commentRequestDto))
			return Response.status(Response.Status.BAD_REQUEST).entity("Form not valid").build();
		if(!IsCommentingOnPurchaseValid(user,commentRequestDto.getPurchaseId()))
			return Response.status(Response.Status.BAD_REQUEST).entity("Bad request").build();
		Comment comment=new Comment(commentRequestDto.getFactoryId(),commentRequestDto.getPurchaseId(),commentRequestDto.getComment(),commentRequestDto.getRating(),LocalDate.now(),Status.InProcess,DeletionStatus.Active);
		return Response.status(Response.Status.OK).entity(commentDao.Save(comment)).build();
	}
	private boolean IsCommentingOnPurchaseValid(User user, Integer purchaseId) {
		// TODO Auto-generated method stub
		Purchase purchase=purchaseDao.GetById((int)purchaseId);
		return purchase.getBuyerId()==user.getId() && purchase.IsApproved() && commentDao.GetByPurchaseId((int)purchaseId)==null;
	}
	private boolean IsRequestInValid(CommentRequestDTO commentRequestDto) {
		// TODO Auto-generated method stub
		return commentRequestDto.getComment().isBlank() || commentRequestDto.getFactoryId()==null || commentRequestDto.getPurchaseId()==null || commentRequestDto.getRating()==null;
	}
	public Response GetRejectionReason(Integer purchaseId, User user) {
		// TODO Auto-generated method stub
		if(purchaseId==null)
			return Response.status(Response.Status.BAD_REQUEST).entity("purchaseId not sent").build();
		RejectedPurchase rejectedPurchase=rejectedPurchaseDao.GetByPurchaseId((int)purchaseId);
		if(rejectedPurchase==null)
			return Response.status(Response.Status.BAD_REQUEST).entity("Purchase rejection not found").build();
		return Response.status(Response.Status.OK).entity(rejectedPurchase.getRejectionReason()).build();

	}


}
