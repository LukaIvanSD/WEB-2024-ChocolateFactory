package dto;

import beans.CartArticle;

public class CartArticleDTO {

	private CartArticle cartArticle;
	private double totalPrice;
	private ChocolateDTO chocolate;
	private String validationMessage;
	public CartArticleDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CartArticleDTO(CartArticle cartArticle, double totalPrice, ChocolateDTO chocolate) {
		super();
		this.cartArticle = cartArticle;
		this.totalPrice = totalPrice;
		this.chocolate = chocolate;
		this.validationMessage = "";
	}
	
	
	public CartArticleDTO(CartArticle cartArticle, double totalPrice, ChocolateDTO chocolate,
			String validationMessage) {
		super();
		this.cartArticle = cartArticle;
		this.totalPrice = totalPrice;
		this.chocolate = chocolate;
		this.validationMessage = validationMessage;
	}
	public String getValidationMessage() {
		return validationMessage;
	}
	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}
	public CartArticle getCartArticle() {
		return cartArticle;
	}
	public void setCartArticle(CartArticle cartArticle) {
		this.cartArticle = cartArticle;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public ChocolateDTO getChocolate() {
		return chocolate;
	}
	public void setChocolate(ChocolateDTO chocolate) {
		this.chocolate = chocolate;
	}
}
