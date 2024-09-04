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

import beans.ChocolateItem;
import beans.PurchaseArticle;

public class PurchaseArticleDAO {

	private String contextPath;
	private HashMap<Integer, PurchaseArticle> purchaseArticles = new HashMap<>();
	
	public PurchaseArticleDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PurchaseArticleDAO(String contextPath) {
		this.contextPath=contextPath;
		loadPurchaseArticles();
	}
	
	private void loadPurchaseArticles() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/purchaseArticles.txt");
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
					int purchaseId = Integer.parseInt(st.nextToken().trim());
					int chocolatePriceId = Integer.parseInt(st.nextToken().trim());
					int quantity = Integer.parseInt(st.nextToken().trim());
					String deletionStatus = st.nextToken().trim();
					purchaseArticles.put(id, new PurchaseArticle(id,purchaseId,chocolatePriceId,quantity,deletionStatus));
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
		for (Integer id : purchaseArticles.keySet()) {
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
		    File file = new File(contextPath + "/purchaseArticles.txt");
		    out = new BufferedWriter(new FileWriter(file));
		    for (PurchaseArticle purchaseArticle : purchaseArticles.values()) {
		        out.write(purchaseArticle.getId() + ";");
		        out.write(purchaseArticle.getPurchaseId() + ";");
		        out.write(purchaseArticle.getChocolatePriceId() + ";");
		        out.write(purchaseArticle.getQuantity() + ";");
		        out.write(purchaseArticle.getDeletionStatus().toString() + "\n");
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
	
	public Collection<PurchaseArticle>GetAll(){
		return purchaseArticles.values().stream()
                .filter(purchaseArticle -> purchaseArticle.IsActive())
                .collect(Collectors.toList());
	}
	
	public PurchaseArticle GetById(int id)
	{
		for(PurchaseArticle purchaseArticle : purchaseArticles.values())
			if(purchaseArticle.getId()==id)
			{
				if(purchaseArticle.IsActive())
					return purchaseArticle;
				return null;
			}
				
			return null;
	}
	
	public Collection<PurchaseArticle>GetByPurchaseId(int purchaseId){
		return purchaseArticles.values().stream()
                .filter(purchaseArticle -> purchaseArticle.IsActive() && purchaseArticle.getPurchaseId() == purchaseId)
                .collect(Collectors.toList());
	}
	

	public Boolean DeleteById(int id)
	{
		for(PurchaseArticle purchaseArticle : purchaseArticles.values())
			if(purchaseArticle.getId()==id)
			{
				if(purchaseArticle.Delete())
				{
					SaveToCsv();
					return true;
				}
					return false;
			}
				
			return false;
	}
	
	public Boolean Update(PurchaseArticle purchaseArticle)
	{
		   if (purchaseArticles.containsKey(purchaseArticle.getId()) && purchaseArticles.get(purchaseArticle.getId()).IsActive()) 
		   {
			   purchaseArticles.put(purchaseArticle.getId(), purchaseArticle);
		        SaveToCsv();
		        return true;
		    }
		   return false;
	}
	
	public PurchaseArticle Save(PurchaseArticle purchaseArticle) {
		purchaseArticle.setId(NextId());
		purchaseArticles.put(purchaseArticle.getId(), purchaseArticle);
		SaveToCsv();
		return purchaseArticle;
	}
}
