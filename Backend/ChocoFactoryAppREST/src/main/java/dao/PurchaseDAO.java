package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Purchase;

public class PurchaseDAO {

	private String contextPath;
	private HashMap<Integer, Purchase> purchases = new HashMap<>();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	public PurchaseDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PurchaseDAO(String contextPath) {
		this.contextPath=contextPath;
		loadPurchases();
	}
	
	private void loadPurchases() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/purchases.txt");
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
					LocalDateTime date = LocalDateTime.parse(st.nextToken().trim(), formatter);
					double price = Double.parseDouble(st.nextToken().trim());
					int buyerId = Integer.parseInt(st.nextToken().trim());
					String purchaseStatus = st.nextToken().trim();
					String deletionStatus = st.nextToken().trim();
					purchases.put(id, new Purchase(id,date,price,buyerId,purchaseStatus,deletionStatus));
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
		for (Integer id : purchases.keySet()) {
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
		    File file = new File(contextPath + "/purchases.txt");
		    out = new BufferedWriter(new FileWriter(file));
		    for (Purchase purchase : purchases.values()) {
		        out.write(purchase.getId() + ";");
		        out.write(purchase.getPurchaseDate().format(formatter) + ";");
		        out.write(purchase.getPrice() + ";");
		        out.write(purchase.getBuyerId() + ";");
		        out.write(purchase.getPurchaseStatus() + ";");
		        out.write(purchase.getDeletionStatus().toString() + "\n");
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
	
	public Collection<Purchase>GetAll(){
		return purchases.values().stream()
                .filter(purchase -> purchase.IsActive())
                .collect(Collectors.toList());
	}
	
	public Purchase GetById(int id)
	{
		for(Purchase purchase : purchases.values())
			if(purchase.getId()==id)
			{
				if(purchase.IsActive())
					return purchase;
				return null;
			}
				
			return null;
	}
	
	public Boolean DeleteById(int id)
	{
		for(Purchase purchase : purchases.values())
			if(purchase.getId()==id)
			{
				if(purchase.Delete())
				{
					SaveToCsv();
					return true;
				}
					return false;
			}
				
			return false;
	}
	
	public Purchase Update(Purchase purchase)
	{
		   if (purchases.containsKey(purchase.getId()) && purchases.get(purchase.getId()).IsActive()) 
		   {
			   purchases.put(purchase.getId(), purchase);
		        SaveToCsv();
		        return purchase;
		    }
		   return null;
	}
	
	public Purchase Save(Purchase purchase) {
		purchase.setId(NextId());
		purchases.put(purchase.getId(), purchase);
		SaveToCsv();
		return purchase;
	}
	
	public Collection<Purchase>GetByBuyerId(int buyerId){
		return purchases.values().stream()
                .filter(purchase -> purchase.IsActive() && purchase.getBuyerId() == buyerId)
                .collect(Collectors.toList());
	}

	
	
}
