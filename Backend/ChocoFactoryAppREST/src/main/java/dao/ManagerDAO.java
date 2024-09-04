package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Customer;
import beans.Manager;
import beans.User;

public class ManagerDAO {
	private String contextPath;
	private HashMap<Integer, Manager> managers = new HashMap<>();
	
	
	public ManagerDAO() {
		
	}
	
	/***
	 * @param contextPath Putanja do aplikacije u Tomcatu. Moze se pristupiti samo iz servleta.
	 */
	public ManagerDAO(String contextPath) {
		this.contextPath=contextPath;
		loadManagers();
	}
	private void loadManagers() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/managers.txt");
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
					int userId = Integer.parseInt(st.nextToken().trim());
					int factoryId = Integer.parseInt(st.nextToken().trim());
					String deletionStatus=st.nextToken().trim();
					managers.put(id, new Manager(id,userId,factoryId,deletionStatus));
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
		for (Integer id : managers.keySet()) {
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
		    File file = new File(contextPath + "/managers.txt");
		    out = new BufferedWriter(new FileWriter(file));
		    for (Manager manager : managers.values()) {
		        out.write(manager.getId() + ";");
		        out.write(manager.getUserId() + ";");
		        out.write(manager.getFactoryId() + ";");
		        out.write(manager.getDeletionStatus().toString() + "\n");
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

	public Collection<Manager>GetAll(){
		return managers.values().stream().filter(manager->manager.IsActive()).collect(Collectors.toList());
	}
	public Manager GetById(int id)
	{
		for(Manager manager : managers.values())
			if(manager.getId()==id)
			{
				if(manager.IsActive())
					return manager;
				return null;
			}
			return null;
	}
	public Manager GetByUser(int userId)
	{
		for(Manager manager : managers.values())
			if(manager.getUserId()==userId)
			{
				if(manager.IsActive())
					return manager;
				return null;
			}
			return null;
	}
	public Boolean DeleteById(int id)
	{
		for(Manager manager : managers.values())
			if(manager.getId()==id)
			{
				if(manager.Delete())
				{
					SaveToCsv();
					return true;
				}
				return false;
			}
				
			return false;
	}
	public Boolean DeleteByUserId(int userId)
	{
		for(Manager manager :  managers.values())
			if(manager.getUserId()==userId)
			{		
				if(manager.Delete())
				{
					SaveToCsv();
					return true;
				}
				return false;
			}			
			return false;
	}
	public Boolean Update(Manager manager)
	{
		   if (managers.containsKey(manager.getId()) && managers.get(manager.getId()).IsActive()) {
			   managers.put(manager.getId(), manager);
		        SaveToCsv();
		        return true;
		    }
		   return false;
	}
	public void Save(Manager manager) {
		manager.setId(NextId());
		managers.put(manager.getId(), manager);
		SaveToCsv();
		
	}
}
