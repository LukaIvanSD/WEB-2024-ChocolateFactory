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

import beans.Chocolate;


public class ChocolateDAO {

	private String contextPath;
	private HashMap<Integer, Chocolate> chocolates = new HashMap<>();
	
	public ChocolateDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ChocolateDAO(String contextPath) {
		this.contextPath=contextPath;
		loadChocolates();
	}
	
	private void loadChocolates() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/chocolates.txt");
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
					String name = st.nextToken().trim();
					String category = st.nextToken().trim();
					String type = st.nextToken().trim();
					double weight = Double.parseDouble(st.nextToken().trim());
					String description = st.nextToken().trim();
					int factoryCreatorId = Integer.parseInt(st.nextToken().trim());
					String imagePath = st.nextToken().trim();
					String deletionStatus = st.nextToken().trim();
					chocolates.put(id, new Chocolate(id,name,category,type,weight,description,imagePath,deletionStatus,factoryCreatorId));
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
		for (Integer id : chocolates.keySet()) {
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
		    File file = new File(contextPath + "/chocolates.txt");
		    out = new BufferedWriter(new FileWriter(file));
		    for (Chocolate chocolate : chocolates.values()) {
		        out.write(chocolate.getId() + ";");
		        out.write(chocolate.getName() + ";");
		        out.write(chocolate.getCategory() + ";");
		        out.write(chocolate.getType() + ";");
		        out.write(chocolate.getWeight() + ";");
		        out.write(chocolate.getDescription() + ";");
		        out.write(chocolate.getFactoryCreatorId() + ";");
		        out.write(chocolate.getImagePath() + ";");
		        out.write(chocolate.getDeletionStatus().toString() + "\n");
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
	
	public Collection<Chocolate>GetAll(){
		return chocolates.values().stream()
                .filter(chocolate -> chocolate.IsActive())
                .collect(Collectors.toList());
	}
	
	public Chocolate GetById(int id)
	{
		for(Chocolate chocolate : chocolates.values())
			if(chocolate.getId()==id)
			{
				if(chocolate.IsActive())
					return chocolate;
				return null;
			}
				
			return null;
	}
	
	public Chocolate GetByIdIncludingDeleted(int id)
	{
		for(Chocolate chocolate : chocolates.values())
			if(chocolate.getId()==id)
			{
				return chocolate;
			}
				
			return null;
	}
	
	public Boolean DeleteById(int id)
	{
		for(Chocolate chocolate : chocolates.values())
			if(chocolate.getId()==id)
			{
				if(chocolate.Delete())
				{
					SaveToCsv();
					return true;
				}
					return false;
			}
				
			return false;
	}
	
	public Boolean Update(Chocolate chocolate)
	{
		   if (chocolates.containsKey(chocolate.getId()) && chocolates.get(chocolate.getId()).IsActive()) 
		   {
		        chocolates.put(chocolate.getId(), chocolate);
		        SaveToCsv();
		        return true;
		    }
		   return false;
	}
	
	public Chocolate Save(Chocolate chocolate) {
		chocolate.setId(NextId());
		chocolates.put(chocolate.getId(), chocolate);
		SaveToCsv();
		return chocolate;
	}
	
}
