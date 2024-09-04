package dto;

import beans.Chocolate;
import beans.ChocolateItem;
import beans.ChocolateItem.Availability;
import beans.ChocolatePrice;

public class ChocolateDTO {
	private Chocolate chocolate;
	private ChocolateItem chocolateItem;
	private ChocolatePrice chocolatePrice;
	public ChocolateDTO() {}
	public ChocolateDTO(Chocolate chocolate, ChocolateItem cholocateItem, ChocolatePrice chocolatePrice) {
		super();
		this.chocolate = chocolate;
		this.chocolateItem = cholocateItem;
		this.chocolatePrice = chocolatePrice;
	}
	public Chocolate getChocolate() {
		return chocolate;
	}
	public void setChocolate(Chocolate chocolate) {
		this.chocolate = chocolate;
	}
	public ChocolatePrice getChocolatePrice() {
		return chocolatePrice;
	}
	public void setChocolatePrice(ChocolatePrice chocolatePrice) {
		this.chocolatePrice = chocolatePrice;
	}
	public ChocolateItem getChocolateItem() {
		return chocolateItem;
	}
	public void setChocolateItem(ChocolateItem chocolateItem) {
		this.chocolateItem = chocolateItem;
	}
	
}
