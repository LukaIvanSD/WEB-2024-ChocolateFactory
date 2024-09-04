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
import beans.CancelledPurchase;

public class CancelledPurchaseDAO {
	private String contextPath;
	private HashMap<Integer, CancelledPurchase> cancelledPurchases = new HashMap<>();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	public CancelledPurchaseDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CancelledPurchaseDAO(String contextPath) {
		this.contextPath=contextPath;
		loadCancelledPurchase();
	}
	
	private void loadCancelledPurchase() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/cancelledPurchases.txt");
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
					LocalDate date = LocalDate.parse(st.nextToken().trim(), formatter);

					cancelledPurchases.put(userId, new CancelledPurchase(userId,date));
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
		    File file = new File(contextPath + "/cancelledPurchases.txt");
		    out = new BufferedWriter(new FileWriter(file));
		    for (CancelledPurchase cancelledPurchase : cancelledPurchases.values()) {
		        out.write(cancelledPurchase.getPurchaseId() + ";");
		        out.write(cancelledPurchase.getDate().format(formatter) + "\n");
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
	
	public Collection<CancelledPurchase>GetAll(){
		return cancelledPurchases.values();
                
	}
	
	public CancelledPurchase GetByPurchaseId(int purchaseId)
	{
		for(CancelledPurchase cancelledPurchase : cancelledPurchases.values())
		{
			if(cancelledPurchase.getPurchaseId()==purchaseId)
			{
					return cancelledPurchase;
			}
		}
				
			return null;
	}
	
	public void Save(CancelledPurchase cancelledPurchase) {
		cancelledPurchases.put(cancelledPurchase.getPurchaseId(), cancelledPurchase);
		SaveToCsv();
	}
}
