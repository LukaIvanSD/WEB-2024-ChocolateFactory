package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Customer;
import beans.Manager;

public class CustomerDAO {
	private String contextPath;
	private HashMap<Integer, Customer> customers = new HashMap<>();
	
	
	public CustomerDAO() {
		
	}
	
	/***
	 * @param contextPath Putanja do aplikacije u Tomcatu. Moze se pristupiti samo iz servleta.
	 */
	public CustomerDAO(String contextPath) {
		this.contextPath=contextPath;
		loadCustomers();
	}
	private void loadCustomers() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/customers.txt");
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
					int bonusPoints = Integer.parseInt(st.nextToken().trim());
					int customerTypeId = Integer.parseInt(st.nextToken().trim());
					String deletionStatus = st.nextToken().trim();
					customers.put(id, new Customer(id,userId,bonusPoints,customerTypeId,deletionStatus));
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
		for (Integer id : customers.keySet()) {
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
		    File file = new File(contextPath + "/customers.txt");
		    out = new BufferedWriter(new FileWriter(file));
		    for (Customer customer : customers.values()) {
		        out.write(customer.getId() + ";");
		        out.write(customer.getUserId() + ";");
		        out.write(customer.getBonusPoints() + ";");
		        out.write(customer.getCustomerTypeId() + ";");
		        out.write(customer.getDeletionStatus().toString() + "\n");
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
	public Collection<Customer>GetAll(){
		return customers.values().stream()
                .filter(customer -> customer.IsActive())
                .collect(Collectors.toList());
	}
	public Customer GetById(int id)
	{
		for(Customer customer : customers.values())
			if(customer.getId()==id)
			{
				if(customer.IsActive())
					return customer;
				return null;
			}
				
			return null;
	}
	public Customer GetByUser(int userId)
	{
		for(Customer customer : customers.values()) {
			if(customer.getUserId()==userId)
			{
				if(customer.IsActive())
					return customer;
				return null;
			}
		}
			
		return null;
	}
	public Boolean DeleteById(int id)
	{
		for(Customer customer : customers.values())
			if(customer.getId()==id)
			{
				if(customer.Delete())
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
		for(Customer customer : customers.values())
			if(customer.getUserId()==userId)
			{
				if(customer.Delete())
				{
					SaveToCsv();
					return true;
				}
					return false;
			}			
			return false;
	}
	public Boolean Update(Customer customer)
	{
		   if (customers.containsKey(customer.getId()) && customers.get(customer.getId()).IsActive()) 
		   {
		        customers.put(customer.getId(), customer);
		        SaveToCsv();
		        return true;
		    }
		   return false;
	}
	public Customer Save(Customer customer) {
		customer.setId(NextId());
		customers.put(customer.getId(), customer);
		SaveToCsv();
		return customer;
		
	}
}
