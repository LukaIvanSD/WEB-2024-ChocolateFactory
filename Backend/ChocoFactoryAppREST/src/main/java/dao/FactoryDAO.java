package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.CartArticle.DeletionStatus;
import beans.Factory;
import beans.Manager;

public class FactoryDAO {
	private String contextPath;
	private HashMap<Integer, Factory> factories = new HashMap<>();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	
	
	public FactoryDAO() {
		
	}
	
	public FactoryDAO(String contextPath) {
		this.contextPath=contextPath;
		loadFactories();
	}
	private void loadFactories() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/factories.txt");
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
					String openFrom=st.nextToken().trim();
					String openTo=st.nextToken().trim();
					int locationId = Integer.parseInt(st.nextToken().trim());
					String logoPath=st.nextToken().trim();
					double rating = Double.parseDouble(st.nextToken().trim());
					String deletionStatus=st.nextToken().trim();
					LocalTime OpenFrom = LocalTime.parse(openFrom, formatter);
					LocalTime OpenTo = LocalTime.parse(openTo, formatter);
					factories.put(id, new Factory(id,name,OpenFrom,OpenTo,locationId,logoPath,rating,deletionStatus));
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
		for (Integer id : factories.keySet()) {
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
		    File file = new File(contextPath + "/factories.txt");
		    out = new BufferedWriter(new FileWriter(file));
		    for (Factory factory : factories.values()) {
		        out.write(factory.getId() + ";");
		        out.write(factory.getName() + ";");
		        out.write(factory.getOpenFrom().format(formatter) + ";");
		        out.write(factory.getOpenTo().format(formatter) + ";");
		        out.write(factory.getLocationId() + ";");
		        out.write(factory.getLogoPath() + ";");
		        out.write(factory.getRating() + ";");
		        out.write(factory.getDeletionStatus().toString() + "\n");
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

	public Collection<Factory>GetAll(){
		return factories.values().stream().filter(factory->factory.IsActive()).collect(Collectors.toList());
	}
	public Factory GetById(int id)
	{
		for(Factory factory : factories.values())
			if(factory.getId()==id)
			{
				if(factory.IsActive())
					return factory;
				return null;
			}
			return null;
	}
	
	public Factory GetByIdIncludingDeleted(int id)
	{
		for(Factory factory : factories.values())
			if(factory.getId()==id)
			{
					return factory;
			}
			return null;
	}
	
	public Boolean DeleteById(int id)
	{
		for(Factory factory : factories.values())
			if(factory.getId()==id)
			{
				if(factory.Delete())
				{
					SaveToCsv();
					return true;
				}
				return false;
			}
				
			return false;
	}
	public Boolean Update(Factory factory)
	{
		   if (factories.containsKey(factory.getId()) && factories.get(factory.getId()).IsActive()) {
			   factories.put(factory.getId(), factory);
		        SaveToCsv();
		        return true;
		    }
		   return false;
	}
	public void Save(Factory factory) {
		factory.setId(NextId());
		factories.put(factory.getId(), factory);
		SaveToCsv();
		
	}
}
