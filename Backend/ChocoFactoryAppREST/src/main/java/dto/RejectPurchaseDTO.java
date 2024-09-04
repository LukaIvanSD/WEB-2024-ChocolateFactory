package dto;

public class RejectPurchaseDTO {
	private String reason;
	private Integer purchaseId;
	public RejectPurchaseDTO() {}
	public RejectPurchaseDTO(String reason, Integer purchaseId) {
		super();
		this.reason = reason;
		this.purchaseId = purchaseId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Integer getPurchaseId() {
		return purchaseId;
	}
	public void setPurchaseId(Integer purchaseId) {
		this.purchaseId = purchaseId;
	}
	
}
