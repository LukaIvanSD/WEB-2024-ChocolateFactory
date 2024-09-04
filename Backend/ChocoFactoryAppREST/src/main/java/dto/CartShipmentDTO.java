package dto;

import java.util.ArrayList;

import beans.Factory;

public class CartShipmentDTO {

	private ArrayList<CartArticleDTO> cartArticles;
	private Factory factory;
	private double totalPrice;
	public CartShipmentDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CartShipmentDTO(ArrayList<CartArticleDTO> cartArticles, Factory factory, double totalPrice) {
		super();
		this.cartArticles = cartArticles;
		this.factory = factory;
		this.totalPrice = totalPrice;
	}

	public ArrayList<CartArticleDTO> getCartArticles() {
		return cartArticles;
	}
	public void setCartArticles(ArrayList<CartArticleDTO> cartArticles) {
		this.cartArticles = cartArticles;
	}
	
	public Factory getFactory() {
		return factory;
	}

	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	
}
