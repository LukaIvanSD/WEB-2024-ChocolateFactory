package dto;

public class CartArticleRequestDTO {
	private Integer chocolateItemId;
	private Integer quantity;
	public CartArticleRequestDTO() {}
	public Integer getChocolateItemId() {
		return chocolateItemId;
	}
	public void setChocolateItemId(Integer chocolateItemId) {
		this.chocolateItemId = chocolateItemId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
