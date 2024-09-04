package dto;

import java.util.ArrayList;
import java.util.List;

import beans.Cart;
import beans.Customer;

public class CartDTO {

	private Cart cart;
	private ArrayList<CartShipmentDTO> shipments;
	private int numberOfArticles;
	public CartDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CartDTO(Cart cart, ArrayList<CartShipmentDTO> shipments, int numberOfAricles) {
		super();
		this.cart = cart;
		this.shipments = shipments;
		this.numberOfArticles = numberOfAricles;
	}
	public CartDTO(ArrayList<CartShipmentDTO> shipments) {
		this.shipments = shipments;
	}
	public Cart getCart() {
		return cart;
	}
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	public ArrayList<CartShipmentDTO> getShipments() {
		return shipments;
	}
	public void setShipments(ArrayList<CartShipmentDTO> shipments) {
		this.shipments = shipments;
	}
	public int getNumberOfArticles() {
		return numberOfArticles;
	}
	public void setNumberOfArticles(int numberOfAricles) {
		this.numberOfArticles = numberOfAricles;
	}
	
	
}
