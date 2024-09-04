package dto;

import java.util.Map;

public class GhostItemsDTO {
	 private Integer chocolateItemId;
	    private Integer quantity;
	
	public GhostItemsDTO() {}

	public GhostItemsDTO(Integer chocolateItemId, Integer quantity) {
		super();
		this.chocolateItemId = chocolateItemId;
		this.quantity = quantity;
	}

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
