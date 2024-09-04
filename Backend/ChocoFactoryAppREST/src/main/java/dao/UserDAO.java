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
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.CartArticle.DeletionStatus;
import beans.Customer;
import beans.Manager;
import beans.User;
import beans.User.UserType;
/***
 * <p>Klasa namenjena da ucita korisnike iz fajla i pruza operacije nad njima (poput pretrage).
 * Korisnici se nalaze u fajlu WebContent/users.txt u obliku: <br>
 * firstName;lastName;email;username;password</p>
 * <p><b>NAPOMENA:</b> Lozinke se u praksi <b>nikada</b> ne snimaju u istom tekstualnom obliku.</p>
 * @author Lazar
 *
 */
public class UserDAO {
	
	private HashMap<Integer, User> users = new HashMap<>();
	private String contextPath;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	
	public UserDAO() {
		
	}
	public UserDAO(String contextPath) {
		this.contextPath=contextPath;
		loadUsers();
	}
	
	private void loadUsers() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/users.txt");
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
					String firstName = st.nextToken().trim();
					String lastName = st.nextToken().trim();
					String username = st.nextToken().trim();
					String password = st.nextToken().trim();
					String sex=st.nextToken().trim();
					String dateOfBirth=st.nextToken().trim();
					String userType = st.nextToken().trim();
					String deletionStatus = st.nextToken().trim();
					LocalDate date = LocalDate.parse(dateOfBirth, formatter);
					users.put(id, new User(id,firstName,lastName,username,password,sex,date,userType,deletionStatus));
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

	public void SaveToCsv() {
		BufferedWriter out = null;
		System.out.println(contextPath + "/users.txt");
		try {
		    File file = new File(contextPath + "/users.txt");
		    out = new BufferedWriter(new FileWriter(file));
		    for (User user : users.values()) {
		        out.write(user.getId() + ";");
		        out.write(user.getFirstName() + ";");
		        out.write(user.getLastName() + ";");
		        out.write(user.getUsername() + ";");
		        out.write(user.getPassword() + ";");
		        out.write(user.getSex().toString() + ";");
		        out.write(user.getDateOfBirth().format(formatter) + ";");
		        out.write(user.getUserType().toString() + ";");
		        out.write(user.getDeletionStatus().toString() + "\n");
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
	
	public Collection<User>GetAll(){
		return users.values().stream().filter(user->user.IsActive()).collect(Collectors.toList());
	}
	public User GetById(int id)
	{
		for(User user : users.values())
			if(user.getId()==id)
			{
				if(user.IsActive())
					return user;
				return null;
			}
			return null;
	}
	public User GetByCredentials(String username, String password) {
		for(User user:users.values())
		{
			if(user.getUsername().equals(username) && user.getPassword().equals(password))
				return user;
				
		}
		return null;
	}
	
	public Boolean DeleteById(int id)
	{
		for(User user : users.values())
			if(user.getId()==id)
			{
				if(user.Delete())
				{
					SaveToCsv();
					return true;
				}
				return false;
			}
				
			return false;
	}
	
	public Boolean Update(User user)
	{
		   if (users.containsKey(user.getId()) && users.get(user.getId()).IsActive()) {
			   users.put(user.getId(), user);
		        SaveToCsv();
		        return true;
		    }
		   return false;
	}
	
	public void Save(User user) {
		user.setId(NextId());
		users.put(user.getId(), user);
		SaveToCsv();
		
	}
	
	public int NextId()
	{
		Integer maxId = -1;
		for (Integer id : users.keySet()) {
			int idNum =id;
			if (idNum > maxId) {
				maxId = idNum;
			}
		}
		return ++maxId;
	}
	
	public Boolean Exists(String username) {
		for(User user:users.values())
		{
			if(user.getUsername().toLowerCase().equals(username.toLowerCase()))
				return true;
				
		}
		return false;
	}
}
