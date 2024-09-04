package dto;

import java.time.LocalDate;

public class SearchPurchasesParamsDTO {

	private String factoryName;
	private LocalDate purchaseRangeFrom;
	private LocalDate purchaseRangeTo;
	private double priceFrom;
	private double priceTo;
	public SearchPurchasesParamsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SearchPurchasesParamsDTO(String factoryName, LocalDate purchaseRangeFrom, LocalDate purchaseRangeTo,
			double priceFrom, double priceTo) {
		super();
		this.factoryName = factoryName;
		this.purchaseRangeFrom = purchaseRangeFrom;
		this.purchaseRangeTo = purchaseRangeTo;
		this.priceFrom = priceFrom;
		this.priceTo = priceTo;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public LocalDate getPurchaseRangeFrom() {
		return purchaseRangeFrom;
	}
	public void setPurchaseRangeFrom(LocalDate purchaseRangeFrom) {
		this.purchaseRangeFrom = purchaseRangeFrom;
	}
	public LocalDate getPurchaseRangeTo() {
		return purchaseRangeTo;
	}
	public void setPurchaseRangeTo(LocalDate purchaseRangeTo) {
		this.purchaseRangeTo = purchaseRangeTo;
	}
	public double getPriceFrom() {
		return priceFrom;
	}
	public void setPriceFrom(double priceFrom) {
		this.priceFrom = priceFrom;
	}
	public double getPriceTo() {
		return priceTo;
	}
	public void setPriceTo(double priceTo) {
		this.priceTo = priceTo;
	}
	
	
	
	
}
