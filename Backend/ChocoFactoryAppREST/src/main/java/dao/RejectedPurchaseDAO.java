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
import beans.RejectedPurchase;

public class RejectedPurchaseDAO {
	private String contextPath;
	private HashMap<Integer, RejectedPurchase> rejectedPurchases = new HashMap<>();
	
	public RejectedPurchaseDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RejectedPurchaseDAO(String contextPath) {
		this.contextPath=contextPath;
		loadRejectedPurchase();
	}
	
	private void loadRejectedPurchase() {
		BufferedReader in = null;
		try {
			File file = new File(contextPath + "/rejectedPurchases.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					int purchaseId = Integer.parseInt(st.nextToken().trim());
					String rejectReason = st.nextToken().trim();
					rejectedPurchases.put(purchaseId, new RejectedPurchase(purchaseId,rejectReason));
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
		    File file = new File(contextPath + "/rejectedPurchases.txt");
		    out = new BufferedWriter(new FileWriter(file));
		    for (RejectedPurchase rejectedPurchase : rejectedPurchases.values()) {
		        out.write(rejectedPurchase.getPurchaseId()+ ";");
		        out.write(rejectedPurchase.getRejectionReason() + "\n");
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
	
	public Collection<RejectedPurchase>GetAll(){
		return rejectedPurchases.values();
                
	}
	
	public RejectedPurchase GetByPurchaseId(int purchaseId)
	{
		for(RejectedPurchase rejectedPurchase : rejectedPurchases.values())
		{
			if(rejectedPurchase.getPurchaseId()==purchaseId)
			{
					return rejectedPurchase;
			}
		}
				
			return null;
	}
	
	public void Save(RejectedPurchase rejectedPurchase) {
		rejectedPurchases.put(rejectedPurchase.getPurchaseId(), rejectedPurchase);
		SaveToCsv();
	}
}
