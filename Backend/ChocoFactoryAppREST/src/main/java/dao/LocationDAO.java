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

import beans.Customer;
import beans.Location;

public class LocationDAO {
	private String contextPath;
	private HashMap<Integer, Location> locations = new HashMap<>();
	
	
	public LocationDAO() {
		
	}

	public LocationDAO(String contextPath) {
		this.contextPath=contextPath;
		loadLocations();
	}
	private void loadLocations() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/locations.txt");
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
					double longitude = Double.parseDouble(st.nextToken().trim());
					double latitude = Double.parseDouble(st.nextToken().trim());
					String address = st.nextToken().trim();
					String city = st.nextToken().trim();
					String postcode = st.nextToken().trim();
					String deletionStatus = st.nextToken().trim();
					locations.put(id, new Location(id,longitude,latitude,address,deletionStatus,postcode,city));
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
		for (Integer id : locations.keySet()) {
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
		    File file = new File(contextPath + "/locations.txt");
		    out = new BufferedWriter(new FileWriter(file));
		    for (Location location : locations.values()) {
		        out.write(location.getId() + ";");
		        out.write(location.getLongitude() + ";");
		        out.write(location.getLatitude() + ";");
		        out.write(location.getAddress() + ";");
		        out.write(location.getCity()+";");
		        out.write(location.getPostcode()+";");
		        out.write(location.getDeletionStatus().toString() + "\n");
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
	public Collection<Location>GetAll(){
		return locations.values().stream()
                .filter(customer -> customer.IsActive())
                .collect(Collectors.toList());
	}
	public Location GetById(int id)
	{
		for(Location location : locations.values())
			if(location.getId()==id)
			{
				if(location.IsActive())
					return location;
				return null;
			}
				
			return null;
	}
	public Boolean DeleteById(int id)
	{
		for(Location location : locations.values())
			if(location.getId()==id)
			{
				if(location.Delete())
				{
					SaveToCsv();
					return true;
				}
					return false;
			}
				
			return false;
	}

	public Boolean Update(Location location)
	{
		   if (locations.containsKey(location.getId()) && locations.get(location.getId()).IsActive()) 
		   {
			   locations.put(location.getId(), location);
		        SaveToCsv();
		        return true;
		    }
		   return false;
	}
	public void Save(Location location) {
		location.setId(NextId());
		locations.put(location.getId(), location);
		SaveToCsv();
		
	}
}
