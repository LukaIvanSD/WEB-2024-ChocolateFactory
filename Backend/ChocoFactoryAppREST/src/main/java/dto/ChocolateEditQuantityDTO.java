package dto;

import beans.ChocolateItem;

public class ChocolateEditQuantityDTO {

	private ChocolateItem chocolateItem;
	private Integer quantity;
	public ChocolateEditQuantityDTO() {}
	public ChocolateEditQuantityDTO(ChocolateItem chocolateItem, Integer quantity) {
		super();
		this.chocolateItem = chocolateItem;
		this.quantity = quantity;
	}
	public ChocolateItem getChocolateItem() {
		return chocolateItem;
	}
	public void setChocolateItem(ChocolateItem chocolateItem) {
		this.chocolateItem = chocolateItem;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}
