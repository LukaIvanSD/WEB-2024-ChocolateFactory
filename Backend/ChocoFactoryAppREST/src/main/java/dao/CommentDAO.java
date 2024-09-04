package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Chocolate;
import beans.Comment;

public class CommentDAO {
	private String contextPath;
	private HashMap<Integer, Comment> comments = new HashMap<>();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	public CommentDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CommentDAO(String contextPath) {
		this.contextPath=contextPath;
		loadComments();
	}
	
	private void loadComments() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/comments.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					int id = Integer.parseInt(st.nextToken().trim());
					int factoryId=Integer.parseInt(st.nextToken().trim());
					int purchaseId=Integer.parseInt(st.nextToken().trim());
					String text = st.nextToken().trim();
					int rating = Integer.parseInt(st.nextToken().trim());
					LocalDate dateCreated=LocalDate.parse(st.nextToken().trim(), formatter);
					String status = st.nextToken().trim();
					String deletionStatus = st.nextToken().trim();
					comments.put(id, new Comment(id,factoryId,purchaseId,text,rating,dateCreated,status,deletionStatus));
				}
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (Exception e) { }
			}
		}
	}
	
	public int NextId()
	{
		Integer maxId = -1;
		for (Integer id : comments.keySet()) {
			int idNum =id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		return ++maxId;
	}
	
	public void SaveToCsv() {
		BufferedWriter out = null;
		try {
		    File file = new File(contextPath + "/comments.txt");
		    out = new BufferedWriter(new FileWriter(file));
		    for (Comment comment : comments.values()) {
		        out.write(comment.getId() + ";");
		        out.write(comment.getFactoryId() + ";");
		        out.write(comment.getPurchaseId() + ";");
		        out.write(comment.getText() + ";");
		        out.write(comment.getRating() + ";");
		        out.write(comment.getDateCreated().format(formatter)+ ";");
		        out.write(comment.getStatus().toString() + ";");
		        out.write(comment.getDeletionStatus().toString() + "\n");
		    }
		} catch (IOException ex) {
		    ex.printStackTrace();
		} finally {
		    if (out != null) {
		        try {
		            out.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}
	}
	
	public Collection<Comment>GetAll(){
		return comments.values().stream()
                .filter(comment -> comment.IsActive())
                .collect(Collectors.toList());
	}
	
	public Comment GetById(int id)
	{
		for(Comment comment : comments.values())
			if(comment.getId()==id)
			{
				if(comment.IsActive())
					return comment;
				return null;
			}
				
			return null;
	}
	public Comment GetByPurchaseId(int purchaseId)
	{
		for(Comment comment : comments.values())
			if(comment.getPurchaseId()==purchaseId)
			{
				if(comment.IsActive())
					return comment;
				return null;
			}
				
			return null;
	}
	public List<Comment> GetByFactoryId(int factoryId)
	{
		List<Comment>foundComments=new ArrayList<>();
		for(Comment comment : comments.values())
			if(comment.getFactoryId()==factoryId)
			{
				if(comment.IsActive())
					foundComments.add(comment);
			}
				
			return foundComments;
	}
	
	public Boolean DeleteById(int id)
	{
		for(Comment comment : comments.values())
			if(comment.getId()==id)
			{
				if(comment.Delete())
				{
					SaveToCsv();
					return true;
				}
					return false;
			}
				
			return false;
	}
	
	public Boolean Update(Comment comment)
	{
		   if (comments.containsKey(comment.getId()) && comments.get(comment.getId()).IsActive()) 
		   {
			   comments.put(comment.getId(), comment);
		        SaveToCsv();
		        return true;
		    }
		   return false;
	}
	
	public Comment Save(Comment comment) {
		comment.setId(NextId());
		comments.put(comment.getId(), comment);
		SaveToCsv();
		return comment;
	}
}
