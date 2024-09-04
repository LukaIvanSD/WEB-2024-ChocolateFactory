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

public class ChocolateItemDAO {

	private String contextPath;
	private HashMap<Integer, ChocolateItem> chocolateItems = new HashMap<>();
	
	public ChocolateItemDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ChocolateItemDAO(String contextPath) {
		this.contextPath=contextPath;
		loadChocolateItems();
	}
	
	private void loadChocolateItems() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/chocolateItems.txt");
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
					int chocolateId = Integer.parseInt(st.nextToken().trim());
					int quantity = Integer.parseInt(st.nextToken().trim());
					int factoryId = Integer.parseInt(st.nextToken().trim());
					String deletionStatus = st.nextToken().trim();
					chocolateItems.put(id, new ChocolateItem(id,chocolateId,quantity,factoryId,deletionStatus));
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
		for (Integer id : chocolateItems.keySet()) {
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
		    File file = new File(contextPath + "/chocolateItems.txt");
		    out = new BufferedWriter(new FileWriter(file));
		    for (ChocolateItem item : chocolateItems.values()) {
		        out.write(item.getId() + ";");
		        out.write(item.getChocolateId() + ";");
		        out.write(item.getQuantity() + ";");
		        out.write(item.getFactoryId() + ";");
		        out.write(item.getDeletionStatus().toString() + "\n");
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
	
	public Collection<ChocolateItem>GetAll(){
		return chocolateItems.values().stream()
                .filter(item -> item.IsActive())
                .collect(Collectors.toList());
	}
	
	public ChocolateItem GetById(int id)
	{
		for(ChocolateItem item : chocolateItems.values())
			if(item.getId()==id)
			{
				if(item.IsActive())
					return item;
				return null;
			}
				
			return null;
	}
	
	public ChocolateItem GetByIdIncludingDeleted(int id) {
		for(ChocolateItem item : chocolateItems.values())
			if(item.getId()==id)
			{
				return item;
			}
				
			return null;
	}
	
	public Collection<ChocolateItem>GetByChoclateId(int chocolateId){
		return chocolateItems.values().stream()
                .filter(item -> item.IsActive() && item.getChocolateId() == chocolateId)
                .collect(Collectors.toList());
	}
	
	public Collection<ChocolateItem>GetByFactoryId(int factoryId){
		return chocolateItems.values().stream()
                .filter(item -> item.IsActive() && item.getFactoryId() == factoryId)
                .collect(Collectors.toList());
	}
	
	public Boolean DeleteById(int id)
	{
		for(ChocolateItem item : chocolateItems.values())
			if(item.getId()==id)
			{
				if(item.Delete())
				{
					SaveToCsv();
					return true;
				}
					return false;
			}
				
			return false;
	}
	
	public Boolean Update(ChocolateItem item)
	{
		   if (chocolateItems.containsKey(item.getId()) && chocolateItems.get(item.getId()).IsActive()) 
		   {
			   chocolateItems.put(item.getId(), item);
		        SaveToCsv();
		        return true;
		    }
		   return false;
	}
	
	
	
	public ChocolateItem Save(ChocolateItem item) {
		item.setId(NextId());
		chocolateItems.put(item.getId(), item);
		SaveToCsv();
		return item;
	}
}
