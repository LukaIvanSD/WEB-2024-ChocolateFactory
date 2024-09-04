package beans;

public class RejectedPurchase {
	private int purchaseId;
	private String rejectionReason;
	public RejectedPurchase() {}
	public RejectedPurchase(int purchaseId, String rejectionReason) {
		super();
		this.setPurchaseId(purchaseId);
		this.rejectionReason = rejectionReason;
	}
	public String getRejectionReason() {
		return rejectionReason;
	}
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
	public int getPurchaseId() {
		return purchaseId;
	}
	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}
	
}
