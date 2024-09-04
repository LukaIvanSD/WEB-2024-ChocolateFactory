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

import beans.ChocolatePrice;
import beans.ChocolatePrice.Validity;

public class ChocolatePriceDAO {

	private String contextPath;
	private HashMap<Integer, ChocolatePrice> chocolatePrices = new HashMap<>();
	
	public ChocolatePriceDAO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ChocolatePriceDAO(String contextPath) {
		this.contextPath=contextPath;
		loadChocolatePrices();
	}
	

	private void loadChocolatePrices() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/chocolatePrices.txt");
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
					double price = Double.parseDouble(st.nextToken().trim());
					String validityStatus = st.nextToken().trim();					
					String deletionStatus = st.nextToken().trim();
					chocolatePrices.put(id, new ChocolatePrice(id,chocolateItemId,price,validityStatus,deletionStatus));
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
		for (Integer id : chocolatePrices.keySet()) {
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
		    File file = new File(contextPath + "/chocolatePrices.txt");
		    out = new BufferedWriter(new FileWriter(file));
		    for (ChocolatePrice price : chocolatePrices.values()) {
		        out.write(price.getId() + ";");
		        out.write(price.getChocolateItemId() + ";");
		        out.write(price.getPrice() + ";");
		        out.write(price.getValidityStatus() + ";");
		        out.write(price.getDeletionStatus().toString() + "\n");
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
	
	public Collection<ChocolatePrice>GetAll(){
		return chocolatePrices.values().stream()
                .filter(item -> item.IsActive())
                .collect(Collectors.toList());
	}
	
	public ChocolatePrice GetById(int id)
	{
		for(ChocolatePrice price : chocolatePrices.values())
			if(price.getId()==id)
			{
				if(price.IsActive())
					return price;
				return null;
			}
				
			return null;
	}
	
	
	public Boolean Update(ChocolatePrice price)
	{	
		InvalidatePrice(price.getChocolateItemId());
		Save(price);
		return true;
	}
		
	public ChocolatePrice Save(ChocolatePrice price) {
		price.setId(NextId());
		price.Validate();
		chocolatePrices.put(price.getId(), price);
		SaveToCsv();
		return price;
		
	}
	public Boolean InvalidatePrice(int itemId) {
		for(ChocolatePrice price : chocolatePrices.values()) {
			if(price.getChocolateItemId() == itemId && price.IsValid())
			{
				price.Invalidate();
				SaveToCsv();
				return true;
			}
		}
		return false;
	}

	public ChocolatePrice GetByChocolateItemId(int chocolateItemId){
		for(ChocolatePrice price : chocolatePrices.values()) {
			if(price.getChocolateItemId() == chocolateItemId && price.IsValid())
				return price;
		}
		return null;
	}

	public ChocolatePrice getValid(int chocolateItemId) {
		// TODO Auto-generated method stub
		for(ChocolatePrice chocolatePrice : chocolatePrices.values())
		{
			if(chocolatePrice.getChocolateItemId()==chocolateItemId && chocolatePrice.IsValid())
				return chocolatePrice;
		}
		return null;
	}

	
}
