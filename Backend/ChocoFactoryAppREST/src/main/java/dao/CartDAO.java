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

import beans.Cart;
import beans.ChocolateItem;

public class CartDAO {

	private String contextPath;
	private HashMap<Integer, Cart> carts = new HashMap<>();
	
	public CartDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CartDAO(String contextPath) {
		this.contextPath=contextPath;
		loadCarts();
	}
	
	private void loadCarts() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/carts.txt");
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
					int customerId = Integer.parseInt(st.nextToken().trim());
					carts.put(id, new Cart(id,customerId));
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
		for (Integer id : carts.keySet()) {
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
		    File file = new File(contextPath + "/carts.txt");
		    out = new BufferedWriter(new FileWriter(file));
		    for (Cart cart : carts.values()) {
		        out.write(cart.getId() + ";");
		        out.write(cart.getCustomerId() + "\n");
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
	
	public Collection<Cart>GetAll(){
		return carts.values();
	}
	
	public Cart GetById(int id)
	{
		for(Cart cart : carts.values())
			if(cart.getId()==id)
			{
					return cart;
			}
				
			return null;
	}
	
	
	public Cart GetByCustomerId(int customerId){
		for(Cart cart : carts.values())
			if(cart.getCustomerId()==customerId)
			{
				return cart;
			}
				
			return null;
	}
	
		
	public Boolean Update(Cart cart)
	{
		   if (carts.containsKey(cart.getId())) 
		   {
			    carts.put(cart.getId(), cart);
		        SaveToCsv();
		        return true;
		    }
		   return false;
	}
	
	
	
	public Cart Save(Cart cart) {
		cart.setId(NextId());
		carts.put(cart.getId(), cart);
		SaveToCsv();
		return cart;
	}
}
