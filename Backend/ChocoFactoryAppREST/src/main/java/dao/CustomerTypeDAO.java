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

import beans.CustomerType;

public class CustomerTypeDAO {

	private String contextPath;
	private HashMap<Integer, CustomerType> customerTypes = new HashMap<>();
	
	
	public CustomerTypeDAO() {
		
	}
	
	/***
	 * @param contextPath Putanja do aplikacije u Tomcatu. Moze se pristupiti samo iz servleta.
	 */
	public CustomerTypeDAO(String contextPath) {
		this.contextPath=contextPath;
		loadCustomers();
	}
	private void loadCustomers() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/customerTypes.txt");
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
					String type = st.nextToken().trim();
					double discount = Double.parseDouble(st.nextToken().trim());
					double pointsForUpgrade = Double.parseDouble(st.nextToken().trim());
					customerTypes.put(id, new CustomerType(id,type,discount,pointsForUpgrade));
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
		for (Integer id : customerTypes.keySet()) {
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
		    for (CustomerType customerType : customerTypes.values()) {
		        out.write(customerType.getId() + ";");
		        out.write(customerType.getType() + ";");
		        out.write(customerType.getDiscount() + ";");
		        out.write(customerType.getPointsForUpgrade() + ";");
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
	public Collection<CustomerType>GetAll(){
		return customerTypes.values();
	}
	public CustomerType GetById(int id)
	{
		for(CustomerType customerType : customerTypes.values())
			if(customerType.getId()==id)
			{
				return customerType;
			}
				
			return null;
	}
	
}
