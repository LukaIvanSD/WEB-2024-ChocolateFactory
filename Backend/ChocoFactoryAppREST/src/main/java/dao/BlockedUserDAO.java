package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.BlockedUser;
import beans.Purchase;
import beans.CartArticle.DeletionStatus;

public class BlockedUserDAO {
	private String contextPath;
	private HashMap<Integer, BlockedUser> blockedUsers = new HashMap<>();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
	
	public BlockedUserDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BlockedUserDAO(String contextPath) {
		this.contextPath=contextPath;
		loadBlockedUsers();
	}
	
	private void loadBlockedUsers() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/blockedUsers.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					int userId = Integer.parseInt(st.nextToken().trim());
					LocalDateTime date = LocalDateTime.parse(st.nextToken().trim(), formatter);
					String deletionStatus = st.nextToken().trim();
					blockedUsers.put(userId, new BlockedUser(userId,date,deletionStatus));
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
		try {
		    File file = new File(contextPath + "/blockedUsers.txt");
		    out = new BufferedWriter(new FileWriter(file));
		    for (BlockedUser blockedUser : blockedUsers.values()) {;
		        out.write(blockedUser.getUserId() + ";");
		        out.write(blockedUser.getBlockDate().format(formatter) + ";");
		        out.write(blockedUser.getDeletionStatus().toString() + "\n");
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
	
	public Collection<BlockedUser>GetAll(){
		return blockedUsers.values().stream()
                .filter(blockedUser -> blockedUser.IsActive())
                .collect(Collectors.toList());
	}
	
	public BlockedUser GetByUserId(int userId)
	{
		for(BlockedUser blockedUser : blockedUsers.values())
		{
			if(blockedUser.getUserId()==userId)
			{
				if(blockedUser.IsActive())
					return blockedUser;
				return null;
			}
		}
				
			return null;
	}
	
	public Boolean DeleteByUserId(int userId)
	{
		for(BlockedUser blockedUser : blockedUsers.values())
			if(blockedUser.getUserId()==userId)
			{
				if(blockedUser.Delete())
				{
					SaveToCsv();
					return true;
				}
					return false;
			}
				
			return false;
	}
	
	public BlockedUser Save(BlockedUser blockedUser) {
		blockedUser.setDeletionStatus(DeletionStatus.Active);
		blockedUsers.put(blockedUser.getUserId(), blockedUser);
		SaveToCsv();
		return blockedUser;
	}
}
