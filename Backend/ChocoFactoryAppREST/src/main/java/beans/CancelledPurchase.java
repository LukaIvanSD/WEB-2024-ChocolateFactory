package beans;

import java.time.LocalDate;

public class CancelledPurchase {
	private int purchaseId;
	private LocalDate date;
	
	public CancelledPurchase() {}
	public CancelledPurchase(int purchaseId, LocalDate date) {
		super();
		this.purchaseId = purchaseId;
		this.date = date;
	}
	public int getPurchaseId() {
		return purchaseId;
	}
	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public Boolean IsCancelledInLastMonth() {
		   LocalDate now = LocalDate.now();
		    LocalDate thirtyDaysAgo = now.minusDays(30);
		    return !date.isBefore(thirtyDaysAgo);
	}
	
	
}
