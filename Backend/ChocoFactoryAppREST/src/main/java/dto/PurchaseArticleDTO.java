package dto;

import beans.Chocolate;
import beans.ChocolatePrice;
import beans.PurchaseArticle;

public class PurchaseArticleDTO {

	private Chocolate chocolate;
	
	private double price;
	
	private int quantity;
	

	public PurchaseArticleDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PurchaseArticleDTO(Chocolate chocolate, double price, int quantity) {
		super();
		this.chocolate = chocolate;
		this.price = price;
		this.quantity = quantity;
	}

	public Chocolate getChocolate() {
		return chocolate;
	}

	public void setChocolate(Chocolate chocolate) {
		this.chocolate = chocolate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	
	
	
}
