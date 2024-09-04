package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.CartArticle;

public class CartArticleDAO {

	private String contextPath;
	private HashMap<Integer, CartArticle> cartArticles = new HashMap<>();
	
	
	public CartArticleDAO() {
		
	}
	
	public CartArticleDAO(String contextPath) {
		this.contextPath=contextPath;
		loadCartArticles();
	}
	private void loadCartArticles() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/cartArticles.txt");
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
					int chocolateItemId = Integer.parseInt(st.nextToken().trim());
					int cartId = Integer.parseInt(st.nextToken().trim());
					int quantity=Integer.parseInt(st.nextToken().trim());
					String deletionStatus = st.nextToken().trim();
					cartArticles.put(id, new CartArticle(id, chocolateItemId, cartId,quantity ,deletionStatus));
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
		for (Integer id : cartArticles.keySet()) {
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
		    File file = new File(contextPath + "/cartArticles.txt");
		    out = new BufferedWriter(new FileWriter(file));
		    for (CartArticle cartArticle : cartArticles.values()) {
		        out.write(cartArticle.getId() + ";");
		        out.write(cartArticle.getChocolateItemId() + ";");
		        out.write(cartArticle.getCartId() + ";");
		        out.write(cartArticle.getQuantity()+";");
		        out.write(cartArticle.getDeletionStatus() + "\n");
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

	public Collection<CartArticle>GetAll(){
		return cartArticles.values().stream().filter(cartArticle->cartArticle.IsActive()).collect(Collectors.toList());
	}
	public CartArticle GetById(int id)
	{
		for(CartArticle cartArticle : cartArticles.values())
			if(cartArticle.getId()==id)
			{
				if(cartArticle.IsActive())
					return cartArticle;
				return null;
			}
			return null;
	}
	
	public Collection<CartArticle> GetByCartId(int cartId)
	{
			return cartArticles.values().stream().filter(cartArticle->cartArticle.IsActive() && cartArticle.getCartId() == cartId).collect(Collectors.toList());
	}
	
	
	public Boolean DeleteById(int id)
	{
		for(CartArticle cartArticle : cartArticles.values())
			if(cartArticle.getId()==id)
			{
				if(cartArticle.Delete())
				{
					SaveToCsv();
					return true;
				}
				return false;
			}
				
			return false;
	}
	
	public void ClearCartById(int cartId)
	{
		for(CartArticle cartArticle : cartArticles.values())
			if(cartArticle.getCartId() == cartId)
			{
				if(cartArticle.Delete())
				{
					SaveToCsv();
				}
			}
	}
	
	
	public Boolean Update(CartArticle cartArticle)
	{
		   if (cartArticles.containsKey(cartArticle.getId()) && cartArticles.get(cartArticle.getId()).IsActive()) {
			   cartArticles.put(cartArticle.getId(), cartArticle);
		        SaveToCsv();
		        return true;
		    }
		   return false;
	}
	public CartArticle Save(CartArticle cartArticle) {
		cartArticle.setId(NextId());
		cartArticles.put(cartArticle.getId(), cartArticle);
		SaveToCsv();
		return cartArticle;
		
	}
}
