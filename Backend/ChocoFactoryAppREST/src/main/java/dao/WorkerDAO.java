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

import beans.Manager;
import beans.Worker;

public class WorkerDAO {
	private String contextPath;
	private HashMap<Integer, Worker> workers = new HashMap<>();
	
	
	public WorkerDAO() {
		
	}
	
	/***
	 * @param contextPath Putanja do aplikacije u Tomcatu. Moze se pristupiti samo iz servleta.
	 */
	public WorkerDAO(String contextPath) {
		this.contextPath=contextPath;
		loadWorkers();
	}
	private void loadWorkers() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/workers.txt");
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
					workers.put(id, new Worker(id,userId,factoryId,deletionStatus));
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
		for (Integer id : workers.keySet()) {
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
		    File file = new File(contextPath + "/workers.txt");
		    out = new BufferedWriter(new FileWriter(file));
		    for (Worker worker : workers.values()) {
		        out.write(worker.getId() + ";");
		        out.write(worker.getUserId() + ";");
		        out.write(worker.getFactoryId() + ";");
		        out.write(worker.getDeletionStatus().toString() + "\n");
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

	public Collection<Worker>GetAll(){
		return workers.values().stream().filter(worker->worker.IsActive()).collect(Collectors.toList());
	}
	public Worker GetById(int id)
	{
		for(Worker worker : workers.values())
			if(worker.getId()==id)
			{
				if(worker.IsActive())
					return worker;
				return null;
			}
			return null;
	}
	
	public Worker GetByUser(int userId)
	{
		for(Worker worker : workers.values())
			if(worker.getUserId()==userId)
			{
				if(worker.IsActive())
					return worker;
				return null;
			}
			return null;
	}
	public Boolean DeleteById(int id)
	{
		for(Worker worker : workers.values())
			if(worker.getId()==id)
			{
				if(worker.Delete())
				{
					SaveToCsv();
					return true;
				}
				return false;
			}
				
			return false;
	}
	public Boolean Update(Worker worker)
	{
		   if (workers.containsKey(worker.getId()) && workers.get(worker.getId()).IsActive()) {
			   workers.put(worker.getId(), worker);
		        SaveToCsv();
		        return true;
		    }
		   return false;
	}
	public void Save(Worker worker) {
		worker.setId(NextId());
		workers.put(worker.getId(), worker);
		SaveToCsv();
		
	}
}
